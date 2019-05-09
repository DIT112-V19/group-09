package com.group9.dora

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import me.aflak.bluetooth.Bluetooth

class BT {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var bluetooth: BluetoothHandler

        fun init(context: Context, activity: Activity){
            bluetooth = BluetoothHandler(context, activity)
            bluetooth.init(context)
        }
    }

}