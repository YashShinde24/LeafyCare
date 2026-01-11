# LeafyCare 🌿
Smart plant health monitoring and disease detection for Android.


---

## Table of Contents
- [Overview](#overview)
- [Problem](#problem)
- [Solution](#solution)
- [Key Features](#key-features)
- [System Architecture](#system-architecture)
- [Technology Stack](#technology-stack)
- [Machine Learning Model](#machine-learning-model)
  - [Model Performance](#model-performance)
- [Screenshots](#screenshots)
- [Installation & Setup](#installation--setup)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## Overview
LeafyCare is an Android application that automatically identifies plant diseases from leaf images and provides actionable care recommendations. The app combines on-device machine learning with curated plant care data and external plant information APIs to deliver accurate, accessible insights to gardeners and growers.

## Problem
- Dependence on experts for diagnosis, which can be expensive or unavailable.
- Fragmented care information scattered across many unverified sources.
- Manual visual diagnosis by non-experts is error-prone.
- New gardeners lack easy-to-use tools for monitoring plant health.

## Solution
LeafyCare provides:
1. On-device disease identification using a compact Convolutional Neural Network (CNN) via TensorFlow Lite.
2. Structured care instructions (soil, watering, sunlight) mapped to predicted disease and plant species.
3. Optional real-time enrichment from authoritative external plant APIs.
4. A clean native Android UI for effortless use.

## Key Features
- AI-driven disease classification (TensorFlow Lite).
- Detailed, structured care guides for affected plants.
- Fast, case-insensitive search (starts after 3 characters).
- Local authentication and session management (SQLite).
- External API integration for verified botanical data.

## System Architecture
Layered architecture for separation of concerns:
1. UI Layer — Activities and Fragments.
2. ML Inference Layer — TensorFlow Lite model and preprocessing pipeline.
3. Data Management Layer — SQLite for local storage and caching.
4. API Layer — Interfaces to external plant data services (optional).

## Technology Stack
- Platform: Android
- Language: Java
- IDE: Android Studio
- Database: SQLite
- Machine Learning: TensorFlow / TensorFlow Lite
- Architecture: Layered modular design

## Machine Learning Model
The app uses a CNN optimized for mobile inference. It extracts color, texture and lesion features to classify leaf images into disease categories.

- Dataset: thousands of labeled images across species and disease classes.
- Inference: on-device using a TensorFlow Lite model for fast and private predictions.
- Model file: place your `.tflite` model in the app assets (see Installation & Setup).

### Model Performance
Accuracy:
- The model achieved an accuracy of 86.3% on the training dataset and 83.7% on the testing dataset, indicating good generalization performance with minimal overfitting.
- Confusion matrix(s) for the final model (per-class performance).
- Key metrics: accuracy, precision, recall, F1-score (per class and macro/micro averages).

![Confusion Matrix](Screenshots/Confusion%20matrix/confusion_matrix.png)

## Screenshots
Add app screenshots here to demonstrate the UI and the core flows:
- Home / Dashboard
- Image capture or upload screen
- Prediction result (disease + care recommendations)
- History or saved sessions

To embed images in the README, use relative paths (example):
```md
![Home screen](docs/images/screenshot-home.png)
![Prediction result](docs/images/screenshot-prediction.png)
```
Store images in `docs/images/` (recommended) or `assets/docs/images/`. Use descriptive filenames and include alt text.

## Installation & Setup

### Prerequisites
- Android Studio (latest)
- Android SDK
- Java JDK 8+
- Physical device or emulator (recommended API level specified in project)

### Clone the repository
```bash
git clone https://github.com/YashShinde24/LeafyCare.git
cd LeafyCare
```

### Add the ML model
Place your TensorFlow Lite model in:
```
app/src/main/assets/model.tflite
```
(or whichever assets path your project uses)



### Build & Run
1. Open the project in Android Studio.
2. Let Gradle sync and install required SDKs.
3. Build and run on a device or emulator.



## Contributing
Contributions are welcome. Please:
- Open an issue describing the change or feature.
- Fork the repo and create a feature branch.
- Add tests for significant changes.
- Submit a pull request with a clear description.

## License
Specify your license here (e.g., MIT). Replace this line with a LICENSE file and a short summary.

## Contact
Maintainer: Yash Shinde — link to GitHub profile or email.

---

## Appendix: What to include in the repo for better documentation
- `docs/images/` — UI screenshots, training curves, confusion matrices
- `docs/model/` — training logs, model card, model quantization notes
- `docs/report.md` — short explanation of dataset, preprocessing, and metrics
