package com.example.todolist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.todolist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainScreenViewModel
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setCurrentFragment(MainScreenFragment())
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commit()
        }

        //viewModel = ViewModelProvider(this)[MainViewModel::class.java]
     //   viewModel.todoList.observe(this) {
           // Log.d("lubrek", it.toString())
         //   if(count == 0) {
        //        count++
     //           val item = it[0]
     //           viewModel.changeCompletedState(item)
   //         }
   //     }
    }