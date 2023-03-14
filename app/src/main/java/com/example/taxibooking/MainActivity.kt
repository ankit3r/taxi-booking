package com.example.taxibooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taxibooking.activitys.LoginPageHolderActivity
import com.example.taxibooking.activitys.MapsActivity
import com.example.taxibooking.databinding.ActivityMainBinding
import com.example.taxibooking.utils.NetworkUtils.Companion.isInternetAvailable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener {
            if (isInternetAvailable(this))
                startActivity(Intent(this,MapsActivity::class.java))
            else
                Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show()
        }


    }
}