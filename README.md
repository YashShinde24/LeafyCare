🌿 LeafyCare
Smart Plant Health Monitoring & Disease Detection Android Application

LeafyCare is an AI-driven Android application developed to automatically identify plant diseases and deliver accurate, structured plant care recommendations. The application integrates machine learning, local data storage, and external plant information APIs to support early disease detection and reliable plant health management.

The solution is designed for farmers, gardeners, and students, providing an accessible and efficient alternative to traditional expert-based diagnosis.

Table of Contents

Introduction

Problem Statement

Proposed Solution

Features

System Architecture

Technology Stack

Installation and Setup

Dataset and Machine Learning Model

Application Workflow

Future Enhancements

Use Cases

Conclusion

Introduction

Plant diseases pose a significant threat to crop productivity and plant health. Early identification is critical, yet traditional diagnosis methods rely heavily on expert knowledge, which is often unavailable or costly. LeafyCare addresses this challenge by employing AI-based image recognition to detect plant diseases and present verified plant care information through a user-friendly mobile application.

Problem Statement

Plant disease diagnosis depends on expert availability

Reliable plant care information is often scattered

Manual identification is time-consuming and error-prone

Limited intelligent tools for beginners and small-scale growers

Proposed Solution

LeafyCare provides a smart plant health monitoring system that:

Identifies plant diseases using machine learning image classification

Maps disease predictions to structured plant care data

Retrieves verified plant information using external APIs

Displays results through a clean and intuitive Android interface

Features
AI-Based Plant Disease Detection

TensorFlow-based image classification model

Trained on thousands of labeled plant leaf images

Feature extraction includes color variation, texture, and lesion patterns

Classifies plants as healthy or diseased

Detailed Plant Care Information

Common and scientific names

Genus and family

Growth pattern

Soil requirements

Sunlight needs

Watering schedule

Pruning and maintenance guidelines

Smart Search Functionality

Case-insensitive plant search

Activated after a minimum of three characters

Optimized for accuracy and performance

Secure User Authentication

User registration and login system

Local SQLite database support

Designed for future cloud database migration

External Plant API Integration

Access to verified and up-to-date plant data

Ensures data accuracy and consistency

Minimizes manual data entry

System Architecture

The application follows a modular layered architecture:

User Interface Layer
↓
Machine Learning Inference Layer
↓
Data Management Layer (SQLite / Cloud-Ready)
↓
External Plant Information API Layer

Technology Stack

Platform: Android

Programming Language: Java

IDE: Android Studio

Database: SQLite

Machine Learning: TensorFlow

APIs: External Plant Information APIs

Architecture: Modular / Layered

Installation and Setup
Prerequisites

Android Studio (latest version)

Android SDK

Java JDK 8 or higher

Android emulator or physical device

Internet connection

Steps

Clone the repository

Open the project in Android Studio

Sync Gradle dependencies

Connect an emulator or physical device

Run the application

Dataset and Machine Learning Model
Dataset

Thousands of labeled plant leaf images

Healthy and diseased samples

Multiple plant disease categories

Images captured under varied lighting and backgrounds

Machine Learning Model

Framework: TensorFlow

Model Type: Convolutional Neural Network (CNN)

Input: Leaf image

Output: Disease classification result

Optimized for on-device inference

Application Workflow

User logs into the application

User captures or uploads a leaf image

Image is processed by the ML model

Disease prediction is generated

Prediction is mapped to plant care information

Results are displayed to the user

Future Enhancements

Real-time disease detection using live camera feed

Fertilizer and pesticide recommendations

Multilingual and regional language support

Weather-based plant care alerts

Cloud database integration

Use Cases

Early plant disease detection

Smart plant care assistant for gardeners and farmers

Educational support for agriculture students

Centralized plant health information system

Conclusion

LeafyCare demonstrates the effective application of AI-based image recognition and mobile computing in plant health management. By enabling early disease detection and providing reliable care recommendations, the application helps users improve plant health and make informed decisions through an accessible Android platform.
