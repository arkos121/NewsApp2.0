package com.example.newp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


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

//    fun updateData(newItems: List<CardItem>) {
//        items.clear()
//        items.addAll(newItems)
//        notifyDataSetChanged()
//    }
    fun updateData(newItems:WeatherResponse?){
        newItems?.let { item ->
            items.clear()
            Log.d("Adap", "updateData: ${item.name}")
            items.add(CardItem("City", item.name))
            items.add(CardItem("Temperature", "${item.main.tempInCelsius()}°C"))
            items.add(CardItem("Humidity", "${item.main.humidity}%"))
            items.add(CardItem("Wind Speed", "${item.wind.speed} m/s"))
            items.add(CardItem("SOMETHING","${item.weather[0].description}"))

        }
    }

}