package com.ubertech.cryptech.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ubertech.cryptech.API.Models.Request.LoginRequest
import com.ubertech.cryptech.API.Models.Response.LoginResponse
import com.ubertech.cryptech.API.Services.ApiClient
import com.ubertech.cryptech.API.Services.ApiInterface
import com.ubertech.cryptech.Main.MainActivity
import com.ubertech.cryptech.R
import com.ubertech.cryptech.Utilities.TinyDB
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

class Login : AppCompatActivity() {

    // Views
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var register: TextView
    lateinit var login: ImageView

    // Global data sources
    private var api: ApiInterface? = null
    private var db: TinyDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Views
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        register = findViewById(R.id.register)
        login = findViewById(R.id.imageView2)

        // Initiating tinyDB
        db = TinyDB(this@Login)
        
        val verified = db!!.getBoolean("verified")
        val logged = db!!.getBoolean("logged")
        
        if(logged) {
            
            if(verified) {

                startActivity(Intent(this@Login, MainActivity::class.java))
                finish()

            } else {

                startActivity(Intent(this@Login, Verify::class.java))
                finish()

            }
            
        }

        // Initiating Retrofit API
        api = ApiClient.client.create(ApiInterface::class.java)

        login.setOnClickListener {

            login.isClickable = false

            when {
                email.text.isNullOrEmpty() -> Toast.makeText(this@Login, "Enter your Email ID to continue", Toast.LENGTH_SHORT).show()
                password.text.isNullOrBlank() -> Toast.makeText(this@Login, "Enter your Password to continue", Toast.LENGTH_SHORT).show()
                else -> api!!.requestLogin(LoginRequest(email.text.toString(), password.text.toString()))
                        .enqueue(object : retrofit2.Callback<LoginResponse>{
                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                login.isClickable = true
                                Timber.e(t)
                                Toast.makeText(this@Login, "Something Wen't wrong", Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                                when {
                                    response.isSuccessful -> {

                                        login.isClickable = true

                                        Toast.makeText(this@Login, "You are successfully logged in", Toast.LENGTH_SHORT).show()

                                        db!!.putString("jwt",response.body()!!.jwt!!)
                                        db!!.putBoolean("logged",true)
                                        db!!.putBoolean("verified",true)
                                        startActivity(Intent(this@Login, MainActivity::class.java))
                                        finish()

                                    }
                                    response.code() == 417 -> {

                                        login.isClickable = true

                                        db!!.putString("jwt",response.body()!!.jwt!!)
                                        db!!.putBoolean("logged",true)
                                        startActivity(Intent(this@Login, Verify::class.java))
                                        finish()

                                    }
                                    response.code() == 401 -> {
                                        login.isClickable = true
                                        Toast.makeText(this@Login, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }

                        })
            }

        }

        register.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }

    }

}
