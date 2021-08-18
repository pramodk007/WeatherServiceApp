package com.androiddev.weatherserviceapp.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.androiddev.weatherserviceapp.R
import com.androiddev.weatherserviceapp.databinding.ActivityWeatherBinding
import com.androiddev.weatherserviceapp.util.ApiState
import com.androiddev.weatherserviceapp.util.HelperFunctions
import com.androiddev.weatherserviceapp.util.HelperFunctions.toggleVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private  var dayInWeek: Int = 0
    private  var day: Int = 0
    private lateinit var month_name: String
    private lateinit var c:Calendar
    private lateinit var binding: ActivityWeatherBinding
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val bundle :Bundle? = intent.extras
        val userName = bundle!!.getString("userName")
        val district = bundle.getString("district")

        weatherViewModel.getCityData(district.toString())
        binding.userName.text = userName

        c = Calendar.getInstance()
        dateAndTime()
        initListener()
        switchView()
        lifecycleScope.launchWhenStarted {
            weatherViewModel._postStateFlow.collect {
                when(it){
                    is ApiState.Loading -> {
                        Toast.makeText(this@WeatherActivity,"loading..", Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(this@WeatherActivity,"No such city found.",Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Success -> {
                        try{
                            binding.State.text = it.data.location.name
                            binding.condition.text = it.data.current.condition.text
                            binding.Latitude.text = it.data.location.lat.toString()
                            binding.Longitude.text = it.data.location.lon.toString()
                            binding.temperatureinC.text = it.data.current.temp_c.toString() + "\u2103"
                            binding.temperatureinF.text = it.data.current.temp_f.toString() + "℉"
                            binding.dayInWeek.text = getDayName(dayInWeek)
                            binding.dayInMonth.text = day.toString()
                            binding.Month.text = month_name
                        }catch (e:Exception){
                            Toast.makeText(this@WeatherActivity,"No such city found.",Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }
    private fun initListener()
    {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { weatherViewModel.getCityData(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.State.text = ""
                binding.condition.text = ""
                binding.Latitude.text = "N/A"
                binding.Longitude.text = "N/A"
                binding.temperatureinC.text = ""
                binding.temperatureinF.text = ""
                binding.dayInWeek.text = ""
                binding.dayInMonth.text = ""
                binding.Month.text = ""
                return true
            }

        })
    }
    private fun switchView(){

        binding.showFOrC.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.changeT2F.text = "change to Celsius"
                binding.temperatureinC.toggleVisibility()
                binding.temperatureinF.toggleVisibility()
            }
            else{
                binding.changeT2F.text = "change to Fahrenheit"
                binding.temperatureinF.toggleVisibility()
                binding.temperatureinC.toggleVisibility()
            }
        }
    }
    private fun dateAndTime(){
        ///month
        val month_date = SimpleDateFormat("MMMM")
        month_name = month_date.format(c.getTime())
        //day in month
        day = c.get(Calendar.DAY_OF_MONTH)
        //day in week
        dayInWeek = c.get(Calendar.DAY_OF_WEEK)

    }

    fun getDayName(dayInWeek: Int):String {
        when (dayInWeek) {
            1 -> return "Sunday"
            2 -> return "Monday"
            3 -> return "Tuesday"
            4 -> return "Wednesday"
            5 -> return "Thursday"
            6 -> return "Friday"
            7 -> return "Saturday"
            else -> return "Time has stopped"
        }
    }
}