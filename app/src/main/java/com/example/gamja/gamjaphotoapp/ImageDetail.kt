package com.example.gamja.gamjaphotoapp

import android.app.ProgressDialog
import android.content.ClipDescription
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import kotlinx.android.synthetic.main.gamja_detailcardview.view.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import java.util.*


@Suppress("DEPRECATION")

class ImageDetail(
    private var c: Context, private var myBtn: Button, private var myGridView: GridView,
    private var onePicture: ShortInfo.Photo, private val photoURL: String, private val infoUrl: String
) : AsyncTask<String, Void, ImageDetail.DetailInfo?>() {

    class DetailInfo(
        private var views: String, private var dateUploaded: String, private var lastUpdated: String
        , private var userName: String, private var location: String, private var description: String
        , private var url: String, private var bit: Bitmap?
    ) {

        fun getBit(): Bitmap? {
            return bit
        }

        fun getViews(): String? {
            return views
        }

        fun getDateUploaded(): String? {
            return dateUploaded
        }

        fun getLastUpdated(): String? {
            return lastUpdated
        }

        fun getUserName(): String? {
            return userName
        }

        fun getLocation(): String? {
            return location
        }

        fun getDescription(): String? {
            return description
        }

        fun getUrl(): String? {
            return url
        }

    }

    override fun onPreExecute() {
        super.onPreExecute()

        Toast.makeText(c, "데이터 가져오는 중", Toast.LENGTH_SHORT).show()

    }

    override fun doInBackground(vararg url: String): ImageDetail.DetailInfo? {
        var bit: Bitmap? = this.getBitmap()
        var info: ImageDetail.DetailInfo? = this.getInfo(bit)
        return info
    }

    override fun onPostExecute(info: ImageDetail.DetailInfo?) {
        super.onPostExecute(info)
        var bit: Bitmap? = info?.getBit()

        if (info != null) {
            Toast.makeText(c, photoURL, Toast.LENGTH_LONG).show()

            myGridView.adapter = MrAdapter(c, onePicture, info)

            myBtn.setOnClickListener {
                it.isEnabled = false

                val path: String = Environment.getExternalStorageDirectory().toString()
                val file = File(path + "/DCIM/" + onePicture.getID() + ".jpg")
                try {
                    file.createNewFile()
                    val fileStream = FileOutputStream(file)
                    bit?.compress(Bitmap.CompressFormat.JPEG, 100, fileStream)
                    fileStream.flush()
                    fileStream.close()

                    Toast.makeText(c, "Saved", Toast.LENGTH_LONG).show()

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show()
                }
            }

        } else
            Toast.makeText(c, "이미지를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
    }

    private fun getInfo(bit: Bitmap?): DetailInfo? {
        var result: String? = getJSON(infoUrl)

        if (result == null)
            return null

        result = result.replace("jsonFlickrApi(", "");
        result = result.replace(")", "");
        try {
            val jsonObject = JSONObject(result)

            val data = jsonObject.getJSONObject("photo")

            val views = data.getString("views")
            val dataUploaded = Date(data.getJSONObject("dates").getString("posted").toLong()*1000).toGMTString()
            val lastUpdated = Date(data.getJSONObject("dates").getString("lastupdate").toLong()*1000).toGMTString()
            val userName = data.getJSONObject("owner").getString("username")
            var location = data.getJSONObject("owner").getString("location")
            if(location == "") location = "NO DATA"
            var description = data.getJSONObject("description").getString("_content")
            if(description == "") description = "NO DATA"
            val url = data.getJSONObject("urls").getJSONArray("url").getJSONObject(0).getString("_content")

            return ImageDetail.DetailInfo(
                views,
                dataUploaded,
                lastUpdated,
                userName,
                location,
                description,
                url,
                bit
            )
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getBitmap(): Bitmap? {
        try {
            val url = URL(photoURL)
            val con = url.openConnection() as HttpURLConnection

            con.requestMethod = "GET"
            con.connectTimeout = 15000
            con.readTimeout = 15000
            con.doInput = true

            if (con.responseCode == 200) {
                val inputStreamReader = BufferedInputStream(con.inputStream)
                var bitmap: Bitmap = BitmapFactory.decodeStream(inputStreamReader)
                inputStreamReader.close()
                /**
                 * #TODOLSIT
                 */

                return bitmap
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    class MrAdapter(
        private var c: Context,
        private var pic: ShortInfo.Photo,
        private var info: ImageDetail.DetailInfo
    ) :
        BaseAdapter() {

        override fun getCount(): Int {
            return 1
        }

        override fun getItem(pos: Int): ImageDetail.DetailInfo? {
            return info
        }

        override fun getItemId(pos: Int): Long {
            return pos.toLong()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var convertView = view
            if (convertView == null) {
                val viewId: Int = R.layout.gamja_detailcardview
                convertView = LayoutInflater.from(c).inflate(viewId, viewGroup, false)
            }

            val titleTxt = convertView!!.photoDetailTitle as TextView
            val image = convertView!!.photoImage as ImageView
            val views = convertView!!.photoViewNum as TextView
            val dateUpload = convertView!!.photoDateUploadTxt as TextView
            val lastUpdated = convertView!!.photoLastUpdateTxt as TextView
            val userName = convertView!!.ownerUserName as TextView
            val location = convertView!!.ownerLocationTxt as TextView
            val description = convertView!!.photoDescriptionTxt as TextView
            val url = convertView!!.photoURLTxt as TextView

            views.text = info.getViews()
            dateUpload.text = info.getDateUploaded()
            lastUpdated.text = info.getLastUpdated()
            userName.text = info.getUserName()
            location.text = info.getLocation()
            description.text = info.getDescription()
            url.text = info.getUrl()

            titleTxt.text = pic.getTitle()
            image.setImageBitmap(info.getBit())

            return convertView
        }
    }


    private fun connect(requestUrl: String): Any {
        try {
            val url = URL(requestUrl)
            val con = url.openConnection() as HttpURLConnection

            con.requestMethod = "GET"
            con.connectTimeout = 15000
            con.readTimeout = 15000
            con.doInput = true

            return con

        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return "url Error" + e.message
        } catch (e: IOException) {
            e.printStackTrace()
            return "connect Error" + e.message
        }
    }

    private fun getJSON(requestUrl: String): String? {

        val connection = connect(requestUrl)
        if (connection.toString().startsWith("Error")) {
            return connection.toString()
        }

        try {
            val con = connection as HttpURLConnection
            if (con.responseCode == 200) {
                val inputStreamReader = BufferedInputStream(con.inputStream)
                val bufferedReader = BufferedReader((InputStreamReader(inputStreamReader)))
                val stringBuffer = StringBuffer()
                var line: String?
                while (true) {
                    line = bufferedReader.readLine()
                    if (line == null) break
                    stringBuffer.append(line + "\n");
                }
                inputStreamReader.close()
                bufferedReader.close()
                return stringBuffer.toString()
            } else {
                return "Error " + con.responseMessage
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "Error " + e.message
        }
    }


}