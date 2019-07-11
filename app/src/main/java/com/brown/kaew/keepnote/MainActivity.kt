package com.brown.kaew.keepnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.brown.kaew.keepnote.databinding.ActivityMainBinding
import com.brown.kaew.keepnote.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val mViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(
            MainViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = mViewModel


//        navController = findNavController(R.id.nav_host_fragment)
        // set action bar
        setSupportActionBar(binding.toolBar)
    }
}

