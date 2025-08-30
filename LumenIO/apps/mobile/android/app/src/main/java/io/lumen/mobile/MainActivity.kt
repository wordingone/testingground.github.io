package io.lumen.mobile

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Size
import android.view.SurfaceView
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
  private lateinit var previewView: PreviewView
  private lateinit var overlay: OverlayView
  private lateinit var renderer: FilamentRenderer
  private val exec = Executors.newSingleThreadExecutor()
  private lateinit var hand: HandLandmarkerHelper
  private val fsm = GestureFSM()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    previewView = PreviewView(this)
    overlay = OverlayView(this)
    val root = FrameLayout(this).apply { addView(previewView); addView(overlay) }
    setContentView(root)

    hand = HandLandmarkerHelper(this, onResult = { r ->
      overlay.points = r.points; overlay.postInvalidate()
      // simple pinch heuristic: use landmark 4 (thumb tip) & 8 (index tip)
      val pinch = r.points.takeIf { it.size >= 9 }?.let {
        val d = hypot(it[4][0]-it[8][0], it[4][1]-it[8][1])
        (1.0f - d).coerceIn(0f,1f)
      } ?: 0f
      val ev = fsm.step(GestureParams(pinch, 0.5f, 0.5f, 0f, 0f))
      if (ev.startsWith("drag:") || ev.startsWith("rotate:") || ev.startsWith("scale:")) {
        // map to a transform; here we rotate around Y slightly as demo
        val angle = (System.nanoTime() % 1_000_000_000L) / 1e9f
        val mat = yRotation(angle)
        renderer.applyTransform(mat)
      }
    }, onError = { e -> e.printStackTrace() })
    hand.setup()

    startCamera()
    setupRenderer()
  }

  private fun setupRenderer() {
    val surfaceView = SurfaceView(this)
    (previewView.parent as FrameLayout).addView(surfaceView)
    surfaceView.holder.addCallback(object: android.view.SurfaceHolder.Callback {
      override fun surfaceCreated(holder: android.view.SurfaceHolder) {
        renderer = FilamentRenderer(this@MainActivity)
        renderer.init(holder.surface, surfaceView.width.coerceAtLeast(1), surfaceView.height.coerceAtLeast(1))
        renderer.loadGltfFromAssets("models/sample.gltf")
        surfaceView.post(object: Runnable { override fun run() { renderer.renderFrame(); surfaceView.post(this) }})
      }
      override fun surfaceChanged(h: android.view.SurfaceHolder, f: Int, w: Int, hgt: Int) {}
      override fun surfaceDestroyed(h: android.view.SurfaceHolder) { /* destroy renderer */ }
    })
  }

  private fun startCamera() {
    val providerFuture = ProcessCameraProvider.getInstance(this)
    providerFuture.addListener({
      val provider = providerFuture.get()
      val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
      val analysis = ImageAnalysis.Builder().setTargetResolution(Size(640,480)).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
      analysis.setAnalyzer(exec) { img ->
        val bmp = img.toBitmap() ?: return@setAnalyzer
        hand.process(bmp)
        img.close()
      }
      provider.unbindAll()
      provider.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA, preview, analysis)
    }, ContextCompat.getMainExecutor(this))
  }

  private fun ImageProxy.toBitmap(): Bitmap? {
    val plane = planes.firstOrNull() ?: return null
    // Convert YUV→ARGB fast path for demo; production should use RenderScript/ScriptIntrinsicYuvToRGB or GPU path
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
      // Simplified; replace with a correct YUV→RGB conversion
    }
  }

  private fun hypot(dx: Float, dy: Float) = kotlin.math.sqrt(dx*dx + dy*dy)
  private fun yRotation(theta: Float): FloatArray {
    val c = kotlin.math.cos(theta); val s = kotlin.math.sin(theta)
    return floatArrayOf( c,0f, s,0f, 0f,1f,0f,0f, -s,0f,c,0f, 0f,0f,0f,1f )
  }
}
