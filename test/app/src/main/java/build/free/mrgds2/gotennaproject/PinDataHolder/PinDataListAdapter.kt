package build.free.mrgds2.gotennaproject.PinDataHolder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import build.free.mrgds2.gotennaproject.R
import kotlinx.android.synthetic.main.pin_data_item.view.*
import java.text.FieldPosition

import build.free.mrgds2.gotennaproject.api.PinDataModel





class PinDataListAdapter : RecyclerView.Adapter<PinDataListAdapter.PinDataViewholder>() {
    //==============================================================================================
    // Class Properties
    //==============================================================================================


    private var pdList: MutableList<PinDataModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinDataViewholder {
       //set the view for the holder
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellforRows = layoutInflater.inflate(R.layout.pin_data_item,parent,false) //on create view

        return PinDataViewholder(cellforRows)
    }

    //==============================================================================================
    // Constructor
    //==============================================================================================
  init {
       pdList  = ArrayList()
    }


    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    fun setPinDataModelList(PinDataList: MutableList<PinDataModel>) {
        this.pdList = PinDataList
        notifyDataSetChanged()
    }

    //==============================================================================================
    // RecyclerView.Adapter Methods
    //==============================================================================================


    override fun onBindViewHolder(holder: PinDataViewholder, position: Int) {

       val pinDatamodel : PinDataModel= pdList.get(position)

         holder.itemView.pin_name.setText(pinDatamodel.name)

    }
    override fun getItemCount(): Int {
        return pdList.size
    }

    //==============================================================================================
    // ChatViewHolder Class
    //==============================================================================================
    class PinDataViewholder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var tvName : TextView

        var tvDescription : TextView


        init {
            tvName= itemView.findViewById<View>(R.id.pin_name) as TextView
            tvDescription= itemView.findViewById<View>(R.id.pin_des) as TextView
        }

    }
}