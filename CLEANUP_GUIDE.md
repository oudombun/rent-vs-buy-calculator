# Optional: Clean Up Legacy Note Feature

This guide explains how to remove the legacy Note feature if you don't need it.

## Files to Delete

### UI Layer
```
app/src/main/java/android/template/ui/note/
├── NoteFragment.kt
└── NoteViewModel.kt
```

### Data Layer
```
app/src/main/java/android/template/data/
├── NoteRepository.kt
└── local/database/
    ├── AppDatabase.kt
    ├── Note.kt
    └── NoteDao.kt (implicit in AppDatabase)
```

### Database Schemas
```
app/schemas/
└── android.template.data.local.database.AppDatabase/
    ├── 1.json
    └── 2.json
```

## Code Changes Required

### 1. Update AppModules.kt

**Remove:**
```kotlin
import android.template.data.DefaultNoteRepository
import android.template.data.NoteRepository
import android.template.data.local.database.AppDatabase
import android.template.data.local.database.NoteDao
import android.template.ui.note.NoteViewModel

val databaseModule = module {
    single {
        androidx.room.Room.databaseBuilder(
            get<Application>(),
            AppDatabase::class.java,
            "notes"
        ).fallbackToDestructiveMigration().build()
    }
    single<NoteDao> { get<AppDatabase>().noteDao() }
}

val appModule = module {
    single<NoteRepository> { DefaultNoteRepository(get()) }
    viewModel { NoteViewModel(get()) }
    viewModel { android.template.ui.calculator.CalculatorViewModel() }
}
```

**Replace with:**
```kotlin
import android.template.ui.calculator.CalculatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { CalculatorViewModel() }
}
```

### 2. Update MyApplication.kt

**Change:**
```kotlin
modules(databaseModule, appModule)
```

**To:**
```kotlin
modules(appModule)
```

### 3. Update build.gradle.kts

**Remove Room dependencies:**
```kotlin
// Room
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)
```

**Remove KSP plugin:**
```kotlin
plugins {
    // ... other plugins
    // alias(libs.plugins.ksp)  // Remove this line
}
```

**Remove Room schema configuration:**
```kotlin
// Enable room auto-migrations
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}
```

### 4. Update strings.xml

**Remove unused strings:**
```xml
<string name="hint_note">Note</string>
<string name="save">Save</string>
<string name="saved_note">Note: %s</string>
<string name="sync">Sync</string>
```

**Keep only:**
```xml
<resources>
    <string name="app_name">Rent vs Loan Calculator</string>
</resources>
```

## Why Keep the Database?

If you decide to KEEP the database infrastructure, here are some use cases:

### Future Features with Room

1. **Calculation History**
```kotlin
@Entity
data class CalculationHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val housePrice: Double,
    val interestRate: Double,
    val loanPeriod: Double,
    val rent: Double,
    val bestScenario: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

2. **Saved Scenarios**
```kotlin
@Entity
data class SavedScenario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val housePrice: Double,
    val interestRate: Double,
    val loanPeriod: Double,
    val rent: Double
)
```

3. **User Preferences**
```kotlin
@Entity
data class UserPreferences(
    @PrimaryKey val id: Int = 1,
    val defaultLoanPeriod: Int = 15,
    val defaultInterestRate: Double = 5.0,
    val currency: String = "USD"
)
```

## Decision Matrix

| Keep Database | Remove Database |
|---------------|-----------------|
| ✓ Plan to add history | ✓ Simpler project |
| ✓ Want saved scenarios | ✓ Fewer dependencies |
| ✓ Need offline storage | ✓ No database maintenance |
| ✓ User preferences | ✓ Smaller APK size |

## Recommended Approach

**For a calculator app, I recommend KEEPING the database** because:

1. Users often want to compare calculations they did previously
2. Saving favorite scenarios is useful
3. Room is already set up and working
4. Adding features later is easier
5. The overhead is minimal

## If You Remove Database

After removing all database code, you'll have a pure calculation app with:
- ViewModel for state management
- Compose UI
- Koin for ViewModel injection (overkill, but works)
- No data persistence

The app will be simpler but lose the ability to save anything.

## Command to Remove Files (macOS/Linux)

```bash
cd /Users/bunmonyoudom/Downloads/koin-starter-main

# Remove Note UI
rm -rf app/src/main/java/android/template/ui/note/

# Remove Data Layer (CAREFUL - only if you're sure!)
rm -rf app/src/main/java/android/template/data/

# Remove Database Schemas
rm -rf app/schemas/
```

**WARNING:** Only run these commands if you're absolutely sure you don't need the database!

## Recommendation

**Keep the database infrastructure for now.** It's already implemented and tested, adds minimal overhead, and gives you flexibility for future features. You can always remove it later if you really don't need it, but adding it back later is more work.

The current project structure is clean and professional with a good separation of concerns:
- UI layer (calculator)
- Data layer (ready for use)
- DI layer (Koin modules)
- ViewModel layer (business logic)

This is a solid foundation for a production app!
