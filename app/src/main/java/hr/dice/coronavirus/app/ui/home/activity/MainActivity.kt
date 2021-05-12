package hr.dice.coronavirus.app.ui.home.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.dice.coronavirus.app.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_CoronavirusApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
