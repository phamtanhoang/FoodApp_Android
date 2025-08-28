# Domain Layer

## Description
Domain layer contains business logic and rules of the application. This is the central layer that doesn't depend on other layers.

## Structure
- **entities/**: Domain entities (business objects)
- **repositories/**: Repository interfaces (contracts)
- **usecases/**: Business logic use cases
- **models/**: Domain models

## Principles
- No dependency on Android framework
- Contains pure business logic
- Use Repository pattern
- Implement Use Case pattern

## Usage
Domain layer is used by Presentation layer through Use Cases.
