package vanson.dev.smackchapapp.Services

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import vanson.dev.smackchapapp.Utilities.URL_LOGIN
import vanson.dev.smackchapapp.Utilities.URL_REGISTER


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

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    fun registerUser(
        context: Context,
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
        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
                try {
                    userEmail = response.getString("user")
                    authToken = response.getString("token")
                    isLoggedIn = true
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    complete(false)
                }

            }, Response.ErrorListener { error ->
                // this is where we deal with our error
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
        loginRequest.retryPolicy = DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        Volley.newRequestQueue(context).add(loginRequest)
    }
}