package io.lumen.mobile

enum class GState { IDLE, PINCH, DRAG, ROTATE, SCALE }

data class GestureParams(
  val pinch: Float, val x: Float, val y: Float, val angle: Float, val span: Float
)

class GestureFSM(private val thresh: Float = 0.8f) {
  var state = GState.IDLE; private set
  private var startX=0f; private var startY=0f; private var startAngle=0f; private var startSpan=0f

  fun step(p: GestureParams): String = when(state) {
    GState.IDLE -> if (p.pinch >= thresh) { state = GState.PINCH; startX=p.x; startY=p.y; startAngle=p.angle; startSpan=p.span; "pinch:start" } else "idle"
    GState.PINCH -> when {
      p.pinch < thresh -> { state = GState.IDLE; "pinch:cancel" }
      hypot(p.x-startX, p.y-startY) > 0.04f -> { state = GState.DRAG; "drag:start" }
      kotlin.math.abs(p.angle-startAngle) > 0.15f -> { state = GState.ROTATE; "rotate:start" }
      kotlin.math.abs(p.span-startSpan) > 0.05f -> { state = GState.SCALE; "scale:start" }
      else -> "pinch:hold"
    }
    GState.DRAG -> if (p.pinch < thresh) { state = GState.IDLE; "drag:end" } else "drag:move"
    GState.ROTATE -> if (p.pinch < thresh) { state = GState.IDLE; "rotate:end" } else "rotate:move"
    GState.SCALE -> if (p.pinch < thresh) { state = GState.IDLE; "scale:end" } else "scale:move"
  }

  private fun hypot(dx: Float, dy: Float) = kotlin.math.sqrt(dx*dx + dy*dy)
}
