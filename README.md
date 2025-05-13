
# 🧑‍💻 Face Recognition Model and Helper Classes

Welcome to the Face Detection and Recognition system for Android. This project integrates **Google ML Kit**, **TensorFlow Lite**, and **Cosine Similarity** to perform real-time face recognition on mobile devices.

---

## 📱 Overview

This Android application enables efficient and accurate face detection and recognition using lightweight machine learning models. It demonstrates how to embed and compare face vectors using helper classes and TFLite models.

---

## 🔍 Features

✅ Face Detection using ML Kit  
✅ Face Embedding with TensorFlow Lite  
✅ Cosine Similarity for Matching Faces  
✅ Modular & Reusable Helper Classes  
✅ Real-Time Processing Support

---

## 🧠 Core Components

### 🧩 1. FaceRecognitionModel
- Loads a pre-trained `.tflite` model.
- Converts facial images to vector embeddings.
- Handles input normalization and model inference.

### 🛠️ 2. FaceRecognitionHelper
- Crops and processes face bitmaps.
- Interfaces with the model class.
- Executes recognition tasks asynchronously.

### 🧮 3. CosineSimilarity
- Calculates cosine similarity between two face embeddings.
- Returns a score indicating facial match accuracy.

---

## 🎯 Use Cases

🔐 Secure App Login  
🖼️ Organizing and Tagging Photos  
👤 Personalized App Experiences  
🔍 Intelligent Image Search  

---

## 🛠️ Tech Stack

- **Languages**: Java / Kotlin  
- **Libraries**:  
  - ML Kit Vision (Face Detection)  
  - TensorFlow Lite  
  - Material3 Components 
- **Tools**: Android Studio, Gradle

---
## 🚀 Getting Started

1. **Clone the Repository**
   git clone https://github.com/GestureControl/FaceDetection.git
2. **Open in Android Studio**
3. **Add the TFLite Model**
   Place your face_model.tflite file in the app/src/main/assets directory.
4. **Run the App**
   Use a physical Android device or emulator with camera access.

   ---

## 📌 Future Improvements
1. Add encryption for on-device storage
2. Expand face dataset
3. Integrate Firebase or remote DB
4. UI improvements

---

## 🧪 Demo

