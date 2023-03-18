package iotdevice.com.iotDevice.home

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

import iotdevice.com.iotDevice.common.BaseActivity
import iotdevice.com.iotDevice.more.MoreFragment
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.ActivityHomeBinding


class HomeActivity : BaseActivity() {

    val manager = supportFragmentManager
    lateinit var binding: ActivityHomeBinding

    enum class FragmentType {
        Home, More
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragmentTo(FragmentType.Home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                changeFragmentTo(FragmentType.More)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun changeFragmentTo(type: FragmentType) {
        val transaction = manager.beginTransaction()
        when(type) {
            FragmentType.Home -> {
                if (manager.fragments.isEmpty() || manager.findFragmentByTag("Home")?.isVisible != true) {
                    title = getString(R.string.home_title)

                    // Remove entire back stack when user pressed home button.
                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    val homeFragment = HomeFragment()
                    transaction.replace(R.id.baseFragment, homeFragment, "Home")
                    commitFragment(transaction)
                }
            }
            FragmentType.More -> {
                if (manager.fragments.isEmpty() || manager.findFragmentByTag("More")?.isVisible != true) {
                    title = getString(R.string.more_title)

                    // Remove entire back stack when user pressed home button.
                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    val moreFragment = MoreFragment()
                    transaction.replace(R.id.baseFragment, moreFragment, "More")
                    commitFragment(transaction)
                }
            }
        }
    }

    fun commitFragment(transaction: FragmentTransaction) {
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

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
