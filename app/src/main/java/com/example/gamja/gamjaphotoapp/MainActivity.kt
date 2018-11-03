package com.example.gamja.gamjaphotoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import kotlinx.android.synthetic.main.gamja_search.*


class MainActivity : AppCompatActivity() {

    private var  myUrl:String = "https://secure.flickr.com/services/rest/?method=flickr.photos.search"
    private var apiKey:String ="&api_key=5db3ffc3ea67dd7f9c0a321c850f322b"
    private var safeSearch:String="&safe_search=1"
    private var contentType:String="&content_type=1"
    private var searchSort:String="&sort=interestingness-desc"
    private var myFormat:String ="&format=json"
    private var searchText:String="&text="
    private var requestUrl:String = myUrl + apiKey + safeSearch + contentType + searchSort+ myFormat  + searchText

    private var keyWord:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamja_search)


        Toast.makeText(this,keyWord, Toast.LENGTH_SHORT).show()
        searchBtn.setOnClickListener{
            keyWord = SearchTxt.text.toString()
            getJSONINFO(this, requestUrl, gamjaGridView, keyWord).execute()
        }

    }
}

