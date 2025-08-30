# LumenIO – Mobile (Android) Slice

**What:** Device-responsive 3D platform (parent system). This slice delivers **mobile**: CameraX + MediaPipe Hand Landmarker → gesture FSM → Filament PBR renderer.

## Build
- Android Studio Giraffe+ (JDK 17).
- Put a hand landmark model into `apps/mobile/android/app/src/main/assets/hand_landmarker.task` (see AI Edge Hand Landmarker guide).  
- `./gradlew :apps:mobile:android:app:assembleDebug`

## Run
Launch on a device (front camera). Pinch → rotate demo applies Y-rotation to the loaded glTF model.

## Deps (sources)
- CameraX 1.4.2 (stable matrix & dependency snippet).  
- MediaPipe Tasks – Hand Landmarker (`com.google.mediapipe:tasks-vision`).  
- Filament 1.57.1 (`filament-android`, `gltfio-android`, `filament-utils-android`).  
Docs cited in code comments.

---

What’s next (tight)

1. Replace ImageProxy.toBitmap() with proper YUV→RGB, or feed MPImage from ImageProxy without intermediate Bitmap. 

2. Add IBL/lighting in FilamentRenderer (KTX environment, skybox), and a proper frame loop / surface resize handling. 

3. Emit gesture_event JSON to a bus; store deterministic replays; unit tests for FSM thresholds.

4. Add depth scaling (z from world landmarks) to improve rotate/scale stability. 
