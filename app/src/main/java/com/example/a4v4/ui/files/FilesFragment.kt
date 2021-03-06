package com.example.a4v4.ui.files

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.FragmentFilesBinding
import com.example.a4v4.rv.MyFilesRvAdapter


class FilesFragment : Fragment(R.layout.fragment_files) {
    private val filesViewModel           :      FilesViewModel           by  viewModels{
    MyViewModelFactory((this.requireActivity().application as MyApp).repository)
    }

    private var _binding                :       FragmentFilesBinding?    =   null
    val binding get()                   =       _binding!!

    private lateinit var recyclerView   :       RecyclerView
    private lateinit var myRvAdapter    :       MyFilesRvAdapter

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

        _binding = FragmentFilesBinding.inflate(inflater, container, false)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_search)?.isVisible =   false
        menu.findItem(R.id.export)?.isVisible      =   false
        menu.findItem(R.id.history)?.isVisible     =   false

        super.onCreateOptionsMenu(menu, inflater)
    }

    /*--------------------------------------------------------------------------------------------*/

    private fun setUpRv() {
        recyclerView                =   binding.fragmentFilesRv
        myRvAdapter                 =   MyFilesRvAdapter(this, arrayListOf())
        recyclerView.layoutManager  =   LinearLayoutManager(requireContext())
        recyclerView.adapter        =   myRvAdapter

        filesViewModel.getFiles().observe(viewLifecycleOwner){
                data    ->  myRvAdapter.updateData(data)
        }
    }
}