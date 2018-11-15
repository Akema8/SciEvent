package com.example.margarita.scievent.ShowEvents

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.margarita.scievent.R
import com.example.margarita.scievent.gone
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.visible
import kotlinx.android.synthetic.main.item_event.view.*

//Адаптер секции для простого просмотра
class SectionAdapter(val sections: MutableList<mEvent>,
                     private val itemListener: (mEvent) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val bundle = Bundle()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.item_event, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = sections.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ItemViewHolder -> {
            holder.bind(position)
        }
        else -> throw IllegalStateException("bad holder")
    }


    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
        }

        fun bind(position: Int) = with(itemView) {
            val mevent = sections[position]
            if (mevent != null) {
                tvNameEvent.visible()
                tvNameEvent.text = mevent.name
                tvDateEvent.visibility = View.GONE

                if (mevent.sections != null) {
                    itemView.setOnClickListener(this@ItemViewHolder)
                } else {
                    itemView.setOnClickListener({ itemListener(mevent) })
                }
            } else itemView.gone()

        }

        var fl: Boolean = false

        override fun onClick(v: View?) {
            itemView.llContainerAllEventItem.removeAllViews()

            val llSect = LinearLayout(itemView.context)
            if (!fl) {

                llSect.visible()
                Toast.makeText(itemView.context, "у секции есть подсекции", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "ClickItem")

                llSect.orientation = LinearLayout.VERTICAL
                llSect.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                val tvDescriptionSection = TextView(itemView.context)
                tvDescriptionSection.textSize = 20f
                tvDescriptionSection.text = "Описание: " + sections[position].description
                tvDescriptionSection.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                llSect.addView(tvDescriptionSection)
                //   itemView.llContainerAllEventItem.addView(tvDescriptionSection)

                val rv = RecyclerView(itemView.context)
                rv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                llSect.addView(rv)

                if (sections[position].schedule != null) {
                    rv.layoutManager = LinearLayoutManager(itemView.context)
                    rv.adapter = ScheduleAdapter(sections[position].schedule!!)
                }

                for (ev in sections[position].sections!!) {
                    // create a textview
                    val textView = TextView(itemView.context)
                    textView.textSize = 17f
                    textView.text = ev.name
                    textView.setOnClickListener({ itemListener(ev) })
                    textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    llSect.addView(textView)
                    // itemView.llContainerAllEventItem.addView(textView)

                }

                itemView.llContainerAllEventItem.addView(llSect)
            } else {
                llSect.gone()

            }
            fl = !fl
        }
    }
}