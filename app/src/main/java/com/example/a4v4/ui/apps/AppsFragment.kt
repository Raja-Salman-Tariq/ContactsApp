package com.example.a4v4.ui.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.FragmentAppsBinding
import com.example.a4v4.databinding.FragmentHomeBinding
import com.example.a4v4.rv.MyAppsRvAdapter
import com.example.a4v4.rv.MyRvAdapter


class AppsFragment(val mainActivity: MainActivity) : Fragment(R.layout.fragment_home) {
    val appsViewModel           :   AppsViewModel           by  viewModels{
    MyViewModelFactory((this.requireActivity().application as MyApp).repository!!)
    }
    private var _binding                :   FragmentAppsBinding?    =   null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get()           =   _binding!!

    private lateinit var recyclerView   : RecyclerView
    lateinit var myRvAdapter            : MyAppsRvAdapter

    /*--------------------------------------------------------------------------------------------*/

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        getPermission(mainActivity)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAppsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpRv()

        return root
    }



    /*--------------------------------------------------------------------------------------------*/

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).let{
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)   //show back button
        }
    }

    private fun setUpRv() {
        recyclerView                =   binding.fragmentAppsRv
        myRvAdapter                 =   MyAppsRvAdapter(this, arrayListOf())
        recyclerView.layoutManager  =   LinearLayoutManager(requireContext())
        recyclerView.adapter        =   myRvAdapter

//        appsViewModel.data.observe(viewLifecycleOwner){
//                data    ->  myRvAdapter.updateData(data)
//        }

        myRvAdapter.updateData(appsViewModel.repo.myApps)

    }

    /*--------------------------------------------------------------------------------------------*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*--------------------------------------------------------------------------------------------*/

    fun getApps(type:Short){
//        appsViewModel.getApps(type).observe(viewLifecycleOwner){
//                data    ->  myRvAdapter.updateData(data)
//        }
        myRvAdapter.updateData(appsViewModel.repo.myApps)
    }
}