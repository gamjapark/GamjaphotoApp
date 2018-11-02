package com.example.gamja.gamjaphotoapp

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.GridView
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

@Suppress("DEPRECATION")
class getJSONINFO(private var c: Context, private var myURL: String, private var myGridView: GridView) : AsyncTask<Void, Void, String>(){

    private lateinit var  myprogress:ProgressDialog
    override fun onPreExecute() {
        super.onPreExecute()

        myprogress = ProgressDialog(c)
        myprogress.setTitle("검색 중")
        myprogress.setMessage("Searching...Please wait")
        myprogress.show()
    }
    override fun doInBackground(vararg voids: Void): String {
        return getJSON()
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        myprogress.dismiss()
        if(result.startsWith("url error")){
            Toast.makeText(c, result, Toast.LENGTH_LONG).show()
            Toast.makeText(c, "url이 잘못되어 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
        }else if(result.startsWith("connect error")){
            Toast.makeText(c, result, Toast.LENGTH_LONG).show()
            Toast.makeText(c, "네트워크에 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(c, "네트워크 연결 성공", Toast.LENGTH_LONG).show()
            ShortInfo(c, result, myGridView).execute()
        }
    }
    private fun connect(myURL:String):Any{
        try{
            val url = URL(myURL)
            val con = url.openConnection() as HttpURLConnection

            con.requestMethod = "GET"
            con.connectTimeout = 15000
            con.readTimeout = 15000
            con.doInput = true

            return con

        }catch (e:MalformedURLException){
            e.printStackTrace()
            return "url Error" + e.message
        }catch (e:IOException){
            e.printStackTrace()
            return "connect Error" + e.message
        }
    }

    private fun getJSON(): String {
        val connection = connect(myURL)
        if (connection.toString().startsWith("Error")) {
            return connection.toString()
        }

        try {
            val con = connection as HttpURLConnection
            if (con.responseCode == 200) {
                val inputStreamReader = BufferedInputStream(con.inputStream)
                val bufferedReader = BufferedReader(InputStreamReader(inputStreamReader))

                val stringbuffer = StringBuffer()
                var line: String?

                while(true){
                    line = bufferedReader.readLine()
                    if(line == null) break
                    stringbuffer.append(line +"\n");
                }

                inputStreamReader.close()
                bufferedReader.close()

                return stringbuffer.toString()

            } else {
                return "Error " + con.responseMessage
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "Error " + e.message
        }
    }


}
