package com.example.gamja.gamjaphotoapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Handler
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.gamja_cardview.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

@Suppress("DEPRECATION")

class ShortInfo(private var c: Context, private var result: String, private var myGridView: GridView) : AsyncTask<Void, Void, Boolean>() {


    private var pictures = ArrayList<Photo>()

    override fun onPreExecute() {
        super.onPreExecute()

        Toast.makeText(c, "데이터 가져오는 중", Toast.LENGTH_SHORT).show()

    }
    override fun doInBackground(vararg voids: Void): Boolean? {
        return parSing()
    }

    override fun onPostExecute(isParsed: Boolean?) {
        super.onPostExecute(isParsed)


        if (isParsed!!)
            myGridView.adapter = MrAdapter(c, pictures)
        else
            Toast.makeText(c, "정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()

    }
    private  fun parSing():Boolean{
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
                val id = onePhoto.getString("id")
                val secret = onePhoto.getString("secret")
                val server = onePhoto.getString("server")
                val farm = onePhoto.getString("farm")

                val photoUrl:String = "http://farm"+ farm +".staticflickr.com/"+ server +"/"+ id +"_"+ secret +"_t.jpg"


                try{
                    val url = URL(photoUrl)
                    val con = url.openConnection() as HttpURLConnection

                    con.requestMethod = "GET"
                    con.connectTimeout = 15000
                    con.readTimeout = 15000
                    con.doInput = true

                    if (con.responseCode == 200) {
                        val inputStreamReader = BufferedInputStream(con.inputStream)
                        val bitmap:Bitmap = BitmapFactory.decodeStream(inputStreamReader)
                        inputStreamReader.close()

                        val onePicture = Photo(bitmap ,title, id, secret, server, farm)
                        pictures.add(onePicture)
                    }
                }catch (e: MalformedURLException){
                    e.printStackTrace()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
            return true
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
    }

    class Photo(private var bitmap:Bitmap?, private var title:String, private var id:String, private var secret:String, private var server:String, private var farm:String){
        fun getBitmap():Bitmap?{
            return bitmap
        }
        fun getTitle():String{
            return title
        }
        fun getID():String{
            return id
        }
        fun getSecret():String{
            return secret
        }
        fun getServer():String{
            return server
        }
        fun getFarm():String{
            return farm
        }
    }

    class MrAdapter(private var c: Context, private var pictures:ArrayList<Photo>) : BaseAdapter() {

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
            val imageSmall = convertView!!.photoSmall as ImageView

            val onePicture = this.getItem(i) as Photo

            titleTxt.text = onePicture.getTitle()
            imageSmall.setImageBitmap(onePicture.getBitmap())

            convertView.setOnClickListener {
                Toast.makeText(c,onePicture.getID(),Toast.LENGTH_SHORT).show()
                val intent = Intent(c , DetailActivity::class.java)
                intent.putExtra("title", onePicture.getTitle())
                intent.putExtra("id", onePicture.getID())
                intent.putExtra("secret", onePicture.getSecret())
                intent.putExtra("server", onePicture.getServer())
                intent.putExtra("farm", onePicture.getFarm())
                c.startActivity(intent)
            }
            return convertView
        }
    }
}