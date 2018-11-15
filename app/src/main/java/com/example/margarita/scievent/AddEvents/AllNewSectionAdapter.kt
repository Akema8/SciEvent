package com.example.margarita.scievent.AddEvents

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.margarita.scievent.R
import com.example.margarita.scievent.gone
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mapEvent
import com.example.margarita.scievent.sampledata.mapSectionId
import com.example.margarita.scievent.sampledata.mapSections
import com.example.margarita.scievent.transaction
import com.example.margarita.scievent.visible
import kotlinx.android.synthetic.main.all_event_fragment.view.*
import kotlinx.android.synthetic.main.item_event.view.*

class AllNewSectionAdapter(
        val msect: mapSections,
        private val itemListener: (Array<String>) -> Unit,
        private val itemListener2: (mapSectionId) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val bundle = Bundle()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.item_event, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = msect.sections!!.size

    fun updateAllMessages(allEvent: MutableList<mEvent>) {
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ItemViewHolder -> {
            holder.bind(position)
        }
        else -> throw IllegalStateException("bad holder")
    }


    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {


        fun bind(position: Int) = with(itemView) {

            val mevent = msect.sections!![position]
            tvNameEvent.visible()
            tvNameEvent.text = mevent.name
            tvDateEvent.gone()
            var fl = true

            val linearLayout = LinearLayout(itemView.context)
            itemView.setOnClickListener {
                if (fl) {
                    linearLayout.visible()
                    llContainerAllEventItem.removeAllViews()
                    linearLayout.removeAllViews()
                    linearLayout.orientation = LinearLayout.VERTICAL
                    linearLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    val bt1 = Button(itemView.context)
                    bt1.text = "Добавить расписание"
                    bt1.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    val bt2 = Button(itemView.context)
                    bt2.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    bt2.text = "Добавить подсекцию"
                    linearLayout.addView(bt1)
                    linearLayout.addView(bt2)

                    bt1.setOnClickListener {
                        val t = arrayOf(msect.id, position.toString())
                        itemListener(t)
                    }

                    bt2.setOnClickListener {

                        itemListener2(mapSectionId(msect.id, msect.sections[position], position, null))

                    }
                    llContainerAllEventItem.addView(linearLayout)
                } else
                    linearLayout.gone()
                fl = !fl
            }
        }

    }
}