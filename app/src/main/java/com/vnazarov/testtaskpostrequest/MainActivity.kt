package com.vnazarov.testtaskpostrequest

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var check1: CheckBox
    private lateinit var check2: CheckBox
    private lateinit var check3: CheckBox
    private lateinit var radio1: RadioButton
    private lateinit var radio2: RadioButton
    private lateinit var radio3: RadioButton
    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var textView: TextView

    private val builder = Retrofit.Builder()
        .baseUrl("https://corpus.by/")
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = builder.build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initItems()

        button.setOnClickListener {
            if (editText.text.isEmpty()){
                Toast.makeText(this, "Edit text is empty", Toast.LENGTH_SHORT).show()
            } else {

                executeSendFeedbackForm("test", 0, 0, 0, "radiobutton1", "option2")
            }
        }
    }

    private fun initItems(){
        spinner = findViewById(R.id.spinner)
        check1 = findViewById(R.id.check1)
        check2 = findViewById(R.id.check2)
        check3 = findViewById(R.id.check3)
        radio1 = findViewById(R.id.radio1)
        radio2 = findViewById(R.id.radio2)
        radio3 = findViewById(R.id.radio3)
        button = findViewById(R.id.button_send_request)
        editText = findViewById(R.id.edit_text)
        textView = findViewById(R.id.textView)

        val optionsList = arrayListOf("Опцыя #1", "Опцыя #2", "Опцыя #3")
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, optionsList)
        spinner.adapter = adapter

        val itemSelectedSpinner = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                option = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        spinner.onItemSelectedListener = itemSelectedSpinner

        radio1.setOnClickListener {
            radio = 1
        }
        radio2.setOnClickListener {
            radio = 2
        }
        radio3.setOnClickListener {
            radio = 3
        }

        check1.setOnClickListener {
            dCheck1 = if (it.isActivated) 1
            else 0
        }

        check2.setOnClickListener {
            dCheck2 = if (it.isActivated) 1
            else 0
        }

        check3.setOnClickListener {
            dCheck3 = if (it.isActivated) 1
            else 0
        }
    }

    private fun executeSendFeedbackForm(text: String, check1: Int, check2: Int, check3: Int, mode: String, option: String){
        val apiService = retrofit.create(APIService::class.java)

        val call = apiService.sendUserFeedback(text, check1, check2, check3, mode, option)

        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(this@MainActivity, "Success!", Toast.LENGTH_SHORT).show()
                println(response)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }
}