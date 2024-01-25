package com.example.cosigo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosigo.R
import com.example.cosigo.adapters.LinkAdapter
import com.example.cosigo.databinding.FragmentHomeBinding
import com.example.cosigo.utils.NetworkResult
import com.example.cosigo.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { LinkAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    private fun requestApiData() {
        mainViewModel.getProfileData()
        mainViewModel.profileResponse.observe(viewLifecycleOwner,{ response ->
            when(response){
                is NetworkResult.Success -> {
                    response.data?.let {
                        //val productList = ArrayList(it) // Convert to ArrayList
                        //mAdapter.products = productList
                        setupRecyclerView()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        //binding.recyclerview.adapter = mAdapter
        //binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

    }


}