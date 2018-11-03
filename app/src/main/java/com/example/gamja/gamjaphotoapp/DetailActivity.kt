package com.example.gamja.gamjaphotoapp

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.gamja_imagedetail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamja_imagedetail)

        gamjaBack.setOnClickListener{
            finish()
        }

        val intent = intent
        val title:String = intent.getStringExtra("title")
        val id:String = intent.getStringExtra("id")
        val secret:String = intent.getStringExtra("secret")
        val server:String = intent.getStringExtra("server")
        val farm:String = intent.getStringExtra("farm")


        val onePhoto = ShortInfo.photo(null,title, id, secret, server, farm)
        val photoUrl:String = "http://farm"+ onePhoto.getFarm() +".staticflickr.com/"+ onePhoto.getServer() +"/"+ onePhoto.getID() +"_"+ onePhoto.getSecret() +"_b.jpg"

        ImageDetail(this, gamjaSave ,gamjaDetailGridView ,onePhoto, photoUrl ).execute()

    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}