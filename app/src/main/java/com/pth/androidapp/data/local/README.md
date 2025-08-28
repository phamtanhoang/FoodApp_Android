# Local Data Package

## Description
Contains all local data of the application (Database, Preferences, Cache).

## Structure
- **database/**: Room database and entities
- **preferences/**: SharedPreferences and encrypted preferences
- **cache/**: Cache management

## Principles
- Use Room for database
- Use EncryptedSharedPreferences for sensitive data
- Implement proper data validation
- Use Coroutines for async operations
