# Take-Home Task: Multi-Tenant Cost Tracking API

## Overview

Build a simplified backend service that tracks daily cloud spend for multiple companies (tenants).
Each tenant can have multiple teams, and each team can submit daily cost records.

A key requirement of this task is strict tenant isolation. Data must never be accessible across tenants.

This task is designed to be completed within 3 to 5 days.

## Time expectation

- Estimated effort: 6 to 10 focused hours
- Focus on correctness, clean structure, and clear reasoning
- Feature completeness is less important than sound design decisions

If you make assumptions or simplify parts of the task, document them clearly in your README.

## Tech stack requirements

You must use:
- Java
- Spring Boot
- PostgreSQL
- Docker (Dockerfile + docker-compose)

You may use:
- Spring Data JPA / Hibernate or plain SQL (your choice)
- Flyway or Liquibase for database migrations (recommended, not mandatory)


## Tooling & Versions (Standardized)

To keep evaluation consistent, please use the following toolchain:

- **Java:** JDK 21 (LTS)
- **Framework:** Spring Boot 3.5.x
- **Build Tool:** Gradle 8.14.x **via Gradle Wrapper** (Kotlin DSL)
  - Use `./gradlew`
  - Do **not** rely on a system-installed Gradle
- **Container Runtime:** Docker + Docker Compose


## Core requirements

### 1) Data model

Create the following entities/tables:

#### Tenant
- id (UUID or long)
- name (string, unique)

#### Team
- id
- tenant_id (foreign key to Tenant)
- name

#### CostRecord
- id
- team_id (foreign key to Team)
- date (YYYY-MM-DD)
- cost_amount (decimal)

Recommended constraints:
- tenant.name must be unique
- (tenant_id, team.name) must be unique
- (team_id, date) must be unique to prevent duplicate cost entries

### 2) Multi-tenancy rule (critical)

All requests must be scoped to a single tenant using a required HTTP header:

X-Tenant-Id: <tenant_id>

Tenant creation (`POST /tenants`) is a global operation and does not require the `X-Tenant-Id` header.
Clients must first create a tenant, then use the returned tenant `id` value as `X-Tenant-Id` for all other API calls.

Rules:
- If X-Tenant-Id is missing or invalid, return 400 Bad Request
- Tenant scoping must be enforced server-side for all endpoints
- A tenant must never access another tenant’s teams, cost records, or reports
- Do not rely on the client to send correct IDs. Enforce isolation in the backend

### 3) REST API endpoints

#### Tenants

Create tenant
- POST /tenants

Request body:
```json
{
  "name": "Acme Corp"
}
```


Response example:
```json
{
  "id": "c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11",
  "name": "Acme Corp"
}
```

#### Teams

Create a team under the current tenant
- POST /teams
- Requires header: X-Tenant-Id

Request body:
```json
{
  "name": "Platform Team"
}
```

Response example:
```json
{
  "id": "b4f7f1c0-7c1d-44e9-a52e-1c3e5b4b22aa",
  "tenantId": "c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11",
  "name": "Platform Team"
}
```

List teams for the current tenant
- GET /teams
- Requires header: X-Tenant-Id

#### Cost Records

Submit a daily cost record for a team
- POST /cost-records
- Requires header: X-Tenant-Id

Request body:
```json
{
  "teamId": "b4f7f1c0-7c1d-44e9-a52e-1c3e5b4b22aa",
  "date": "2025-01-01",
  "costAmount": 123.45
}
```

Rules:
- The team must belong to the tenant specified by X-Tenant-Id
- If the team does not exist under that tenant, return 404 Not Found
- If a cost record for (teamId, date) already exists, return 409 Conflict

List cost records for the tenant’s teams within a date range
- GET /cost-records?from=YYYY-MM-DD&to=YYYY-MM-DD
- Requires header: X-Tenant-Id

#### Reporting

Get total cost for a tenant over a date range
- GET /reports/total?from=YYYY-MM-DD&to=YYYY-MM-DD
- Requires header: X-Tenant-Id

Response example:
```json
{
  "tenantId": "c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11",
  "from": "2025-01-01",
  "to": "2025-01-31",
  "totalCost": 4567.89
}
```

Get cost breakdown by team for a tenant over a date range
- GET /reports/by-team?from=YYYY-MM-DD&to=YYYY-MM-DD
- Requires header: X-Tenant-Id

Response example:
```json
{
  "tenantId": "c1b0f0a2-1c7a-4df6-9a2d-9f2b0a1d1b11",
  "from": "2025-01-01",
  "to": "2025-01-31",
  "items": [
    {
      "teamId": "b4f7f1c0-7c1d-44e9-a52e-1c3e5b4b22aa",
      "teamName": "Platform Team",
      "totalCost": 3000.00
    },
    {
      "teamId": "d2a1d2f0-2db9-4e7c-9f12-91b2f7f21011",
      "teamName": "Data Team",
      "totalCost": 1567.89
    }
  ]
}
```

## Validation and error handling

At minimum:
- Validate required fields and basic formats
- Use meaningful HTTP status codes:
  - 400 for invalid input or missing X-Tenant-Id
  - 404 for resources not found within tenant scope
  - 409 for duplicates
- Error response format is your choice but must be consistent

## Docker requirements (mandatory)

Your solution must include:

1. Dockerfile
- Builds and runs the Spring Boot application

2. docker-compose.yml
- Starts PostgreSQL and the application
- Uses environment variables for database configuration
- Exposes ports so the API is reachable locally

3. One-command run:
docker compose up --build

After startup, the API must be accessible for testing.

## Deliverables

Your submission repository must contain:

- Full source code
- Dockerfile
- docker-compose.yml
- A README.md that explains:
  - How to run the project
  - How tenant scoping is enforced
  - Example API calls (curl examples are sufficient)
  - Key design decisions and trade-offs
  - What you would improve with more time

## Notes

- No UI is required
- Keep the solution simple and production-minded
- Partial solutions are acceptable if clearly explained

Good luck,
Team Atomity
