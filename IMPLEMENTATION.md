# Implementation Notes

Hello Greetings Atomity Team,

I am Rakesh & Wishing you Happy New Year,

So, Here's a brief about how I completed my task..

## Requirements Analysis

- **Tenant**: Multi-tenant system with strict isolation
- **Team**: Teams belonging to specific tenants
- **CostRecords**: Daily cost tracking per team
- **Strict Isolation of Tenants**: Can only access tenant via tenantId in header

At this point I was little thrown as I never worked with headers and stuff. I went through Google, YouTube and learnt about HTTP procedures again, and saw few examples via AI assistants that to work with HTTPServlets in Java, where I learnt more about Filters and ThreadLocals and successfully implemented it.

I also learnt about Docker a little, as much required by the Task!

I drew a rough diagram about data flow of the system.

## Implementation Journey

### Started Implementing it
There are so many things I learnt while implementing the Task:

1. **Implemented Entities** - Created Tenant, Team, and CostRecord entities with proper relationships

2. **Repositories** - Set up JPA repositories for data access

3. **Tried to test the DB** - Is it forming or not?
   At this point, PostgreSQL thrown an error, as I don't know how to use Docker properly. I thought it might create database with tables at that point that's not the case, I created DB manually at first.

4. **Context** - Implemented TenantContext for thread-local storage

5. **Filter** - Created TenantFilter to filter TenantId from headers

6. **Services** - Implemented business logic
   In Report Service, first I implemented it with direct List without DataStructures like Map, later on while working I just thought about using Map, I took help of few online resources and implemented it properly.
   
   There were few times I couldn't debug the issues so I used AI assistants like GPT, Gemini as debuggers.

7. **DTOs** - Created request/response objects

8. **Controllers** - Implemented REST API endpoints

## Testing

Tested the Tenant Isolation as well as all API endpoints manually.
- Created two tenants and checked all endpoints and isolation
- Verified that tenants cannot access each other's data

Everything about running and testing the program is in README in detail! I asked AmazonQ to generate a README for my project in detail and I also verified it.

## Reflection

I am sorry for taking so much time to submit this task. I completed it in around 2-3 days, while I also have few academic procedures for the final year project to look after.

Overall, I really enjoyed completing this task! It made me aware of my skills and talents again and there is much more to learn. It also showed me a clear fact of what I am capable of and my adaptability. I was able to learn very quickly and adapt to the situation in no time, which showed me how I am able to work in this industry of agile development.

## Key Learning Points

- **HTTP Headers & Filters**: Learned how to work with HTTP servlets and implement custom filters
- **ThreadLocal**: Understanding thread-local storage for request scoping
- **Docker**: Basic containerization and multi-service setup
- **Multi-tenancy**: Implementing strict tenant isolation at multiple layers
- **Spring Boot**: Advanced features like custom filters and JPA relationships
- **Problem Solving**: Using various resources and tools for debugging

## Technologies Used

- **Java 21** with Spring Boot 3.5.x
- **PostgreSQL** for database
- **Docker & Docker Compose** for containerization
- **JPA/Hibernate** for ORM
- **Gradle** for build management

Thanks for this opportunity! I don't know whether I pass or fail but I am looking forward to working with Atomity Team.

---
