package com.example.a4v4.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.databinding.FragmentHomeBinding
import com.example.a4v4.rv.MyRvAdapter
import com.example.a4v4.application.MyApp

class HomeFragment : Fragment(R.layout.fragment_home) {
    val homeViewModel           :   HomeViewModel           by  viewModels{
    MyViewModelFactory((this.requireActivity().application as MyApp).repository)
    }
    private var _binding                :   FragmentHomeBinding?    =   null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get()           =   _binding!!

    private lateinit var recyclerView   : RecyclerView
    lateinit var myRvAdapter    : MyRvAdapter


    /*--------------------------------------------------------------------------------------------*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpRv()

        return root
    }

    /*--------------------------------------------------------------------------------------------*/


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).let{
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.hamburger_icon)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)   //show back button
        }
    }

    private fun setUpRv() {
        recyclerView                =   binding.fragmentHomeRv
        myRvAdapter                 =   MyRvAdapter(this, arrayListOf())
        recyclerView.layoutManager  =   LinearLayoutManager(requireContext())
        recyclerView.adapter        =   myRvAdapter

        homeViewModel.data.observe(viewLifecycleOwner){
                data    ->  run{
            myRvAdapter.updateData(data)
            homeViewModel.repo.run {
                Log.d(
                    "cachesizes",
                    "all: :null= ${allContacts == null}, valNull= ${allContacts.value == null}, ${allContacts.value?.size} \n" +
                            "u::null= ${ufoneContacts == null}, valNull= ${ufoneContacts.value == null}, ${ufoneContacts.value?.size}\n " +
                            "jazz::null= ${jazzContacts == null}, valNull= ${jazzContacts.value == null}, ${jazzContacts.value?.size}\n"
                )
            }
                }
        }


    }

    /*--------------------------------------------------------------------------------------------*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*--------------------------------------------------------------------------------------------*/

    fun getContacts(type:Short){
        homeViewModel.getContacts(type).observe(viewLifecycleOwner){
                data    ->  run{
            myRvAdapter.updateData(data)
            homeViewModel.repo.run {
                Log.d(
                    "cachesizes",
                    "all: :null= ${allContacts == null}, valNull= ${allContacts.value == null}, ${allContacts.value?.size} \n" +
                            "u::null= ${ufoneContacts == null}, valNull= ${ufoneContacts.value == null}, ${ufoneContacts.value?.size}\n " +
                            "jazz::null= ${jazzContacts == null}, valNull= ${jazzContacts.value == null}, ${jazzContacts.value?.size}\n"
                )
            }
        }
        }
    }
}