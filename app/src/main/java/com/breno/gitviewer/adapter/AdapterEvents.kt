package com.breno.gitviewer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breno.gitviewer.helper.HelperEvents
import com.breno.gitviewer.helper.ModelEventAdapter
import com.breno.gitviewer.databinding.ItemEventsBinding
import com.breno.gitviewer.model.Event

class AdapterEvents(val context: Context) : RecyclerView.Adapter<AdapterEvents.EventsViewHolder>() {

    var list = mutableListOf<Event>()
    private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.bind(list[position].actor.display_login, HelperEvents().verifyEvent(list[position].type))
    }

    override fun getItemCount() = list.size

    inner class EventsViewHolder(private val binding: ItemEventsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String, event: ModelEventAdapter) {
            binding.typeEvents.text = context.getString(event.message)
            binding.imgEvents.setImageResource(event.image)
            binding.cardEvents.setCardBackgroundColor(ContextCompat.getColor(context, event.color))
            binding.nameUserEvents.text = name
        }

    }

    fun setList(list: List<Event>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}