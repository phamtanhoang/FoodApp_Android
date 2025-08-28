# 🍔 FoodApp Android

## 📱 Description
Food Delivery application built with Kotlin, using Clean Architecture and modern Android development practices.

## 🏗️ Architecture
Project uses **Clean Architecture** with 4 main layers:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  (Activities, Fragments, ViewModels, UI Components)       │
├─────────────────────────────────────────────────────────────┤
│                     Domain Layer                           │
│  (Use Cases, Entities, Repository Interfaces)             │
├─────────────────────────────────────────────────────────────┤
│                      Data Layer                            │
│  (Repositories, Data Sources, Models, DTOs)               │
├─────────────────────────────────────────────────────────────┤
│                      Core Layer                            │
│  (Base Classes, Utilities, DI, Network)                   │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Main Features
- ✅ **Authentication**: Login/Register with Firebase
- 🏠 **Home**: Image slider with auto-play
- 🍕 **Food**: Food management
- 🛒 **Instamart**: Grocery delivery
- 🍽️ **Dineout**: Restaurant booking
- 🧞 **Genie**: Various services
- 🌐 **Multi-language**: Support for Vietnamese and English

## 🛠️ Technologies Used
- **Language**: Kotlin
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Database**: Room
- **Network**: Retrofit + OkHttp
- **Image Loading**: Glide
- **Navigation**: Navigation Component
- **UI**: Material Design + ViewBinding
- **Authentication**: Firebase Auth
- **Backend**: Firebase Firestore

## 📁 Project Structure

```
app/src/main/java/com/pth/androidapp/
├── core/                           # Core functionality
│   ├── base/                       # Base classes
│   ├── common/                     # Common utilities
│   ├── di/                         # Dependency injection
│   ├── extensions/                 # Extension functions
│   ├── network/                    # Network utilities
│   ├── security/                   # Security utilities
│   └── utils/                      # Utility classes
├── data/                           # Data layer
│   ├── local/                      # Local data sources
│   ├── remote/                     # Remote data sources
│   ├── repositories/               # Repository implementations
│   └── models/                     # Data models
├── domain/                         # Domain layer
│   ├── entities/                   # Domain entities
│   ├── repositories/               # Repository interfaces
│   ├── usecases/                   # Use cases
│   └── models/                     # Domain models
├── presentation/                    # Presentation layer
│   ├── auth/                       # Authentication
│   ├── main/                       # Main app
│   ├── splash/                     # Splash screen
│   └── common/                     # Common UI components
└── MyApplication.kt
```

## 🚀 Installation and Setup

### Requirements
- Android Studio Hedgehog
- Android SDK 24+ (API level 24)
- JDK 17

### Installation
1. Clone repository:
```bash
git clone https://github.com/your-username/FoodApp_Android.git
```

2. Open project in Android Studio

3. Sync Gradle files

4. Run application on device or emulator

## 📖 Usage Guide

### For Developers
- Each package has its own README.md file
- Follow Clean Architecture principles
- Use Hilt for dependency injection
- Implement proper error handling

### For Contributors
- Fork project
- Create feature branch
- Commit changes with clear messages
- Create Pull Request

⭐ If this project is helpful, please give it a star!