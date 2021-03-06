package vanson.dev.smackchapapp.Services

import android.graphics.Color
import vanson.dev.smackchapapp.Controller.App
import java.util.*

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun returnAvatarColor(color: String): Int{
        var r = 0
        var g = 0
        var b = 0
//        val strippedColor = color.replace("[","").replace("]","").replace(",", "")
        val strippedColor = color.replace(",|\\[|\\]".toRegex(), "")
        val scanner = Scanner(strippedColor).useLocale(Locale.US)
        if(scanner.hasNext()){
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r,g,b)
    }

    fun logOut(){
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
        MessageService.clearChannels()
        MessageService.clearMessages()
    }
}