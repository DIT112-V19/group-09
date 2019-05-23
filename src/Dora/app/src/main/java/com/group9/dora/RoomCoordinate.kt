package com.group9.dora

class RoomCoordinate(val positionX: Float, val positionY: Float, var hasBeenDrawn: Boolean = false) {

    fun getX(): Float{
        return positionX
    }

    fun getY(): Float{
        return positionY
    }

}