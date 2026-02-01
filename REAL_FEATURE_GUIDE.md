# Add User Profile feature (step-by-step + code)

Feature: Profile screen with Jetpack Compose. GET /users/me, PATCH /users/me with body `{ "displayName": "..." }`.

---

## Step 1 – DTOs

**Path:** `app/src/main/java/android/template/data/remote/`

**File:** `ProfileResponse.kt`

```kotlin
package android.template.data.remote

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatarUrl") val avatarUrl: String?
)
```

**File:** `ProfileUpdateRequest.kt`

```kotlin
package android.template.data.remote

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("displayName") val displayName: String
)
```

---

## Step 2 – API interface

**Path:** `app/src/main/java/android/template/data/remote/`

**File:** `ProfileApiService.kt`

```kotlin
package android.template.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileApiService {

    @GET("users/me")
    suspend fun getProfile(): ProfileResponse

    @PATCH("users/me")
    suspend fun updateProfile(@Body body: ProfileUpdateRequest): ProfileResponse
}
```

---

## Step 3 – Koin: provide ProfileApiService

**Path:** `app/src/main/java/android/template/di/`

**File:** `AppModules.kt` — in **networkModule**, add:

```kotlin
single<ProfileApiService> { get<Retrofit>().create(ProfileApiService::class.java) }
```

(If Profile uses another base URL, add another Retrofit in networkModule and create ProfileApiService from that.)

---

## Step 4 – Repository

**Path:** `app/src/main/java/android/template/data/`

**File:** `ProfileRepository.kt`

```kotlin
package android.template.data

import android.template.data.remote.ProfileResponse
import android.template.data.remote.ProfileUpdateRequest

interface ProfileRepository {
    suspend fun getProfile(): ProfileResponse
    suspend fun updateDisplayName(name: String): ProfileResponse
}

class DefaultProfileRepository(
    private val profileApiService: ProfileApiService
) : ProfileRepository {

    override suspend fun getProfile(): ProfileResponse {
        return profileApiService.getProfile()
    }

    override suspend fun updateDisplayName(name: String): ProfileResponse {
        return profileApiService.updateProfile(ProfileUpdateRequest(name))
    }
}
```

---

## Step 5 – Koin: provide ProfileRepository + ProfileViewModel

**Path:** `app/src/main/java/android/template/di/`

**File:** `AppModules.kt` — in **appModule**, add:

```kotlin
single<ProfileRepository> { DefaultProfileRepository(get()) }
viewModel { ProfileViewModel(get()) }
```

---

## Step 6 – ViewModel

**Path:** `app/src/main/java/android/template/ui/profile/` (new package)

**File:** `ProfileViewModel.kt`

```kotlin
package android.template.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.template.data.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val response = profileRepository.getProfile()
                _uiState.value = ProfileUiState.Success(
                    displayName = response.displayName,
                    email = response.email,
                    avatarUrl = response.avatarUrl
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e)
            }
        }
    }

    fun updateDisplayName(name: String) {
        viewModelScope.launch {
            try {
                val response = profileRepository.updateDisplayName(name)
                _uiState.value = ProfileUiState.Success(
                    displayName = response.displayName,
                    email = response.email,
                    avatarUrl = response.avatarUrl
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e)
            }
        }
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val displayName: String, val email: String, val avatarUrl: String?) : ProfileUiState
    data class Error(val throwable: Throwable) : ProfileUiState
}
```

---

## Step 7 – Composable Screen

**Path:** `app/src/main/java/android/template/ui/profile/`

**File:** `ProfileScreen.kt`

```kotlin
package android.template.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import android.template.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var displayNameInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Success) {
            displayNameInput = (uiState as ProfileUiState.Success).displayName
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.profile),
            style = MaterialTheme.typography.headlineMedium
        )

        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.throwable.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is ProfileUiState.Success -> {
                // Email (read-only)
                OutlinedTextField(
                    value = state.email,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                // Display Name (editable)
                OutlinedTextField(
                    value = displayNameInput,
                    onValueChange = { displayNameInput = it },
                    label = { Text(stringResource(R.string.display_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Save Button
                Button(
                    onClick = {
                        if (displayNameInput.isNotBlank()) {
                            viewModel.updateDisplayName(displayNameInput)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save))
                }

                // Optional: Avatar display
                state.avatarUrl?.let { url ->
                    // Add Coil or similar image loading library
                    // AsyncImage(model = url, contentDescription = "Avatar")
                }
            }
        }
    }
}
```

---

## Step 8 – Navigation (Optional)

If you want multiple screens, you can use Jetpack Compose Navigation. Otherwise, you can replace `NoteScreen` with `ProfileScreen` in `MainActivity.kt`.

**To add navigation:**

Update `MainActivity.kt`:

```kotlin
package android.template.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.template.ui.note.NoteScreen
import android.template.ui.profile.ProfileScreen
import android.template.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "notes",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("notes") {
                            NoteScreen()
                        }
                        composable("profile") {
                            ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}
```

To navigate from Notes to Profile, add a button in `NoteScreen.kt`:

```kotlin
// In NoteScreen, add:
Button(onClick = { /* Pass navController and call: */ navController.navigate("profile") }) {
    Text("Profile")
}
```

---

## Step 9 – Strings

**Path:** `app/src/main/res/values/`

**File:** `strings.xml` — add:

```xml
<string name="profile">Profile</string>
<string name="display_name">Display name</string>
<string name="email">Email</string>
```

---

## Order (Summary)

1. **DTOs**: `ProfileResponse.kt`, `ProfileUpdateRequest.kt`
2. **API Service**: `ProfileApiService.kt`
3. **Koin DI (Network)**: `AppModules.kt` → networkModule: `single<ProfileApiService> { get<Retrofit>().create(ProfileApiService::class.java) }`
4. **Repository**: `ProfileRepository.kt`
5. **Koin DI (App)**: `AppModules.kt` → appModule: `single<ProfileRepository> { DefaultProfileRepository(get()) }`, `viewModel { ProfileViewModel(get()) }`
6. **ViewModel**: `ProfileViewModel.kt`
7. **Composable UI**: `ProfileScreen.kt` (use `koinViewModel()` for DI)
8. **Navigation**: Update `MainActivity.kt` with NavHost (optional, if multiple screens)
9. **Strings**: `strings.xml`

---

## Key Differences from XML/Fragment Approach

- **No XML layouts** – All UI is in `@Composable` functions
- **No ViewBinding** – Direct Kotlin code with Compose
- **No Fragments** – Use `@Composable` functions instead
- **State management** – `collectAsStateWithLifecycle()` replaces `lifecycleScope.launch + repeatOnLifecycle`
- **Koin integration** – Use `koinViewModel()` from `org.koin.androidx.compose` instead of `by viewModel()`
- **Navigation** – Use Jetpack Compose Navigation with `NavHost` and `composable()` instead of Navigation Component with XML

---