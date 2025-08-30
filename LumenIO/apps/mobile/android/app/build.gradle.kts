plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  namespace = "io.lumen.mobile"
  compileSdk = 34
  defaultConfig {
    applicationId = "io.lumen.mobile"
    minSdk = 23
    targetSdk = 34
    versionCode = 1
    versionName = "0.1.0"
  }
  buildTypes { release { isMinifyEnabled = false } }
  compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
  kotlinOptions { jvmTarget = "17" }
}

dependencies {
  // CameraX (use stable; RC exists but keep prod-stable) — per official table
  val camerax = "1.4.2" // https://developer.android.com/jetpack/androidx/releases/camera
  implementation("androidx.camera:camera-core:$camerax")
  implementation("androidx.camera:camera-camera2:$camerax")
  implementation("androidx.camera:camera-lifecycle:$camerax")
  implementation("androidx.camera:camera-view:$camerax")

  // MediaPipe Tasks – Hand Landmarker (Android)
  implementation("com.google.mediapipe:tasks-vision:latest.release") // https://ai.google.dev/edge/mediapipe/solutions/vision/hand_landmarker/android

  // Filament (PBR renderer + glTF loader + utils) — latest release 1.57.1
  val filament = "1.57.1" // https://mvnrepository.com/artifact/com.google.android.filament/filament-android
  implementation("com.google.android.filament:filament-android:$filament")
  implementation("com.google.android.filament:gltfio-android:$filament")
  implementation("com.google.android.filament:filament-utils-android:$filament")

  // KotlinX
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}
