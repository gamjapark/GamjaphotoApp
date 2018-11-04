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
class getJSONINFO(
    private var c: Context, private var myURL: String, private var myGridView: GridView
) :
    AsyncTask<Void, Void, String>() {

    override fun onPreExecute() {
        super.onPreExecute()
        Toast.makeText(c, "검색 중", Toast.LENGTH_SHORT).show()
    }

    override fun doInBackground(vararg voids: Void): String? {
        return getJSON()
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        if (result.startsWith("url e")) {
            Toast.makeText(c, result, Toast.LENGTH_LONG).show()
            Toast.makeText(c, "urlnox이 잘못되어 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
        } else if (result.startsWith("connect e")) {
            Toast.makeText(c, result, Toast.LENGTH_LONG).show()
            Toast.makeText(c, "네트워크에 연결할 수 없습니다.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(c, "네트워크 연결 성공", Toast.LENGTH_LONG).show()
            ShortInfo(c, result, myGridView).execute()
        }
    }

    private fun connect(myURL: String): Any {
        try {
            val url = URL(myURL)
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

    private fun getJSON(): String? {

        val connection = connect(myURL)
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
