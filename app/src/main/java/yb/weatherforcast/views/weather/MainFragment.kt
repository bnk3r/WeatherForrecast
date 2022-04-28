package yb.weatherforcast.views.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import yb.weatherforcast.R
import yb.weatherforcast.controllers.vimo.ActivityScopedWeatherVimo
import yb.weatherforcast.controllers.weather.formatCelsius
import yb.weatherforcast.databinding.FragmentMainBinding
import yb.weatherforcast.models.business.Weather
import yb.weatherforcast.models.business.WeatherError
import yb.weatherforcast.models.ui.WeatherData
import yb.weatherforcast.models.ui.extractMainAdatperDays
import yb.weatherforcast.views.main.IWeatherDisplayerContract
import yb.weatherforcast.views.main.WeatherDaysAdapter
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor() : Fragment(), IWeatherDisplayerContract.View {

    @Inject
    lateinit var vimo: ActivityScopedWeatherVimo

    @Inject
    lateinit var weatherDaysAdapter: WeatherDaysAdapter

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.result.adapter = weatherDaysAdapter

        vimo.weatherData.observe(viewLifecycleOwner) {
            handleWeatherData(it)
        }
        launchNewWeatherSearch()

        binding.swipeRefreshLayout.setOnRefreshListener {
            launchNewWeatherSearch()
        }

        binding.networkIssueLayout.refreshButton.setOnClickListener {
            launchNewWeatherSearch()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showProgress(visible: Boolean) {
        if (visible) {
            binding.includeWeatherHeader.root.visibility = View.GONE
            binding.swipeRefreshLayout.visibility = View.GONE
            binding.networkIssueLayout.root.visibility = View.GONE
        }
        binding.swipeRefreshLayout.isRefreshing = visible
    }

    override fun showWeather(weather: Weather?) {
        weather?.let {
            binding.includeWeatherHeader.root.visibility = View.VISIBLE
            binding.swipeRefreshLayout.visibility = View.VISIBLE
            binding.includeWeatherHeader.temperature.text = it.currentTemperature.formatCelsius()
            Glide.with(binding.root.context)
                .load("http://openweathermap.org/img/wn/${it.currentTemperatureIcon}@2x.png")
                .centerInside()
                .into(binding.includeWeatherHeader.icon)
            weatherDaysAdapter.updateData(it.extractMainAdatperDays())
            if (it.days.isEmpty()) {
                binding.result.visibility = View.GONE
            } else {
                binding.result.visibility = View.VISIBLE
            }
        }
    }

    override fun showError(error: WeatherError?) {
        error?.let {
            when (error.type) {
                WeatherError.Type.VOLLEY -> {
                    binding.networkIssueLayout.root.visibility = View.VISIBLE
                }
                else -> {
                    // TODO Better error handling
                    Toast.makeText(requireContext(), error.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun showCityName(city: String?, progress: Boolean) {
        if (city != null) {
            val cityName = city.uppercase()
            requireActivity().title = cityName
        } else {
            requireActivity().title =
                if (progress) getString(R.string.app_name)
                else getString(R.string.network_issue_title)
        }
    }

    private fun handleWeatherData(data: WeatherData) {
        showProgress(data.progress)
        showCityName(data.weather?.city?.name, data.progress)
        showWeather(data.weather)
        showError(data.error)
    }

    fun launchNewWeatherSearch() {
        vimo.findWeather()
    }

}