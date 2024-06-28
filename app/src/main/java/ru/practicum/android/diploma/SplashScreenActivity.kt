package ru.practicum.android.diploma

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.root.ui.RootActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(TIME_SPLASH_SCREEN)
            val intent = Intent(this@SplashScreenActivity, RootActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val TIME_SPLASH_SCREEN = 2000L
    }
}
