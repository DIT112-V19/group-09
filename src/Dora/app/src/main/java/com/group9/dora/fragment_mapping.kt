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
import android.support.v4.math.MathUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.layout_manualcontrols.*
import java.util.*


class fragment_mapping : Fragment() {

    lateinit var canvas: Canvas
    lateinit var canvasBitmap: Bitmap
    val paint = Paint()

    var automaticMapping: Boolean = false

    var canvasSizeX = 0
    var canvasSizeY = 0

    var paintStroke = 10f


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_mapping, container, false)
        bluetoothHandler(view)

        val canvas = view.findViewById<ImageView>(R.id.imageViewCanvas)

        canvasHandler(canvas)
        buttonHandler(view)
        //simulateRealArduinoCar() //TURN THIS ON FOR DEBUGGING WITHOUT USING AN ACTUAL CAR

        return view
    }

    fun buttonHandler(view: View){
        val startButton = view.findViewById<Button>(R.id.button_start)
        val stopButton = view.findViewById<Button>(R.id.button_stop)

        BT.bluetooth.startButton = startButton
        BT.bluetooth.stopButton = stopButton

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
                startButton.isEnabled = true
                stopButton.isEnabled = false
            if(BT.bluetooth.isConnected()) {
                BT.bluetooth.sendMessage("S")
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
        drawCanvas(canvasImage)
    }

    fun clearCanvas(canvasImage: ImageView){
        if(::canvasBitmap.isInitialized)canvasBitmap.recycle()
        setCanvas()
        canvasBitmap = canvasBitmap.copy(canvasBitmap.config, true)
        canvas = Canvas(canvasBitmap)
        canvasImage.setImageBitmap(canvasBitmap)
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL

        //Values:
        paint.strokeWidth = paintStroke
        paint.textSize = 40f

        paint.textAlign = Paint.Align.CENTER


        for(index in 0 until Measure.measurements.size){
            Measure.measurements[index].hasBeenDrawn = false
        }
    }

    fun setCanvas(){
        //val value = MathUtils.clamp(highestVal.toInt(), 250, 99999)
        canvasSizeX = 610
        canvasSizeY = 810
        canvasBitmap = Bitmap.createBitmap(canvasSizeX, canvasSizeY, Bitmap.Config.ARGB_8888)
    }


    fun highestValueX(): Float{
        var value = 0f
        var highestValue = 0f
        for(index in 0 until Measure.measurements.size){
            value += Measure.measurements[index].positionX
            if(value > highestValue){
                highestValue = value
            }
        }
        return highestValue
    }

    fun highestValueY(): Float{
        var value = 0f
        var highestValue = 0f
        for(index in 0 until Measure.measurements.size){
            value += Measure.measurements[index].positionY
            if(value > highestValue){
                highestValue = value
            }
        }
        return highestValue
    }

    fun lowestValueX(): Float{
        var value = 0f
        var lowestValue = 0f
        for(index in 0 until Measure.measurements.size){
            value += Measure.measurements[index].positionX
            if(value < lowestValue){
                lowestValue = value
            }
        }
        return lowestValue
    }

    fun lowestValueY(): Float{
        var value = 0f
        var lowestValue = 0f
        for(index in 0 until Measure.measurements.size){
            value += Measure.measurements[index].positionY
            if(value < lowestValue){
                lowestValue = value
            }
        }
        return lowestValue
    }


    fun simulateRealArduinoCar(){


        /*val timerTask = object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
        */
        Measure.addCoordinate(RoomCoordinate(300f, -200f))
        Measure.addCoordinate(RoomCoordinate(100f, 500f))
        Measure.addCoordinate(RoomCoordinate(-225f, -200f))
        Measure.addCoordinate(RoomCoordinate(-125f, 100f))
        Measure.addCoordinate(RoomCoordinate(-500f, -200f))
        Measure.addCoordinate(RoomCoordinate(100f, 300f))
        Measure.addCoordinate(RoomCoordinate(400f, 200f))
        /*
                }
            }
        }

            val timer = Timer()
            timer.scheduleAtFixedRate(timerTask, 0, 1000L)*/
    }

    fun drawCanvas(canvasImage: ImageView){

        val timerTask = object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                        clearCanvas(canvasImage)

                    var scaleFactor = 1f

                    val potentialScaleX = (canvasSizeX / ((highestValueX() - lowestValueX()) + (paint.strokeWidth * 2)))
                    val potentialScaleY = (canvasSizeY / ((highestValueY() - lowestValueY()) + (paint.strokeWidth * 2)))

                    if (potentialScaleX < 1 || potentialScaleY < 1) {
                        if (potentialScaleX < potentialScaleY) {
                            scaleFactor = potentialScaleX * 0.9f
                        } else {
                            scaleFactor = potentialScaleY * 0.9f
                        }
                    }

                    paint.strokeWidth = paintStroke * scaleFactor

                    val offsetX =
                        ((canvasSizeX / 2) - (lowestValueX() * scaleFactor + highestValueX() * scaleFactor) / 2)
                    val offsetY =
                        ((canvasSizeY / 2) - (lowestValueY() * scaleFactor + highestValueY() * scaleFactor) / 2)


                    var posX = 0f
                    var posY = 0f
                    var lastColor: Int = Color.YELLOW

                    for (index in 0 until Measure.measurements.size) {
                        if (!Measure.measurements[index].hasBeenDrawn) {
                            if (Math.abs(Measure.measurements[index].positionX) >= Math.abs(Measure.measurements[index].positionY)) {
                                paint.color = Color.BLUE
                            } else {
                                paint.color = Color.RED
                            }

                            if(lastColor != paint.color){
                                lastColor = paint.color


                                Log.d("APP", "Drawing line. Dots collected: " + Measure.dominantLine.size)
                                Measure.drawDominantText(canvas)
                                Measure.dominantLine.clear()
                            }

                            Measure.measurements[index].hasBeenDrawn = true
                            canvas.drawLine(
                                (posX * scaleFactor) + offsetX,
                                (posY * scaleFactor) + offsetY,
                                ((posX + Measure.measurements[index].positionX) * scaleFactor) + offsetX,
                                ((posY + Measure.measurements[index].positionY) * scaleFactor) + offsetY,
                                paint
                            )
                            if(Measure.dominantLine.isEmpty()){
                                Measure.dominantLine.add(RoomCoordinate((posX * scaleFactor) + offsetX, (posY * scaleFactor) + offsetY))
                            }
                            Measure.dominantLine.add(
                                RoomCoordinate(((posX + Measure.measurements[index].positionX) * scaleFactor) + offsetX, ((posY + Measure.measurements[index].positionY) * scaleFactor) + offsetY)
                            )

                        }
                        posX += Measure.measurements[index].positionX
                        posY += Measure.measurements[index].positionY
                    }

                    canvasImage.setImageBitmap(canvasBitmap)
                }
                }
            }

        val timer = Timer()
        timer.scheduleAtFixedRate(timerTask, 0, 250L)


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