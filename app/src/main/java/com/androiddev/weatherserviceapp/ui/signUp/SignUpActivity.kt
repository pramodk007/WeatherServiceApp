package com.androiddev.weatherserviceapp.ui.signUp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.androiddev.weatherserviceapp.R
import com.androiddev.weatherserviceapp.databinding.ActivitySignupBinding
import com.androiddev.weatherserviceapp.ui.weather.WeatherActivity
import com.androiddev.weatherserviceapp.util.CalendarHelper
import com.androiddev.weatherserviceapp.util.HelperFunctions.afterTextChanged
import com.androiddev.weatherserviceapp.util.PinCodeApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var state: String? = null
    private var districtCopy: String = ""
    private var district: String? = null
    private var pin: String = ""
    private var mobileNumber: String = ""
    private var fullName: String = ""
    private var dateOfBirth: String = ""
    private var gender: String = ""
    private var addressLineOne: String = ""
    private var addressLineTwo: String = ""
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, gender)
        binding.TextFieldGender.setAdapter(arrayAdapter)

        showDatePicker()
        checkPinCode()

        lifecycleScope.launchWhenStarted {
            viewModel._pinCodeStateFlow.collect { it ->
                when (it) {
                    is PinCodeApiState.Loading -> {
                        Toast.makeText(this@SignUpActivity, "Checking..", Toast.LENGTH_SHORT).show()
                    }
                    is PinCodeApiState.Failure -> {
                        Toast.makeText(this@SignUpActivity, "Invalid Pin Code!", Toast.LENGTH_SHORT).show()
                    }
                    is PinCodeApiState.Success -> {
                        try {
                            district = it.data.PostOffice[0].District
                            state = it.data.PostOffice[0].State
                            districtCopy = district.toString()
                            binding.textViewState.text = state
                            binding.textViewDistrict.text = district


                        } catch (e: Exception) {
                            Toast.makeText(this@SignUpActivity, "Invalid Pin Code!", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                }
            }

        }
        binding.buttonCheck.setOnClickListener {
            checkFiled()
            viewModel.getPinCode(pin)
        }
        binding.buttonRegister.setOnClickListener {
            if(checkFiled() && binding.textViewState.text.isNotEmpty() && binding.textViewDistrict.text.isNotEmpty()) {

                val i = Intent(this@SignUpActivity, WeatherActivity::class.java)
                i.putExtra("userName",fullName)
                i.putExtra("district",districtCopy)
                startActivity(i)
            }
        }
    }

    private fun showDatePicker() {

        //dateOfBirth.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))

        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.TextFieldDateOfBirth.setText(sdf.format(cal.time))
            }

        binding.borderOfDOB.setEndIconOnClickListener {
            // Respond to end icon presses
            val dialog = DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = CalendarHelper.getCurrentDateInMills()
            dialog.show()
        }
    }

    private fun checkPinCode() {

        binding.TextFieldPinCode.afterTextChanged {
            pin = binding.TextFieldPinCode.text.toString()
            if (pin.trim().isEmpty()) {
                binding.textViewState.text = ""
                binding.textViewDistrict.text = ""
            } else if (pin.length == 6) {
                binding.buttonCheck.isEnabled = true
            } else if (pin.length < 6) {
                binding.buttonCheck.isEnabled = false
                binding.textViewState.text = ""
                binding.textViewDistrict.text = ""
            }

        }

    }

    private fun checkFiled():Boolean {
        //check name field
        fullName = binding.TextFieldFirstName.text.toString()
        mobileNumber = binding.TextFieldMobileNumber.text.toString()
        gender = binding.TextFieldGender.text.toString()
        dateOfBirth = binding.TextFieldDateOfBirth.text.toString()
        addressLineOne = binding.TextFieldAddressLineOne.text.toString()



        if (mobileNumber.trim().isEmpty()) {
            Toast.makeText(applicationContext, "Please enter your Mobile Number! ", Toast.LENGTH_SHORT).show()
            return false

        }else if(mobileNumber.length < 10){
            Toast.makeText(applicationContext, "Please enter valid Mobile Number! ", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (fullName.trim().isEmpty()) {
            Toast.makeText(applicationContext, "Please enter your Full Name! ", Toast.LENGTH_SHORT).show()
            return false

        } else if (gender == "Choose Gender") {
            Toast.makeText(applicationContext, "Please select your Gender! ", Toast.LENGTH_SHORT).show()
            return false

        } else if (dateOfBirth.trim().isEmpty()) {
            Toast.makeText(applicationContext, "Please select your DOB! ", Toast.LENGTH_SHORT).show()
            return false
        } else if (addressLineOne.trim().isEmpty()) {
            Toast.makeText(applicationContext, "Please enter your address! ", Toast.LENGTH_SHORT).show()
            return false
        } else if (addressLineOne.length <= 3) {
            Toast.makeText(applicationContext, "Please enter your address more than 3 character! ", Toast.LENGTH_SHORT).show()
            return false
        }else if(pin.trim().isEmpty()) {
            Toast.makeText(applicationContext, "Please enter your Pin Code! ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}