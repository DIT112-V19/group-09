package com.group9.dora

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import android.widget.LinearLayout


class fragment_manualcontrols : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_manualcontrols, container, false)

        setup(view)

        return view
    }

    fun handleBluetooth(){

    }

    fun handleInput(x: Int, y: Int, width: Int, height: Int){
        if(isInBox(x, y, width, height, 1, 1)){
            Log.d("APP", "Forward-left");
        }else if(isInBox(x, y, width, height, 2, 1)){
            Log.d("APP", "Forward");
        }else if(isInBox(x, y, width, height, 3, 1)){
            Log.d("APP", "Forward-right");
        }else if(isInBox(x, y, width, height, 1, 2)){
            Log.d("APP", "Left");
        }else if(isInBox(x, y, width, height, 2, 2)){
            Log.d("APP", "Center");
        }else if(isInBox(x, y, width, height, 3, 2)){
            Log.d("APP", "Right");
        }else if(isInBox(x, y, width, height, 1, 3)){
            Log.d("APP", "Back-left");
        }else if(isInBox(x, y, width, height, 2, 3)){
            Log.d("APP", "Back");
        }else if(isInBox(x, y, width, height, 3, 3)){
            Log.d("APP", "Back-right");
        }else{
            Log.d("APP", "No ducks found");
        }
    }

    fun isInBox(x: Int, y: Int, width: Int, height: Int, widthBox: Int, heightBox: Int): Boolean {
        return x > ((width / 3) * (widthBox - 1)) && x < ((width / 3) * (widthBox)) && y > ((height / 3) * (heightBox - 1)) && y < ((height / 3) * (heightBox))
    }


    @SuppressLint("ClickableViewAccessibility")
    fun setup(v: View){
        val gamepadArea = v.findViewById<LinearLayout>(R.id.gamepad_area)

        val handleTouch = View.OnTouchListener { v, event ->
            val x = event.x.toInt()
            val y = event.y.toInt()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> handleInput(x, y, gamepadArea.width, gamepadArea.height)
            }

            true
        }


        gamepadArea.setOnTouchListener(handleTouch)
    }

}