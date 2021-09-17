package com.example.a4v4.rv

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.databinding.RvAppsLayoutBinding
import com.example.a4v4.ui.apps.AppsFragment

class MyAppsRvAdapter(
    private val fragment    :   AppsFragment,
    private val data        :   ArrayList<PackageInfo>,
)   :   RecyclerView.Adapter<MyAppsRvAdapter.MyViewHolder>()//, Filterable
{
    /*-------------------------------  V I E W   H O L D E R  ------------------------------------*/

    class MyViewHolder(
        private val binding : RvAppsLayoutBinding,
        val name            :   TextView            =   binding.rvAppName,
        val img             :   ImageView           =   binding.rvAppImg,
    )   : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =
            RvAppsLayoutBinding.inflate(
                LayoutInflater.from(fragment.context),
                parent,
                false
            )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myData              =   data[position]
        holder.name.text        =   myData.applicationInfo.loadLabel(fragment.requireContext().packageManager)
        holder.img.setImageDrawable(myData.applicationInfo.loadIcon(fragment.requireContext().packageManager))
    }

    /*--------------------------------------------------------------------------------------------*/

    override fun getItemCount() =   data.size//dataFiltered.size

    /*-------------------------  C O N V E N I E N C E   M E T H O D  ----------------------------*/

    // handles live data updation and empty text visibility
    fun updateData(updData  :   List<PackageInfo>) {
        data.clear()
        if (updData.isEmpty()) {
            fragment.binding.emptyRvAppsTxt.visibility  =   View.VISIBLE
        }
        else {
            fragment.binding.emptyRvAppsTxt.visibility = View.GONE
            data.addAll(updData)
        }
        notifyDataSetChanged()
    }
}