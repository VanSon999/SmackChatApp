package vanson.dev.smackchapapp.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import vanson.dev.smackchapapp.Controller.App
import vanson.dev.smackchapapp.Utilities.*


//import android.content.Context
//import android.util.Log
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
//import org.json.JSONObject
//import vanson.dev.smackchapapp.Utilities.URL_REGISTER

//object AuthService {
//    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){
//        val url = URL_REGISTER
//        val jsonBody = JSONObject()
//        jsonBody.put("email", email)
//        jsonBody.put("password", password)
//        val requestBody = jsonBody.toString()
//
//        val registerRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { _ -> complete(true) }, Response.ErrorListener{
//            error -> Log.d("ERROR", "Could not register user: $error")
//            complete(false)
//        }){
//            override fun getBodyContentType():String{
//                return "application/json; charset=utf-8"
//            }
//
//            override fun getBody(): ByteArray {
//                return requestBody.toByteArray()
//            }
//        }
//
//        Volley.newRequestQueue(context).add(registerRequest)
//    }
//}

object AuthService {

//    var isLoggedIn = false
//    var userEmail = ""
//    var authToken = ""

    fun registerUser(
        email: String,
        password: String,
        complete: (Boolean) -> Unit
    ) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest =
            object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
                println(response)
                complete(true)
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not register user: $error")
                complete(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }
        registerRequest.retryPolicy = DefaultRetryPolicy( //Handle Volley send multiple request when slow connection!
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        App.prefs.requestQueue.add(registerRequest)
    }

    fun loginUser(email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
                try {
                    App.prefs.userEmail = response.getString("user")
                    App.prefs.authToken = response.getString("token")
                    App.prefs.isLoggedIn = true
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    complete(false)
                }

            }, Response.ErrorListener { error ->
                // this is where we deal with our error
                Log.d("ERROR", "Could not login user: $error")
                complete(false)
            }) {

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }
        loginRequest.retryPolicy = DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        App.prefs.requestQueue.add(loginRequest)
    }

    fun createUser(name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor",avatarColor)
        val requestBody = jsonBody.toString()

        val createRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                complete(true)
            }catch (e: JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not add user: $error")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf<String,String>("Authorization" to "Bearer ${App.prefs.authToken}")
            }
        }

        createRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        App.prefs.requestQueue.add(createRequest)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit){
        val findUserRequest = object : JsonObjectRequest(Method.GET, "$URL_FIND_USER_BY_EMAIL${App.prefs.userEmail}", null, Response.Listener {response ->
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            }catch (e: JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener{error ->
            Log.d("ERROR", "Could find user: $error")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf<String,String>("Authorization" to "Bearer ${App.prefs.authToken}")
            }
        }
        findUserRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        App.prefs.requestQueue.add(findUserRequest)
    }
}
