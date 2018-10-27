package iotdevice.com.iotDevice.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.strongloop.android.loopback.callbacks.ListCallback
import com.strongloop.android.loopback.callbacks.ObjectCallback
import iotdevice.com.iotDevice.App
import iotdevice.com.iotDevice.chart.ChartFragment
import iotdevice.com.iotDevice.common.ChartUtils.Companion.transmitFragment
import iotdevice.com.iotDevice.common.DialogUtils
import iotdevice.com.iotDevice.deviceAction.AddDevicesActivity
import iotdevice.com.iotDevice.deviceAction.EditDeviceActivity
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
import org.jetbrains.anko.startActivity
import java.net.HttpURLConnection

class HomeFragment : Fragment(), TokenManager.LoginListener , AnkoLogger {

    var imageList : ArrayList<ImageModel> = arrayListOf()

    private var searchView: SearchView? = null

    private lateinit  var imageAdapter: ImageListAdapter
    private lateinit var customerDeviceRepository: CustomerDeviceRepository
    private lateinit var customerRepository: CustomerRepository
    val adapter = App.sInstance.loopBackAdapter.apply {
        customerDeviceRepository = this.createRepository(CustomerDeviceRepository::class.java)
        customerRepository = this.createRepository(CustomerRepository::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        activity.title = getString(R.string.home_screen_title)

        imageList.clear()
        addDeviceFab.setOnClickListener({ _ ->
            val intent = Intent(activity, AddDevicesActivity::class.java)

            intent.putParcelableArrayListExtra("itemList", imageList)
            startActivity(intent)
        })

        imageListRecyclerView.layoutManager = LinearLayoutManager(activity)

        imageAdapter = ImageListAdapter()
        imageListRecyclerView.adapter = imageAdapter

        imageAdapter.setOnItemClickListener(object : ImageListAdapter.ClickListener {
            override fun onItemLongClick(position: Int, v: View) {
                info("onLongItemClick position: $position")
            }

            override fun onItemClick(position: Int, v: View) {
                info("onItemClick position: $position , deviceId : ${imageList[position].deviceId}")
                val chartFragment = ChartFragment()
                val bundle = Bundle()
                bundle.putParcelable("device", imageList[position])
                transmitFragment(fragmentManager, chartFragment, bundle)
            }

            override fun onEditClick(position: Int, v: View) {
                activity.startActivity<EditDeviceActivity>("deviceId" to imageList[position].deviceId, "deviceName" to imageList[position].displayName)
            }

            override fun onDelClick(position: Int, v: View) {
                DialogUtils.createConfirmDialog(activity, getString(R.string.del_device_title), getString(R.string.del_device_desc)) {

                    customerDeviceRepository.delDevice(imageList[position].id, object : ObjectCallback<CustomerDeviceModel> {
                        override fun onSuccess(myObj: CustomerDeviceModel?) {
                            getDevice()
                        }

                        override fun onError(t: Throwable?) {
                            DialogUtils.createAlertDialog(activity, getString(R.string.del_device_title),
                                    getString(R.string.del_device_fail))
                        }
                    })
                }
            }
        })

        getDevice()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        homeSwipeRefreshLayout.setOnRefreshListener({
            getDevice()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.home_menu, menu)

        val searchMenuItem = menu?.findItem(R.id.my_search)
        searchView = searchMenuItem?.actionView as SearchView

        searchView?.apply {
            // Get the SearchView and set the searchable configuration
            val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

            setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))

            setIconifiedByDefault(true)
            queryHint = getString(R.string.search_hint)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    info{ "string $query"}

                    if (newText.isEmpty()) {
                        imageAdapter.restoreItems()
                        return false
                    }

                    imageAdapter.filterItems(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    info{ "submit $query"}
                    clearFocus()
                    return false
                }

            })

            setOnCloseListener {
//                imageAdapter.restoreItems()
                false
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    fun getDevice() {

        customerDeviceRepository.findDevice(customerRepository.currentUserId as Int, object: ListCallback<CustomerDeviceModel> {
            override fun onSuccess(objects: MutableList<CustomerDeviceModel>?) {
                imageList.clear()
                objects?.forEach {  imageList.add(ImageModel(it.id, it.deviceId, "img_1", it.displayName)) }
                imageAdapter.setItems(imageList)
                homeSwipeRefreshLayout.isRefreshing = false
            }

            override fun onError(t: Throwable?) {
                info("error : $t")
                homeSwipeRefreshLayout.isRefreshing = false
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
                } else {
                    DialogUtils.createAlertDialog( activity, getString(R.string.home_title))

                }
//                HttpURLConnection.HTTP_UNSUPPORTED_TYPE
//                t as HttpResponseException .statusCode
            }

        })
    }

    fun createAlertDialog() {

        val builder = AlertDialog.Builder(context).apply {
            this.setTitle("NetworkError")
            this.setMessage("Please Check your connection status")
            this.setPositiveButton("OK"){_, _-> }
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onLoginComplete(result: CustomerModel) {
        info("completed $result")
    }

    override fun onLoginError(err: Throwable) {
        info("login fail $err")
    }
}