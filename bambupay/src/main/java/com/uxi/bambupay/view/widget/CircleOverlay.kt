package com.uxi.bambupay.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CircleOverlay(c: Context, attributeSet: AttributeSet) : View(c, attributeSet) {

    private val transparentPaint = Paint().apply {
        color = Color.TRANSPARENT
        strokeWidth = 10f
    }

    private val overlayPaint = Paint().apply {
        color = Color.TRANSPARENT
        strokeWidth = 10f
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        path.reset()
        path.addCircle((width / 2).toFloat(), (height / 2).toFloat(), (DIAMETER / 2).toFloat(), Path.Direction.CW)
        path.fillType = Path.FillType.INVERSE_EVEN_ODD

        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (DIAMETER / 2).toFloat(), transparentPaint)
        canvas?.drawPath(path, overlayPaint)
        canvas?.clipPath(path)
        canvas?.drawColor(Color.parseColor(TRANSPARENT))
    }

    companion object {
        const val DIAMETER = 700
        const val TRANSPARENT = "#A6000000"
    }
}