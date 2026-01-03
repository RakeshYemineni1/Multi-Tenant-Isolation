# Multi-Tenant Cost Tracking API

A comprehensive Spring Boot application that tracks daily cloud spend for multiple companies (tenants) with strict tenant isolation and security.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Testing Guide](#testing-guide)
- [Database Schema](#database-schema)
- [Security & Tenant Isolation](#security--tenant-isolation)
- [Development](#development)
- [Troubleshooting](#troubleshooting)

## Overview

This application provides a multi-tenant cost tracking system where:
- Multiple companies (tenants) can track their daily cloud spending
- Each tenant has complete data isolation from other tenants
- Teams within a tenant can submit daily cost records
- Comprehensive reporting shows total costs and team breakdowns
- All operations are secured with tenant-based access control

## Features

### Core Functionality
- Multi-Tenant Architecture - Complete data isolation between tenants
- Team Management - Create and manage teams within tenants
- Cost Tracking - Submit and track daily cost records per team
- Comprehensive Reporting - Total costs and team breakdown reports
- Date Range Filtering - Query costs within specific date ranges

### Security & Isolation
- Header-Based Authentication - X-Tenant-Id header validation
- Request Filtering - Automatic tenant scoping for all operations
- Database Constraints - Multi-level data integrity enforcement
- Thread-Safe Context - ThreadLocal tenant context management

### Technical Features
- Docker Containerization - One-command deployment
- PostgreSQL Database - Robust data persistence
- RESTful API - Clean, intuitive endpoint design
- UUID Primary Keys - Security-focused entity identification

## Architecture

### System Components

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client App    │───▶│  Spring Boot    │───▶│   PostgreSQL    │
│   (Postman)     │    │      API        │    │    Database     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │  Tenant Filter  │
                       │ (X-Tenant-Id)   │
                       └─────────────────┘
```

### Data Model

```
Tenant (1) ──────── (N) Team (1) ──────── (N) CostRecord
   │                     │                        │
   ├─ id (UUID)          ├─ id (UUID)            ├─ id (UUID)
   └─ name               ├─ tenant_id (FK)       ├─ tenant_id (FK)
                         └─ name                 ├─ team_id (FK)
                                                 ├─ date
                                                 └─ cost
```

### Multi-Tenant Isolation Layers

1. **HTTP Filter Layer** - TenantFilter validates X-Tenant-Id header
2. **Service Layer** - TenantContext provides thread-local tenant scoping
3. **Repository Layer** - All queries automatically include tenant_id
4. **Database Layer** - Unique constraints prevent cross-tenant conflicts

## Quick Start

### Prerequisites
- Docker (version 20.0+)
- Docker Compose (version 2.0+)
- Postman or curl for API testing

### Installation & Setup

1. **Clone/Download the project**
   ```bash
   cd cost-tracker
   ```

2. **Start the application**
   ```bash
   docker compose up --build
   ```

3. **Verify startup**
   - Application: http://localhost:8081
   - Database: PostgreSQL on localhost:5432
   - Wait for "Started CostTrackerApplication" log message

4. **Test basic connectivity**
   ```bash
   curl http://localhost:8081
   ```

## API Documentation

### Base URL
```
http://localhost:8081
```

### Authentication
All endpoints (except POST /tenants) require the `X-Tenant-Id` header:
```
X-Tenant-Id: <tenant-uuid>
```

### Endpoints Overview

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/tenants` | Create new tenant | No |
| POST | `/teams` | Create team | Yes |
| GET | `/teams` | List teams | Yes |
| POST | `/cost-records` | Submit cost record | Yes |
| GET | `/cost-records` | List cost records | Yes |
| GET | `/reports/total` | Get total cost report | Yes |
| GET | `/reports/by-team` | Get team breakdown | Yes |

### Detailed API Reference

#### 1. Create Tenant
```http
POST /tenants
Content-Type: application/json

{
  "name": "Acme Corporation"
}
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Acme Corporation"
}
```

#### 2. Create Team
```http
POST /teams
Content-Type: application/json
X-Tenant-Id: 550e8400-e29b-41d4-a716-446655440000

{
  "name": "Platform Engineering"
}
```

**Response:**
```json
{
  "id": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
  "name": "Platform Engineering"
}
```

#### 3. List Teams
```http
GET /teams
X-Tenant-Id: 550e8400-e29b-41d4-a716-446655440000
```

**Response:**
```json
[
  {
    "id": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
    "name": "Platform Engineering"
  },
  {
    "id": "6ba7b811-9dad-11d1-80b4-00c04fd430c8",
    "name": "Data Science"
  }
]
```

#### 4. Submit Cost Record
```http
POST /cost-records
Content-Type: application/json
X-Tenant-Id: 550e8400-e29b-41d4-a716-446655440000

{
  "teamId": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
  "date": "2025-01-15",
  "costAmount": 1250.75
}
```

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "teamId": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
  "date": "2025-01-15",
  "cost": 1250.75
}
```

#### 5. List Cost Records
```http
GET /cost-records?from=2025-01-01&to=2025-01-31
X-Tenant-Id: 550e8400-e29b-41d4-a716-446655440000
```

**Response:**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "teamId": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
    "date": "2025-01-15",
    "cost": 1250.75
  }
]
```

#### 6. Get Total Cost Report
```http
GET /reports/total?from=2025-01-01&to=2025-01-31
X-Tenant-Id: 550e8400-e29b-41d4-a716-446655440000
```

**Response:**
```json
{
  "tenantId": "550e8400-e29b-41d4-a716-446655440000",
  "from": "2025-01-01",
  "to": "2025-01-31",
  "totalCost": 15750.25
}
```

#### 7. Get Team Breakdown Report
```http
GET /reports/by-team?from=2025-01-01&to=2025-01-31
X-Tenant-Id: 550e8400-e29b-41d4-a716-446655440000
```

**Response:**
```json
{
  "tenantId": "550e8400-e29b-41d4-a716-446655440000",
  "from": "2025-01-01",
  "to": "2025-01-31",
  "items": [
    {
      "teamId": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
      "teamName": "Platform Engineering",
      "totalCost": 8500.50
    },
    {
      "teamId": "6ba7b811-9dad-11d1-80b4-00c04fd430c8",
      "teamName": "Data Science",
      "totalCost": 7249.75
    }
  ]
}
```

## Testing Guide

### Complete Testing Workflow

#### Step 1: Environment Setup
```bash
# Start the application
docker compose up --build

# Wait for startup completion (look for this log):
# "Started CostTrackerApplication in X.XXX seconds"

```

#### Step 2: Basic Functionality Test

**2.1 Create First Tenant**
```bash
curl -X POST http://localhost:8081/tenants \
  -H "Content-Type: application/json" \
  -d '{"name": "Acme Corp"}'
```
*Save the returned `id` as `TENANT_A_ID`*

**2.2 Create Second Tenant**
```bash
curl -X POST http://localhost:8081/tenants \
  -H "Content-Type: application/json" \
  -d '{"name": "Beta Inc"}'
```
*Save the returned `id` as `TENANT_B_ID`*

**2.3 Create Teams for Tenant A**
```bash
# Team 1
curl -X POST http://localhost:8081/teams \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: $TENANT_A_ID" \
  -d '{"name": "Platform Team"}'

# Team 2
curl -X POST http://localhost:8081/teams \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: $TENANT_A_ID" \
  -d '{"name": "Data Team"}'
```
*Save the team IDs as `TEAM_A1_ID` and `TEAM_A2_ID`*

**2.4 Create Teams for Tenant B**
```bash
curl -X POST http://localhost:8081/teams \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: $TENANT_B_ID" \
  -d '{"name": "DevOps Team"}'
```

**2.5 Submit Cost Records**
```bash
# Costs for Tenant A
curl -X POST http://localhost:8081/cost-records \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: $TENANT_A_ID" \
  -d '{"teamId": "'$TEAM_A1_ID'", "date": "2025-01-15", "costAmount": 1500.00}'

curl -X POST http://localhost:8081/cost-records \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: $TENANT_A_ID" \
  -d '{"teamId": "'$TEAM_A2_ID'", "date": "2025-01-15", "costAmount": 2250.50}'
```

#### Step 3: Tenant Isolation Testing

**3.1 Test Valid Access**
```bash
# Should return teams for Tenant A
curl -X GET http://localhost:8081/teams \
  -H "X-Tenant-Id: $TENANT_A_ID"
```

**3.2 Test Cross-Tenant Isolation**
```bash
# Should return empty or different teams (not Tenant A's teams)
curl -X GET http://localhost:8081/teams \
  -H "X-Tenant-Id: $TENANT_B_ID"
```

**3.3 Test Missing Header**
```bash
# Should return 403 Forbidden
curl -X GET http://localhost:8081/teams
```

**3.4 Test Invalid Tenant ID**
```bash
# Should return 403 Forbidden
curl -X GET http://localhost:8081/teams \
  -H "X-Tenant-Id: 00000000-0000-0000-0000-000000000000"
```

#### Step 4: Reporting Tests

**4.1 Total Cost Report**
```bash
curl -X GET "http://localhost:8081/reports/total?from=2025-01-01&to=2025-01-31" \
  -H "X-Tenant-Id: $TENANT_A_ID"
```

**4.2 Team Breakdown Report**
```bash
curl -X GET "http://localhost:8081/reports/by-team?from=2025-01-01&to=2025-01-31" \
  -H "X-Tenant-Id: $TENANT_A_ID"
```

#### Step 5: Edge Case Testing

**5.1 Duplicate Tenant Names**
```bash
# Should return error (409 Conflict or 400 Bad Request)
curl -X POST http://localhost:8081/tenants \
  -H "Content-Type: application/json" \
  -d '{"name": "Acme Corp"}'
```

**5.2 Invalid Date Formats**
```bash
# Should return 400 Bad Request
curl -X GET "http://localhost:8081/cost-records?from=invalid-date&to=2025-01-31" \
  -H "X-Tenant-Id: $TENANT_A_ID"
```

**5.3 Future Date Range**
```bash
# Should return empty results (no error)
curl -X GET "http://localhost:8081/cost-records?from=2030-01-01&to=2030-01-31" \
  -H "X-Tenant-Id: $TENANT_A_ID"
```

### Postman Testing Collection

For easier testing, create a Postman collection with these requests:

1. **Environment Variables**
   - `base_url`: `http://localhost:8081`
   - `tenant_a_id`: (set after creating tenant)
   - `tenant_b_id`: (set after creating tenant)
   - `team_a1_id`: (set after creating team)

2. **Collection Structure**
   ```
   Cost Tracker API
   ├── Setup
   │   ├── Create Tenant A
   │   ├── Create Tenant B
   │   ├── Create Team A1
   │   └── Create Team A2
   ├── Operations
   │   ├── List Teams
   │   ├── Submit Cost Record
   │   └── List Cost Records
   ├── Reports
   │   ├── Total Cost Report
   │   └── Team Breakdown Report
   └── Security Tests
       ├── Missing Header Test
       ├── Invalid Tenant Test
       └── Cross-Tenant Access Test
   ```

## Database Schema

### Tables

#### tenants
```sql
CREATE TABLE tenants (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);
```

#### teams
```sql
CREATE TABLE teams (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    name VARCHAR(255) NOT NULL,
    UNIQUE(tenant_id, name)
);
```

#### cost_records
```sql
CREATE TABLE cost_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    team_id UUID NOT NULL REFERENCES teams(id),
    date DATE NOT NULL,
    cost DECIMAL(19,2) NOT NULL,
    UNIQUE(tenant_id, team_id, date)
);
```

### Database Access

To inspect the database directly:

```bash
# Connect to PostgreSQL
docker exec -it cost-tracker-postgres psql -U postgres -d tenantdb

# List tables
\dt

# View data
SELECT * FROM tenants;
SELECT * FROM teams;
SELECT * FROM cost_records;

# Exit
\q
```

## Security & Tenant Isolation

### Multi-Layer Security

1. **HTTP Filter Layer**
   - `TenantFilter` intercepts all requests
   - Validates `X-Tenant-Id` header presence and format
   - Verifies tenant exists in database
   - Bypasses only `POST /tenants` endpoint

2. **Application Layer**
   - `TenantContext` provides thread-local tenant storage
   - All service methods automatically scoped to current tenant
   - ThreadLocal cleanup in filter's finally block

3. **Data Layer**
   - All entities include `tenant_id` foreign key
   - Composite unique constraints prevent cross-tenant conflicts
   - Repository queries automatically include tenant filtering

4. **Database Layer**
   - Foreign key constraints ensure referential integrity
   - Unique constraints prevent duplicate data within tenants
   - UUID primary keys prevent ID guessing attacks

### Security Testing Checklist

- Cannot access other tenant's data with different X-Tenant-Id
- Cannot access any data without X-Tenant-Id header
- Cannot access data with invalid/non-existent tenant ID
- Cannot create teams/costs for non-existent tenants
- Cannot create duplicate tenants with same name
- Cannot create duplicate teams within same tenant
- Cannot create duplicate cost records for same team/date

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/multitenant/costtracker/
│   │   ├── config/          # Configuration classes
│   │   ├── context/         # TenantContext (ThreadLocal)
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Request/Response objects
│   │   ├── entity/         # JPA entities
│   │   ├── filter/         # HTTP filters
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic
│   │   └── CostTrackerApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/               # Test classes
```

### Key Components

- **TenantFilter**: HTTP request filtering and tenant validation
- **TenantContext**: Thread-local tenant ID storage
- **Entities**: Tenant, Team, CostRecord with proper relationships
- **Services**: Business logic with automatic tenant scoping
- **Controllers**: REST API endpoints with proper error handling

### Local Development Setup

1. **Without Docker** (requires local PostgreSQL):
   ```bash
   # Start PostgreSQL locally
   # Update application.properties with local DB settings
   ./gradlew bootRun
   ```

2. **With Docker** (recommended):
   ```bash
   docker compose up --build
   ```

### Building

```bash
# Build JAR
./gradlew build

# Run tests
./gradlew test

# Build Docker image
docker build -t cost-tracker .
```

## Troubleshooting

### Common Issues

#### 1. Application Won't Start
```bash
# Check if ports are available
netstat -an | grep 8081
netstat -an | grep 5432

# Check Docker containers
docker ps
docker logs cost-tracker-app
docker logs cost-tracker-postgres
```

#### 2. Database Connection Issues
```bash
# Restart containers
docker compose down
docker compose up --build

# Check database logs
docker logs cost-tracker-postgres
```

#### 3. 403 Forbidden Errors
- Ensure `X-Tenant-Id` header is included
- Verify tenant ID exists in database
- Check header format (must be valid UUID)

#### 4. 500 Internal Server Errors
```bash
# Check application logs
docker logs cost-tracker-app

# Common causes:
# - Invalid UUID format in requests
# - Missing required fields in request body
# - Database constraint violations
```

### Debug Commands

```bash
# View all containers
docker ps -a

# Follow application logs
docker logs -f cost-tracker-app

# Connect to database
docker exec -it cost-tracker-postgres psql -U postgres -d tenantdb

# Restart specific service
docker compose restart app
```

### Performance Monitoring

```bash
# Check container resource usage
docker stats

# Monitor database connections
docker exec cost-tracker-postgres psql -U postgres -d tenantdb -c "SELECT * FROM pg_stat_activity;"
```

## Key Design Decisions

### 1. UUID Primary Keys
- **Benefit**: Prevents ID enumeration attacks
- **Trade-off**: Slightly larger storage footprint
- **Alternative**: Sequential IDs with proper access controls

### 2. ThreadLocal Tenant Context
- **Benefit**: Clean service layer without tenant parameter passing
- **Trade-off**: Requires careful cleanup to prevent memory leaks
- **Alternative**: Explicit tenant parameter in all service methods

### 3. Filter-Based Authentication
- **Benefit**: Centralized tenant validation
- **Trade-off**: All requests go through filter overhead
- **Alternative**: Controller-level tenant validation

### 4. Single Database Multi-Tenancy
- **Benefit**: Easier management and cross-tenant analytics
- **Trade-off**: Requires careful query scoping
- **Alternative**: Database-per-tenant or schema-per-tenant

### 5. Manual UUID Generation
- **Benefit**: Consistent across all JPA implementations
- **Trade-off**: Slightly more code in services
- **Alternative**: JPA auto-generation (implementation dependent)

## Future Enhancements

### Short Term
- [ ] Input validation with custom error messages
- [ ] API documentation with OpenAPI/Swagger
- [ ] Logging and monitoring integration

### Medium Term
- [ ] Caching layer for improved performance
- [ ] Audit trail for all operations
- [ ] Bulk operations for cost records
- [ ] Export functionality (CSV, Excel)

### Long Term
- [ ] Authentication and authorization (JWT, OAuth2)
- [ ] Multi-region deployment support
- [ ] Advanced analytics and dashboards
- [ ] Integration with cloud cost APIs

---

## Support

For questions or issues:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review application logs: `docker logs cost-tracker-app`
3. Verify database state using the provided SQL commands
4. Test with the provided curl examples

**Happy Cost Tracking!**