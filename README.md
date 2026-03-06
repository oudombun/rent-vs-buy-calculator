# Rent or Buy Calculator

**Should you rent or buy?** Make an informed decision with clear, data-driven insights. This Android app compares the true costs of renting versus buying a home so you can approach one of life’s biggest financial decisions with confidence.

[![Get it on Google Play](https://img.shields.io/badge/Google_Play-Get_it_on_Google_Play-414141?style=for-the-badge&logo=google-play)](https://play.google.com/store/apps/details?id=com.oudombun.rentvsown)

**Download:** [Rent or Buy Calculator on Google Play](https://play.google.com/store/apps/details?id=com.oudombun.rentvsown)

---

## 📱 App listing (Play Store)

### Short description
Rent or Buy Calculator helps you compare the true costs of renting versus buying a home. Get clear, data-driven insights to make one of life's biggest financial decisions with confidence.

### Key features (store listing)

- **📊 Loan calculator** – Calculate exact monthly mortgage payments, total interest over the loan period, and support for 10-, 15-, 20-year or custom-term loans with instant, detailed breakdowns.
- **💰 Rent vs buy comparison** – Compare multiple saving scenarios (20%, 40%, 50%, 60%, 70%, 80%, 90%, 100% down), factor in monthly rent, see how long it takes to save for a down payment, and view total costs ranked from best to worst.
- **🎯 Best option** – Automatically identifies the most cost-effective strategy, compares rent paid vs interest paid, shows payoff timelines per scenario, and accounts for different down payment amounts.

### How it works

1. Enter home price and interest rate.  
2. Choose loan period (10, 15, 20 years or custom).  
3. Calculate your monthly mortgage payment.  
4. Enter current monthly rent (or 0 if rent-free).  
5. Get instant comparisons of all buying scenarios.

The app shows whether it’s better to buy now with a full loan, save for a partial down payment first, or save up and buy with cash (no loan).

---

## 📱 What This App Does (summary)

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

### Tech stack

| Category      | Technology |
|---------------|------------|
| **Language**  | Kotlin 2.2 |
| **Min SDK**   | 23 |
| **Target SDK**| 36 |
| **UI**       | Jetpack Compose (Compose BOM 2025.01), Material 3 |
| **Architecture** | MVVM |
| **DI**        | Koin 4.x (Android + Compose) |
| **State**     | Kotlin Flow & StateFlow |
| **Navigation**| Jetpack Navigation Compose |
| **Build**     | Gradle 8.x (Kotlin DSL), JDK 17 |

---

## 📁 Project Structure

```
app/src/main/java/com/oudombun/rentvsown/
├── ui/
│   ├── calculator/              # 🎯 Main calculator feature
│   │   ├── CalculatorScreen.kt
│   │   ├── CalculatorViewModel.kt
│   │   └── CalculatorModels.kt
│   ├── about/                  # About screen
│   │   └── AboutScreen.kt
│   ├── theme/                  # Material 3 theming
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── MainActivity.kt
├── di/
│   └── AppModules.kt           # Koin configuration
└── HomeCalculatorApp.kt        # Application class
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
