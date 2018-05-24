package iotdevice.com.iotDevice.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.strongloop.android.loopback.callbacks.ListCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.DeviceAction.AddDevicesActivity
import iotdevice.com.iotDevice.model.CustomerDeviceModel
import iotdevice.com.iotDevice.model.relateview.ImageModel
import iotdevice.com.iotDevice.repository.CustomerDeviceRepository
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iotDevice.repository.DeviceRepository
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HomeFragment : Fragment() , AnkoLogger {

    private var imageList : MutableList<ImageModel> = mutableListOf()

    private lateinit  var imageAdapter: ImageListAdapter

//    private val imageList by lazy {
//        listOf(
//                ImageModel("img_1","This is a photo of 1"),
//                ImageModel("img_2","This is a photo of 2"),
//                ImageModel("img_3","This is a photo of 3"),
//                ImageModel("img_4","This is a photo of 4"),
//                ImageModel("img_5","This is a photo of 5"),
//                ImageModel("img_6","This is a photo of 6"),
//                ImageModel("img_7","This is a photo of 7"),
//                ImageModel("img_8","This is a photo of 8"),
//                ImageModel("img_9","This is a photo of 9"),
//                ImageModel("img_10","This is a photo of 10"),
//                ImageModel("img_11","This is a photo of 11"),
//                ImageModel("img_12","This is a photo of 12"),
//                ImageModel("img_13","This is a photo of 13"),
//                ImageModel("img_14","This is a photo of 14"),
//                ImageModel("img_15","This is a photo of 15")
//        )
//    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }


    override fun onStart() {
        super.onStart()
        imageList.clear()
        addDeviceFab.setOnClickListener({ _ ->
            val intent = Intent(activity, AddDevicesActivity::class.java)
            startActivity(intent)
        })

        imageListRecyclerView.layoutManager = LinearLayoutManager(activity)

        imageAdapter = ImageListAdapter(imageList)
        imageListRecyclerView.adapter = imageAdapter

        getDevice()
    }

    fun getDevice() {
        val adapter = App.sInstance.loopBackAdapter

        val customerDeviceRepository = adapter.createRepository(CustomerDeviceRepository::class.java)
        val customerRepository = adapter.createRepository(CustomerRepository::class.java)
        val deviceRepository = adapter.createRepository(DeviceRepository::class.java)

        customerDeviceRepository.findDevice(customerRepository.currentUserId as Int, object: ListCallback<CustomerDeviceModel> {
            override fun onSuccess(objects: MutableList<CustomerDeviceModel>?) {
                objects!!.forEach {  imageList.add(ImageModel("img_1", it.displayName)) }
                if (imageList.isNotEmpty()) {


                    imageAdapter.notifyDataSetChanged()
//                    myList.forEach {
//                        deviceRepository.findById(it, object: ObjectCallback<DeviceModel> {
//                            override fun onSuccess(device: DeviceModel?) {
//                                device.
//                            }
//
//                            override fun onError(t: Throwable?) {
//                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                            }
//
//                        } )
//                    }

                }
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
            }

        })




    }
}