package com.alsaev.myapps.crossesandcircles.view

import android.animation.Animator
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
    private val EMPTY = 0
    val CROSS = 1
    val CIRCLE = 2

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
    private var figures = ArrayList<Figure>()

    private var fieldSpans = arrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)

    private var size = 0f
    private var spanSize = 0f
    private var canTouch = true
    private var isFinished = false

    var finishListener: FinishListener? = null

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
            if (motionEvent.action == MotionEvent.ACTION_DOWN && canTouch) {
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

                figures.forEach {
                    if (it.position == pos) {
                        isExist = true
                        return@forEach
                    }
                }

                if (!isExist) {
                    if (figures.isEmpty()) {
                        setFigureInSpan(CROSS, pos)
                    } else
                        setFigureInSpan(if (figures.last() is CircleFigure) CROSS else CIRCLE, pos)
                }
            }
            return@setOnTouchListener true
        }
    }

    fun setFigureInSpan(figure: Int, position: Int) {
        canTouch = false
        figures.add(if (figure == CROSS) CrossFigure(position) else CircleFigure(position))
        figures.last().start()
        fieldSpans[position] = figure

        checkOnWin()
    }

    private fun checkOnWin() {
        for (i in 0 until 3) {
            val j = i * 3
            if (fieldSpans[j] != EMPTY && fieldSpans[j] == fieldSpans[j + 1] && fieldSpans[j] == fieldSpans[j + 2]) {
                finish(fieldSpans[j])
                return
            }
            if (fieldSpans[i] != EMPTY && fieldSpans[i] == fieldSpans[i + 3] && fieldSpans[i] == fieldSpans[i + 6]) {
                finish(fieldSpans[i])
                return
            }
        }
        if (fieldSpans[0] != EMPTY && fieldSpans[0] == fieldSpans[4] && fieldSpans[0] == fieldSpans[8]) {
            finish(fieldSpans[0])
            return
        }
        if (fieldSpans[2] != EMPTY && fieldSpans[2] == fieldSpans[4] && fieldSpans[2] == fieldSpans[6]) {
            finish(fieldSpans[2])
            return
        }
    }

    private fun finish(figure: Int) {
        Log.d("WIN", if (figure == CROSS) "CROSS" else "CIRCLE")
        isFinished = true
        finishListener?.onFinish(fieldSpans[figure])
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGameField(canvas)
        drawFigures(canvas)
    }

    private fun drawFigures(canvas: Canvas) {
        figures.forEach {
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
        private var padding = spanSize / 5

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
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    if (!isFinished) canTouch = true
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            })
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
            val len1 = if (progress < 50) length * (progress / 50) else length
            canvas.drawLine(x1, y1, x1 + len1, y1 + len1, crossPaint)

            val len2 = if (progress < 50) 0f else length * ((progress - 50) / 50)
            canvas.drawLine(x2, y1, x2 - len2, y1 + len2, crossPaint)
        }

        override fun start() {
            animator.setFloatValues(0f, 100f)
            animator.start()
        }
    }

    interface FinishListener {
        fun onFinish(winFigure: Int)
    }
}