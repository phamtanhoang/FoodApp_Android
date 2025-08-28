# Data Layer

## Description
Data layer is responsible for managing all application data, including local storage, remote API, and data models.

## Structure
- **local/**: Local data (Database, Preferences, Cache)
- **remote/**: Data from server (API, DTO, Services)
- **repositories/**: Repository interface implementations
- **models/**: Data models and entities

## Principles
- Follow Repository pattern
- Use DataSource pattern for local and remote
- Implement proper error handling
- Use Kotlin Coroutines and Flow

## Usage
Data layer is used by Domain layer through Repository interfaces.
