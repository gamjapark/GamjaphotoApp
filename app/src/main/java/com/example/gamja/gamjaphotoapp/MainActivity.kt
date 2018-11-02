package com.example.gamja.gamjaphotoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import kotlinx.android.synthetic.main.gamja_search.*


class MainActivity : AppCompatActivity() {

    private var  my_url:String = "https://secure.flickr.com/services/rest/?method=flickr.photos.search"
    private var api_key:String ="&api_key=5db3ffc3ea67dd7f9c0a321c850f322b"
    private var search_text:String="&text='cat'"
    private var safe_search:String="&safe_search=1"
    private var content_type:String="&content_type=1"
    private var search_sort:String="&sort=interestingness-desc"
    private var per_page:String ="&per_page=5"
    private var myformat:String ="&format=json"
    private var REQUESTURL:String = my_url + api_key + search_text + safe_search + content_type + search_sort+ per_page + myformat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamja_search)

         searchBtn.setOnClickListener{
            getJSONINFO(this, REQUESTURL, gamjaGridView).execute()
        }

    }
}

