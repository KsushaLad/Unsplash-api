package com.codinginflow.imagesearchapp

import android.content.Context
import android.view.ScaleGestureDetector

import android.view.MotionEvent
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View

// TODO надо более явно называть переменные. Если я буду работать в файле, где будет много методов, то я не пойму что такое c
// TODO Мне прийдется подниматься на верх, чтобы узнать, что это Context
class MyScaleGestures(c: Context?) : View.OnTouchListener, OnScaleGestureListener {
    private var view: View? = null
    // Здесь
    // TODO тут лучше изспользовать ленивые переменные  private val gestureScale by lazy { ScaleGestureDetector(c, this) }
    private val gestureScale: ScaleGestureDetector
    private var scaleFactor = 0.1f
    private var inScale = false
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        this.view = view
        gestureScale.onTouchEvent(event)
        return true
    }

    // TODO студия подсветила, что в котлие есть более простые сеттеры
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor *= detector.scaleFactor
        scaleFactor = if (scaleFactor < 1) 1F else scaleFactor
        scaleFactor = (scaleFactor * 1150).toInt().toFloat() / 1150
        view?.setScaleX(scaleFactor)
        view?.setScaleY(scaleFactor)
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        inScale = true
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        inScale = false
    }

    // TODO init здесь не нужен
    init {
        gestureScale = ScaleGestureDetector(c, this)
    }
}