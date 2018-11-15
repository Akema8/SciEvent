package com.example.margarita.scievent.MyEventEdit

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.App
import com.example.margarita.scievent.R
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mapEvent
import com.example.margarita.scievent.visible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_event.view.*

class AllEventsAdapterId(//val allEvent: MutableList<String>,
        val allEvent: HashMap<String, mEvent>,
        private val itemListener: (mapEvent) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val bundle = Bundle()

    var mKeys = allEvent.keys

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.item_event, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = allEvent.size


    fun getKey(position: Int): String {
        return mKeys.elementAt(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ItemViewHolder -> {

            holder.bind(position)
        }
        else -> throw IllegalStateException("bad holder")
    }


    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        init {
        }
        fun bind(position : Int /*lstEvent: mEvent?*/) = with(itemView) {

            val lstEvent = allEvent.get(getKey(position))
            if (lstEvent != null) {
                btRemoveItem.visible()
                btRemoveItem.setOnClickListener {

                    App.myRef.getReference("event").child(getKey(position)).removeValue()

                    val user = FirebaseAuth.getInstance().currentUser
                    App.myRef.getReference("users").child(user!!.uid).child("events").child(getKey(position)).removeValue()
                    allEvent.remove(getKey(position))
                    notifyDataSetChanged()
                }
                tvNameEvent.visible()
                tvNameEvent.text = lstEvent.name
                tvDateEvent.text = DateUtils.formatDateTime(context,
                        lstEvent.date,
                        DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
                     //   lstEvent.date.toString()
                val map=mapEvent(getKey(position),lstEvent)
                itemView.setOnClickListener({ itemListener(map) })
            }
        }
    }
}