package com.example.a4v4.rv

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.databinding.RvContactLayoutBinding
import com.example.a4v4.ui.home.HomeFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
//  for paging attempt 1
class ListAdapter(
    private val fragment    :   HomeFragment,
    private val data        :   ArrayList<ContactsModel>,
    private var dataFiltered:   ArrayList<ContactsModel> =   ArrayList(),
)   :   PagingDataAdapter<ContactsModel, ListAdapter.MyViewHolder>(DIFF_CALLBACK), Filterable
{

    companion object{
        private val DIFF_CALLBACK   =   object :    DiffUtil.ItemCallback<ContactsModel>(){
            override fun areItemsTheSame(oldItem: ContactsModel, newItem: ContactsModel): Boolean {
                return oldItem.number==newItem.number
            }

            override fun areContentsTheSame(
                oldItem: ContactsModel,
                newItem: ContactsModel
            ): Boolean {
                return oldItem  ==  newItem
            }
        }
    }
    /*-------------------------------  V I E W   H O L D E R  ------------------------------------*/

    class MyViewHolder(
        private val binding :   RvContactLayoutBinding,
        val img           :   CircleImageView     =   binding.rvContactImg,
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

        return MyViewHolder(binding).apply {
            itemView.setOnClickListener {
                fragment.homeViewModel.repo.selectedContact.postValue(dataFiltered[adapterPosition])
                (fragment.requireActivity() as MainActivity).run {
                    openDetailsFragment()
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                        ?.hideSoftInputFromWindow(
                            findViewById<ViewGroup>(android.R.id.content).rootView.windowToken,
                            0
                        )
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myData              =   dataFiltered[position]

        Picasso.get().load(myData.getImgUri()).placeholder(R.drawable.icon_place_holder).into(holder.img)

        holder.name.text        =   myData.name
        holder.number.text      =   myData.number
    }

    /*--------------------------------------------------------------------------------------------*/

    override fun getItemCount() =   dataFiltered.size

    /*-------------------------  C O N V E N I E N C E   M E T H O D  ----------------------------*/

    // handles live data updation and empty text visibility
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

    /*--------------------  F I L T R A T  I O N    O V E R R I D E S ----------------------------*/

    override fun getFilter(): Filter {
        // A filter object which...
        return object : Filter() {
            //...performs filtering according to given filter condition
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                dataFiltered = if (constraint.isNullOrEmpty()) data else { // if no condition, dont filter
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
                dataFiltered = if (results?.values == null) {
                    ArrayList()
                }
                else {
                    results.values as ArrayList<ContactsModel>
                }
                notifyDataSetChanged()
                if (dataFiltered.isEmpty())
                    fragment.binding.emptyRvTxt.visibility=View.VISIBLE
                else
                    if (fragment != null)
                        if (fragment._binding != null)
                            if (fragment._binding?.emptyRvTxt != null)
                                fragment._binding?.emptyRvTxt?.visibility=View.INVISIBLE
            }
        }
    }
}