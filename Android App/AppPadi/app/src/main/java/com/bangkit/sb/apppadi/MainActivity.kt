package com.bangkit.sb.apppadi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.bangkit.sb.apppadi.customview.ViewButton
import com.bangkit.sb.apppadi.databinding.ActivityMainBinding
import com.bangkit.sb.apppadi.scanning.ScanActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var button: ViewButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        button = binding.logButton

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setAnimation()

        binding.logButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ScanActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun setAnimation() {
        val textView =ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val btn = ObjectAnimator.ofFloat(binding.logButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(textView, btn)
            start()
        }
    }


}