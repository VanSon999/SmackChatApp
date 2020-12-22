package vanson.dev.smackchapapp.Controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*
import vanson.dev.smackchapapp.R
import vanson.dev.smackchapapp.Services.AuthService

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBarLogin.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        if(AuthService.isLoggedIn){
            finish()
        }
    }

    fun loginCreateUserBtnClicked(view : View){
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
    }

    fun loginLoginBtnClicked(view: View){
        enableSpinner(true)
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyBoard()
        if(email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        }else{
            Toast.makeText(this, "Make sure user email and password are filled in.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun errorToast(){
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
    fun enableSpinner(enable: Boolean){
        if(enable){
            progressBarLogin.visibility = View.VISIBLE
        }else{
            progressBarLogin.visibility = View.INVISIBLE
        }
        loginEmailText.isEnabled = !enable
        loginPasswordText.isEnabled = !enable
        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    fun hideKeyBoard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }
}