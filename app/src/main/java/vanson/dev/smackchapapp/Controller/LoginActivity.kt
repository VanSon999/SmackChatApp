package vanson.dev.smackchapapp.Controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import vanson.dev.smackchapapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginCreateUserBtnClicked(view : View){
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
    }

    fun loginLoginBtnClicked(view: View){

    }
}