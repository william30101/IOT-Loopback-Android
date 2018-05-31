package iotdevice.com.iotDevice.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iotdevice.com.iotDevice.model.relateview.ImageModel
import iotdevice.com.iot_device.R
import kotlinx.android.synthetic.main.layout_image_list_item.view.*





class ImageListAdapter(val feedModelItems: List<ImageModel>) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    lateinit var clickListener: ClickListener


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
        fun onItemClick(position: Int, v: View)
        fun onItemLongClick(position: Int, v: View)
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