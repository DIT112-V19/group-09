package com.group9.dora

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*
import me.aflak.bluetooth.Bluetooth
import android.bluetooth.BluetoothDevice
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import me.aflak.bluetooth.DeviceCallback
import java.lang.Compiler.enable




class MainActivity : AppCompatActivity() {

    var fragments: Array<Fragment> = arrayOf(fragment_mapping(), fragment_manualcontrols())

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->


        when (item.itemId) {
            R.id.navigation_mapping -> {
                supportFragmentManager
                    .beginTransaction()
                    .hide(fragments[1])
                    .show(fragments[0])
                    .commit()
            }
            R.id.navigation_manual -> {
                supportFragmentManager
                    .beginTransaction()
                    .hide(fragments[0])
                    .show(fragments[1])
                    .commit()
            }
            else -> {
                supportFragmentManager
                    .beginTransaction()
                    .hide(fragments[1])
                    .show(fragments[0])
                    .commit()
            }
        }







        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.subtitle = "Group 09 (Team \uD83D\uDC3F)"

        initBluetooth()

        navigation.itemIconTintList = resources.getColorStateList(R.color.colorWhite)
        navigation.itemTextColor = resources.getColorStateList(R.color.colorWhite)
        navigation.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        initFragments()
    }

    fun initFragments(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, fragments[0])
            .add(R.id.fragment_container, fragments[1])
            .hide(fragments[1])
            .commit()
    }

    fun initBluetooth(){
        BT.init(this,this)
    }

    override fun onStop() {
        super.onStop()
        BT.bluetooth.unRegister()
    }
}
