package com.example.a4v4.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.databinding.RvContactLayoutBinding
import com.example.a4v4.ui.home.HomeFragment
import com.example.a4v4.database.DummyModel
import de.hdodenhof.circleimageview.CircleImageView

class MyRvAdapter(
    private val fragment    :   HomeFragment,
    private val data:   ArrayList<DummyModel>)
    : RecyclerView.Adapter<MyRvAdapter.MyViewHolder>() {

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
        val myData              =   data[position]

        holder.imgId.background =   ContextCompat.getDrawable(fragment.requireContext(), R.drawable.icon_place_holder )
        holder.name.text        =   myData.name
        holder.number.text      =   myData.number

        holder.itemView.setOnClickListener {
            fragment.homeViewModel.repo.selectedContact =   data[position]
            (fragment.requireActivity() as MainActivity).openDetailsFragment()
        }
    }

    override fun getItemCount() =   data.size

    fun updateData(updData  :   List<DummyModel>) {
        data.clear()
        if (updData.isEmpty()) {
            fragment.binding.emptyRvTxt.visibility  =   View.VISIBLE
        }
        else {
            fragment.binding.emptyRvTxt.visibility = View.GONE
            data.addAll(updData)
        }
        notifyDataSetChanged()
    }
}