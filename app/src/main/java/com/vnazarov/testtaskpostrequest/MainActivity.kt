package com.vnazarov.testtaskpostrequest

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initItems()

        button.setOnClickListener {
            if (editText.text.isEmpty()) {
                Toast.makeText(this, "Edit text is empty", Toast.LENGTH_SHORT).show()
            } else {

                readResponse()
            }
        }
    }

    private fun initItems() {
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
        val adapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            optionsList
        )
        spinner.adapter = adapter

        val itemSelectedSpinner = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                option = p2 + 1
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
            dCheck1 = if (check1.isChecked) 1
            else 0
        }

        check2.setOnClickListener {
            dCheck2 = if (check2.isChecked) 1
            else 0
        }

        check3.setOnClickListener {
            dCheck3 = if (check3.isChecked) 1
            else 0
        }
    }

    private fun readResponse(){
        val client = OkHttpClient.Builder().build()
        val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
        val body = RequestBody.create(
            mediaType,
            "text=${editText.text}.&checkbox1=$dCheck1&checkbox2=$dCheck2&checkbox3=$dCheck3&mode=radiobutton$radio&selector=option$option"
        )
        val request = Request.Builder()
            .url("https://corpus.by/ServiceDemonstrator/api.php")
            .method("POST", body)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val jObject = response.body!!.string()
                    val str = JSONObject(jObject).getString("result")

                    runOnUiThread { textView.text = str }
                }
            }
        })
    }
}