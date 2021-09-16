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
//    private var dataFiltered:   ArrayList<ContactsModel> =   ArrayList(),
)   :   RecyclerView.Adapter<MyAppsRvAdapter.MyViewHolder>()//, Filterable
{
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
//        val myData              =   dataFiltered[position]

        var appIcon             :   Drawable?   =   null

//        fragment.requireContext().packageManager.apply{
//            holder.name.text        =   getApplicationLabel(myData)
//            appIcon                 =   getApplicationIcon(myData)
//        }

        holder.name.text        =   myData.applicationInfo.loadLabel(fragment.requireContext().packageManager)
        holder.img.setImageDrawable(myData.applicationInfo.loadIcon(fragment.requireContext().packageManager))
    }

    override fun getItemCount() =   data.size//dataFiltered.size

    fun updateData(updData  :   List<PackageInfo>) {
        data.clear()
//        dataFiltered.clear()
        if (updData.isEmpty()) {
            fragment.binding.emptyRvAppsTxt.visibility  =   View.VISIBLE
        }
        else {
            fragment.binding.emptyRvAppsTxt.visibility = View.GONE
            data.addAll(updData)
//            dataFiltered.addAll(updData)
        }
        notifyDataSetChanged()
    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                dataFiltered = if (constraint.isNullOrEmpty()) data else {
//                    val filteredList = ArrayList<ContactsModel>()
//                    data
//                        .filter {
//                            (it.name.lowercase().contains(constraint?.toString()?.lowercase())) or
//                                    (it.number.contains(constraint))
//                        }
//                        .forEach { filteredList.add(it) }
//                    filteredList
//                }
//                return FilterResults().apply { values = dataFiltered }
//            }
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                dataFiltered = if (results?.values == null) {
//                    ArrayList()
//                }
//                else {
//                    results.values as ArrayList<ContactsModel>
//                }
//                notifyDataSetChanged()
//                if (dataFiltered.isEmpty())
//                    fragment.binding.emptyRvTxt.visibility=View.VISIBLE
//                else
//                    fragment.binding.emptyRvTxt.visibility=View.INVISIBLE
//            }
//        }
//    }
}