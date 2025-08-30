package io.lumen.mobile

import android.content.Context
import android.view.Surface
import com.google.android.filament.*
import com.google.android.filament.utils.*
import com.google.android.filament.gltfio.*
import java.nio.ByteBuffer

class FilamentRenderer(private val ctx: Context) {
  private lateinit var engine: Engine
  private lateinit var swapChain: SwapChain
  private lateinit var renderer: Renderer
  private lateinit var scene: Scene
  private lateinit var view: View
  private lateinit var camera: Camera
  private lateinit var assetLoader: AssetLoader
  private lateinit var resourceLoader: ResourceLoader
  private lateinit var asset: FilamentAsset

  fun init(surface: Surface, width: Int, height: Int) {
    engine = Engine.create()
    swapChain = engine.createSwapChain(surface)
    renderer = engine.createRenderer()
    scene = engine.createScene()
    view = engine.createView().apply { this.scene = scene }
    camera = engine.createCamera(EntityManager.get().create()).apply {
      setProjection(45.0, width.toDouble()/height, 0.05, 100.0, Camera.Fov.VERTICAL)
      lookAt(0.0, 0.0, 3.0, 0.0, 0.0, 0.0)
    }
    view.camera = camera

    assetLoader = AssetLoader(engine, UbershaderProvider(engine), EntityManager.get())
    resourceLoader = ResourceLoader(engine, true, true)
  }

  fun loadGltfFromAssets(path: String) {
    val buf = ctx.assets.open(path).readBytes()
    asset = assetLoader.createAssetFromJson(ByteBuffer.wrap(buf))
    resourceLoader.loadResources(asset)
    scene.addEntities(asset.entities)
    // basic IBL/lighting skipped for brevity; add filament-utils helpers in production
  }

  fun applyTransform(tx: FloatArray) {
    // tx = 4x4 column-major matrix
    val root = asset.root
    if (root != 0) TransformManager.getInstance().apply {
      val inst = getInstance(root)
      setTransform(inst, tx)
    }
  }

  fun renderFrame() {
    if (renderer.beginFrame(swapChain)) {
      renderer.render(view)
      renderer.endFrame()
    }
  }

  fun destroy() {
    // Destroy all Filament objects (omitted for brevity)
  }
}
