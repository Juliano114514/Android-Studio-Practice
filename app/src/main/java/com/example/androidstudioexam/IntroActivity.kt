package com.example.androidstudioexam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.androidstudioexam.chapter3.CalculatorActivity

class IntroActivity : AppCompatActivity() {

  val btn3: Button by lazy { findViewById(R.id.intro_btn3) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.intro_layout)
    initListener()
  }

  private fun initListener(){
    btn3.setOnClickListener { startActivity(Intent(this, CalculatorActivity::class.java)) }
  }

}