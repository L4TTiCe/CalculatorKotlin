package io.github.l4ttice.calculatorkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DBG","In SplashActivity");
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
