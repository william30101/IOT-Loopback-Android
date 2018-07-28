package iotdevice.com.iotDevice.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import iotdevice.com.iotDevice.model.DeviceStatusModel
import iotdevice.com.iot_device.R

class Utils {
    companion object {
        fun combineProductivityToList(item: DeviceStatusModel) {
            item.productivityLit.add(0 ,item.productivity0)
            item.productivityLit.add(1 ,item.productivity1)
            item.productivityLit.add(2 ,item.productivity2)
            item.productivityLit.add(3 ,item.productivity3)
            item.productivityLit.add(4 ,item.productivity4)
            item.productivityLit.add(5 ,item.productivity5)
            item.productivityLit.add(6 ,item.productivity6)
            item.productivityLit.add(7 ,item.productivity7)
            item.productivityLit.add(8 ,item.productivity8)
            item.productivityLit.add(9 ,item.productivity9)
            item.productivityLit.add(10 ,item.productivity10)
            item.productivityLit.add(11 ,item.productivity11)
            item.productivityLit.add(12 ,item.productivity12)
            item.productivityLit.add(13 ,item.productivity13)
            item.productivityLit.add(14 ,item.productivity14)
            item.productivityLit.add(15 ,item.productivity15)
            item.productivityLit.add(16 ,item.productivity16)
            item.productivityLit.add(17 ,item.productivity17)
            item.productivityLit.add(18 ,item.productivity18)
            item.productivityLit.add(19 ,item.productivity19)
            item.productivityLit.add(20 ,item.productivity20)
            item.productivityLit.add(21 ,item.productivity21)
            item.productivityLit.add(22 ,item.productivity22)
            item.productivityLit.add(23 ,item.productivity23)
        }

        fun transmitFragment(fragmentManager: FragmentManager, fragment: Fragment, bundle: Bundle) {

            fragment.arguments = bundle

            fragmentManager.beginTransaction().apply {
                replace(R.id.baseFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}