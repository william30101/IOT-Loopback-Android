package iotdevice.com.iotDevice.home

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import iotdevice.com.iotDevice.repository.DeviceRepository
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.FragmentHomeBinding
import org.apache.http.client.HttpResponseException
import java.net.HttpURLConnection

class HomeFragment : Fragment(), TokenManager.LoginListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    var imageList : ArrayList<ImageModel> = arrayListOf()

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    private var searchView: SearchView? = null

    private lateinit  var imageAdapter: ImageListAdapter
    private lateinit var customerDeviceRepository: CustomerDeviceRepository
    private lateinit var customerRepository: CustomerRepository
    private lateinit var deviceRepository: DeviceRepository
    val adapter = App.sInstance.loopBackAdapter.apply {
        customerDeviceRepository = this.createRepository(CustomerDeviceRepository::class.java)
        customerRepository = this.createRepository(CustomerRepository::class.java)
        deviceRepository = this.createRepository(DeviceRepository::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.home_screen_title)

        imageList.clear()
        binding.addDeviceFab.setOnClickListener({ _ ->
            val intent = Intent(activity, AddDevicesActivity::class.java)

            intent.putParcelableArrayListExtra("itemList", imageList)
            startActivity(intent)
        })

        binding.imageListRecyclerView.layoutManager = LinearLayoutManager(activity)



        imageAdapter = ImageListAdapter(requireContext())
        binding.imageListRecyclerView.adapter = imageAdapter

        imageAdapter.setOnItemClickListener(object : ImageListAdapter.ClickListener {
            override fun onItemLongClick(position: Int, v: View) {
                val intent = Intent(requireContext(), EditDeviceActivity::class.java).apply {
                    putExtra("deviceId", imageList[position].deviceId)
                    putExtra("deviceName", imageList[position].displayName)
                }

                startActivity(intent)
            }

            override fun onItemClick(position: Int, v: View) {
                Log.i(tag, "onItemClick position: $position , deviceId : ${imageList[position].deviceId}")
                val chartFragment = ChartFragment()
                val bundle = Bundle()
                bundle.putParcelable("device", imageList[position])
                transmitFragment(fragmentManager!!, chartFragment, bundle)
            }
        })

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.imageListRecyclerView)

        getDevice()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        binding.homeSwipeRefreshLayout.setOnRefreshListener({
            getDevice()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val showAll = menu.findItem(R.id.show_all)
        showAll?.setOnMenuItemClickListener {
            imageAdapter.restoreItems()
            true
        }

        val showFC = menu.findItem(R.id.filter_fc)
        showFC?.setOnMenuItemClickListener {
            imageAdapter.restoreItems()
            imageAdapter.filterFCItems()
             true
        }

        val showFA = menu.findItem(R.id.filter_fa)
        showFA?.setOnMenuItemClickListener {
            imageAdapter.restoreItems()
            imageAdapter.filterFAItems()
            true
        }

        val searchMenuItem = menu.findItem(R.id.my_search)
        searchView = searchMenuItem?.actionView as SearchView

        searchView?.apply {
            // Get the SearchView and set the searchable configuration
            val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

            setIconifiedByDefault(true)
            queryHint = getString(R.string.search_hint)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    Log.i(tag.toString(), "string $query")

                    if (newText.isEmpty()) {
                        imageAdapter.restoreItems()
                        return false
                    }

                    imageAdapter.filterItems(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.i(tag.toString(), "submit $query")
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

                objects?.forEach {
                    imageList.add(ImageModel(it.id, it.deviceId, "img_1", it.displayName, it.factoryCode))
                }

                imageAdapter.setItems(imageList)
                binding.homeSwipeRefreshLayout.isRefreshing = false
            }

            override fun onError(t: Throwable?) {
                Log.i(tag, "error : $t")
                binding.homeSwipeRefreshLayout.isRefreshing = false
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
                    DialogUtils.createAlertDialog(requireActivity(), getString(R.string.home_title))

                }
//                HttpURLConnection.HTTP_UNSUPPORTED_TYPE
//                t as HttpResponseException .statusCode
            }

        })
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {

            // backup of removed item for undo purpose
            val deletedItem = imageList.get(viewHolder.adapterPosition)

            // remove the item from recycler view
            DialogUtils.createConfirmDialog(requireActivity(), getString(R.string.del_device_title), getString(R.string.del_device_desc), {

                customerDeviceRepository.delDevice(deletedItem.id, object : ObjectCallback<CustomerDeviceModel> {
                    override fun onSuccess(myObj: CustomerDeviceModel?) {
                        getDevice()
                    }

                    override fun onError(t: Throwable?) {
                        DialogUtils.createAlertDialog(requireActivity(), getString(R.string.del_device_title),
                                getString(R.string.del_device_fail))
                    }
                })}, {
                imageAdapter.restoreItems()
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
        Log.i(tag, "completed $result")
    }

    override fun onLoginError(err: Throwable) {
        Log.i(tag, "login fail $err")
    }

    companion object {
        const val tag = "HomeFragment"
    }
}