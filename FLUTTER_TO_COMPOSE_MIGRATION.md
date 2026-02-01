# Flutter to Compose Migration Guide

This document explains how the Flutter Rent vs Loan Calculator was converted to Jetpack Compose.

## Architecture Comparison

### Flutter (Original)
```
main.dart
├── MyApp (MaterialApp)
├── CalculatorScreen (StatefulWidget)
└── _CalculatorScreenState
    ├── State variables
    ├── Business logic methods
    └── UI builder methods
```

### Compose (Converted)
```
CalculatorViewModel.kt    # State + Business Logic
CalculatorModels.kt        # Data classes
CalculatorScreen.kt        # UI Components
```

## Component Mapping

| Flutter | Compose | Notes |
|---------|---------|-------|
| `StatefulWidget` | `ViewModel` + `@Composable` | State management separated |
| `State<T>` | `StateFlow<T>` | Reactive state |
| `setState()` | `MutableStateFlow.update()` | State updates |
| `TextEditingController` | `String` in state | No controller needed |
| `GlobalKey<FormState>` | Validation in ViewModel | Direct validation |
| `Widget build()` | `@Composable fun` | UI composition |
| `Column`, `Row` | `Column`, `Row` | Same names! |
| `Container` | `Box`, `Card`, `Surface` | Multiple options |
| `Card` | `Card` | Similar API |
| `TextField` | `OutlinedTextField` | Material 3 |
| `ElevatedButton` | `Button` | Material 3 |
| `ExpansionTile` | Custom with `animateContentSize()` | Built manually |
| `Future.delayed()` | `delay()` from coroutines | Suspend function |
| `SegmentedButton` | `FilterChip` in Row | Similar concept |
| `CheckboxListTile` | `Checkbox` + `Text` | Compose separately |

## Key Differences

### State Management

**Flutter:**
```dart
class _CalculatorScreenState extends State<CalculatorScreen> {
  double? monthlyPayment;
  
  void calculateStep1() {
    setState(() {
      monthlyPayment = calculatedValue;
    });
  }
}
```

**Compose:**
```kotlin
class CalculatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()
    
    fun calculateLoan() {
        _state.update { it.copy(
            loanCalculation = calculatedValue
        )}
    }
}
```

### UI Construction

**Flutter:**
```dart
Widget _buildStep1Section() {
  return Form(
    child: Column(
      children: [
        TextField(...),
        ElevatedButton(...),
      ],
    ),
  );
}
```

**Compose:**
```kotlin
@Composable
fun LoanCalculatorSection(state: CalculatorState, viewModel: CalculatorViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OutlinedTextField(...)
        Button(...)
    }
}
```

### Input Validation

**Flutter:**
```dart
String? validateHousePrice(String? value) {
  if (value == null || value.isEmpty) {
    return 'Please enter house price';
  }
  // ... more validation
  return null;
}
```

**Compose:**
```kotlin
private fun validateHousePrice(value: String): Double? {
    if (value.isEmpty()) {
        _state.update { it.copy(housePriceError = "Please enter house price") }
        return null
    }
    // ... more validation
    return price
}
```

### Async Operations

**Flutter:**
```dart
void calculateStep1() async {
  setState(() {
    isCalculatingStep1 = true;
  });
  
  await Future.delayed(Duration(milliseconds: 300));
  
  // calculations...
  
  setState(() {
    isCalculatingStep1 = false;
  });
}
```

**Compose:**
```kotlin
fun calculateLoan() {
    viewModelScope.launch {
        _state.update { it.copy(isCalculatingStep1 = true) }
        
        delay(300)
        
        // calculations...
        
        _state.update { it.copy(isCalculatingStep1 = false) }
    }
}
```

## Data Models

### Flutter (Inline Class)
```dart
class ScenarioResult {
  final String name;
  final int percentSaved;
  final double monthsRenting;
  // ...
  
  ScenarioResult({
    required this.name,
    required this.percentSaved,
    required this.monthsRenting,
    // ...
  });
}
```

### Kotlin (Data Class)
```kotlin
data class ScenarioResult(
    val name: String,
    val percentSaved: Int,
    val monthsRenting: Double,
    // ...
)
```

## UI Components

### Cards with Expansion

**Flutter:**
```dart
ExpansionTile(
  title: Text(...),
  children: [
    // expanded content
  ],
)
```

**Compose:**
```kotlin
@Composable
fun ScenarioCard(isExpanded: Boolean, onToggle: () -> Unit) {
    Card {
        Column(modifier = Modifier.animateContentSize()) {
            Surface(onClick = onToggle) {
                // Header - always visible
            }
            if (isExpanded) {
                // Expanded content
            }
        }
    }
}
```

### Conditional Rendering

**Flutter:**
```dart
if (condition) ...[
  Widget1(),
  Widget2(),
]
```

**Compose:**
```kotlin
if (condition) {
    Widget1()
    Widget2()
}
```

## Number Formatting

**Flutter:**
```dart
import 'package:intl/intl.dart';

String formatCurrency(double amount) {
  return NumberFormat.currency(symbol: '\$', decimalDigits: 0).format(amount);
}
```

**Compose:**
```kotlin
import java.text.NumberFormat
import java.util.*

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }.format(amount)
}
```

## Color System

**Flutter:**
```dart
Color(0xFF2F2F2F)
Color(0xFF2ECC71)
```

**Compose:**
```kotlin
Color(0xFF2F2F2F)
Color(0xFF2ECC71)
```
*Note: Colors are identical in both!*

## Material Design

### Flutter (Material 3)
```dart
MaterialApp(
  theme: ThemeData(
    useMaterial3: true,
    primaryColor: Color(0xFF2F2F2F),
  ),
)
```

### Compose (Material 3)
```kotlin
MaterialTheme {
    // Material 3 by default
    // Colors defined in Theme.kt
}
```

## Testing Considerations

### Flutter Testing
- Widget tests
- Integration tests
- Unit tests for business logic

### Compose Testing
- Compose UI tests with `ComposeTestRule`
- ViewModel tests with `turbine` or direct Flow testing
- Unit tests for calculations

## Performance Notes

1. **Flutter**: Rebuilds widget tree on setState
2. **Compose**: Recomposes only changed UI elements (smart recomposition)

## Benefits of Compose Version

1. **Separation of Concerns**: Clear separation between UI and business logic
2. **Type Safety**: Kotlin's strong type system
3. **State Management**: Reactive state with Flow
4. **Modern Android**: Native Android components and patterns
5. **Testability**: ViewModel can be tested independently
6. **Lifecycle Aware**: Automatic cleanup with viewModelScope

## Potential Improvements

1. Add proper error handling with sealed classes
2. Implement saved state for configuration changes
3. Add navigation for multiple screens
4. Implement dependency injection for ViewModel
5. Add unit tests for calculations
6. Add UI tests for user flows

## Migration Checklist

- [x] Convert state management to ViewModel + StateFlow
- [x] Convert UI to Composable functions
- [x] Implement input validation
- [x] Port all calculations
- [x] Style with Material 3
- [x] Handle loading states
- [x] Implement expandable cards
- [x] Add proper spacing and padding
- [x] Format numbers correctly
- [x] Handle edge cases (rent-free scenario)
- [x] Remove chart visualization (can be added later with chart library)

## Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [ViewModel Guide](https://developer.android.com/topic/libraries/architecture/viewmodel)
