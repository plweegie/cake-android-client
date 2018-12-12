package com.waracle.androidtest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset


class MainFragment : Fragment() {

    private lateinit var adapter: CakeAdapter

    companion object {
        private val TAG = MainFragment::class.java.simpleName
        private const val JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
                "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CakeAdapter().also {
            main_recyclerview.adapter = it
        }

        val adapterData = loadData()
        adapter.data.addAll(adapterData)
    }

    @Throws(IOException::class, JSONException::class)
    private fun loadData(): List<Cake> {
        val urlConnection = URL(JSON_URL).openConnection() as HttpURLConnection

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