package com.example.a4v4.ui.calllogs

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.application.MyApp
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.databinding.FragmentAppsBinding
import com.example.a4v4.rv.MyCallLogsRvAdapter


class CallLogsFragment(val contact  : LiveData<ContactsModel?>) : Fragment(R.layout.fragment_home) {

    private val appsViewModel           :       CallLogsViewModel           by  viewModels{
    MyViewModelFactory((this.requireActivity().application as MyApp).repository)
    }

    private var _binding                :       FragmentAppsBinding?    =   null
    val binding get()                   =       _binding!!

    private lateinit var recyclerView   :       RecyclerView
    private lateinit var myRvAdapter    :       MyCallLogsRvAdapter

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
        recyclerView                =   binding.fragmentAppsRv
        myRvAdapter                 =   MyCallLogsRvAdapter(this, arrayListOf())
        recyclerView.layoutManager  =   LinearLayoutManager(requireContext())
        recyclerView.adapter        =   myRvAdapter

//        myRvAdapter.updateData(contact.fetchCallLog(requireContext()))

        contact.observe(viewLifecycleOwner){
                contact    ->  myRvAdapter.updateData(contact?.fetchCallLog(requireContext())!!)
        }
    }
}