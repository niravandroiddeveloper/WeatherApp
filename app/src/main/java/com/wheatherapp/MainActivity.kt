package com.wheatherapp

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.wheatherapp.openweathermap.R

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R
            .layout.activity_main)



    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

}
