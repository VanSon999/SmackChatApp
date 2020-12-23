package vanson.dev.smackchapapp.Controller

import android.app.Application
import vanson.dev.smackchapapp.Utilities.SharedPrefs

//Global class, be init before any thing activity initialize, to prepare data for app
// Can used any where
class App : Application(){
    companion object{
        lateinit var prefs : SharedPrefs
    }
    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}