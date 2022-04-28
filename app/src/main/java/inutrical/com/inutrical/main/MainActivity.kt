package inutrical.com.inutrical.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dmax.dialog.SpotsDialog
import inutrical.com.inutrical.Property
import inutrical.com.inutrical.R
import inutrical.com.inutrical.api.LocalData
import inutrical.com.inutrical.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    var result = MutableLiveData<Property>()

    lateinit var loading : android.app.AlertDialog

    lateinit var uname:TextView
    lateinit var umail:TextView

    /* val fragmentStack: FragmentStack by lazy {
         FragmentStack(
             this,
             supportFragmentManager,
             R.id.cont
         )
     }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(my_toolbar)

        binding.lifecycleOwner = this


        binding.data = result

        uname = binding.root.findViewById(R.id.username)
        umail = binding.root.findViewById(R.id.email)

       loading = SpotsDialog.Builder().setContext(this).build()

        supportActionBar?.title = ""
        drawer = findViewById(R.id.drawer_layout)
        toggle =
            ActionBarDrawerToggle(this, drawer, my_toolbar, R.string.app_name, R.string.app_name)

        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        back_btn.setOnClickListener {
            onBackPressed()
        }

        /*fragmentStack.push(LoginFragment())
        fragmentStack.push(ForgotPasswordFragment())
        fragmentStack.push(ResetPasswordFragment())
        fragmentStack.push(MainFragment())
        fragmentStack.push(CalculateFragment())
*/

        logout.setOnClickListener {
            LocalData.saveUser(null,null,null,this)
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


    }

    fun setData(name:String,mail:String){

        uname.text = name
        umail.text = mail
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    fun changeToolbar(boolean: Boolean) {
        if (boolean) {
            my_toolbar.setBackgroundColor(resources.getColor(R.color.mblue))
            back_btn.setImageResource(R.drawable.backwhite)
        } else {
            my_toolbar.setBackgroundColor(resources.getColor(R.color.mback))
            back_btn.setImageResource(R.drawable.back)
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // toggle.syncState()
        //my_toolbar.setNavigationIcon(android.R.drawable.ic_menu_today)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun showToggle(show: Boolean) {
        if (show) {
            my_toolbar.setNavigationIcon(R.drawable.toggle)
            back_btn.visibility = View.GONE
        } else {
            my_toolbar.setNavigationIcon(null)
            back_btn.visibility = View.VISIBLE

        }
    }

    fun hideKeybaord(v: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    fun showProgress(show: Boolean) {
        if (show)
           loading.show()
        else
            loading.dismiss()

    }


}
