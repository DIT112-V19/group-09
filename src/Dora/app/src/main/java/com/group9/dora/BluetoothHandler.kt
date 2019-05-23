package com.group9.dora

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.widget.Button
import me.aflak.bluetooth.Bluetooth
import me.aflak.bluetooth.DeviceCallback
import android.bluetooth.BluetoothAdapter
import me.aflak.bluetooth.BluetoothCallback









class BluetoothHandler(private val context: Context, val activity: Activity) {

        var bluetooth: Bluetooth
        lateinit var startButton: Button
        lateinit var stopButton: Button

        init {
            bluetooth = Bluetooth(context)
        }

        fun isConnected(): Boolean{
            return bluetooth.isConnected
        }

        fun init(context: Context) {
            bluetooth = Bluetooth(context)
        }

        fun getCarMacAddress(): String{
            return "20:16:12:14:66:18"
        }

        fun sendMessage(msg: String){
            if(bluetooth.isConnected)bluetooth.send(msg)
        }

        fun unRegister(){
            bluetooth.removeBluetoothCallback()
            bluetooth.removeCommunicationCallback()
            bluetooth.removeDiscoveryCallback()
            if(bluetooth.isEnabled)bluetooth.onStop()
        }

        fun connect(macAddress: String, btButton: Button){
            bluetooth.onStart();
            bluetooth.enable();

            val devices = bluetooth.pairedDevices
            for (device: BluetoothDevice in devices) {
                if (device.address == macAddress) {
                    bluetooth.connectToDevice(device)
                    Log.d("BLUETOOTH", "PAIRING WITH ${device.name}")
                }
            }

            bluetooth.setDeviceCallback(object : DeviceCallback {
                override fun onDeviceConnected(device: BluetoothDevice) {
                    activity.runOnUiThread {
                        BTEnabled(btButton)
                    }

                    Log.d("BLUETOOTH CONNECTED", device.name)
                }

                override fun onDeviceDisconnected(device: BluetoothDevice, message: String) {
                    activity.runOnUiThread {
                        BTDisabled(btButton)
                    }
                    Log.d("BLUETOOTH DISCONNECTED", message)
                    Log.d("BLUETOOTH", "RECONNECT")
                }

                override fun onMessage(message: String) {
                    Log.d("BLUETOOTH MSG", message)
                    Measure.verifyCoordinate(message)
                    Measure.verifyDistance(message)
                }

                override fun onError(message: String) {
                    Log.d("BLUETOOTH ERROR", message)
                    activity.runOnUiThread {
                        BTDisabled(btButton)
                    }
                }

                override fun onConnectError(device: BluetoothDevice, message: String) {
                    Log.d("BLUETOOTH CONNECT ERROR", message)
                    activity.runOnUiThread {
                        BTDisabled(btButton)
                    }
                }
            })

            bluetooth.setBluetoothCallback(object : BluetoothCallback {
                override fun onBluetoothTurningOn() {}

                override fun onBluetoothOn() {}

                override fun onBluetoothTurningOff() {
                    activity.runOnUiThread {
                        BTDisabled(btButton)
                    }
                }

                override fun onBluetoothOff() {
                    activity.runOnUiThread {
                        BTDisabled(btButton)
                    }
                }

                override fun onUserDeniedActivation() {
                    // when using bluetooth.showEnableDialog()
                    // you will also have to call bluetooth.onActivityResult()
                }
            })
        }

     fun BTEnabled(button: Button){
         button.isEnabled = true
         button.text = "Disconnect Bluetooth"
         button.backgroundTintList = context.resources.getColorStateList(R.color.abortColor)
     }

    fun BTDisabled(button: Button){
        button.isEnabled = true
        button.text = "Connect via Bluetooth"
        button.backgroundTintList = context.resources.getColorStateList(R.color.bluetoothColor)
        if(::startButton.isInitialized)startButton.isEnabled = true
        if(::stopButton.isInitialized)stopButton.isEnabled = false
    }
}