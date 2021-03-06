package com.example.margarita.scievent.MyEventEdit

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.R
import com.example.margarita.scievent.sampledata.mapSectionId
import com.example.margarita.scievent.sampledata.mapSections
import com.example.margarita.scievent.visible
import kotlinx.android.synthetic.main.item_event.view.*

class EditSubSectionAdapter(val sectionsWithIdEvent: mapSectionId,
                            private val itemListener: (mapSectionId) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.item_event, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = sectionsWithIdEvent.event.sections!!.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ItemViewHolder -> {
            holder.bind(position)
            //  bundle.putSerializable(EVENT_KEY, sections[position])
        }
        else -> throw IllegalStateException("bad holder")
    }

    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            //itemView.setOnClickListener(this)
        }

        fun bind(position: Int) = with(itemView) {
            val sectionThisEvent = sectionsWithIdEvent.event.sections!![position]
            tvNameEvent.visible()
            tvNameEvent.text = sectionThisEvent.name
            tvDateEvent.visibility = View.GONE
            // itemView.setOnClickListener(this@ItemViewHolder)
            itemView.setOnClickListener({ itemListener(mapSectionId(sectionsWithIdEvent.id, sectionThisEvent, sectionsWithIdEvent.position, position)) })
        }

        override fun onClick(v: View?) {
        }
    }
}

