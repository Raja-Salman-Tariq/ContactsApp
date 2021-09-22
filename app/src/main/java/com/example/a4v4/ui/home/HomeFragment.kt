package com.example.a4v4.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.FragmentHomeBinding
import com.example.a4v4.rv.ListAdapter
import com.example.a4v4.rv.MyRvAdapter
import com.example.a4v4.utils.PaginationHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.widget.Toast





class HomeFragment : Fragment(R.layout.fragment_home) {
    val homeViewModel                   :   HomeViewModel           by  viewModels{
    MyViewModelFactory((this.requireActivity().application as MyApp).repository)
    }

    var _binding                :   FragmentHomeBinding?    =   null
    val binding get()                   =   _binding!!

    lateinit var recyclerView   :   RecyclerView
    lateinit var myRvAdapter            :   MyRvAdapter /*ListAdapter*/

    /*----------------------  L I F E C Y C L E   C A L L B A C K S ------------------------------*/

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

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).let{
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.hamburger_icon)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)   //show back button
            it.setActionBarTitle(it.myDrawer.selectedTitle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*--------------------------------------------------------------------------------------------*/

    private val paginationHelper    =   PaginationHelper(this)
    private fun setUpRv() {

        homeViewModel.loading.observe(viewLifecycleOwner){
            (requireActivity() as MainActivity).handleLoading(it)
        }

        recyclerView                =   binding.fragmentHomeRv
        myRvAdapter                 =   MyRvAdapter(this, arrayListOf()) /*ListAdapter(this, arrayListOf())*/
        recyclerView.layoutManager  =   LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter        =   myRvAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    (requireActivity() as MainActivity).handleLoading(true)
//                    Toast.makeText(requireContext(), "Last", Toast.LENGTH_LONG).show()
                    myRvAdapter.getPage()
                    (requireActivity() as MainActivity).handleLoading(false)
                }
            }
        })

        // paging attempt 2
//        paginationHelper.adapter    =   myRvAdapter

        //  for paging attempt 1
//        lifecycleScope.launch {
//            homeViewModel.getPagedItems().collectLatest {
//                myRvAdapter.submitData(it)
//            }
//        }

        homeViewModel.getContacts(0).observe(viewLifecycleOwner){
                myRvAdapter.updateData(it)
//            paginationHelper.pageData(it)
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    // updates with other category cached list of contacts
    fun getContacts(type:Short){
        homeViewModel.getContacts(type).observe(viewLifecycleOwner){
                data    ->  myRvAdapter.updateData(data)
        }
    }
}