package com.example.tf2servers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tf2servers.R
import com.example.tf2servers.databinding.CardViewBinding
import com.example.tf2servers.models.ServerInfo
import com.example.tf2servers.screens.ListFragment


class ListAdapter(private val fragment: ListFragment): RecyclerView.Adapter<ListAdapter.ListHolder>() {
    private val listInfo = ArrayList<ServerInfo>()
    class ListHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = CardViewBinding.bind(item)
        fun bind(new: ServerInfo) = with(binding){
            if (new.game == "VS Saxton Hale (1.55)") gameImage.setImageResource(R.drawable.saxton)
            else gameImage.setImageResource(R.drawable.tf2)
            tvName.text = new.serverName
            tvMap.text = "Map: ${new.mapName}"
            tvPlayers.text = "Players: ${new.playerCount}/${new.maxPlayers}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return ListHolder(view)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bind(listInfo[position])
        holder.binding.delete.setOnClickListener {
            listInfo.remove(listInfo[position])
            fragment.deleteListener(listInfo)
            notifyDataSetChanged()
        }
        holder.binding.alert.setOnClickListener {
            fragment.alertListener(listInfo[position])
        }
    }

    override fun getItemCount(): Int {
        return listInfo.size
    }
    fun addInfo(info: ServerInfo){
        listInfo.add(info)
        notifyDataSetChanged()
    }

    fun clear() {
        listInfo.clear()
        notifyDataSetChanged()
    }
}