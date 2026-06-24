package com.example.proyecto_jags

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ctUsuario:EditText=findViewById(R.id.ctUsuario)
        val ctPassword:EditText=findViewById(R.id.ctPassword)
        val btnAcceder:Button=findViewById(R.id.btnAcceder)

        btnAcceder.setOnClickListener {

            val usuario = ctUsuario.text.toString()
            val password = ctPassword.text.toString()

            val request = LoginRequest(
                userName = usuario,
                password = password
            )

            RetrofitClient.instance.login(request)
                .enqueue(object : retrofit2.Callback<LoginResponse> {

                    override fun onResponse(
                        call: retrofit2.Call<LoginResponse>,
                        response: retrofit2.Response<LoginResponse>
                    ) {

                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@MainActivity,
                                "Login exitoso",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(
                                this@MainActivity,
                                Principal::class.java
                            )

                            startActivity(intent)

                            finish()

                        } else {

                            val errorText = response.errorBody()?.string()

                            Toast.makeText(
                                this@MainActivity,
                                "Credenciales incorrectas",
                                Toast.LENGTH_LONG
                            ).show()

                            android.util.Log.e("API_ERROR", errorText ?: "Sin detalle")
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<LoginResponse>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}