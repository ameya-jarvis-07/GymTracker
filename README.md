# IronLog — Offline Gym Tracker

IronLog is a modern, fully offline Android application built with Jetpack Compose and Material 3. It helps users track their fitness journey with a focus on privacy and reliability.

## Features

- **Personal Profile**: Setup your fitness goals, height, weight, and experience level.
- **Weekly Split**: Create and manage your weekly gym timetable (e.g., PPL, Arnold Split).
- **Attendance Tracking**: Mark your attendance before starting a workout to maintain consistency.
- **Live Workout Sessions**: Log exercises, sets, reps, and weights in real-time.
- **Progress Analytics**: Visualize your performance and consistency with detailed reports.
- **Fully Offline**: All data is stored locally using Room Database and DataStore. No internet required.

## Technical Stack

- **Kotlin**: Core programming language.
- **Jetpack Compose**: Modern UI toolkit.
- **Material 3 Expressive**: Visually polished and responsive design.
- **MVVM Clean Architecture**: Scalable and maintainable code structure.
- **Room Database**: Local persistence for workout logs and user data.
- **Hilt**: Dependency injection.
- **Navigation Compose**: Smooth transitions between screens.
- **DataStore**: Lightweight storage for user preferences.
- **Coroutines & StateFlow**: Asynchronous programming and reactive UI updates.

## Architecture

The project follows Clean Architecture principles:
- `data/`: Local database, DAOs, and repository implementations.
- `domain/`: Business logic, models, and repository interfaces.
- `ui/`: Compose screens, ViewModels, and theme configuration.

## Setup

1. Open the project in Android Studio (Ladybug or newer).
2. Sync the project with Gradle files.
3. Build and run the app on an emulator or physical device (Min SDK 26).
