package com.example.gamja.gamjaphotoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import kotlinx.android.synthetic.main.gamja_search.*


class MainActivity : AppCompatActivity() {
    // https://secure.flickr.com/services/rest/?method=flickr.photos.search&api_key=5db3ffc3ea67dd7f9c0a321c850f322b&safe_search=1&content_type=1&sort=interestingness-desc&format=json&text=

    private val apiKey: String = "&api_key=5db3ffc3ea67dd7f9c0a321c850f322b"
    private var sortType: String = "&sort=interestingness-desc"
    private var currentPage: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gamja_search)

        searchBtn.setOnClickListener {
            getJSONINFO(
                this,
                getRequestUrl(SearchTxt.text.toString(), 20, this.currentPage), gamjaGridView
            ).execute()
        }
        prevBtn.setOnClickListener {
            if (this.currentPage - 1 == 0) {
                Toast.makeText(this, "-첫 페이지-", Toast.LENGTH_LONG).show()
            } else {
                --this.currentPage
                pageCheck.text = (this.currentPage).toString()
                getJSONINFO(
                    this,
                    getRequestUrl(SearchTxt.text.toString(), 20, this.currentPage), gamjaGridView
                ).execute()
            }
        }
        nextBtn.setOnClickListener {
            ++this.currentPage
            pageCheck.text = (this.currentPage).toString()
            getJSONINFO(
                this,
                getRequestUrl(SearchTxt.text.toString(), 20, this.currentPage), gamjaGridView
            ).execute()
        }
    }

    fun getRequestUrl(keyword: String, perPage: Int, page: Int): String {
        val host: String? = "https://secure.flickr.com/services/rest/"
        val requestUrl: String? = "?method=flickr.photos.search&format=json&content_type=1&safe_search=1"
        val pageInfo: String? = "&per_page=$perPage&page=$page"
        return host + requestUrl + this.apiKey + this.sortType + pageInfo + "&text=" + keyword
    }
}

