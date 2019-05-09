package com.group9.dora

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import kotlinx.android.synthetic.main.layout_manualcontrols.*


class fragment_mapping : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_mapping, container, false)
        bluetoothHandler(view)
        drawCanvas(view)
        return view
    }

    fun drawCanvas(view: View){
        var bitMap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)

        bitMap = bitMap.copy(bitMap.config, true)
        // Construct a canvas with the specified bitmap to draw into
        val canvas = Canvas(bitMap)
        // Create a new paint with default settings.
        val paint = Paint()
        // smooths out the edges of what is being drawn
        paint.isAntiAlias = true
        // set color
        paint.color = Color.BLACK
        // set style
        paint.style = Paint.Style.STROKE
        // set stroke
        paint.strokeWidth = 4.5f
        // draw circle with radius 30
        canvas.drawCircle(500f, 500f, 30f, paint)
        paint.textSize = 60f

        canvas.drawText("Susan is asian", 250f, 250f, paint)
        // set on ImageView or any other view
        view.findViewById<ImageView>(R.id.imageViewCanvas).setImageBitmap(bitMap)
    }

    fun bluetoothHandler(view: View){
        val bluetoothBT = view.findViewById<Button>(R.id.bluetooth_button)


        if(BT.bluetooth.isConnected()){
            BT.bluetooth.BTEnabled(bluetoothBT)
        }else{
            BT.bluetooth.BTDisabled(bluetoothBT)
        }



        bluetoothBT.setOnClickListener {
            if(!BT.bluetooth.isConnected()){
                bluetoothBT.text = "Connecting..."
                bluetoothBT.isEnabled = false
                BT.bluetooth.connect(BT.bluetooth.getCarMacAddress(), bluetoothBT)
            }else{
                BT.bluetooth.bluetooth.disconnect()
            }
        }
    }

}