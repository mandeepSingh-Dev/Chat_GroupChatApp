package com.example.chat__groupchatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chat__groupchatapp.databinding.ActivityUsersGroupBinding

class UsersGroupActivity : AppCompatActivity() {

    lateinit var binding : ActivityUsersGroupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}