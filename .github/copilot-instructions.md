# Copilot Instructions

## Project Structure

This repository contains multiple projects:

- **Frontend**: A Next.js application located at `apps/frontend`
- **Content API**: A Python FastAPI application with GraphQL located at `apps/content`

## Best Practices

### Frontend (Next.js)

When working with the frontend codebase:

- Follow React hooks best practices (dependencies arrays, avoid unnecessary rerenders)
- Use TypeScript for type safety wherever possible
- Implement proper component structure with clear separation of concerns
- Follow the established project patterns for state management
- Use CSS modules or styled-components according to the existing pattern
- Ensure responsive design considerations are maintained
- Keep bundle size in mind when adding dependencies
- Follow accessibility (a11y) best practices

### Content API (Python/FastAPI/GraphQL)

When working with the content API:

- Follow Ruff style guidelines for Python code listed in `ruff.toml`
- Use type hints consistently throughout Python code
- Structure GraphQL schemas following established patterns in the project
- Implement proper error handling and validation
- Use async/await patterns appropriately with FastAPI
- Keep database queries optimized and prevent N+1 query issues
- Handle authentication and authorization consistently
- Use pytest for testing and ensure coverage is maintained

## Testing and Linting

### Frontend

Before submitting any changes to the frontend:

```bash
# Navigate to frontend directory
cd apps/frontend

# Run linting
npm run lint

# Run tests
npm run test
```

### Content API

Before submitting any changes to the content API:

```bash
# Navigate to content directory
cd apps/content

# Run linting
ruff check .

# Run tests
uv run pytest . --cov
```

Always ensure all tests pass and linting issues are resolved before considering any changes complete.

## Contributing

When making changes:

1. Understand the existing architecture before modifying
2. Make minimal, focused changes that address specific requirements
3. Document any complex logic or architecture decisions
4. Update relevant documentation when changing functionality
5. Always run appropriate tests and linting commands before submitting changes

