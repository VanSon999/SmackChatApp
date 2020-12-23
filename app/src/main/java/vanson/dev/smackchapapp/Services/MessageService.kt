package vanson.dev.smackchapapp.Services

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import vanson.dev.smackchapapp.Controller.App
import vanson.dev.smackchapapp.Model.Channel
import vanson.dev.smackchapapp.Utilities.URL_GET_CHANNELS
import java.util.ArrayList

object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannels(complete: (Boolean) -> Unit){
        val channelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->
            try{
                for( x in 0 until response.length()){
                    val channel = response.getJSONObject(x)
                    this.channels.add(Channel(channel.getString("name"), channel.getString("description"), channel.getString("_id")))
                }
                complete(true)
            }catch (e: JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener {error ->
            Log.d("ERROR", "Could not get channels: $error")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                return  return mutableMapOf<String,String>("Authorization" to "Bearer ${App.prefs.authToken}")
            }
        }
        channelsRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        App.prefs.requestQueue.add(channelsRequest)
    }
}