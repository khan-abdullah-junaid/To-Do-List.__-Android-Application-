# To-Do List (Android App)

A modern, offline-first Android application built to manage daily tasks efficiently. This project demonstrates up-to-date Android development best practices, featuring a fully declarative UI, reactive state management, and robust local persistence.

## 🚀 Features
* **Task Management:** Add, delete, and toggle completion status of tasks.
* **Dynamic Filtering:** Filter tasks by All, Active, or Completed states.
* **Progress Tracking:** Real-time visual progress bar calculating daily completion rates.
* **Priority Persistence:** Assign priorities (Low, Medium, High) to tasks. The app remembers your last-used priority across sessions.
* **Dark/Light Mode Support:** Adapts seamlessly to the system's theme using Compose Material 3.
* **Offline First:** All data is persisted locally and instantly available upon app launch.

## 🛠️ Tech Stack & Architecture

This project was developed using the **MVVM (Model-View-ViewModel)** architectural pattern to ensure a clean separation of concerns.

### Frontend (UI)
* **[Jetpack Compose](https://developer.android.com/jetpack/compose):** Modern declarative UI toolkit.
* **[Compose Material 3](https://m3.material.io/):** Design system for adaptive and accessible UI components.
* **StateFlow & Coroutines:** For reactive, lifecycle-aware UI state observation.

### Backend (Data & Business Logic)
* **[Room Database](https://developer.android.com/training/data-storage/room):** SQLite object mapping library for robust local data storage.
* **[Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore):** Modern key-value storage for saving user preferences (e.g., last used task priority).
* **[Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlin.org/docs/flow.html):** For asynchronous operations and reactive data streams.
* **ViewModel:** Manages UI-related data in a lifecycle-conscious way.

### Testing
* **JUnit4:** For unit testing business logic (e.g., `TaskViewModelTest`).
* **Compose UI Test:** For UI and integration testing, simulating user interactions (e.g., `TaskScreenTest`).

## 📸 Screenshots
*(Note: Add screenshots of your app running in Light and Dark mode here)*
<div align="center">

  <img width="20%" height="20%" alt="Screenshot_20260928-225456_To-Do List" src="https://github.com/user-attachments/assets/a1ca41d5-73a3-4acc-8f3c-0cf514275283" /> <img width="20%" height="20%" alt="Screenshot_20260927-152533_To-Do List" src="https://github.com/user-attachments/assets/95477a4a-3cd7-4751-aea5-64eca723ab2f" />


</div>

## ⚙️ How to Build and Run
1. Clone the repository:
   ```bash
   git clone https://github.com/khan-abdullah-junaid/todo-list-android.git
   ```
2. Open the project in **Android Studio** (Koala or newer recommended).
3. Let Gradle sync the project dependencies.
4. Click the **Run** button to install the app on an emulator or physical device.

## 💡 Future Enhancements (Roadmap)
* Implement push notifications for high-priority tasks.
* Add remote sync capabilities using Retrofit and an external REST API.
* Implement AppWidgets for home screen access.

---
*This project was created as a portfolio piece to demonstrate modern Android development proficiency.*
