## Calc App

Simple calculator Android app built with **Jetpack Compose**, **Koin** for dependency injection, and **MVVM** architecture.  
Includes a **GitHub Actions** CI workflow to build and run tests on each push / PR.

---

### Features

- **Basic calculator**
  - **Operators**: addition `+`, subtraction `-`, multiplication `×`, division `÷`.
  - **Editing**: clear (`C`), delete last character (`DEL`).
  - **Decimal support**: `.` for fractional numbers.
- **Modern UI**
  - Built with **Jetpack Compose** + **Material3**.
  - Responsive layout with large, readable display and rounded buttons.
- **Clean architecture**
  - **MVVM** pattern (`CalculatorViewModel`) with a **domain** layer (`Calculator` interface + `CalculatorImpl`).
  - **Koin** for dependency injection.
- **Automation**
  - **GitHub Actions** workflow to build and test on each push / PR.

---

### Tech stack

- **Language**: Kotlin
- **UI**: Jetpack Compose, Material3
- **Architecture**: MVVM + small domain layer
- **DI**: Koin (Android + Compose integration)
- **Build system**: Gradle (KTS)
- **CI/CD**: GitHub Actions

---

### Project structure

High-level structure (most relevant files only):

```text
Calc-App/
├─ settings.gradle.kts
├─ build.gradle.kts
├─ gradle.properties
├─ app/
│  ├─ build.gradle.kts
│  └─ src/
│     ├─ main/
│     │  ├─ AndroidManifest.xml
│     │  ├─ java/com/example/calcapp/
│     │  │  ├─ CalcApp.kt                  // Application class, starts Koin
│     │  │  ├─ di/
│     │  │  │  └─ AppModule.kt             // Koin DI module
│     │  │  ├─ domain/
│     │  │  │  ├─ Calculator.kt            // Calculator interface
│     │  │  │  └─ CalculatorImpl.kt        // Calculator implementation
│     │  │  └─ ui/
│     │  │     ├─ MainActivity.kt          // Entry Activity
│     │  │     ├─ CalculatorViewModel.kt   // MVVM ViewModel
│     │  │     ├─ CalculatorScreen.kt      // Compose UI
│     │  │     └─ theme/
│     │  │        ├─ Theme.kt
│     │  │        └─ Type.kt
│     │  └─ res/
│     │     ├─ values/
│     │     │  ├─ strings.xml
│     │     │  ├─ colors.xml
│     │     │  └─ themes.xml
└─ .github/
   └─ workflows/
      └─ android-ci.yml                    // CI workflow
```

---

### Architecture

- **MVVM**
  - **Model / Domain**:
    - `Calculator` (interface) exposes:
      - `fun input(symbol: String): String`
      - `fun clear(): String`
      - `fun delete(): String`
      - `fun evaluate(): String`
    - `CalculatorImpl` holds and updates the current expression string and evaluates it using a small, custom parser.
  - **ViewModel**:
    - `CalculatorViewModel`:
      - Depends on `Calculator` (injected by Koin).
      - Exposes `display: StateFlow<String>` for the UI.
      - Methods: `onSymbolClick`, `onClear`, `onDelete`, `onEquals` update the `display` state.
  - **View (UI)**:
    - `CalculatorScreen`:
      - Composable using `CalculatorViewModel`.
      - Observes `display` using `collectAsState()`.
      - Renders an output display and a button grid for digits and operators.
    - `MainActivity`:
      - Hosts the Compose UI and retrieves the ViewModel via `koinViewModel()`.

- **Dependency Injection (Koin)**
  - `CalcApp`:
    - Extends `Application`.
    - Starts Koin in `onCreate()`:
      - `startKoin { androidContext(this@CalcApp); modules(appModule) }`
  - `appModule`:
    - `single<Calculator> { CalculatorImpl() }`
    - `viewModel { CalculatorViewModel(get()) }`
  - In Compose:
    - `MainActivity` uses `val viewModel: CalculatorViewModel = koinViewModel()` to get a ViewModel instance.

---

### Calculator logic

- Expression is stored as a `String` inside `CalculatorImpl`.
- Supported features:
  - Digits: `0–9`.
  - Decimal point: `.`.
  - Operators: `+`, `-`, `×`, `÷` (internally normalized to `*` and `/`).
  - Clear: resets expression to `"0"`.
  - Delete: removes the last character, reverts to `"0"` if empty.
- Evaluation:
  - Normalizes `×` → `*`, `÷` → `/`.
  - Performs a simple tokenization and evaluates with **operator precedence**:
    - First `*` and `/`, then `+` and `-`.
  - Uses `BigDecimal` for numeric operations and returns a string with trailing zeros stripped.
  - If evaluation fails, returns `"Error"`.

> This evaluator is intentionally simple and meant for demo/testing use, not for complex or production-grade expression parsing.

---

### Getting started

#### Prerequisites

- **Android Studio** (Giraffe or newer recommended).
- **JDK 17** installed (Android Studio’s bundled JDK 17 is fine).

#### Running the app

1. Clone or open the `Calc-App` project in Android Studio.
2. Let **Gradle sync** and download dependencies.
3. Connect a device or start an emulator.
4. Select the `app` run configuration.
5. Click **Run**.

You should see a calculator screen with:
- A large display area.
- Buttons for numbers, decimal point, operators, `C`, `DEL`, and `=`.

---

### Building and testing locally

From the project root:

- **Assemble debug APK**

```bash
./gradlew assembleDebug
```

- **Run unit tests**

```bash
./gradlew testDebugUnitTest
```

(On Windows, use `gradlew.bat` instead of `./gradlew`.)

---

### CI/CD (GitHub Actions)

- Workflow file: `.github/workflows/android-ci.yml`
- Triggers:
  - `push` to `main` or `master`
  - `pull_request` targeting `main` or `master`
- Steps:
  - **Checkout** the repository.
  - **Set up JDK 17** using `actions/setup-java@v4`.
  - **Cache Gradle** wrapper and caches for faster builds.
  - Make `gradlew` executable.
  - **Build Debug**:

    ```bash
    ./gradlew assembleDebug
    ```

  - **Run unit tests**:

    ```bash
    ./gradlew testDebugUnitTest
    ```

#### Notes

- Ensure the Gradle wrapper is present and committed:
  - `gradlew`, `gradlew.bat`
  - `gradle/wrapper/gradle-wrapper.jar`
  - `gradle/wrapper/gradle-wrapper.properties`
- Once pushed to a GitHub repository with this structure, every push / PR will automatically:
  - Compile the app.
  - Run unit tests.

---

### Extending the app

Some ideas for future improvements:

- **More operations**
  - Support parentheses `()` and more operators (e.g. `%`, `√`, `^`).
  - Replace the naive evaluator with a robust expression parser.
- **State handling**
  - Support memory functions (M+, M-, MR, MC).
  - Persist last result across configuration changes or app restarts.
- **UI enhancements**
  - Advanced theming (custom color schemes, typography).
  - Animations and haptic feedback.
- **Testing**
  - Add unit tests for `CalculatorImpl` (e.g. `CalculatorImplTest`).
  - Add UI tests for `CalculatorScreen` using Compose testing APIs.

---

### Troubleshooting

- **Gradle sync errors**
  - Ensure you are using **Android Studio** with **JDK 17**.
  - Check that you have a stable internet connection for dependency download.
- **CI build failures**
  - Make sure Gradle wrapper files are committed.
  - Check the GitHub Actions logs for the failing step (build vs tests).

---

### License

You can add your preferred license here (e.g. MIT, Apache 2.0).


