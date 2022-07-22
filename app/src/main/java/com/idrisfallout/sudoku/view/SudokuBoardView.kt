package com.idrisfallout.sudoku.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SudokuBoardView (context: Context, attributeSet: AttributeSet): View(context, attributeSet){

    private var sqrtSize = 3
    private var size = 9

    private var cellSizePixels = 0f

    private var selectedRow = 0
    private var selectedColumn = 0

    private val thicklinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4f
    }
    private val thinlinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2f
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efedef")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas){
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
    }

    private fun fillCells(canvas: Canvas) {
        if (selectedRow == -1 || selectedColumn == -1) return
        for (r in 0..size){
            for (c in 0..size){
                if (r == selectedRow && c == selectedColumn){
                    fillCell(canvas, r, c, selectedCellPaint)
                }else if (r == selectedRow || c == selectedColumn){
                    fillCell(canvas, r, c, conflictingCellPaint)
                }else if (r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedColumn / sqrtSize){
                    fillCell(canvas, r, c, conflictingCellPaint)
                }
            }
        }

    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels, (c+1) * cellSizePixels, (r+1) * cellSizePixels, paint)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), thicklinePaint)

        for (i in 1 until size){
            val paintToUse = when(i % sqrtSize){
                0 -> thicklinePaint
                else -> thinlinePaint
            }
            canvas.drawLine(i * cellSizePixels, 0f, i * cellSizePixels, height.toFloat(), paintToUse)
            canvas.drawLine(0f, i * cellSizePixels, width.toFloat(), i * cellSizePixels, paintToUse)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action){
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float): Boolean{
        selectedRow = (y / cellSizePixels).toInt()
        selectedColumn = (x / cellSizePixels).toInt()
        invalidate()
        return true
    }

}