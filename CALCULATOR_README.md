# Rent vs Loan Calculator - Android App

A comprehensive Android application built with Jetpack Compose and Koin that helps users make informed decisions about buying vs renting property.

## Features

### Loan Calculator
- Calculate monthly loan payments based on house price, interest rate, and loan period
- Support for standard loan periods (10, 15, 20 years) or custom periods
- Real-time validation of inputs
- Display total interest paid over the loan period

### Rent Comparison
- Compare different down payment scenarios (0%, 20%, 40%, 50%, 60%, 70%, 80%, 90%, 100%)
- Calculate net savings after rent
- Support for rent-free scenarios (living with family)
- Detailed breakdown of each scenario showing:
  - Time renting vs buying
  - Total rent paid
  - Down payment amount
  - Loan amount and payoff time
  - Interest paid
  - Total cost comparison

### Smart Analysis
- Automatically sorts scenarios by total cost to find the best option
- Shows detailed calculations for each scenario
- Expandable cards for detailed information
- Visual indicators for the best financial option

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Koin
- **Database**: Room (kept for future features)
- **State Management**: Kotlin Flow & StateFlow

## Project Structure

```
app/src/main/java/android/template/
├── data/
│   ├── local/
│   │   └── database/
│   │       ├── AppDatabase.kt
│   │       └── Note.kt
│   └── NoteRepository.kt
├── di/
│   └── AppModules.kt
├── ui/
│   ├── calculator/
│   │   ├── CalculatorModels.kt      # Data models
│   │   ├── CalculatorViewModel.kt    # Business logic
│   │   └── CalculatorScreen.kt       # UI components
│   ├── note/                         # Legacy note feature (can be removed)
│   └── theme/
│       ├── Theme.kt
│       └── Type.kt
└── MyApplication.kt
```

## Key Components

### CalculatorViewModel
Handles all business logic including:
- Input validation
- Loan payment calculations using amortization formula
- Scenario comparisons
- State management

### CalculatorScreen
Compose UI with two main sections:
1. **Loan Calculator Section**: Input house price, interest rate, and loan period
2. **Rent Comparison Section**: Input rent amount and view scenario comparisons

## Calculations

### Monthly Payment Formula
```
M = P * [r(1+r)^n] / [(1+r)^n - 1]
Where:
M = Monthly payment
P = Principal (loan amount)
r = Monthly interest rate
n = Number of months
```

### Scenario Comparison
For each down payment percentage:
1. Calculate months needed to save the down payment
2. Calculate total rent paid during saving period
3. Calculate loan amount after down payment
4. Calculate interest paid on remaining loan
5. Sum rent + interest for total cost

## Changes from Original Template

### Removed
- ✗ API/Network layer (Retrofit, OkHttp)
- ✗ NoteApiService and related remote data sources
- ✗ Network module in Koin configuration

### Kept
- ✓ Room database (for potential future features)
- ✓ Koin dependency injection
- ✓ Jetpack Compose setup
- ✓ Modern Android architecture

## Build & Run

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on emulator or device (minSdk 23)

## Dependencies

```kotlin
// Core
androidx.core:core-ktx
androidx.lifecycle:lifecycle-runtime-ktx

// Compose
androidx.compose.bom
androidx.compose.ui
androidx.compose.material3
androidx.activity:activity-compose
androidx.lifecycle:lifecycle-viewmodel-compose
androidx.navigation:navigation-compose

// Koin
io.insert-koin:koin-android
io.insert-koin:koin-androidx-compose

// Room (for future use)
androidx.room:room-runtime
androidx.room:room-ktx
```

## Future Enhancements

- Save calculation history to Room database
- Add graphs/charts for visual comparison
- Support for different currencies
- Property appreciation calculations
- Tax benefit calculations
- Export results as PDF/share functionality

## Converted from Flutter

This app is a Kotlin/Compose conversion of a Flutter application, maintaining all core functionality while leveraging Android-native UI components and architecture patterns.

## License

Licensed under the Apache License, Version 2.0
