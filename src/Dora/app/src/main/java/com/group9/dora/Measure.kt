package com.group9.dora

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


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
                var isWall = false
                if(splittedString[0].contains("Q")){
                    isWall = true
                }
                if(splittedString[0].replace("Q", "").toFloatOrNull() != null && splittedString[1].toFloatOrNull() != null && (splittedString[0].replace("Q", "").toFloat() != 0f && splittedString[1].toFloat() != 0f)){
                    addCoordinate(RoomCoordinate(splittedString[0].replace("Q", "").toFloat(), splittedString[1].toFloat(), false, isWall))
                }
            }
        }

        fun drawDominantText(canvas: Canvas, scaleFactor: Float) {
                var middleX = 0f
                var middleY = 0f

                var distance = 0.0

                for (index in 0 until dominantLine.size) {
                    middleX += dominantLine[index].positionX
                    middleY += dominantLine[index].positionY

                    if(index != 0){
                        distance += getDistance(dominantLine[index], dominantLine[index - 1])
                    }
                }

                    middleX /= dominantLine.size
                    middleY /= dominantLine.size

                    val fillPaint = Paint()
                    fillPaint.color = Color.BLACK
                    fillPaint.textSize = Math.max(35*scaleFactor, 25f)
                    fillPaint.textAlign = Paint.Align.CENTER

                    val stkPaint = Paint()
                    stkPaint.style = Paint.Style.STROKE
                    stkPaint.strokeWidth = 10f
                    stkPaint.color = Color.WHITE
                    stkPaint.textSize = Math.max(35*scaleFactor, 25f)
                    stkPaint.textAlign = Paint.Align.CENTER


                    canvas.drawText("${distance.roundToInt()} cm", middleX, middleY, stkPaint)
                    canvas.drawText("${distance.roundToInt()} cm", middleX, middleY, fillPaint)

        }

        fun getDistance(point1: RoomCoordinate, point2: RoomCoordinate): Double {
            val dx   = point1.positionX - point2.positionX;
            val dy   = point1.positionY - point2.positionY;
            val dist = Math.sqrt( (dx*dx + dy*dy).toDouble().absoluteValue );
            return dist;
        }

        fun getAngle(point1: RoomCoordinate, point2: RoomCoordinate): Double {
            val dx   = point1.positionX - point2.positionX;
            val dy   = point1.positionY - point2.positionY;
            val degree = Math.atan2(dy.toDouble(), dx.toDouble())
            return Math.toDegrees(degree);
        }

    }

}