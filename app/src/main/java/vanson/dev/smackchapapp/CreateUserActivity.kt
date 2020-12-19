package vanson.dev.smackchapapp

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    var userAvatar = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]" //working with swift and mac
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
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

    }
}