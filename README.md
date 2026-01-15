# FooDiddy - Fitness Tracker

<div align="center">
  <img src="app/src/main/res/drawable/logo1.png" alt="FooDiddy Logo" width="300"/>
  
  [![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
  [![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
  [![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)
  [![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
</div>

## 📱 About

FooDiddy is a modern, intuitive fitness tracking application built with Jetpack Compose for Android. Track your daily water intake, calorie consumption, and weight progress with a beautiful, user-friendly interface. The app provides personalized recommendations based on your BMI and fitness goals.

## ✨ Features

### 🎯 Core Functionality
- **Water Intake Tracking**: Monitor your daily water consumption with visual progress indicators
- **Calorie Tracking**: Log your daily calorie intake and stay within your target goals
- **Weight Management**: Track your weight over time and visualize your progress
- **Weekly Weight Analysis**: View average weekly weight trends with interactive charts
- **History Tracking**: Access complete history of water, calorie, and weight entries
- **Personalized Onboarding**: BMI calculation and customized daily goal recommendations

### 🎨 User Experience
- **Modern UI**: Built entirely with Jetpack Compose and Material Design 3
- **Dark Theme**: Eye-friendly dark theme throughout the app
- **Smooth Animations**: Fluid transitions and interactive elements
- **Intuitive Navigation**: Bottom navigation bar for easy access to all features
- **Real-time Progress**: Visual circular progress indicators with color gradients

### ⚙️ Customization
- **Adjustable Goals**: Modify daily water and calorie targets anytime
- **Weight Units**: Switch between kilograms (kg) and pounds (lbs)
- **Smart Recommendations**: Auto-adjust targets based on your progress
- **Flexible Input**: Easy-to-use dialogs for quick data entry

## 📸 Screenshots

### Onboarding Experience
<div align="center">
  <img src="screenshots/Onboarding 1.png" alt="Onboarding Step 1" width="250"/>
  <img src="screenshots/Onboarding 2.png" alt="Onboarding Step 2" width="250"/>
</div>

*Personalized onboarding with BMI calculation and goal recommendations*

### Dashboard
<div align="center">
  <img src="screenshots/Dashboard.png" alt="Dashboard" width="250"/>
</div>

*Main dashboard showing daily progress for water and calorie intake*

### History Tracking
<div align="center">
  <img src="screenshots/History 1.png" alt="Empty History" width="250"/>
  <img src="screenshots/History 2.png" alt="History with Data" width="250"/>
</div>

*Track your complete history of water, calorie, and weight entries*

### Weekly Weight Analysis
<div align="center">
  <img src="screenshots/Average Weekly Weight 1.png" alt="Empty Weekly Weight" width="250"/>
  <img src="screenshots/Average Weekly Weight 2.png" alt="Weekly Weight with Data" width="250"/>
</div>

*Visualize your weekly weight trends and progress*

### Settings
<div align="center">
  <img src="screenshots/Settings.png" alt="Settings" width="250"/>
</div>

*Customize your daily goals and preferences*

## 🏗️ Architecture

FooDiddy follows modern Android development best practices:

### Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Database
- **Dependency Injection**: Manual DI with Application class
- **Async Operations**: Kotlin Coroutines & Flow
- **Data Storage**: DataStore Preferences
- **Navigation**: Jetpack Navigation Compose

### Project Structure
```
app/src/main/java/com/fitness/tracker/
├── data/
│   ├── converter/       # Type converters for Room
│   ├── dao/            # Data Access Objects
│   ├── database/       # Room database configuration
│   ├── entity/         # Database entities
│   ├── preferences/    # DataStore preferences
│   └── repository/     # Repository pattern implementation
├── ui/
│   ├── components/     # Reusable UI components
│   ├── navigation/     # Navigation setup
│   ├── screen/         # Screen composables
│   ├── theme/          # App theming
│   └── viewmodel/      # ViewModels
├── FitnessApplication.kt
└── MainActivity.kt
```

### Key Components

#### Data Layer
- **Room Database**: Local data persistence for water, calorie, and weight entries
- **DataStore**: User preferences and settings storage
- **Repository Pattern**: Single source of truth for data operations

#### UI Layer
- **Jetpack Compose**: Declarative UI with Material Design 3
- **ViewModels**: State management and business logic
- **Navigation**: Type-safe navigation between screens

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 24 or higher
- Kotlin 1.9.0 or higher

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/foodiddy.git
cd foodiddy
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on an emulator or physical device

### Configuration

The app uses the following Gradle configuration:
- **Compile SDK**: 34
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34
- **Kotlin Version**: 1.9.0
- **Compose BOM**: 2023.10.01

## 🎯 Usage

### First Launch
1. Enter your personal information (name, gender, height, weight)
2. Review your calculated BMI and health category
3. Set or adjust your daily water and calorie goals
4. Define your target weight

### Daily Tracking
1. **Dashboard**: Tap on the circular progress indicators to log water or calories
2. **Weight**: Tap on your current weight to update it
3. **History**: View and manage all your entries by date
4. **Weekly Weight**: Monitor your weekly average weight trends
5. **Settings**: Adjust your goals and preferences anytime

## 🛠️ Dependencies

### Core Libraries
```gradle
// Jetpack Compose
androidx.compose:compose-bom:2023.10.01
androidx.compose.material3:material3
androidx.compose.material:material-icons-extended

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// DataStore
androidx.datastore:datastore-preferences:1.0.0

// Navigation
androidx.navigation:navigation-compose:2.7.6

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
```

## 🎨 Design Highlights

- **Color-Coded Progress**: Water (red gradient) and Calories (green gradient)
- **Interactive Elements**: Bouncy animations on tap interactions
- **Custom Typography**: Bahnschrift font for a modern look
- **Responsive Layout**: Adapts to different screen sizes
- **Material Design 3**: Latest design guidelines and components

## 📊 Features in Detail

### BMI Calculation
The app calculates your Body Mass Index (BMI) during onboarding and provides:
- BMI value with one decimal precision
- Health category classification
- Personalized recommendations for water and calorie intake
- Ideal weight calculation based on height

### Smart Target Adjustment
The app can automatically suggest target adjustments based on your progress and goals, helping you stay on track with your fitness journey.

### Data Persistence
All your data is stored locally on your device using Room Database, ensuring:
- Fast data access
- Offline functionality
- Data privacy and security
- No internet connection required

## 🔒 Privacy

FooDiddy respects your privacy:
- All data is stored locally on your device
- No data is sent to external servers
- No account creation required
- No tracking or analytics

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Akashi**

## 🙏 Acknowledgments

- Material Design 3 for the beautiful design system
- Jetpack Compose team for the amazing UI toolkit
- Android community for continuous support and inspiration

## 📧 Contact

For questions or feedback, please open an issue on GitHub.

---

<div align="center">
  Made with ❤️ by Akashi | © 2026
</div>
