package com.waracle.androidtest

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class CakeViewModel : ViewModel() {

    //Replaced JSON with a different one containing up-to-date images
    companion object {
        private const val JSON_URL = "https://gist.githubusercontent.com/plweegie/29869f02bad10191661f073f87e1a8ea/" +
                "raw/f48a71dd10073ebd8dee97f689bb0210c3fb5cc2/cake.json"
    }

    private var cakes: MutableLiveData<List<Cake>>? = null

    fun getCakes(): LiveData<List<Cake>>? {
        if (cakes == null) {
            cakes = MutableLiveData()
            CakeLoaderTask().execute(JSON_URL)
        }
        return cakes
    }

    inner class CakeLoaderTask : AsyncTask<String, Void, List<Cake>>() {
        override fun doInBackground(vararg urls: String?): List<Cake> {
            return loadData(urls[0]!!)
        }

        override fun onPostExecute(result: List<Cake>?) {
            cakes?.value = result
        }

        @Throws(IOException::class, JSONException::class)
        private fun loadData(url: String): List<Cake> {
            val urlConnection = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Cache-Control", "private, max-age=86400")
            }

            try {
                val inputStream = BufferedInputStream(urlConnection.inputStream)

                // Can you think of a way to improve the performance of loading data
                // using HTTP headers???

                // Also, Do you trust any utils thrown your way????
                val bytes = StreamUtils.readUnknownFully(inputStream)
                val charset = StreamUtils.parseCharset(urlConnection.getRequestProperty("Content-Type"))
                val jsonText = String(bytes, Charset.forName(charset))

                return convertJSONResponse(JSONArray(jsonText))
            } finally {
                urlConnection.disconnect()
            }
        }

        private fun convertJSONResponse(jsonArray: JSONArray): List<Cake> {

            val cakes: MutableList<Cake> = mutableListOf()
            for (i in 0 until jsonArray.length()) {
                cakes.add(jsonArray.getJSONObject(i).toCake())
            }
            return cakes
        }

        private fun JSONObject.toCake(): Cake = Cake(
                getString("title"),
                getString("desc"),
                getString("image")
        )
    }
}