package com.codinginflow.imagesearchapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var navC: NavController? = null

    // TODO теперь ты пооставляла форс анрапы(!!). В рошлом коммету было описано, как оно должно было выглядить
    //  with(navHostFragment.findNavController()) { navController = this .... }
    @SuppressLint("CommitTransaction", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment

        with(navHostFragment.findNavController()) { navC = this }

        val appBarConfiguration = AppBarConfiguration(navC!!.graph)
        setupActionBarWithNavController(navC!!, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navC!!.navigateUp() || super.onSupportNavigateUp()
    }
}