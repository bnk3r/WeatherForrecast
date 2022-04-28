package yb.weatherforcast.views.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yb.weatherforcast.controllers.weather.formatCelcius
import yb.weatherforcast.databinding.ItemWeatherDayBinding
import yb.weatherforcast.models.ui.WeatherMainAdapterDay

class WeatherDaysAdapter : RecyclerView.Adapter<WeatherDaysAdapter.VH>() {

    inner class VH(private val binding: ItemWeatherDayBinding) : RecyclerView.ViewHolder(binding.root) {

        private val day: TextView = binding.day
        private val min: TextView = binding.min
        private val max: TextView = binding.max
        private val desc: TextView = binding.desc
        private val icon: ImageView = binding.icon

        fun bindDay(value: String) {
            day.text = value
        }

        fun bindMinTemp(value: Int) {
            val s = "min: ${value.formatCelcius()}"
            min.text = s
        }

        fun bindMaxTemp(value: Int) {
            val s = "max: ${value.formatCelcius()}"
            max.text = s
        }

        fun bindDesc(value: String) {
            desc.text = value
        }

        fun bindIcon(value: String) {
            Glide.with(binding.root.context)
                .load("http://openweathermap.org/img/wn/$value@2x.png")
                .centerInside()
                .into(icon)
        }
    }

    private var data = listOf<WeatherMainAdapterDay>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val bindingRow = ItemWeatherDayBinding.inflate(inflater, parent, false)
        return VH(bindingRow)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = data[position]
        holder.bindDay(item.day)
        holder.bindMinTemp(item.minTemp)
        holder.bindMaxTemp(item.maxTemp)
        holder.bindDesc(item.description)
        holder.bindIcon(item.icon)
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(new: List<WeatherMainAdapterDay>) {
        data = new
        notifyDataSetChanged()
    }

}