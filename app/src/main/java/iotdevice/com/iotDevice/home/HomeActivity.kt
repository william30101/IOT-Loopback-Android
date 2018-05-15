package iotdevice.com.iotDevice.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import iotdevice.com.iotDevice.DeviceAction.AddDevicesActivity
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.AnkoLogger

class HomeActivity : AppCompatActivity(), AnkoLogger {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        addDeviceFab.setOnClickListener({ _ ->
            val intent = Intent(this, AddDevicesActivity::class.java)
            startActivity(intent)
        })
    }
}
