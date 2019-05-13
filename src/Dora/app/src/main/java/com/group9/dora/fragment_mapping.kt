package com.group9.dora

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
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

    lateinit var canvas: Canvas
    lateinit var canvasBitmap: Bitmap
    val paint = Paint()

    var automaticMapping: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_mapping, container, false)
        bluetoothHandler(view)

        val canvas = view.findViewById<ImageView>(R.id.imageViewCanvas)

        canvasHandler(canvas)
        buttonHandler(view)

        return view
    }

    fun buttonHandler(view: View){
        val startButton = view.findViewById<Button>(R.id.button_start)
        val stopButton = view.findViewById<Button>(R.id.button_stop)

        if(!automaticMapping){
            startButton.isEnabled = true
            stopButton.isEnabled = false
        }else{
            startButton.isEnabled = false
            stopButton.isEnabled = true
        }

        startButton.setOnClickListener {
            if(BT.bluetooth.isConnected()) {
                startButton.isEnabled = false
                stopButton.isEnabled = true

                BT.bluetooth.sendMessage("G")
            }else{
                AlertDialog.Builder(context)
                    .setTitle("Bluetooth error")
                    .setMessage("Bluetooth is not connected.\nCheck connection and try again.\uD83D\uDC4F")
                    .setPositiveButton(android.R.string.ok, null)
                    .setCancelable(false)
                    .show()
            }
        }

        stopButton.setOnClickListener {
            if(BT.bluetooth.isConnected()) {
                startButton.isEnabled = true
                stopButton.isEnabled = false

                BT.bluetooth.sendMessage("S")
            }else{
                AlertDialog.Builder(context)
                    .setTitle("Bluetooth error")
                    .setMessage("Bluetooth is not connected.\nCheck connection and try again.\uD83D\uDC4F")
                    .setPositiveButton(android.R.string.ok, null)
                    .setCancelable(false)
                    .show()
            }
        }
    }

    fun canvasHandler(canvasImage: ImageView){
        canvasImage.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Clear Mapping?")
                .setMessage("Are you sure you want to clear the mapped area?")
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    Measure.clearCoordinates()
                    clearCanvas(canvasImage)
                }
                .setNegativeButton(android.R.string.no, null)
                .setCancelable(false)
                .show()
        }

        clearCanvas(canvasImage)
    }

    fun clearCanvas(canvasImage: ImageView){
        if(::canvasBitmap.isInitialized)canvasBitmap.recycle()
        canvasBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        canvasBitmap = canvasBitmap.copy(canvasBitmap.config, true)
        canvas = Canvas(canvasBitmap)
        canvasImage.setImageBitmap(canvasBitmap)
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE

        //Values:
        paint.strokeWidth = 4.5f
        paint.textSize = 60f
    }

    fun drawCanvas(canvasImage: ImageView){
        ///TODO: Convert coordinates to actual lines on bitmap
        canvasImage.setImageBitmap(canvasBitmap)
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