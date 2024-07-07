package iotdevice.com.iotDevice.more


import android.accounts.AccountManager
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import iotdevice.com.iotDevice.chart.ChartAdapter
import iotdevice.com.iotDevice.chart.GridLayoutDivider
import iotdevice.com.iotDevice.common.IOTPreference
import iotdevice.com.iotDevice.common.RecycleViewListener
import iotdevice.com.iotDevice.login.LoginActivity
import iotdevice.com.iotDevice.member.info.ChangePasswordActivity
import iotdevice.com.iotDevice.register.RegisterViewModel
import iotdevice.com.iot_device.BuildConfig
import iotdevice.com.iot_device.R
import iotdevice.com.iot_device.databinding.FragmentMoreBinding


class MoreFragment: Fragment(), RecycleViewListener {

    private val moreItemList = mutableListOf<String>()

    private lateinit var moreViewModel: MoreViewModel

    private var _binding: FragmentMoreBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val version = getString(R.string.version_name)
        binding.versionNumber.text = String.format(version, BuildConfig.VERSION_NAME)

        moreViewModel = ViewModelProvider(this)[MoreViewModel::class.java]

        moreItemList.add(resources.getString(R.string.change_password))
        moreItemList.add(resources.getString(R.string.log_out))
        moreItemList.add(resources.getString(R.string.delete_account))

        val moreAdapter = MoreAdapter(requireActivity(), IOTPreference.getUserName())
        moreAdapter.itemList.addAll(moreItemList)
        moreAdapter.listener = this

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycle_dimen)
        binding.moreRecyclewView.addItemDecoration(GridLayoutDivider(1, spacingInPixels, true, 1))

        val manager = GridLayoutManager(context, 1)

        binding.moreRecyclewView.layoutManager = manager
        binding.moreRecyclewView.adapter = moreAdapter


        moreViewModel.isDeleteSuccess.observe(viewLifecycleOwner, Observer {
            val accountManager = AccountManager.get(activity)

            val accounts = accountManager.accounts
            for (index in accounts.indices) {
                accountManager.removeAccount(accounts[index], {
                    if (it.result) {
                        val intent = Intent(activity, LoginActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }, null)
            }
        })
    }

    override fun onClick(bundle: Bundle) {
        val clickItem = bundle.getString("clickItemString")

        when(clickItem) {

            resources.getString(R.string.change_password) -> {
                val intent = Intent(requireContext(),ChangePasswordActivity::class.java)
                requireActivity().startActivity(intent)
            }
            resources.getString(R.string.log_out) -> {
                val accountManager = AccountManager.get(activity)

                val accounts = accountManager.accounts
                for (index in accounts.indices) {
                    accountManager.removeAccount(accounts[index], {
                        if (it.result) {
                            val intent = Intent(activity, LoginActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                    }, null)
                }
            }
            resources.getString(R.string.delete_account) -> {
                // Delete account

                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
                alertDialog.setMessage(resources.getString(R.string.confirm_delete_account))
                alertDialog.setPositiveButton(resources.getString(R.string.new_password_confirm)
                ) { _, _ ->
                    Toast.makeText(
                        activity,
                        "Confirm",
                        Toast.LENGTH_SHORT
                    ).show()

                    moreViewModel.deleteAccount()
                }
                alertDialog.setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                    Toast.makeText(
                    activity,
                    "No",
                    Toast.LENGTH_SHORT
                ).show() }
                alertDialog.setCancelable(false)
                alertDialog.show()
                // Logout on observer function

            }
        }
    }
}