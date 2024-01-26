package com.example.cosigo.ui.fragments

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
import com.example.cosigo.adapters.LinkAdapter
import com.example.cosigo.databinding.FragmentHomeBinding
import com.example.cosigo.models.Basic
import com.example.cosigo.models.Link
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
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var res:Basic

    private lateinit var mainViewModel: MainViewModel
    private val Adapter by lazy { LinkAdapter() }


    
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

        // created this function to get all the required data from the api to populate the fields
        binding.RecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.RecyclerView.adapter=Adapter
        requestApiData()



        binding.topLinkButton.setOnClickListener {
            Setup_TopLink_Button()
            Adapter.setData(res.data.topLinks)
        }

        binding.recentLinkButton.setOnClickListener {

            Setup_RecentLink_Button()
            Adapter.setData(res.data.recentLinks)

        }


        return binding.root
    }
    private fun requestApiData() {
        mainViewModel.getProfileData()
        mainViewModel.profileResponse.observe(viewLifecycleOwner,{ response ->
            when(response){
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility=View.VISIBLE
                }

                is NetworkResult.Success -> {
                    response.data?.let {
                        Adapter.setData(it.data.topLinks)
                        binding.progressBar.visibility=View.GONE
                        res=it;binding.result=it
                        setupChart(it.data.overallUrlChart)
                        Setup_TopLink_Button()
                    }
                }
                is NetworkResult.Error -> {
                    binding.progressBar.visibility=View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
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

    //to set up the chart
    private fun setupChart(data: OverallUrlChart) {
        binding.lineChart.invalidate()
        val entries = data.javaClass.declaredFields.mapIndexed { index, field ->
            field.isAccessible = true
            Entry(index.toFloat(), field.getInt(data).toFloat())
        }
        val dataSet = LineDataSet(entries, "Number of URL")
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.cubicIntensity = 0.2f

        val lineData = LineData(dataSet)

        binding.lineChart.data = lineData
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.xAxis.valueFormatter = MyXAxisValueFormatter(entries) as ValueFormatter

        binding.lineChart.invalidate()
    }

    class MyXAxisValueFormatter(private val entry: List<Entry>) : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            val intValue = value.toInt()
            return if (intValue >= 0 && intValue < entry.size) {
                val date = entry[intValue].data.toString()
                date.substring(0, 4)
            } else {
                ""
            }
        }
    }






}