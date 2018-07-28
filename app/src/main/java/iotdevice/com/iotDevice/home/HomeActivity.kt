package iotdevice.com.iotDevice.home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.AnkoLogger

class HomeActivity : BaseActivity(), AnkoLogger {

    val manager = supportFragmentManager

    enum class FragmentType {
        Home, Settings
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragmentTo(FragmentType.Home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun changeFragmentTo(type: FragmentType) {
        val transaction = manager.beginTransaction()
        when(type) {
            FragmentType.Home -> {
                if (manager.fragments.isEmpty() || !manager.findFragmentByTag("Home").isVisible) {
                    title = "Home"

                    // Remove entire back stack when user pressed home button.
                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    val homeFragment = HomeFragment()
                    transaction.replace(R.id.baseFragment, homeFragment, "Home")
                    commitFragment(transaction)
                }
            }

            // TODO: should add other view

//            FragmentType.Notification -> {
//                val notificationFragment = NotificationDashboard()
//                transaction.replace(R.id.baseFragment, notificationFragment)
//            }

            else -> {

            }
        }

    }

    fun commitFragment(transaction: FragmentTransaction) {
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // Open Home fragment
        changeFragmentTo(FragmentType.Home)
    }

    override fun onBackPressed() {
        if (manager.backStackEntryCount ==1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
