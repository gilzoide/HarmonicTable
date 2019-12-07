package com.gilzoide.harmonictable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.ceil

const val DEFAULT_ROWS = 5
const val DEFAULT_COLUMNS = 5


class Keyboard : ViewGroup {
    private var _densityRadius: Float = 0f

    private var rows: Int = DEFAULT_ROWS
    private var columns: Int = DEFAULT_COLUMNS
    private var radius: Float = HEXAGON_DEFAULT_RADIUS
        set(value) {
            field = value
            _densityRadius = value * resources.displayMetrics.density
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
        val a = context.obtainStyledAttributes(attrs, R.styleable.Keyboard, defStyle, 0)

        rows = a.getInt(R.styleable.Keyboard_rows, DEFAULT_ROWS)
        columns = a.getInt(R.styleable.Keyboard_columns, DEFAULT_COLUMNS)
        radius = a.getDimension(R.styleable.Keyboard_keyRadius, HEXAGON_DEFAULT_RADIUS)

        a.recycle()

        refreshKeys()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = columns * getS(_densityRadius) + _densityRadius * 0.5
        val height = (rows + 0.5f) * getH(_densityRadius)
        setMeasuredDimension(ceil(width).toInt(), ceil(height).toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val currentCount = childCount
        if (currentCount == 0) return
        val leftPos = paddingLeft
        val topPos = paddingTop

        val w = ceil(getW(_densityRadius))
        val h = ceil(getH(_densityRadius))
        val s = getS(_densityRadius)
        val wInt = w.toInt()
        val hInt = h.toInt()

        for (i in 0 until currentCount) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                val iRow = i / columns
                val iColumn = i % columns
                val l = leftPos + (iColumn * s).toInt()
                val t = topPos + (iRow * h + (iColumn % 2) * h * 0.5f).toInt()
                child.layout(l, t, l + wInt, t + hInt)
            }
        }
    }

    private fun refreshKeys() {
        val currentCount = childCount
        val newCount = rows * columns
        if (currentCount < newCount) {
            for (i in currentCount until newCount) {
                val key = HexagonView(context)
                key.radius = radius
                addView(key)
            }
        }
    }
}
