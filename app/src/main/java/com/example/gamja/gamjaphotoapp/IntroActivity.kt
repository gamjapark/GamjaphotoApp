package com.example.gamja.gamjaphotoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler


class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamja_intro)

        Handler().postDelayed(
            {
                startActivity(Intent( this, MainActivity::class.java))
            }
            ,1300L)

    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}