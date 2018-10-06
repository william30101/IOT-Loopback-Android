package iotdevice.com.iotDevice.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.model.relateview.ImageModel
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.layout_image_list_item.view.*


class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    lateinit var clickListener: ClickListener
    private var feedModelItems: ArrayList<ImageModel> = arrayListOf()
    private var firstItems: ArrayList<ImageModel> = arrayListOf()
    private var isFirstTime = true

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.layout_image_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feedModelItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindImageModel( feedModelItems[position] )
    }

    interface ClickListener {
        fun onDelClick(position: Int, v: View)
        fun onEditClick(position: Int, v: View)
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
        val listItems = firstItems.filter { it.displayName.contains(keyword)}
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        fun bindImageModel(imageModel: ImageModel){
            // set displayName
            itemView.descriptionTextView.text = imageModel.displayName

            itemView.editDevice.setOnClickListener({
                clickListener.onEditClick( adapterPosition, itemView)
            })

            itemView.delDevice.setOnClickListener({
                clickListener.onDelClick ( adapterPosition, itemView)
            })

            // set image
            when(imageModel.imageName){
                "img_1" -> itemView.imageView.setImageResource(R.drawable.ic_baseline_computer_24px)
                else -> itemView.imageView.setImageResource(R.drawable.ic_baseline_computer_24px)
            }

        }

        override fun onClick(v: View?) {
            clickListener.onItemClick( adapterPosition, v!!)
        }




    }

}