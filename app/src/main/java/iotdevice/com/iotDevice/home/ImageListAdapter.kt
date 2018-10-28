package iotdevice.com.iotDevice.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import iotdevice.com.iotDevice.model.relateview.ImageModel
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.layout_image_list_item.view.*


class ImageListAdapter(val context: Context) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    lateinit var clickListener: ClickListener
    private var feedModelItems: ArrayList<ImageModel> = arrayListOf()
    private var firstItems: ArrayList<ImageModel> = arrayListOf()
    private var isFirstTime = true

    val iconList = arrayListOf(R.mipmap.list_item_0, R.mipmap.list_item_1,
            R.mipmap.list_item_2, R.mipmap.list_item_3)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.layout_image_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feedModelItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindImageModel( feedModelItems[position], position, context)
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View)
        fun onItemLongClick(position: Int, v: View)
    }

    fun setItems(items: ArrayList<ImageModel>) {
        if (isFirstTime) {
            firstItems = items
            isFirstTime = false
        }

        feedModelItems.clear()
        feedModelItems.addAll(items)
        notifyDataSetChanged()
    }

    fun filterItems(keyword: String) {
        val listItems = feedModelItems.filter { it.displayName.contains(keyword)}
        setItems(ArrayList(listItems))
    }

    fun filterFCItems() {
        val listItems = feedModelItems.filter { it.factoryCode.toUpperCase().contains("FC")}
        setItems(ArrayList(listItems))
    }

    fun filterFAItems() {
        val listItems = feedModelItems.filter { it.displayName.toUpperCase().contains("FA")}
        setItems(ArrayList(listItems))
    }

    fun restoreItems() {
        if (firstItems.size > 0) {
            setItems(firstItems)
        }
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        var viewBackground: RelativeLayout? = null
        var viewForeground: RelativeLayout? = null

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        fun bindImageModel(imageModel: ImageModel, position: Int, context: Context){
            // set displayName
            itemView.descriptionTextView.text = imageModel.displayName

            viewBackground = itemView.view_background
            viewForeground = itemView.view_foreground

//            itemView.editDevice.setOnClickListener({
//                clickListener.onEditClick( adapterPosition, itemView)
//            })

//            itemView.delDevice.setOnClickListener({
//                clickListener.onDelClick ( adapterPosition, itemView)
//            })

            itemView.imageView.setImageDrawable(context.getDrawable(iconList[position % 4]))
        }

        override fun onClick(v: View?) {
            clickListener.onItemClick( adapterPosition, v!!)
        }

        override fun onLongClick(v: View?): Boolean {
            clickListener.onItemLongClick(adapterPosition, v!!)
            return true
        }
    }

}