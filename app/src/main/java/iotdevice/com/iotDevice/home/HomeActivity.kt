package iotdevice.com.iotDevice.home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.AnkoLogger

class HomeActivity : BaseActivity(), AnkoLogger {

    val manager = supportFragmentManager

    enum class FragmentType {
        home, dashboard, notification
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragmentTo(FragmentType.home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun changeFragmentTo(type: FragmentType) {
        val transaction = manager.beginTransaction()
        when(type) {
            FragmentType.home -> {
                title = "Home"
                val homeFragment = HomeFragment()
                transaction.replace(R.id.baseFragment, homeFragment)
            }

//            FragmentType.dashboard -> {
//                val dashboardFragment = DashboardFragment()
//                transaction.replace(R.id.baseFragment, dashboardFragment)
//            }
//
//            FragmentType.notification -> {
//                val notificationFragment = NotificationDashboard()
//                transaction.replace(R.id.baseFragment, notificationFragment)
//            }

        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // Open home fragment
        changeFragmentTo(FragmentType.home)
    }
}
