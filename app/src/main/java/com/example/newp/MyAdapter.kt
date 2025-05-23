package com.example.newp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newp.apis.WeatherResponse
import java.util.Locale


class MyAdapter(private var items: MutableList<CardItem>) :

    RecyclerView.Adapter<MyAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewtype, parent, false)
        return CardViewHolder(view)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titletextview = itemView.findViewById<TextView>(R.id.titleTextView)
        val descriptiontextview = itemView.findViewById<TextView>(R.id.descriptionTextView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.titletextview.text = item.title
        holder.descriptiontextview.text = item.description
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: WeatherResponse?, state: String, petrol: String, diesel: String) {
        Log.d("Adap", "updateData: ${newItems?.name}")
        newItems?.let { item ->
            items.clear()
            Log.d("Adap", "updateData: ${item.name}")
            items.add(CardItem("State",state))
            items.add(CardItem("City", item.name))
            items.add(CardItem("Petrol Price",petrol?:""))
            items.add(CardItem("Diesel Price",diesel?:""))
            items.add(CardItem("Temperature", "${item.main.tempInCelsius(item.main.temp)}°C"))
            items.add(CardItem("Feels Like", "${item.main.tempInCelsius(item.main.feels_like)}°C"))
            items.add(CardItem("Description", item.weather[0].description.uppercase(Locale.ROOT)))
            items.add(CardItem("Humidity", "${item.main.humidity}%"))
            items.add(CardItem("Pressure", "${item.main.pressure} hPa"))
            items.add(CardItem("Wind Speed", "${item.wind.speed} m/s"))
            items.add(CardItem("Wind Direction", "${item.wind.deg}°"))
            // Add more weather data if needed
            items.add(CardItem("Max Temp", "${item.main.tempInCelsius(item.main.temp_max)}°C"))
            items.add(CardItem("Min Temp", "${item.main.tempInCelsius(item.main.temp_min)}°C"))
            notifyDataSetChanged()

        }
    }

//    fun setData() {
//        items.clear()
//        items.addAll(
//            listOf(
//        CardItem("State", ""),
//        CardItem("City", ""),
//        CardItem("Petrol Price", ""),
//        CardItem("Diesel Price", ""),
//        CardItem("Temperature", ""),
//        CardItem("Feels Like", ""),
//        CardItem("Description", ""),
//        CardItem("Humidity", ""),
//        CardItem("Pressure", ""),
//        CardItem("Wind Speed", ""),
//        CardItem("Wind Direction", ""),
//        CardItem("Max Temp", ""),
//        CardItem("Min Temp", "")))
//        notifyDataSetChanged()
//    }
}