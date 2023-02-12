package com.vnazarov.testtaskpostrequest

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    private val builder = Retrofit.Builder()
        .baseUrl("https://corpus.by/")
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = builder.build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initItems()

        button.setOnClickListener {
            if (editText.text.isEmpty()) {
                Toast.makeText(this, "Edit text is empty", Toast.LENGTH_SHORT).show()
            } else {

                val client = OkHttpClient.Builder().build()
                val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
                val body = RequestBody.create(
                    mediaType,
                    "text=Груша цвіла апошні год.&checkbox1=0&checkbox2=1&checkbox3=1&mode=radiobutton2&selector=option1"
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

                            var str = response.body!!.string()
                            str = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY).toString()
                            } else {
                                Html.fromHtml(str).toString()
                            }
                            runOnUiThread { textView.text = str }

                            println(str)
//                            println(response.body!!.string())
                        }
                    }
                })

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
}