package com.katerina.todoapp.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import com.katerina.todoapp.R
import com.katerina.todoapp.presentation.extensions.viewBinding
import com.katerina.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::inflate)
}