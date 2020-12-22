package vanson.dev.smackchapapp.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.nav_header_main.*
import vanson.dev.smackchapapp.R
import vanson.dev.smackchapapp.Services.AuthService
import vanson.dev.smackchapapp.Services.UserDataService
import vanson.dev.smackchapapp.Utilities.BROADCAST_USER_DATA_CHANGE

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val userDataChangeReceiver = object : BroadcastReceiver(){ //receiver broadcast
        override fun onReceive(context: Context?, intent: Intent?) {
            if(AuthService.isLoggedIn){
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                userImageNavHeader.setImageResource(resources.getIdentifier(UserDataService.avatarName, "drawable", packageName))
                loginBtnNavHeader.text = "Logout"
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_gallery,
            R.id.nav_slideshow
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter( //register listen broadcast
            BROADCAST_USER_DATA_CHANGE))
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun loginBtnNavHeaderClicked(view: View){
        if(AuthService.isLoggedIn){
            UserDataService.logOut()
            userNameNavHeader.text = "Please login!"
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            loginBtnNavHeader.text = "Login"
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
        }else{
            var loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelClicked(view: View){
        if(AuthService.isLoggedIn){
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_diag, null)

            builder.setView(dialogView)
                .setPositiveButton("Add"){ dialogInterface, i ->
                    val name = dialogView.findViewById<EditText>(R.id.addChannelNameText)
                    val description = dialogView.findViewById<EditText>(R.id.addChannelDescriptionText)
                    val channelName = name.text.toString()
                    val descripChannel = description.text.toString()

                }
                .setNegativeButton("Cancel"){dialogInterface, i ->

                }
                .show()
        }else{
            Toast.makeText(this,"You need log in to do that!",Toast.LENGTH_LONG).show()
        }
    }

    fun sendMessageBtnClicked(view: View){
        hideKeyBoard()
        Log.d("Bug", "Dau xanh_3")
    }

    private fun hideKeyBoard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }
}