package com.example.gamja.gamjaphotoapp

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
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


@Suppress("DEPRECATION")

class ImageDetail(private var c: Context,private var myBtn:Button ,private var myGridView: GridView, private var onePicture:ShortInfo.photo, private var photoRul:String): AsyncTask<String, Void, Bitmap?>() {

    private lateinit var  myprogress:ProgressDialog
    private val photoURL:String = photoRul
    override fun onPreExecute() {
        super.onPreExecute()

        myprogress = ProgressDialog(c)
        myprogress.setTitle("데이터 가져오는 중")
        myprogress.setMessage("...Please wait...")
        myprogress.show()
    }
    override fun doInBackground(vararg url: String): Bitmap? {
        return getBitmap()
    }

    override fun onPostExecute(bit:Bitmap?) {
        super.onPostExecute(bit)

        myprogress.dismiss()

        if(bit != null) {
            Toast.makeText(c, photoURL, Toast.LENGTH_LONG).show()
            myGridView.adapter = MrAdapter(c, onePicture, bit)

            myBtn.setOnClickListener{
                it.isEnabled = false

                val path:String = Environment.getExternalStorageDirectory().toString()
                val file = File(path+"/DCIM/" + onePicture.getID() + ".jpg")
                try {
                    file.createNewFile()
                    val fileStream = FileOutputStream(file)
                    bit.compress(Bitmap.CompressFormat.JPEG, 100, fileStream)
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
        }
        else
            Toast.makeText(c, "이미지를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
    }


    private fun getBitmap(): Bitmap? {
        try{
            val url = URL(photoURL)
            val con = url.openConnection() as HttpURLConnection

            con.requestMethod = "GET"
            con.connectTimeout = 15000
            con.readTimeout = 15000
            con.doInput = true

            if (con.responseCode == 200) {
                val inputStreamReader = BufferedInputStream(con.inputStream)
                var bitmap:Bitmap = BitmapFactory.decodeStream(inputStreamReader)
                inputStreamReader.close()
                return bitmap
            }
        }catch (e: MalformedURLException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return null
    }

    class MrAdapter(private var c: Context, private var pic:ShortInfo.photo,private var bitmap:Bitmap?) : BaseAdapter() {

        override fun getCount(): Int {
            return 1
        }

        override fun getItem(pos: Int): Bitmap?{
            return bitmap
        }

        override fun getItemId(pos: Int): Long {
            return pos.toLong()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var convertView = view
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.gamja_detailcardview, viewGroup, false)
            }

            val titleTxt = convertView!!.photoDetailTitle as TextView
            val image = convertView!!.photoImage as ImageView

            titleTxt.text = pic.getTitle()
            image.setImageBitmap(bitmap)

            return convertView
        }
    }
}