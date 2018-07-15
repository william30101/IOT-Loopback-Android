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
import iotdevice.com.iotDevice.chart.ChartActivity
import iotdevice.com.iotDevice.member.TokenManager
import iotdevice.com.iotDevice.model.CustomerDeviceModel
import iotdevice.com.iotDevice.model.CustomerModel
import iotdevice.com.iotDevice.model.relateview.ImageModel
import iotdevice.com.iotDevice.repository.CustomerDeviceRepository
import iotdevice.com.iotDevice.repository.CustomerRepository
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.fragment_home.*
import org.apache.http.client.HttpResponseException
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.net.HttpURLConnection


class HomeFragment : Fragment(), TokenManager.LoginListener , AnkoLogger {

    private var imageList : MutableList<ImageModel> = mutableListOf()

    private lateinit  var imageAdapter: ImageListAdapter

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

        imageAdapter.setOnItemClickListener(object : ImageListAdapter.ClickListener {
            override fun onItemLongClick(position: Int, v: View) {
                info("onLongItemClick position: $position")
            }

            override fun onItemClick(position: Int, v: View) {
                info("onItemClick position: $position , deviceId : ${imageList[position].deviceId}")
                val intent = Intent(activity, ChartActivity::class.java)
                val bundle = Bundle()
                bundle.putLong("deviceId", imageList[position].deviceId.toLong())
                intent.putExtra("homeBundle", bundle)
                startActivity(intent)
            }
        })

        getDevice()
    }

    fun getDevice() {
        val adapter = App.sInstance.loopBackAdapter

        val customerDeviceRepository = adapter.createRepository(CustomerDeviceRepository::class.java)
        val customerRepository = adapter.createRepository(CustomerRepository::class.java)

        customerDeviceRepository.findDevice(customerRepository.currentUserId as Int, object: ListCallback<CustomerDeviceModel> {
            override fun onSuccess(objects: MutableList<CustomerDeviceModel>?) {
                objects!!.forEach {  imageList.add(ImageModel(it.deviceId, "img_1", it.displayName)) }
                if (imageList.isNotEmpty()) {
                    imageAdapter.notifyDataSetChanged()
                }
            }

            override fun onError(t: Throwable?) {
                info("error : $t")

                // Status Code 401
                // if (t.st)
                if(t is HttpResponseException) {
                    when(t.statusCode) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            // the token expired, redirect to login page
//                            val intent = Intent(activity, LoginActivity::class.java)
//                            val bundle = Bundle()
//                            bundle.putLong("deviceId", )
//                            intent.putExtra("homeBundle", bundle)
//                            startActivity(intent)


                            TokenManager.performLoginRequest(TokenManager.mUsername,
                                    TokenManager.passwordFromManager , HomeFragment())
                        }
                    }
                }
//                HttpURLConnection.HTTP_UNSUPPORTED_TYPE
//                t as HttpResponseException .statusCode
            }

        })
    }

    override fun onLoginComplete(result: CustomerModel) {
        info("completed $result")
    }

    override fun onLoginError(err: Throwable) {
        info("login fail $err")
    }
}