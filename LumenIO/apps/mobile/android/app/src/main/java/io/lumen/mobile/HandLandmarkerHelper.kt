package io.lumen.mobile

import android.content.Context
import android.os.SystemClock
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.framework.image.BitmapImageBuilder

class HandLandmarkerHelper(
  private val context: Context,
  private val modelAssetPath: String = "hand_landmarker.task",    // put model into assets
  private val maxHands: Int = 1,
  private val minDet: Float = 0.5f,
  private val minTrack: Float = 0.5f,
  private val minPresence: Float = 0.5f,
  private val onResult: (HandResult) -> Unit,
  private val onError: (Throwable) -> Unit
) {
  private var landmarker: HandLandmarker? = null

  fun setup() {
    val base = com.google.mediapipe.tasks.core.BaseOptions.builder().setModelAssetPath(modelAssetPath).build()
    val opts = HandLandmarker.HandLandmarkerOptions.builder()
      .setBaseOptions(base)
      .setRunningMode(RunningMode.LIVE_STREAM)
      .setNumHands(maxHands)
      .setMinHandDetectionConfidence(minDet)
      .setMinTrackingConfidence(minTrack)
      .setMinHandPresenceConfidence(minPresence)
      .setResultListener { res, inputImg ->
        val handed = res.handedness().firstOrNull()?.firstOrNull() ?: Category.create("Unknown", 0, 0f)
        val pts = res.landmarks().firstOrNull()?.map { floatArrayOf(it.x(), it.y(), it.z()) } ?: emptyList()
        onResult(HandResult(handed.categoryName(), pts))
      }
      .setErrorListener { e -> onError(e) }
      .build()
    landmarker = HandLandmarker.createFromOptions(context, opts)
  }

  fun process(bitmap: android.graphics.Bitmap) {
    val mpImg = BitmapImageBuilder(bitmap).build()
    val ts = SystemClock.uptimeMillis()
    landmarker?.detectAsync(mpImg, ts)
  }

  data class HandResult(val handedness: String, val points: List<FloatArray>)
}
