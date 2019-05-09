package com.group9.dora

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import android.widget.LinearLayout
import android.content.DialogInterface




class fragment_manualcontrols : Fragment() {

    var dialogAcknowledged = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_manualcontrols, container, false)

        setup(view)

        return view
    }


    fun handleInput(x: Int, y: Int, width: Int, height: Int){

        if(isInBox(x, y, width, height, 1, 1)){
            Log.d("APP", "Forward-left");
            BT.bluetooth.sendMessage("1")
        }else if(isInBox(x, y, width, height, 2, 1)){
            Log.d("APP", "Forward");
            BT.bluetooth.sendMessage("2")
        }else if(isInBox(x, y, width, height, 3, 1)){
            Log.d("APP", "Forward-right");
            BT.bluetooth.sendMessage("3")
        }else if(isInBox(x, y, width, height, 1, 2)){
            Log.d("APP", "Left");
            BT.bluetooth.sendMessage("4")
        }else if(isInBox(x, y, width, height, 2, 2)){
            Log.d("APP", "Center");
            BT.bluetooth.sendMessage("5")
        }else if(isInBox(x, y, width, height, 3, 2)){
            Log.d("APP", "Right");
            BT.bluetooth.sendMessage("6")
        }else if(isInBox(x, y, width, height, 1, 3)){
            Log.d("APP", "Back-left");
            BT.bluetooth.sendMessage("7")
        }else if(isInBox(x, y, width, height, 2, 3)){
            Log.d("APP", "Back");
            BT.bluetooth.sendMessage("8")
        }else if(isInBox(x, y, width, height, 3, 3)){
            Log.d("APP", "Back-right");
            BT.bluetooth.sendMessage("9")
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

            if(BT.bluetooth.isConnected()) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> handleInput(x, y, gamepadArea.width, gamepadArea.height)
                    MotionEvent.ACTION_MOVE -> handleInput(x, y, gamepadArea.width, gamepadArea.height)
                    MotionEvent.ACTION_UP -> BT.bluetooth.sendMessage("5")
                }
            }else{
                if(dialogAcknowledged) {
                    dialogAcknowledged = false
                    AlertDialog.Builder(context)
                        .setTitle("Bluetooth error")
                        .setMessage("Bluetooth is not connected.\nCheck connection and try again.\uD83D\uDC4F")
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                            dialogAcknowledged = true
                        })
                        .setCancelable(false)
                        .show()
                }
            }

            true
        }


        gamepadArea.setOnTouchListener(handleTouch)
    }

}