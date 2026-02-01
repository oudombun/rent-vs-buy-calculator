# Quick Start Guide

## Prerequisites

- Android Studio (latest version recommended)
- JDK 17 or higher
- Android SDK with API level 23-36

## Getting Started

### 1. Open the Project

```bash
# Navigate to project directory
cd /Users/bunmonyoudom/Downloads/koin-starter-main

# Open in Android Studio
open -a "Android Studio" .
# OR simply open Android Studio and select the folder
```

### 2. Sync Gradle

Android Studio will automatically prompt you to sync Gradle. If not:
- Click on "File" → "Sync Project with Gradle Files"
- Wait for the sync to complete

### 3. Run the App

**Option A: Using Android Studio**
1. Click the "Run" button (green play icon) in the toolbar
2. Select an emulator or connected device
3. Wait for the app to build and install

**Option B: Using Command Line**
```bash
# Install on connected device
./gradlew installDebug

# Or build APK
./gradlew assembleDebug
# APK will be at: app/build/outputs/apk/debug/app-debug.apk
```

## App Features

### Step 1: Calculate Loan
1. Enter house price (e.g., 500000)
2. Enter annual interest rate (e.g., 5.5)
3. Select loan period (10, 15, or 20 years) or enter custom period
4. Tap "Calculate Loan"
5. View monthly payment and total interest

### Step 2: Compare Scenarios
1. Enter monthly rent (or 0 for rent-free, or leave empty)
2. Tap "Calculate Scenarios"
3. View comparison of different down payment options
4. Tap on any scenario to see detailed breakdown
5. Best option is highlighted in green

## Example Inputs

### Scenario 1: Buying a $500K house with $2000 rent
- House Price: `500000`
- Interest Rate: `5.5`
- Loan Period: `15`
- Monthly Rent: `2000`

**Result:** Shows which down payment percentage minimizes total cost

### Scenario 2: Living rent-free
- House Price: `300000`
- Interest Rate: `6.0`
- Loan Period: `15`
- Monthly Rent: `0`

**Result:** Shows how long it takes to save for the house

### Scenario 3: High rent area
- House Price: `800000`
- Interest Rate: `4.5`
- Loan Period: `20`
- Monthly Rent: `4500`

**Result:** Compares cost of renting vs buying with different down payments

## Troubleshooting

### Build Errors

**Problem:** "Sync failed" or "Could not resolve dependencies"
**Solution:** 
```bash
# Clear Gradle cache
./gradlew clean
./gradlew --stop
rm -rf ~/.gradle/caches/
```

**Problem:** "No Android SDK found"
**Solution:** Open Android Studio → Tools → SDK Manager → Install required SDK

**Problem:** KSP errors
**Solution:** Project no longer uses KSP after API removal. If you see KSP errors, sync Gradle again.

### Runtime Errors

**Problem:** App crashes on launch
**Solution:** 
1. Check Logcat in Android Studio
2. Verify Koin modules are properly configured
3. Ensure minimum SDK is 23 or higher

**Problem:** Calculations seem wrong
**Solution:**
1. Verify input numbers are reasonable
2. Check that interest rate is annual (not monthly)
3. Make sure house price and rent are in same currency

### UI Issues

**Problem:** Text is cut off
**Solution:** App is designed for phones. Use a phone emulator or device.

**Problem:** Cards don't expand
**Solution:** Tap on the card header/title area, not the edges.

## Project Structure

```
koin-starter-main/
├── app/
│   ├── build.gradle.kts          # App dependencies
│   └── src/main/
│       ├── AndroidManifest.xml   # App configuration
│       └── java/android/template/
│           ├── MyApplication.kt   # App entry point (Koin setup)
│           ├── ui/
│           │   ├── MainActivity.kt           # Main activity
│           │   └── calculator/
│           │       ├── CalculatorScreen.kt   # UI components
│           │       ├── CalculatorViewModel.kt # Business logic
│           │       └── CalculatorModels.kt    # Data classes
│           └── di/
│               └── AppModules.kt  # Dependency injection
├── build.gradle.kts              # Root build file
└── gradle/
    └── libs.versions.toml        # Dependency versions
```

## Making Changes

### Modify UI Colors

Edit: `app/src/main/java/android/template/ui/theme/Theme.kt`

### Change App Name

Edit: `app/src/main/res/values/strings.xml`
```xml
<string name="app_name">Your App Name</string>
```

### Add New Calculations

Add methods to: `CalculatorViewModel.kt`

### Modify UI Layout

Edit: `CalculatorScreen.kt`

## Testing

### Manual Testing Checklist

- [ ] App launches without errors
- [ ] Can enter house price
- [ ] Can enter interest rate
- [ ] Can select loan period
- [ ] Can enter custom loan period
- [ ] Calculate button works
- [ ] Results display correctly
- [ ] Can enter rent amount
- [ ] Calculate scenarios button works
- [ ] Scenarios display correctly
- [ ] Can expand/collapse scenario cards
- [ ] Best scenario is highlighted
- [ ] Rent-free scenario works (enter 0)
- [ ] App handles invalid inputs gracefully
- [ ] App works in portrait and landscape

### Unit Testing (Optional)

Create tests in: `app/src/test/java/`

Example:
```kotlin
class CalculatorViewModelTest {
    @Test
    fun `calculate monthly payment returns correct value`() {
        // Test calculation logic
    }
}
```

## Performance Tips

1. **Large Numbers:** App handles up to $1 billion house prices
2. **Long Loan Periods:** Custom periods up to 50 years supported
3. **Many Scenarios:** Shows 9 scenarios (0% to 100% down payment)
4. **Smooth UI:** Uses Compose for efficient recomposition

## Deployment

### Generate Signed APK

1. Build → Generate Signed Bundle/APK
2. Select APK
3. Create or select keystore
4. Choose release variant
5. APK will be in `app/release/`

### Increase Version

Edit `app/build.gradle.kts`:
```kotlin
versionCode = 2  // Increment this
versionName = "1.1"  // Update this
```

## Getting Help

### Documentation
- [Android Compose](https://developer.android.com/jetpack/compose)
- [Koin](https://insert-koin.io/)
- [Material Design 3](https://m3.material.io/)

### Project-Specific Docs
- `CALCULATOR_README.md` - Feature documentation
- `FLUTTER_TO_COMPOSE_MIGRATION.md` - Conversion details
- `PROJECT_SUMMARY.md` - What was changed
- `CLEANUP_GUIDE.md` - Remove legacy features

## Common Tasks

### Reset App State
```bash
# Clear app data
adb shell pm clear android.template
```

### View Logs
```bash
# Filter for your app
adb logcat | grep android.template
```

### Take Screenshots
```bash
# Use Android Studio's screenshot button in the emulator toolbar
# Or use adb
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png
```

## Next Steps

1. **Customize the app** for your needs
2. **Add features** like calculation history
3. **Improve UI** with animations
4. **Add tests** for reliability
5. **Publish** to Play Store (optional)

Happy coding! 🚀
