package vanson.dev.smackchapapp.Controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_user.*
import vanson.dev.smackchapapp.R
import vanson.dev.smackchapapp.Services.AuthService
import vanson.dev.smackchapapp.Utilities.BROADCAST_USER_DATA_CHANGE
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    var userAvatar = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]" //working with swift and mac
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        progressBarCreateUser.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if(color == 0){
            userAvatar = "light$avatar"
        }else{
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatar.setImageResource(resourceId)
    }

    fun generateBackgroundColorBtnClicked(view: View){
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        createAvatar.setBackgroundColor(Color.rgb(r,g,b))

        avatarColor = "[${r.toDouble()/255}, ${g.toDouble()/255}, ${b.toDouble()/255}, 1]"
    }

    fun signUpBtnClicked(view: View){
        enableSpinner(true)
        val email = createEmail.text.toString()
        val password = createPassword.text.toString()
        val userName = createUserName.text.toString()

        if(userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
            AuthService.registerUser( email, password){registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser( email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(userName,email,userAvatar, avatarColor){complete ->
                                if(complete){
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE) //Create broadcast to notify other activity
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableSpinner(false)
                                    finish() // to Stop current activity
                                }else{
                                    errorToast()
                                }
                            }
                        }else{
                            errorToast()
                        }
                    }
                }else{
                    errorToast()
                }
            }
        }else{
            Toast.makeText(this, "Make sure user name, email and password are filled in.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }

    }

    fun errorToast(){
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
    fun enableSpinner(enable: Boolean){
        if(enable){
            progressBarCreateUser.visibility = View.VISIBLE
        }else{
            progressBarCreateUser.visibility = View.INVISIBLE
        }
        createPassword.isEnabled = !enable
        createEmail.isEnabled = !enable
        createUserName.isEnabled = !enable
        signUpBtn.isEnabled = !enable
        createAvatar.isEnabled = !enable
        generateBackgroundColorBtn.isEnabled = !enable
    }
}