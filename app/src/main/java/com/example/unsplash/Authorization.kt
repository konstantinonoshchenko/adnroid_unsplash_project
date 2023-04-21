package com.example.unsplash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.unsplash.utils.Constants.KEY_CLICK

class Authorization : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorization)
        val button = findViewById<Button>(R.id.buttonAuthorization)

        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(KEY_CLICK,true)
            startActivity(intent)
            finish()
        }
    }
}