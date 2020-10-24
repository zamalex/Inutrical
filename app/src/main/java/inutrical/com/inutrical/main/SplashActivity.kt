package inutrical.com.inutrical.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import inutrical.com.inutrical.R
import java.util.*

class SplashActivity : AppCompatActivity() {

    lateinit var timer:Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        timer = Timer()


        timer.schedule(object :TimerTask(){
            override fun run() {
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                finish()
            }
        },3000)
    }
}
