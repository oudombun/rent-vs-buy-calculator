# Build Requirements & Setup

## Java Version Requirement

This project requires **JDK 17 or later** to build.

### Check Your Java Version

```bash
java -version
```

You should see output like:
```
openjdk version "17.0.x" or later
```

### Installing/Updating Java

#### macOS (using Homebrew)
```bash
# Install JDK 17
brew install openjdk@17

# Link it
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk

# Verify
java -version
```

#### macOS (using SDKMAN)
```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash

# Install JDK 17
sdk install java 17.0.10-tem

# Use it
sdk use java 17.0.10-tem
```

#### Windows
1. Download JDK 17 from [Adoptium](https://adoptium.net/)
2. Install and set JAVA_HOME environment variable
3. Add to PATH

#### Linux
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# Verify
java -version
```

### Android Studio Configuration

Android Studio should automatically detect the correct JDK. If not:

1. Open Android Studio
2. Go to **Settings** (Cmd+, on Mac, Ctrl+Alt+S on Windows)
3. Navigate to **Build, Execution, Deployment** → **Build Tools** → **Gradle**
4. Set **Gradle JDK** to version 17 or higher
5. Click **Apply** and **OK**
6. Sync Gradle

### Gradle Wrapper Configuration

The project uses Gradle 8.13.1 which requires JDK 17+. This is configured in:
```
gradle/wrapper/gradle-wrapper.properties
```

### Building from Command Line

Once JDK 17+ is installed:

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Run tests
./gradlew test
```

## Other Requirements

### Android SDK
- Minimum SDK: 23 (Android 6.0)
- Target SDK: 36 (Android 14)
- Compile SDK: 36

### Android Studio
- Recommended: Latest stable version (Ladybug | 2024.2.1 or newer)
- Includes: Android SDK, Gradle, Emulator

## Troubleshooting

### "Gradle requires JVM 17 or later"

**Problem:** Build fails with this error  
**Solution:** Install JDK 17+ and configure Android Studio to use it (see above)

### "Could not find or load main class org.gradle.wrapper.GradleWrapperMain"

**Problem:** Gradle wrapper is corrupted  
**Solution:**
```bash
# Re-download Gradle wrapper
./gradlew wrapper --gradle-version 8.13.1
```

### "SDK location not found"

**Problem:** Android SDK path not set  
**Solution:** Create `local.properties` in project root:
```properties
sdk.dir=/Users/YOUR_USERNAME/Library/Android/sdk
```

### Build is Very Slow

**Solution:** Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx2048m -XX:MaxMetaspaceSize=512m
org.gradle.parallel=true
org.gradle.caching=true
```

## Recommended Tools

### Essential
- **Android Studio** - IDE with everything built-in
- **JDK 17+** - Required for Gradle 8.13.1

### Optional
- **SDKMAN** - Manage multiple JDK versions
- **Homebrew** - Package manager for macOS
- **adb** - Android Debug Bridge (included with Android Studio)

## First-Time Setup Checklist

- [ ] Install JDK 17 or later
- [ ] Install Android Studio (latest stable)
- [ ] Configure Android Studio to use JDK 17+
- [ ] Install Android SDK components (via Android Studio SDK Manager)
- [ ] Clone/open project in Android Studio
- [ ] Wait for Gradle sync to complete
- [ ] Create/connect Android emulator or device
- [ ] Run the app

## Build Variants

### Debug
- Faster builds
- Debugging enabled
- No ProGuard/R8
```bash
./gradlew assembleDebug
```

### Release
- Optimized builds
- ProGuard/R8 enabled (currently disabled in template)
- Requires signing key
```bash
./gradlew assembleRelease
```

## Project Dependencies

Current Gradle Plugin: **8.13.1**  
Current Kotlin Version: **2.2.21**  
Current Compose BOM: **2025.01.00**

See `gradle/libs.versions.toml` for all dependency versions.

## Continuous Integration

For CI/CD pipelines (GitHub Actions, Jenkins, etc.), ensure the build environment has:
- JDK 17+ installed
- Android SDK installed with required components
- Environment variables set:
  - `ANDROID_HOME` or `ANDROID_SDK_ROOT`
  - `JAVA_HOME` (pointing to JDK 17+)

Example GitHub Actions:
```yaml
- uses: actions/setup-java@v3
  with:
    distribution: 'temurin'
    java-version: '17'
```

## Summary

**Minimum Requirements:**
- JDK 17+
- Android Studio (latest stable)
- Android SDK API 23-36

**Recommended:**
- JDK 21 (LTS) for best performance
- 8GB+ RAM
- SSD for faster builds

Once Java 17+ is installed and Android Studio is configured, the project should build without issues!
