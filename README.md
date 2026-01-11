# 🌿 LeafyCare: Smart Plant Health Monitoring & Disease Detection

LeafyCare is an Android application designed to automatically identify plant diseases and provide structured, actionable care recommendations. By integrating **Machine Learning** and, local data management, LeafyCare empowers farmers, gardeners, and students with expert-level botanical insights in the palm of their hand.

Check out our Android App -  
---

## 📌 Table of Contents
* [Introduction](#-introduction)
* [Problem Statement](#-problem-statement)
* [Proposed Solution](#-proposed-solution)
* [Features](#-features)
* [System Architecture](#-system-architecture)
* [Technology Stack](#-technology-stack)
* [Machine Learning Model](#-machine-learning-model)
* [Installation & Setup](#installation--setup)
* [Application Workflow](#-application-workflow)
* [Future Enhancements](#-future-enhancements)

---

## 📖 Introduction
Plant diseases pose a significant threat to global food security and local biodiversity. Traditional diagnosis relies on expert knowledge, which is often expensive or inaccessible. **LeafyCare** bridges this gap using AI-based image recognition to detect diseases early and provide verified maintenance guidelines through a user-friendly mobile interface.

## ⚠️ Problem Statement
- **Expert Dependency:** Diagnosis often requires specialists who aren't always available.
- **Fragmented Data:** Reliable plant care info is scattered across various unverified sources.
- **Manual Errors:** Visual identification by non-experts is prone to high error rates.
- **Barrier to Entry:** Beginners lack the tools to monitor plant health effectively.

## ✅ Proposed Solution
LeafyCare offers a comprehensive health monitoring system that:
1. **Identifies** diseases using Convolutional Neural Networks (CNN).
2. **Maps** predictions to structured care data (Soil, Sunlight, Water).
3. **Retrieves** real-time verified data via external Plant APIs.
4. **Simplifies** the user experience through a clean, native Android UI.

---

## 🚀 Features
- **AI-Based Detection:** TensorFlow-powered classification of thousands of labeled plant images.
- **Detailed Care Guides:** Access growth patterns, soil requirements, and maintenance schedules.
- **Smart Search:** Case-insensitive search optimized for performance (triggers after 3 characters).
- **Secure Authentication:** Integrated login system with local SQLite support.
- **External API Integration:** Real-time fetching of botanical data to ensure accuracy.

---

## 🏗 System Architecture
The application utilizes a **Modular Layered Architecture**:

1. **UI Layer:** Android Activities/Fragments for user interaction.
2. **ML Inference Layer:** TensorFlow Lite for on-device image processing.
3. **Data Management Layer:** SQLite for local storage and session management.
4. **API Layer:** Interface for external plant information services.



---

## 🛠 Technology Stack
* **Platform:** Android
* **Language:** Java
* **IDE:** Android Studio
* **Database:** SQLite
* **Machine Learning:** TensorFlow / TensorFlow Lite
* **Architecture:** Layered Architecture

---

## 🧠 Machine Learning Model
The core of LeafyCare is a **Convolutional Neural Network (CNN)** optimized for mobile performance.
- **Dataset:** Thousands of leaf images across multiple species and disease categories.
- **Feature Extraction:** Focuses on color variations, texture, and lesion patterns.
- **On-Device Inference:** Designed to work offline for immediate classification.




---

Installation & Setup
Prerequisites
Android Studio (latest)
Android SDK
Java JDK 8+
Physical device or emulator (recommended API level specified in project)
Clone the repository
git clone https://github.com/YashShinde24/LeafyCare.git
cd LeafyCare
Add the ML model
Place your TensorFlow Lite model in:

app/src/main/assets/model.tflite
(or whichever assets path your project uses)

Build & Run
Open the project in Android Studio.
Let Gradle sync and install required SDKs.
Build and run on a device or emulator.

### Steps
1. **Clone the repo:**
   ```bash
   git clone [https://github.com/yourusername/LeafyCare.git](https://github.com/yourusername/LeafyCare.git)
