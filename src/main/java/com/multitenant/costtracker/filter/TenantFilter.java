package com.multitenant.costtracker.filter;

import com.multitenant.costtracker.context.TenantContext;
import com.multitenant.costtracker.repository.TenantRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private final TenantRepository tenantRepo;

    public TenantFilter(TenantRepository tenantRepo) {
        this.tenantRepo = tenantRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI().trim();
        if((uri.equals("/tenants") || uri.equals("/tenants/")) && request.getMethod().equalsIgnoreCase("POST")){
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("X-Tenant-Id");
        if(header == null || header.isBlank()){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing X-Tenant-Id");
            return;
        }

        UUID tenantId;

        try{
            tenantId = UUID.fromString(header);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Tenant Id");
            return;
        }

        if(!tenantRepo.existsById(tenantId)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tenant Does Not Exist");
            return;
        }

        try{
            TenantContext.set(tenantId);
            filterChain.doFilter(request, response);
        }finally {
            TenantContext.clear();
        }
    }
}
