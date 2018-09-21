package com.alsaev.myapps.crossesandcircles.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.alsaev.myapps.crossesandcircles.R

class GameFieldView : View {
    private val CIRCLE = 1
    private val CROSS = 2

    private val DEFAULT_GAME_FIELD_COLOR = Color.BLACK
    private val DEFAULT_CROSS_COLOR = Color.BLACK
    private val DEFAULT_CIRCLE_COLOR = Color.BLACK

    private var colorGameField = DEFAULT_GAME_FIELD_COLOR
    private var colorCross = DEFAULT_CROSS_COLOR
    private var colorCircle = DEFAULT_CIRCLE_COLOR

    private var gameFieldPaint = Paint()
    private var crossPaint = Paint()
    private var circlePaint = Paint()

    private var gameFieldAnimation = ValueAnimator()
    private var gameFieldProgress = 0f
    private var figuresProgress = ArrayList<Figure>()

    private var size = 0f
    private var spanSize = 0f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GameFieldView)

        colorGameField = typedArray.getColor(R.styleable.GameFieldView_gfv_gamefieldColor, DEFAULT_GAME_FIELD_COLOR)
        colorCross = typedArray.getColor(R.styleable.GameFieldView_gfv_crossColor, DEFAULT_CROSS_COLOR)
        colorCircle = typedArray.getColor(R.styleable.GameFieldView_gfv_circleColor, DEFAULT_CIRCLE_COLOR)

        typedArray.recycle()
        init()
    }

    private fun init() {
        gameFieldPaint.color = colorGameField
        gameFieldPaint.isAntiAlias = true
        gameFieldPaint.setStrokeJoin(Paint.Join.ROUND);
        gameFieldPaint.setStrokeCap(Paint.Cap.ROUND);

        crossPaint = Paint(gameFieldPaint)
        crossPaint.color = colorCross

        circlePaint = Paint(gameFieldPaint)
        circlePaint.style = Paint.Style.STROKE
        circlePaint.color = colorCircle

        gameFieldAnimation.duration = 1000
        gameFieldAnimation.setFloatValues(0f, 100f)
        gameFieldAnimation.addUpdateListener {
            gameFieldProgress = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mWidth = MeasureSpec.getSize(widthMeasureSpec)
        val mHeight = MeasureSpec.getSize(heightMeasureSpec)

        size = Math.min(mWidth, mHeight).toFloat()

        val strokeWidth = size / 50

        gameFieldPaint.strokeWidth = strokeWidth
        crossPaint.strokeWidth = strokeWidth
        circlePaint.strokeWidth = strokeWidth

        gameFieldAnimation.setFloatValues(0f, size - strokeWidth * 2)

        spanSize = size / 3 //- strokeWidth * 2

        setMeasuredDimension(size.toInt(), size.toInt())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        gameFieldAnimation.start()
        setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                var xCount = 0
                if (motionEvent.x < spanSize)
                    xCount = 0
                else if (motionEvent.x < spanSize * 2)
                    xCount = 1
                else xCount = 2

                var yCount = 0
                if (motionEvent.y < spanSize)
                    yCount = 0
                else if (motionEvent.y < spanSize * 2)
                    yCount = 1
                else yCount = 2

                val pos = xCount + yCount * 3
                var isExist = false

                figuresProgress.forEach {
                    if (it.position == pos) {
                        isExist = false
                        return@forEach
                    }
                }

                if (!isExist) {
                    if (figuresProgress.isEmpty()) {
                        figuresProgress.add(CrossFigure(pos))
                    } else
                        figuresProgress.add(if (figuresProgress.last() is CircleFigure) CrossFigure(pos) else CircleFigure(pos))

                    figuresProgress.last().start()
                }
            }
            return@setOnTouchListener true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGameField(canvas)
        drawFigures(canvas)
    }

    private fun drawFigures(canvas: Canvas) {
        figuresProgress.forEach {
            it.drawSelf(canvas)
        }
    }

    private fun drawGameField(canvas: Canvas) {
        drawVerticalLines(canvas)
        drawHorizontalLines(canvas)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        for (i in 0 until 2) {
            val y = (size * (i + 1)) / 3

            val x1 = size / 2 - gameFieldProgress / 2
            val x2 = size / 2 + gameFieldProgress / 2

            canvas.drawLine(x1, y, x2, y, gameFieldPaint)
        }
    }

    private fun drawVerticalLines(canvas: Canvas) {
        for (i in 0 until 2) {
            val x = (size * (i + 1)) / 3

            val y1 = size / 2 - gameFieldProgress / 2
            val y2 = size / 2 + gameFieldProgress / 2

            canvas.drawLine(x, y1, x, y2, gameFieldPaint)
        }
    }

    abstract inner class Figure(pos: Int) {
        private var padding = spanSize / 10

        protected var animator = ValueAnimator()
        protected var progress = 0f
        protected val x1: Float
        protected val x2: Float
        protected val y1: Float
        protected val y2: Float

        var position = 0

        init {
            animator.duration = 1000
            position = pos
            animator.addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            x1 = (position % 3) * spanSize + padding
            x2 = (position % 3 + 1) * spanSize - padding
            y1 = (position / 3) * spanSize + padding
            y2 = (position / 3 + 1) * spanSize - padding
        }

        abstract fun drawSelf(canvas: Canvas)
        abstract fun start()
    }

    inner class CircleFigure(pos: Int) : Figure(pos) {
        private val rectF: RectF = RectF(x1, y1, x2, y2)

        override fun drawSelf(canvas: Canvas) {
            canvas.drawArc(rectF, 0f, progress, false, circlePaint)
        }

        override fun start() {
            animator.setFloatValues(0f, 360f)
            animator.start()
        }
    }

    inner class CrossFigure(pos: Int) : Figure(pos) {
        private val length = x2 - x1

        override fun drawSelf(canvas: Canvas) {
            Log.d("progress", progress.toString())
            val len1 = if (progress < 50) length * (progress / 50) else length
            canvas.drawLine(x1, y1, x1 + len1, y1 + len1, crossPaint)

            val len2 = if (progress < 100) if (progress < 50) 0f else length * (progress / 50) else length
            canvas.drawLine(x2, y1, x2 -len2, y1 + len2, crossPaint)
        }

        override fun start() {
            animator.setFloatValues(0f, 100f)
            animator.start()
        }
    }
}