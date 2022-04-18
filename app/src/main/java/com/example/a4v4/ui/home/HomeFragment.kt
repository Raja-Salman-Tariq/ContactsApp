package com.example.a4v4.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.FragmentHomeBinding
import com.example.a4v4.rv.MyRvAdapter


class HomeFragment : Fragment(R.layout.fragment_home) {
    val homeViewModel                   :   HomeViewModel           by  viewModels{
    MyViewModelFactory((this.requireActivity().application as MyApp).repository)
    }

    var _binding                :   FragmentHomeBinding?    =   null
    val binding get()                   =   _binding!!

    lateinit var recyclerView   :   RecyclerView
    lateinit var myRvAdapter            :   MyRvAdapter /*ListAdapter*/

    /*----------------------  L I F E C Y C L E   C A L L B A C K S ------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.repo.allContactsLoaded.observe(viewLifecycleOwner){
            requireActivity().invalidateOptionsMenu()
            Log.d("invalidatingopsmen", "onCreateView: $it")
        }

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

    // set up toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.findItem(R.id.menu_search)?.isVisible =   homeViewModel.repo.allContactsLoaded.value!!

        super.onCreateOptionsMenu(menu, inflater)
    }

    /*--------------------------------------------------------------------------------------------*/

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

                    myRvAdapter.getPage()
                    (requireActivity() as MainActivity).handleLoading(false)
                }
            }
        })

        homeViewModel.getContacts(0).observe(viewLifecycleOwner){
                myRvAdapter.updateData(it)
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