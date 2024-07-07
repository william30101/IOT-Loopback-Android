package iotdevice.com.iotDevice.more


import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import iotdevice.com.iotDevice.chart.GridLayoutDivider
import iotdevice.com.iotDevice.common.IOTPreference
import iotdevice.com.iotDevice.common.RecycleViewListener
import iotdevice.com.iotDevice.login.LoginActivity
import iotdevice.com.iotDevice.member.info.ChangePasswordActivity
import iotdevice.com.iotDevice.BuildConfig
import iotdevice.com.iotDevice.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity


class MoreFragment: Fragment(), RecycleViewListener, AnkoLogger {

    private val moreItemList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val version = getString(R.string.version_name)
        view.findViewById<TextView>(R.id.version_number).text = String.format(version, BuildConfig.VERSION_NAME)

        moreItemList.add(resources.getString(R.string.change_password))
        moreItemList.add(resources.getString(R.string.log_out))

        val moreAdapter = MoreAdapter(activity!!, IOTPreference.getUserName())
        moreAdapter.itemList.addAll(moreItemList)
        moreAdapter.listener = this

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycle_dimen)
        view.findViewById<RecyclerView>(R.id.more_recyclew_view).addItemDecoration(GridLayoutDivider(1, spacingInPixels, true, 1))

        val manager = GridLayoutManager(context, 1)

        view.findViewById<RecyclerView>(R.id.more_recyclew_view).layoutManager = manager
        view.findViewById<RecyclerView>(R.id.more_recyclew_view).adapter = moreAdapter
    }

    override fun onClick(bundle: Bundle) {
        val clickItem = bundle.getString("clickItemString")

        when(clickItem) {

            resources.getString(R.string.change_password) -> activity?.startActivity<ChangePasswordActivity>()
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
        }
    }
}