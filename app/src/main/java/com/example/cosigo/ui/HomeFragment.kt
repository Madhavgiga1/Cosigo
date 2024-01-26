package com.example.cosigo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosigo.R
import com.example.cosigo.adapters.RecentLinksAdapter
import com.example.cosigo.adapters.TopLinkAdapter
import com.example.cosigo.databinding.FragmentHomeBinding
import com.example.cosigo.models.Basic
import com.example.cosigo.models.OverallUrlChart
import com.example.cosigo.utils.NetworkResult
import com.example.cosigo.viewmodels.MainViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var res:Basic?=null

    private lateinit var mainViewModel: MainViewModel
    private val topAdapter by lazy { TopLinkAdapter() }
    private val recentAdapter by lazy { RecentLinksAdapter() }

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        binding.greetings.text=mainViewModel.getGreetingMessage()
        requestApiData()

        binding.topLinkButton.setOnClickListener {
            Setup_TopLink_Button()

            binding.RecyclerView.adapter = topAdapter
            setupRecyclerView()
        }

        binding.recentLinkButton.setOnClickListener {

            Setup_RecentLink_Button()
            binding.RecyclerView.adapter = recentAdapter
            setupRecyclerView()
        }


        return binding.root
    }
    private fun requestApiData() {
        mainViewModel.getProfileData()
        mainViewModel.profileResponse.observe(viewLifecycleOwner,{ response ->
            when(response){
                is NetworkResult.Success -> {
                    response.data?.let {

                        binding.result=it
                        topAdapter.links=it.data.topLinks?:emptyList()
                        recentAdapter.recentlinks=it.data.recentLinks?:emptyList()
                        res=it
                        setupChart(it.data.overallUrlChart)
                        Setup_TopLink_Button()
                        binding.RecyclerView.adapter=topAdapter
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

        binding.RecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    fun Setup_TopLink_Button(){
        binding.topLinkButton.setCardBackgroundColor(ContextCompat.getColorStateList(requireContext(),
            R.color.greenishbg
        ))
        binding.topLinkButtonText.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.Cream
        ))

        binding.recentLinkButton.setCardBackgroundColor(ContextCompat.getColorStateList(requireContext(),
            R.color.backgroundwhitishbg
        ))
        binding.recentlinkbuttonText.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.Pewter
        ))
    }
    private fun Setup_RecentLink_Button(){
        binding.recentLinkButton.setCardBackgroundColor(ContextCompat.getColorStateList(requireContext(),
            R.color.greenishbg
        ))
        binding.recentlinkbuttonText.setTextColor(ContextCompat.getColor(requireContext(), R.color.Cream))

        binding.topLinkButton.setCardBackgroundColor(ContextCompat.getColorStateList(requireContext(),
            R.color.backgroundwhitishbg
        ))
        binding.topLinkButtonText.setTextColor(ContextCompat.getColor(requireContext(),
            R.color.Pewter
        ))
    }
    private fun setupChart(data: OverallUrlChart) {
        val entries = data.javaClass.declaredFields.mapIndexed { index, field ->
            field.isAccessible = true
            Entry(index.toFloat(), field.getInt(data).toFloat())
        }

        val dataSet = LineDataSet(entries, "Chart Label")
        val lineData = LineData(dataSet)

        binding.lineChart.data = lineData
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.xAxis.valueFormatter = MyXAxisValueFormatter() as ValueFormatter


        binding.lineChart.invalidate()
    }
    class MyXAxisValueFormatter : IAxisValueFormatter {


        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return value.toInt().toString()
        }
    }





}