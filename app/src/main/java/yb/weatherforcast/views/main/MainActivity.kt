package yb.weatherforcast.views.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import yb.weatherforcast.R
import yb.weatherforcast.databinding.ActivityMainBinding
import yb.weatherforcast.views.weather.MainFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(
) : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                launchNewWeatherSearch()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun launchNewWeatherSearch() {
        (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as MainFragment).launchNewWeatherSearch()
    }


}