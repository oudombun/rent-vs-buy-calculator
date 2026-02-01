# Koin basics (learn by doing)

You already use Koin in this project. This doc explains the ideas so you can read the code and add new features.

---

## 1. What is Koin?

**Dependency injection (DI)** = something else creates the objects you need and gives them to you. You don’t write `NoteRepository()` or `Retrofit.Builder()...` in your ViewModel or Fragment.

**Koin** = a DI library. You say:
- “When someone needs `NoteRepository`, build it like this.”
- “When someone needs `NoteViewModel`, build it like this and give it a `NoteRepository`.”

So: **you define how things are created; Koin creates and injects them when needed.**

---

## 2. Three pieces

| Piece | Where | What it does |
|-------|--------|----------------|
| **Start Koin** | Application | Loads your “recipes” (modules) and the Android context. |
| **Module** | Kotlin (e.g. `di/AppModules.kt`) | Recipes: “how to create X” (DB, API, Repository, ViewModel). |
| **Use** | Fragment, ViewModel, etc. | “Give me a ViewModel” → `by viewModel()`. Koin creates it using the module. |

---

## 3. Module = list of recipes

A **module** is a `module { }` block. Inside you define how to create each type.

```kotlin
val databaseModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "notes").build() }
    single<NoteDao> { get<AppDatabase>().noteDao() }
}
```

- **`module { }`** = “this block is a Koin module.”
- **`single { ... }`** = “there is only one instance for the whole app. Create it like this.”
- **`get()`** = “give me the thing I need.” Koin looks at the type (e.g. `Application`, `AppDatabase`) and creates it or returns the existing one using other definitions in the modules.

So:
- First `single` creates one `AppDatabase` (it uses `get<Application>()` from Koin).
- Second `single` creates one `NoteDao` by calling `get<AppDatabase>().noteDao()`.

---

## 4. `single` vs `viewModel`

| Keyword | Meaning |
|---------|--------|
| **`single { ... }`** | One instance for the whole app (e.g. DB, API, Repository). |
| **`viewModel { ... }`** | One instance per screen/scope. When the Fragment is gone, the ViewModel can be cleared. |

Example:

```kotlin
val appModule = module {
    single<NoteRepository> { DefaultNoteRepository(get(), get()) }  // one repo
    viewModel { NoteViewModel(get()) }                               // one per screen
}
```

- `get()` with no type: Koin uses the parameter type (e.g. `NoteRepository`).
- `get<NoteRepository>()`: “give me the `NoteRepository`” (same here).

---

## 5. Where Koin starts: Application

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(databaseModule, networkModule, appModule)
        }
    }
}
```

- **`startKoin { }`** = “start Koin with this config.”
- **`androidContext(this@MyApplication)`** = “use this Application as Context/Application when someone does `get<Application>()` or `get<Context>()`.”
- **`modules(...)`** = “load these modules.” All their `single` / `viewModel` definitions become available.

So: **once the app starts, Koin knows how to create everything you defined in the modules.**

---

## 6. Using it in a Fragment: `by viewModel()`

```kotlin
class NoteFragment : Fragment() {
    private val viewModel: NoteViewModel by viewModel()
    // ...
}
```

- **`by viewModel()`** = “Koin, give me the `NoteViewModel` for this Fragment.”
- Koin looks at `viewModel { NoteViewModel(get()) }` in your module, creates `NoteViewModel` (and injects `NoteRepository` via `get()`), and returns it.

You don’t create the ViewModel yourself; you don’t pass the Repository. **Koin does it from the module.**

---

## 7. Using it in a class: constructor

Your ViewModel and Repository don’t use `@Inject` or annotations. They just have a normal constructor. Koin calls that constructor when it needs to create them.

```kotlin
class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel()
```

```kotlin
viewModel { NoteViewModel(get()) }
```

Here `get()` means “give me a `NoteRepository`.” Koin uses `single<NoteRepository> { DefaultNoteRepository(get(), get()) }` to create it (and injects `NoteDao` and `NoteApiService` via those `get()` calls).

---

## 8. Flow in this project

1. **App starts** → `MyApplication.onCreate()` → `startKoin { androidContext(...); modules(...) }`.
2. **Modules** define: Database, Dao, Retrofit, ApiService, NoteRepository, NoteViewModel.
3. **User opens Notes screen** → `NoteFragment` uses `by viewModel()`.
4. Koin creates **NoteViewModel** (because of `viewModel { NoteViewModel(get()) }`).
5. To create NoteViewModel, Koin needs **NoteRepository** → it uses `single<NoteRepository> { DefaultNoteRepository(get(), get()) }`.
6. To create DefaultNoteRepository, Koin needs **NoteDao** and **NoteApiService** → it uses their `single { }` definitions.
7. So the chain is: **Fragment** → (Koin) → **ViewModel** → (Koin) → **Repository** → (Koin) → **Dao** + **ApiService**.

You never write `NoteViewModel(NoteRepository(...))`; Koin follows the modules and builds the whole graph.

---

## 9. Quick reference

| You want… | You do… |
|-----------|---------|
| One instance for the app (DB, API, Repository) | `single { ... }` or `single<Interface> { Impl(get(), ...) }` in a module. |
| One ViewModel per screen | `viewModel { MyViewModel(get()) }` in a module. |
| “Give me the X” inside a module | `get<X>()` or `get()`. |
| Get ViewModel in Fragment | `private val viewModel: MyViewModel by viewModel()`. |
| Start Koin | In Application: `startKoin { androidContext(this); modules(...) }`. |

---

## 10. Add a new “injectable” (e.g. ProfileRepository)

1. **Define it in a module** (e.g. in `appModule`):
   ```kotlin
   single<ProfileRepository> { DefaultProfileRepository(get()) }
   ```
2. **Use it in a ViewModel**:
   ```kotlin
   viewModel { ProfileViewModel(get()) }
   ```
   And `ProfileViewModel` has a constructor: `ProfileViewModel(private val profileRepository: ProfileRepository)`.

No annotations. Koin matches by type: when it needs a `ProfileRepository`, it uses the `single<ProfileRepository> { ... }` definition.

That’s Koin in practice: **modules = recipes; `get()` and `by viewModel()` = “give me the thing”; Koin creates it using the recipes.**
