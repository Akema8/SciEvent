package com.example.margarita.scievent.ShowEvents

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.R
import com.example.margarita.scievent.sampledata.mEvent
import kotlinx.android.synthetic.main.item_event.view.*
import java.util.*
import android.widget.TextView
import com.example.margarita.scievent.sampledata.mapEvent


class AllEventsAdapter(val allEvent: TreeMap<Long, MutableList<mapEvent>>,
        private val itemListener: (mapEvent) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val bundle = Bundle()

    var mKeys = allEvent.keys

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.item_event, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = allEvent.size

    fun updateAllMessages(allEvent: MutableList<mEvent>) {
        notifyDataSetChanged()
    }


    fun getKey(position: Int): Long {
        return mKeys.elementAt(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ItemViewHolder -> {
            holder.bind(allEvent.get(getKey(position))!!)//modelList.get(position))
        }
        else -> throw IllegalStateException("bad holder")
    }

    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        fun bind(lstEventMap: MutableList<mapEvent>) = with(itemView) {

            val dateAndTime = Calendar.getInstance()

            val t = DateUtils.formatDateTime(context,
                    dateAndTime.timeInMillis,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
            val lstEvent=lstEventMap[0].event
         //   if (dateAndTime.timeInMillis<lstEvent.date)
           //     itemView.background
            tvDateEvent.text = DateUtils.formatDateTime(context,
                    lstEvent!!.date,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
            llContainerAllEventItem.isBaselineAligned=true

            for (ev in lstEventMap) {
                val textView = TextView(context)
                textView.textSize = 20f

                textView.text = ev.event.name
                textView.setOnClickListener({itemListener(ev)})
                textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                // adds the textview
                llContainerAllEventItem.addView(textView)
            }
        }

    }
}