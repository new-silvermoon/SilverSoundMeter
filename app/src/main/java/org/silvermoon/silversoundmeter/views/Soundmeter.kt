package org.silvermoon.silversoundmeter.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatImageView
import org.silvermoon.silversoundmeter.R
import org.silvermoon.silversoundmeter.util.DecibelUtils


class Soundmeter: AppCompatImageView {

    constructor(context: Context): super(context)
    constructor(context: Context,attrs: AttributeSet): super(context,attrs)

    val mMatrix = Matrix()
    var pin_bitmap: Bitmap? = null
    var paint: Paint? = null
    val ANIMATION_DELAY_INTERVAL = 20L
    var view_width: Int? = null
    var view_height: Int? = null



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(pin_bitmap == null){
            init()
        }

        mMatrix.setRotate(getAngle(DecibelUtils.dbCount),(view_width!!/2).toFloat(),(view_height!! * 215 / 460).toFloat() )
        canvas!!.drawBitmap(pin_bitmap!!,mMatrix,paint)
        canvas!!.drawText((DecibelUtils.dbCount.toInt()).toString() + " DB", (view_width!!/2).toFloat(), (view_height!!*36/46).toFloat(), paint!! )
    }


    fun init(){
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.soundmeter_pin)
        val width = bitmap.width
        val height = bitmap.height
        view_width = getWidth()
        view_height = getHeight()
        val scale_width = view_width!!.toFloat() / width.toFloat()
        val scale_height = view_height!!.toFloat()/ height.toFloat()

        mMatrix.postScale(scale_width,scale_height)
        pin_bitmap = Bitmap.createBitmap(bitmap,0,0,width,height,mMatrix,true)

        paint = Paint()
        paint!!.textSize = 44f
        paint!!.isAntiAlias = true
        paint!!.textAlign = Paint.Align.CENTER
        paint!!.color = Color.WHITE




    }

    fun refresh(){
        postInvalidateDelayed(ANIMATION_DELAY_INTERVAL)
    }

    private fun getAngle(db: Float): Float {
        return (db - 85) * 5 / 3
    }


}