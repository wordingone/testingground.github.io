package io.lumen.mobile

import android.content.Context
import android.graphics.*
import android.view.View

class OverlayView(ctx: Context): View(ctx) {
  var points: List<FloatArray> = emptyList()
  override fun onDraw(c: Canvas) {
    val p = Paint().apply { color = Color.GREEN; strokeWidth = 4f; style = Paint.Style.FILL }
    points.forEach { (x,y,_) -> c.drawCircle(x*width, y*height, 6f, p) }
  }
}
