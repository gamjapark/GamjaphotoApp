package com.example.gamja.gamjaphotoapp

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.gamja_cardview.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@Suppress("DEPRECATION")

class ShortInfo(private var c: Context, private var result: String, private var myGridView: GridView) : AsyncTask<Void, Void, Boolean>() {

    private lateinit var  myprogress:ProgressDialog
    private var pictures = ArrayList<photo>()

    override fun onPreExecute() {
        super.onPreExecute()

        myprogress = ProgressDialog(c)
        myprogress.setTitle("데이터 가져오는 중")
        myprogress.setMessage("...Please wait...")
        myprogress.show()
    }
    override fun doInBackground(vararg voids: Void): Boolean? {
        return Parsing()
    }

    override fun onPostExecute(isParsed: Boolean?) {
        super.onPostExecute(isParsed)

        myprogress.dismiss()
        if (isParsed!!)
            myGridView.adapter = MrAdapter(c, pictures)
        else
            Toast.makeText(c, "정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()

    }
    private  fun Parsing():Boolean{
        result = result.replace("jsonFlickrApi(", "");
        result = result.replace(")", "");
        try {
            val jsonObject = JSONObject(result)
            val photos = jsonObject.getJSONObject("photos")
            val jsonArray = photos.getJSONArray("photo")

            pictures.clear()

            for(i in 0 until jsonArray.length()){
                val onePhoto = jsonArray.getJSONObject(i)

                val title = onePhoto.getString("title")
                val onePicture = photo(title)
                pictures.add(onePicture)
            }
            return true
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
    }

    class photo(private var title:String){
        fun getTitle():String{
            return title
        }
    }

    class MrAdapter(private var c: Context, private var pictures:ArrayList<photo>) : BaseAdapter() {

        override fun getCount(): Int {
            return pictures.size
        }

        override fun getItem(pos: Int): Any {
            return pictures[pos]
        }

        override fun getItemId(pos: Int): Long {
            return pos.toLong()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var convertView = view
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.gamja_cardview, viewGroup, false)
            }

            val titleTxt = convertView!!.photoTitle as TextView

            val onePicture = this.getItem(i) as photo

            titleTxt.text = onePicture.getTitle()

            convertView.setOnClickListener { Toast.makeText(c,onePicture.getTitle(),Toast.LENGTH_SHORT).show() }

            return convertView
        }
    }
}