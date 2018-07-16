package iotdevice.com.iotDevice.chart

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import iotdevice.com.iot_device.R
import org.jetbrains.anko.AnkoLogger

class ChartActivity: AppCompatActivity(), AnkoLogger {

    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        changeFragmentTo()
    }

    override fun onBackPressed() {
        if (manager.backStackEntryCount ==1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }


    private fun changeFragmentTo() {
        val transaction = manager.beginTransaction()
        val chartFragment = ChartFragment()
        transaction.replace(R.id.container, chartFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}