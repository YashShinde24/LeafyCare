# LeafyCare 🌿
Smart plant management, health monitoring, and disease detection for Android.

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
- [Contact](#contact)

---

## Overview
LeafyCare is a smart Android application designed for comprehensive plant management and health monitoring. The application enables users to manage their personal plant collections, detect plant diseases using on-device image recognition, and receive timely care reminders to maintain healthy plants.

In addition to disease detection, LeafyCare provides tools such as light intensity measurement, offline-accessible plant information stored in structured CSV datasets, and customizable reminders for watering and maintenance. Core features operate offline to ensure usability without constant internet access, while optional online services are used for fetching real-time temperature and location-based data.

LeafyCare is primarily targeted at home gardeners and plant enthusiasts, with extended usefulness for small-scale farmers.

---

## Problem
Plant owners and gardeners often face difficulties in maintaining healthy plants due to limited access to timely guidance and reliable diagnostic tools. Identifying plant diseases typically requires expert knowledge, which is not always available or affordable.

Plant care information is often scattered across multiple unverified sources, making it difficult to follow consistent and accurate practices. Managing multiple plants further adds complexity, as users must remember watering schedules, light requirements, and environmental conditions.

As a result, many plant owners struggle with delayed disease identification, inconsistent care routines, and lack of personalized plant management tools.

---

## Solution
LeafyCare provides an integrated Android-based solution that combines plant disease detection, plant management, and care assistance within a single application. The app leverages an on-device Convolutional Neural Network (CNN) deployed using TensorFlow Lite to identify plant diseases from leaf images, with inference performed locally on the device.

While the application requires internet permission for accessing services such as location and real-time temperature data, its core functionalities—including disease detection, plant data access, and reminder management—remain operational without an active internet connection.

LeafyCare allows users to maintain a personalized collection of plants linked to their Google accounts. Plant care information such as watering schedules, soil preferences, and sunlight requirements is sourced from curated CSV datasets, ensuring reliable offline access. Online services are used selectively to enhance recommendations without making core functionality dependent on continuous connectivity.

---

## Key Features
- Image-based plant disease detection using a TensorFlow Lite–optimized CNN
- Plant management system with Google account–linked data storage
- Customizable reminders for watering and plant care
- Light intensity measurement tool
- Offline-first plant care data stored in CSV format
- Image input from camera, gallery, and external providers
- Case-insensitive smart search (minimum 3 characters)
- Optional online enrichment for temperature and location data

---

## System Architecture
LeafyCare follows a layered architecture to ensure modularity and maintainability.

1. **UI Layer**  
   Android Activities and Fragments for user interaction and navigation.

2. **Image Processing & Inference Layer**  
   Handles image preprocessing and on-device disease recognition using TensorFlow Lite.

3. **Data Management Layer**  
   Manages plant data stored in CSV format and user-specific records, with SQLite for local persistence.

4. **Utility & Services Layer**  
   Provides reminder scheduling, light intensity measurement, and background tasks.

5. **API & Network Layer (Optional)**  
   Fetches temperature and location-based data when internet connectivity is available.

---

## Technology Stack
- Platform: Android  
- Programming Language: Java  
- IDE: Android Studio  
- Local Storage: SQLite  
- Plant Data Storage: CSV datasets  
- Machine Learning: TensorFlow, TensorFlow Lite  
- Image Processing: Android Bitmap utilities  
- Authentication & Sync: Google account integration  
- Architecture: Layered modular design  

---

## Machine Learning Model
LeafyCare employs a Convolutional Neural Network (CNN) for image-based plant disease recognition. The model learns visual features such as color distribution, texture, and lesion patterns from leaf images.

- Classification scope: Disease-level classification only  
- Number of classes: 8 (5 used during evaluation for clarity)  
- Dataset: Thousands of labeled leaf images  
- Deployment: TensorFlow Lite (`.tflite`)  
- Inference: Fully on-device for fast and private predictions  

---

### Model Performance
The model was evaluated using unseen test data. Training logs were not preserved, so evaluation is based entirely on test-time metrics.

#### Evaluation Metrics
- Overall test accuracy  
- Precision, recall, and F1-score  
- Confusion matrix  

#### Evaluation Output
```bash
INFO: Created TensorFlow Lite XNNPACK delegate for CPU.
Classes found: ['Black Spot Disease', 'Healthy', 'Mealybugs', 'Powdery', 'Rust']
Total samples processed: 2450
Test Accuracy: 0.8759

Classification Report:

                   precision    recall  f1-score   support

Black Spot Disease     1.00      0.89      0.94       440
Healthy                0.72      1.00      0.84       528
Mealybugs              0.98      0.84      0.91       378
Powdery                0.87      0.92      0.90       550
Rust                   0.96      0.73      0.83       554

accuracy                                   0.88      2450
macro avg             0.91      0.88      0.88      2450
weighted avg          0.90      0.88      0.88      2450
```
## Screenshots
Application UI previews and core workflow screenshots are available at the link below.

[View Screenshots](Screenshots/)

---

#### Confusion Matrix
![Confusion Matrix](Screenshots/Confusion%20Matrix/confusion_matrix.png)

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

Contributions to LeafyCare are welcome.

Open an issue to discuss the proposed feature or fix.

Fork the repository and create a feature branch.

Follow existing project structure and coding conventions.

Add documentation or comments where applicable.

Submit a pull request with a clear description of changes.

## Contact

Maintainer: Yash Shinde
GitHub: https://github.com/YashShinde24
