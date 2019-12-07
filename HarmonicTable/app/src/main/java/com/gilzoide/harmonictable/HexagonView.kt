package com.gilzoide.harmonictable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.math.min

fun getW(radius: Float) = 2f * radius
fun getS(radius: Float) = 1.5f * radius
fun getH(radius: Float) = sqrt(3f) * radius

const val HEXAGON_DEFAULT_RADIUS: Float = 40f
const val HEXAGON_DEFAULT_COLOR: Int = Color.BLACK

class HexagonView : View {
    private var _paint: Paint = Paint()
    private var _path: Path = Path()
    private var _densityRadius: Float = 0f

    var radius: Float = HEXAGON_DEFAULT_RADIUS
        set(value) {
            field = value
            _densityRadius = value * resources.displayMetrics.density
            refreshPath()
            invalidate()
        }

    var strokeColor: Int = HEXAGON_DEFAULT_COLOR
        set(value) {
            field = value
            _paint.color = value
            invalidate()
        }

    private fun getW(): Float = getW(_densityRadius)
    private fun getS(): Float = getS(_densityRadius)
    private fun getH(): Float = getH(_densityRadius)

    private fun refreshPath() {
        val w = getW()
        val s = getS()
        val h = getH()
        val halfH = h * 0.5f
        val sRadius = s - _densityRadius

        _path.apply {
            reset()
            moveTo(0f, halfH)
            lineTo(sRadius, 0f)
            lineTo(s, 0f)
            lineTo(w, halfH)
            lineTo(s, h)
            lineTo(sRadius, h)
            lineTo(0f, halfH)
        }
    }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.HexagonView, defStyle, 0)

        radius = a.getDimension(R.styleable.HexagonView_radius, HEXAGON_DEFAULT_RADIUS)
        strokeColor = a.getColor(R.styleable.HexagonView_strokeColor, HEXAGON_DEFAULT_COLOR)

        _paint.style = Paint.Style.STROKE
        _paint.color = strokeColor

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = ceil(getW()).toInt()
        val h = ceil(getH()).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when(widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(widthSize, w)
            MeasureSpec.UNSPECIFIED -> w
            else -> w
        }

        val height = when(heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(heightSize, h)
            MeasureSpec.UNSPECIFIED -> h
            else -> h
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(_path, _paint)
    }
}
