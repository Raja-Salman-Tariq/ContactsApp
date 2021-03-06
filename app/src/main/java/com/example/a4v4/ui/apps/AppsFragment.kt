package com.example.a4v4.ui.apps

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.FragmentAppsBinding
import com.example.a4v4.rv.MyAppsRvAdapter


class AppsFragment : Fragment(R.layout.fragment_home) {

    private val appsViewModel           :       AppsViewModel           by  viewModels{
    MyViewModelFactory((this.requireActivity().application as MyApp).repository)
    }
    private var _binding                :       FragmentAppsBinding?    =   null
    val binding get()                   =       _binding!!

    private lateinit var recyclerView   :       RecyclerView
    private lateinit var myRvAdapter    :       MyAppsRvAdapter

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

        _binding = FragmentAppsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpRv()

        return root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).let{
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)   //show back button
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*--------------------------------------------------------------------------------------------*/
    // set up toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_search)?.isVisible =   false
        menu.findItem(R.id.export)?.isVisible      =   false
        menu.findItem(R.id.history)?.isVisible      =   false

        super.onCreateOptionsMenu(menu, inflater)
    }

    /*--------------------------------------------------------------------------------------------*/

    // set up recycler view with live data and observer
    private fun setUpRv() {
        appsViewModel.loading.observe(viewLifecycleOwner){
            (requireActivity() as MainActivity).handleLoading(it)
        }

        recyclerView                =   binding.fragmentAppsRv
        myRvAdapter                 =   MyAppsRvAdapter(this, arrayListOf())
        recyclerView.layoutManager  =   LinearLayoutManager(requireContext())
        recyclerView.adapter        =   myRvAdapter

        appsViewModel.getApps().observe(viewLifecycleOwner){
                data    ->  myRvAdapter.updateData(data)
        }
    }
}