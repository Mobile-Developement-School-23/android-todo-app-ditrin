package com.example.todolist.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todolist.R
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setCurrentFragment(MainScreenFragment())
        }

//        val sdk = YandexAuthSdk(
//            this, YandexAuthOptions(this)
//        )
//        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
//        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())
//
//        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK){
//                val yandexAuthToken = sdk.extractToken(it.resultCode, it.data)
//                if (yandexAuthToken != null){
//
//
//                    Log.d("Testus", "$yandexAuthToken.value")
//                    supportFragmentManager.beginTransaction()
//                        .add(R.id.fragmentContainer, MainScreenFragment())
//                        .commit()
//                }
//
//            } else{
//                Log.d("Test", "error")
//            }
//
//        }
//        launcher.launch(intent)
    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commit()
        }
}