package com.example.margarita.scievent.MyEventEdit

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.App
import com.example.margarita.scievent.R
import com.example.margarita.scievent.gone
import com.example.margarita.scievent.sampledata.mapSectionId
import com.example.margarita.scievent.sampledata.mapSections
import com.example.margarita.scievent.visible
import kotlinx.android.synthetic.main.item_event.view.*

class EditSectionAdapter(val sectionsWithIdEvent: mapSections, //idEventWithSectionPosition события и список секций
                         private val itemListener: (mapSectionId) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val bundle = Bundle()
    val EVENT_KEY = "event_key"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.item_event, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ItemViewHolder -> {
            holder.bind(position)
            //  bundle.putSerializable(EVENT_KEY, sections[position])
        }
        else -> throw IllegalStateException("bad holder")
    }


    override fun getItemCount() = sectionsWithIdEvent.sections!!.size

    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            //itemView.setOnClickListener(this)

        }

        fun bind(position: Int) = with(itemView) {
            val sectionThisEvent = sectionsWithIdEvent.sections!![position]
            if (sectionThisEvent != null) {
                tvNameEvent.visible()
                tvNameEvent.text = sectionThisEvent.name
                tvDateEvent.visibility = View.GONE

                itemView.setOnClickListener({ itemListener(mapSectionId(sectionsWithIdEvent.id, sectionThisEvent, position, null)) })
                btRemoveItem.visible()
                btRemoveItem.setOnClickListener {

                    App.myRef.getReference("event").child(sectionsWithIdEvent.id).child("sections").child(position.toString()).removeValue()
                    sectionsWithIdEvent.sections.removeAt(position)
                    notifyDataSetChanged()
                }
            }
            else itemView.gone()


        }

        override fun onClick(v: View?) {

        }
    }

}