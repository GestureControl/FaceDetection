plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.devtools.ksp")

}

android {
    namespace = "com.example.facedetection"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.facedetection"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.camera:camera-lifecycle:1.3.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

// fACE dETECTION tENSORfLOW & mLkIT
    implementation ("com.google.mlkit:face-detection:16.1.6")
    implementation ("org.tensorflow:tensorflow-lite:2.4.0")

// dAGGERhILT
    implementation ("com.google.dagger:hilt-android:2.48")
    ksp ("com.google.dagger:hilt-android-compiler:2.48")




   
        // CameraX core library using the camera2 implementation
        // The following line is optional, as the core library is included indirectly by camera-camera2
        implementation ("androidx.camera:camera-core:1.4.0-alpha04")
        implementation ("androidx.camera:camera-camera2:1.4.0-alpha04")
        // If you want (to additionally use the CameraX Lifecycle library)
        implementation ("androidx.camera:camera-lifecycle:1.4.0-alpha04")
        // If you want (to additionally use the CameraX VideoCapture library)
        implementation ("androidx.camera:camera-video:1.4.0-alpha04")
        // If you want (to additionally use the CameraX View class)
        implementation ("androidx.camera:camera-view:1.4.0-alpha04")
        // If you want (to additionally add CameraX ML Kit Vision Integration)
        implementation ("androidx.camera:camera-mlkit-vision:1.4.0-alpha04")
        // If you want (to additionally use the CameraX Extensions library)
        implementation ("androidx.camera:camera-extensions:1.4.0-alpha04")
    



}