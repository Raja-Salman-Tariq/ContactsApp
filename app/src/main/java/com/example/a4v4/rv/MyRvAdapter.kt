package com.example.a4v4.rv

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.databinding.RvContactLayoutBinding
import com.example.a4v4.ui.home.HomeFragment
import com.example.a4v4.database.ContactsModel
import de.hdodenhof.circleimageview.CircleImageView

class MyRvAdapter(
    private val fragment    :   HomeFragment,
    private val data        :   ArrayList<ContactsModel>,
    private var dataFiltered:   ArrayList<ContactsModel> =   ArrayList(),
)   :   RecyclerView.Adapter<MyRvAdapter.MyViewHolder>(), Filterable
{
    class MyViewHolder(
        private val binding :   RvContactLayoutBinding,
        val imgId           :   CircleImageView     =   binding.rvContactImg,
        val name            :   TextView            =   binding.rvContactName,
        val number          :   TextView            =   binding.rvContactNumber,
    )   : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =
            RvContactLayoutBinding.inflate(
                LayoutInflater.from(fragment.context),
                parent,
                false
            )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myData              =   dataFiltered[position]

        holder.imgId.background =   ContextCompat.getDrawable(fragment.requireContext(), R.drawable.icon_place_holder )
        holder.name.text        =   myData.name
        holder.number.text      =   myData.number

        holder.itemView.setOnClickListener {
            fragment.homeViewModel.repo.selectedContact =   dataFiltered[position]
            (fragment.requireActivity() as MainActivity).openDetailsFragment()
        }
    }

    override fun getItemCount() =   dataFiltered.size

    fun updateData(updData  :   List<ContactsModel>) {
        data.clear()
        dataFiltered.clear()
        if (updData.isEmpty()) {
            fragment.binding.emptyRvTxt.visibility  =   View.VISIBLE
        }
        else {
            fragment.binding.emptyRvTxt.visibility = View.GONE
            data.addAll(updData)
            dataFiltered.addAll(updData)
        }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                dataFiltered = if (constraint.isNullOrEmpty()) data else {
                    val filteredList = ArrayList<ContactsModel>()
                    data
                        .filter {
                            (it.name.lowercase().contains(constraint?.toString()?.lowercase())) or
                                    (it.number.contains(constraint))
                        }
                        .forEach { filteredList.add(it) }
                    filteredList
                }
                return FilterResults().apply { values = dataFiltered }
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<ContactsModel>
                notifyDataSetChanged()
            }
        }
    }
}