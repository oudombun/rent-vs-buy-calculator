# Rent vs Loan Calculator - Android App

A professional Android calculator app built with **Jetpack Compose**, **Koin**, and **MVVM architecture**. This app helps users make informed financial decisions by comparing the costs of buying vs renting property with different down payment scenarios.

## 📱 What This App Does

Calculate and compare:
- Monthly loan payments based on house price, interest rate, and loan period
- Different down payment scenarios (0% to 100%)
- Total costs including rent paid and interest
- Best financial strategy highlighted automatically

**Converted from Flutter to native Android Compose!**

---

## 🚀 Quick Start

### Requirements
- **JDK 17 or later** (see [BUILD_REQUIREMENTS.md](./BUILD_REQUIREMENTS.md))
- Android Studio (latest stable)
- Android device/emulator (API 23+)

### Run the App
1. Open project in Android Studio
2. Wait for Gradle sync
3. Click Run ▶️

For detailed instructions, see [QUICK_START.md](./QUICK_START.md)

---

## 📚 Documentation

### Essential Docs
- **[QUICK_START.md](./QUICK_START.md)** - Get up and running quickly
- **[CALCULATOR_README.md](./CALCULATOR_README.md)** - Features & technology stack
- **[PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md)** - What changed in this project
- **[BUILD_REQUIREMENTS.md](./BUILD_REQUIREMENTS.md)** - Java & build setup
- **[CLEANUP_COMPLETE.md](./CLEANUP_COMPLETE.md)** - ✨ Clean project structure details

### Migration & Architecture
- **[FLUTTER_TO_COMPOSE_MIGRATION.md](./FLUTTER_TO_COMPOSE_MIGRATION.md)** - How Flutter was converted to Compose
- **[KOIN_BASICS.md](./KOIN_BASICS.md)** - Dependency injection setup
- **[COMPOSE_MIGRATION.md](./COMPOSE_MIGRATION.md)** - Compose patterns used

### Optional
- **[CLEANUP_GUIDE.md](./CLEANUP_GUIDE.md)** - Remove legacy features if needed
- **[REAL_FEATURE_GUIDE.md](./REAL_FEATURE_GUIDE.md)** - Add new features

---

## ✨ Features

### Loan Calculator
- Input house price, interest rate, loan period
- Support for custom loan periods
- Real-time validation
- Calculate monthly payments and total interest

### Scenario Comparison
- Compare 9 different down payment scenarios
- Support for rent-free living (0 rent)
- Automatic sorting to find best option
- Detailed breakdowns for each scenario
- Expandable cards with full information

### UI/UX
- Material Design 3
- Smooth animations
- Loading states
- Error handling
- Responsive layout

---

## 🏗️ Architecture

```
MVVM + Compose + Koin
├── UI Layer: Jetpack Compose (@Composable functions)
├── ViewModel: Business logic + State management (StateFlow)
├── Models: Data classes
└── DI: Koin modules
```

### Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM
- **DI:** Koin
- **State:** Kotlin Flow & StateFlow

---

## 📁 Project Structure

```
app/src/main/java/android/template/
├── ui/
│   ├── calculator/              # 🎯 Main calculator feature
│   │   ├── CalculatorScreen.kt
│   │   ├── CalculatorViewModel.kt
│   │   └── CalculatorModels.kt
│   ├── theme/                   # Material 3 theming
│   └── MainActivity.kt
├── data/
│   └── local/database/          # Room (for future use)
├── di/
│   └── AppModules.kt           # Koin configuration
└── MyApplication.kt            # App entry point
```

---

## 🎯 What Changed from Template

### ✅ Added
- ✨ Complete calculator UI in Compose
- ✨ CalculatorViewModel with all business logic
- ✨ Input validation and error handling
- ✨ Scenario comparison algorithms
- ✨ Material 3 UI components
- ✨ Comprehensive documentation

### ❌ Removed (Clean Codebase!)
- 🗑️ All Note-related starter code
- 🗑️ API/Network layer (Retrofit, OkHttp)
- 🗑️ Room database and KSP dependencies
- 🗑️ Remote data sources
- 🗑️ Legacy UI and ViewModels

### 💎 Result
- ✓ Pure calculator app - no legacy code
- ✓ Minimal dependencies (Compose + Koin only)
- ✓ Fast builds (no annotation processing)
- ✓ Clean architecture ready to extend

---

## 🧮 Example Usage

### Scenario 1: Standard Home Purchase
```
House Price: $500,000
Interest Rate: 5.5%
Loan Period: 15 years
Monthly Rent: $2,000
```
**Result:** Best option might be 40% down payment

### Scenario 2: Living Rent-Free
```
House Price: $300,000
Interest Rate: 6.0%
Loan Period: 15 years
Monthly Rent: $0
```
**Result:** Shows how long to save for full purchase

---

## 🔧 Development

### Build
```bash
./gradlew assembleDebug
```

### Install
```bash
./gradlew installDebug
```

### Tests (to be added)
```bash
./gradlew test
```

---

## 🚦 Next Steps

1. **Run the app** - Follow [QUICK_START.md](./QUICK_START.md)
2. **Understand the code** - Read [CALCULATOR_README.md](./CALCULATOR_README.md)
3. **Customize** - Modify colors, add features
4. **Add features** - History, charts, sharing

### Future Enhancements
- 📊 Add charts (bar/line graphs)
- 💾 Save calculation history to Room
- 📤 Export/share results
- 🌙 Dark mode
- 🌍 Multiple currencies
- 📈 Property appreciation calculator

---

## 📄 License

Licensed under the Apache License, Version 2.0

---

## 🤝 Contributing

This is a template/starter project. Feel free to:
- Use it as a base for your own calculator apps
- Modify for different financial calculations
- Add features and improvements
- Learn Compose and Koin patterns from the code

---

## 📞 Need Help?

1. Check the documentation files above
2. Review code comments in `CalculatorViewModel.kt` and `CalculatorScreen.kt`
3. See [FLUTTER_TO_COMPOSE_MIGRATION.md](./FLUTTER_TO_COMPOSE_MIGRATION.md) for patterns

---

**Built with ❤️ using Kotlin, Jetpack Compose, and Koin**
