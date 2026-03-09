# CurrencyX

A modern currency converter built with Jetpack Compose.

## Features

- Real-time exchange rates for 150+ currencies (Exchangerate-API)
- Offline support with cached rates
- Favorite currency pairs
- Material 3 dark theme (emerald)
- Clean architecture: MVVM, domain/data layers, Hilt, Room, Retrofit

## Tech Stack

- **UI**: Jetpack Compose, Material 3
- **Architecture**: MVVM, Clean Architecture
- **DI**: Hilt
- **Networking**: Retrofit, Kotlin Serialization
- **Local**: Room, Coroutines, StateFlow

## Setup

1. Clone the repository.
2. Get a free API key from [exchangerate-api.com](https://www.exchangerate-api.com/).
3. Create or edit `local.properties` in the project root and add:
   ```properties
   API_KEY=your_api_key_here
   ```
4. Build and run:
   ```bash
   ./gradlew assembleDebug
   ```

## Project structure

```
app/src/main/java/com/shrey/currencyx/
├── data/       # API, Room, repository impl, mappers
├── di/         # Hilt modules
├── domain/     # Models, repository interface, use cases
├── ui/         # Screens, ViewModels, components, theme
└── util/       # Resource, extensions, logging
```

## Logging and comments

- **Logs**: Use `LogUtil.e()` only for errors and cache fallback (tag: `CurrencyX`). No debug/verbose in production.
- **KDoc**: Only on public API (repository interface, use cases, reusable composables). One line where enough.
- **Comments**: Only for non-obvious logic (e.g. cache fallback on network error).

## License

MIT
