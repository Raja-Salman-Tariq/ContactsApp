package com.example.a4v4.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.databinding.FragmentAuthBinding
import com.example.a4v4.databinding.FragmentHomeBinding


class AuthFragment : Fragment(R.layout.fragment_home) {

    private var _binding                :   FragmentAuthBinding?    =   null
    val binding get()                   =   _binding!!

    /*----------------------  L I F E C Y C L E   C A L L B A C K S ------------------------------*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
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
}