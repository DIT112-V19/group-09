package com.group9.dora

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log



class Measure {

    companion object {

        var measurements: ArrayList<RoomCoordinate> = ArrayList()
        var dominantLine: ArrayList<RoomCoordinate> = ArrayList()

        var distanceTraveled = 0f

        fun addDistance(dist: Float){
            distanceTraveled = dist
        }

        fun clearDistance(){
            distanceTraveled = 0f
        }

        fun verifyDistance(message: String){
            if(message.startsWith("#")){
                if(message.replace("#","").toFloatOrNull() != null){
                    addDistance(message.replace("#","").toFloat())
                }
            }
        }

        fun addCoordinate(coordinate: RoomCoordinate){
            measurements.add(coordinate)
        }

        fun clearCoordinates(){
            measurements.clear()
        }

        fun verifyCoordinate(message: String){
            val splittedString = message.split(",")

            if(splittedString.size == 2){
                if(splittedString[0].toFloatOrNull() != null && splittedString[1].toFloatOrNull() != null && (splittedString[0].toFloat() != 0f && splittedString[1].toFloat() != 0f)){
                    addCoordinate(RoomCoordinate(splittedString[0].toFloat(), splittedString[1].toFloat()))
                }
            }
        }

        fun drawDominantText(canvas: Canvas) {
                var middleX = 0f
                var middleY = 0f

                for (index in 0 until dominantLine.size) {
                    middleX += dominantLine[index].positionX
                    middleY += dominantLine[index].positionY
                }

                    middleX /= dominantLine.size
                    middleY /= dominantLine.size

                    val fillPaint = Paint()
                    fillPaint.color = Color.BLACK
                    fillPaint.textSize = 40f
                    fillPaint.textAlign = Paint.Align.CENTER

                    val stkPaint = Paint()
                    stkPaint.style = Paint.Style.STROKE
                    stkPaint.strokeWidth = 10f
                    stkPaint.color = Color.WHITE
                    stkPaint.textSize = 40f
                    stkPaint.textAlign = Paint.Align.CENTER


                    canvas.drawText("TEST", middleX, middleY, stkPaint)
                    canvas.drawText("TEST", middleX, middleY, fillPaint)

        }

    }

}