# Presentation Layer

## Description
Presentation layer is responsible for displaying UI and handling user interactions. Uses MVVM pattern with ViewModel and LiveData/StateFlow.

## Structure
- **auth/**: Authentication screens (Login, Register)
- **main/**: Main app screens (Home, Food, Instamart, Dineout, Genie)
- **splash/**: Splash screen
- **common/**: Common UI components and utilities

## Principles
- Use MVVM architecture
- ViewModel should not contain Android framework references
- Use DataBinding and ViewBinding
- Implement proper state management
- Use Navigation Component

## Usage
Presentation layer uses Domain layer through Use Cases and ViewModels.
