package com.example.gamja.gamjaphotoapp

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

        val onePhoto = ShortInfo.Photo(null,title, id, secret, server, farm)

        val photoUrl:String = "http://farm"+ onePhoto.getFarm() +".staticflickr.com/"+ onePhoto.getServer() +"/"+ onePhoto.getID() +"_"+ onePhoto.getSecret() +"_b.jpg"
        var infoUrl: String? = "https://secure.flickr.com/services/rest/"
        infoUrl = "$infoUrl?method=flickr.photos.getInfo&api_key=5db3ffc3ea67dd7f9c0a321c850f322b"
        infoUrl = "$infoUrl&secret=" + onePhoto.getSecret() + "&format=json&photo_id=" + onePhoto.getID()

        ImageDetail(this, gamjaSave ,gamjaDetailGridView ,onePhoto, photoUrl, infoUrl ).execute()

    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}