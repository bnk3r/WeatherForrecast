package yb.weatherforcast.presentation.fragment_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import yb.weatherforcast.R
import yb.weatherforcast.databinding.FragmentMainBinding
import yb.weatherforcast.domain.model.BusinessDataState.*
import yb.weatherforcast.presentation.fragment_main.data.MainFragmentState
import yb.weatherforcast.presentation.fragment_main.data.WeatherMainAdapterDay
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor() : Fragment() {

    @Inject
    lateinit var vimo: MainFragmentViewModel

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
        binding.resultListView.adapter = weatherDaysAdapter
        vimo.fragmentState.observe(viewLifecycleOwner) { state ->
            bindAppBar(state)
            bindHeader(state)
            bindSwipeToRefresh(state)
            bindNextFiveDays(state)
            bindErrorLayout(state)
        }
        vimo.getWeather("Rennes")
    }

    private fun bindAppBar(state: MainFragmentState) {
        requireActivity().title = state.currentCity ?: getString(R.string.app_name)
    }

    private fun bindHeader(state: MainFragmentState) {
        binding.includeWeatherHeader.root.visibility =
            if (state.getWeatherState == SUCCESS) View.VISIBLE
            else View.GONE
        binding.includeWeatherHeader.temperature.text = state.currentTempText
        state.currentTempIconUrl?.let { url ->
            Glide.with(binding.root.context)
                .load(url)
                .centerInside()
                .into(binding.includeWeatherHeader.icon)
        }
    }

    private fun bindSwipeToRefresh(state: MainFragmentState) {
        binding.swipeRefreshLayout.setOnRefreshListener { vimo.getWeather("Rennes") }
        binding.swipeRefreshLayout.visibility =
            if (state.getWeatherState == SUCCESS) View.VISIBLE
            else View.GONE
        binding.swipeRefreshLayout.isRefreshing = state.getWeatherState == LOADING
    }

    private fun bindNextFiveDays(state: MainFragmentState) {
        val nextFiveDays = state.nextFiveDays
        if (nextFiveDays.isNotEmpty() && nextFiveDays.size == 5) {
            weatherDaysAdapter.updateData(nextFiveDays.map { day ->
                WeatherMainAdapterDay(
                    day = day.dayOfTheWeek,
                    minTemp = day.temperatureMin,
                    maxTemp = day.temperatureMax,
                    description = day.periods[0].weatherDescription,
                    icon = day.weatherIconApiID
                )
            })
            binding.resultListView.visibility = View.VISIBLE
        } else {
            binding.resultListView.visibility = View.GONE
        }
    }

    private fun bindErrorLayout(state: MainFragmentState) {
        binding.errorLayout.root.visibility =
            if (state.getWeatherState == ERROR) View.VISIBLE
            else View.GONE
        binding.errorLayout.retryButton.setOnClickListener {
            vimo.getWeather("Rennes")
        }
        state.getWeatherErrorIconId?.let {
            binding.errorLayout.errorIcon.setImageDrawable(resources.getDrawable(it, null))
        }
        state.getWeatherErrorTitleId?.let {
            binding.errorLayout.errorTitle.text = resources.getString(it)
        }
        state.getWeatherErrorMessageId?.let {
            binding.errorLayout.errorMessage.text = resources.getString(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}