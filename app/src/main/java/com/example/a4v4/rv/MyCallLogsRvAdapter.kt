package com.example.a4v4.rv

import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.R
import com.example.a4v4.database.CallLogEntry
import com.example.a4v4.databinding.RvCallLogsLayoutBinding
import com.example.a4v4.ui.calllogs.CallLogsFragment
import com.squareup.picasso.Picasso

class MyCallLogsRvAdapter(
    private val fragment    :   CallLogsFragment,
    private val data        :   ArrayList<CallLogEntry>,
)   :   RecyclerView.Adapter<MyCallLogsRvAdapter.MyViewHolder>()//, Filterable
{
    /*-------------------------------  V I E W   H O L D E R  ------------------------------------*/

    class MyViewHolder(
        private val binding :   RvCallLogsLayoutBinding,
        val date            :   TextView            =   binding.rvCallLogsDate,
        val img             :   ImageView           =   binding.rvCallLogsImg,
        val dur             :   TextView            =   binding.rvCallLogsDuration
    )   : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =
            RvCallLogsLayoutBinding.inflate(
                LayoutInflater.from(fragment.context),
                parent,
                false
            )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myData              =   data[position]
        holder.date.text        =   myData.date
        holder.dur.text        =   myData.dur+" sec"

        val myImg   =  when (myData.type.toInt()) {
            CallLog.Calls.MISSED_TYPE -> {
                R.drawable.missed_call_ic
            }
            else -> {
                R.drawable.ok_call_ic
            }
        }
        holder.img.setImageDrawable(fragment.requireContext().getDrawable(myImg))
        holder.img.scaleY       =   0.75f
        holder.img.scaleX       =   0.75f

    }

    /*--------------------------------------------------------------------------------------------*/

    override fun getItemCount() =   data.size//dataFiltered.size

    /*-------------------------  C O N V E N I E N C E   M E T H O D  ----------------------------*/

    // handles live data updation and empty text visibility
    fun updateData(updData  :   List<CallLogEntry>) {
        data.clear()
        if (updData.isEmpty()) {
            fragment.binding.emptyRvCallLogsTxt.visibility  =   View.VISIBLE
        }
        else {
            fragment.binding.emptyRvCallLogsTxt.visibility = View.GONE
            data.addAll(updData)
        }
        notifyDataSetChanged()
    }
}