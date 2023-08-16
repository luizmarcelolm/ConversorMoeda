package com.example.conversormoeda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var resultado:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultado = findViewById<TextView>(R.id.txt_resultado)
        val buttonConverter = findViewById<Button>(R.id.btn_converter)
        buttonConverter.setOnClickListener{
          converter()
        }
    }

    private fun converter(){
      val selectedCurrency = findViewById<RadioGroup>(R.id.radio_group)
        val checked = selectedCurrency.checkedRadioButtonId

         val currency = when (checked){
             R.id.radio_eur -> "EUR"
             else -> "USD"
         }
        val editText = findViewById<EditText>(R.id.txt_input)
        val valor = editText.text.toString()
        if(valor.isEmpty())
            return


        Thread{
            val url=URL("https://free.currconv.com/api/v7/convert?q=${currency}_BRL&compact=ultra&apiKey=1efcd97e32cfd1ec6ada")
            val conn = url.openConnection()as HttpURLConnection

            try{
             val data = conn.inputStream.bufferedReader().readText()

                val obj = JSONObject(data)

                runOnUiThread{
                    val res = obj.getDouble("${currency}_BRL")
                    resultado.text = "R$${"%.2f".format(valor.toDouble() * res)}"
                    resultado .visibility = View.VISIBLE
                }
            }finally{
                conn.disconnect()
            }
        }.start()

    }
}