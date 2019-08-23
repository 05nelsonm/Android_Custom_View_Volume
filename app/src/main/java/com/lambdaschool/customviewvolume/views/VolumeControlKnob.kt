package com.lambdaschool.customviewvolume.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class VolumeControlKnob(context: Context?, attrs: AttributeSet) : View(context, attrs) {

    private val paintKnob = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintHandle = Paint(Paint.ANTI_ALIAS_FLAG)
    private var startX = 0f
    private var diffX = 0f
    private var rotationLast = rotation
    private var pctVol = 50.0f // starts at 50%

    init {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean { // triggered each time the touch state changes

        when (event.action) {
            MotionEvent.ACTION_DOWN -> { // triggered when view is touched
                startX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                // triggered after ACTION_DOWN but when touch is moved
                // get end point and calculate total distance traveled
                // use the total distance traveled to calculate the desired change in rotation
                // apply that change to your rotation variable
                // you may want to use a minimum and maximum rotation value to limit the rotation
                // use the new rotation to convert to the desired volume setting
                // this will cause the onDraw method to be called again with your new values
                diffX = event.x - startX
                rotation += diffX / 20
                invalidate()
            }
            MotionEvent.ACTION_UP -> { // triggered when touch ends
                if (diffX != 0f) {
                    pctVol += ((rotation - rotationLast) / 85) * 50
                    if (pctVol > 100) {
                        toastPercentVolume(100f)
                    } else if (pctVol < 0) {
                        toastPercentVolume(0f)
                    } else {
                        toastPercentVolume(pctVol)
                    }
                    rotationLast = rotation
                } else {
                    toastPercentVolume(pctVol)
                }
            }
        }
        // get and store start point with event.getX()
        return true // this indicates that the event has been processed
    }

    override fun onDraw(canvas: Canvas?) {

        if (rotation > 85) {
            rotation = 85f
        } else if (rotation < -85) {
            rotation = -85f
        }

        val largeCircleRad = width / 2f - width / 10f
        val smallCircleCenterOffSetX = width / 2f + width / 3.5f

        paintKnob.color = Color.BLACK
        paintHandle.color = Color.RED

        canvas?.rotate(rotation, width / 2f, height / 2f)
        canvas?.drawCircle(width / 2f, height / 2f, largeCircleRad, paintKnob)
        canvas?.drawCircle(smallCircleCenterOffSetX, height / 2f, width / 20f, paintHandle)

        super.onDraw(canvas)
    }

    private fun toastPercentVolume(num: Float) {
        Toast.makeText(context, "Percent Volume = $num%", Toast.LENGTH_SHORT).show()
    }
}