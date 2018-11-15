package com.example.margarita.scievent.MyEventEdit

import android.app.TimePickerDialog
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.App
import com.example.margarita.scievent.R
import com.example.margarita.scievent.gone
import com.example.margarita.scievent.sampledata.mItem
import com.example.margarita.scievent.sampledata.mapSectionId
import com.example.margarita.scievent.sampledata.mapSections
import com.example.margarita.scievent.visible
import kotlinx.android.synthetic.main.edit_item_schedule.*
import kotlinx.android.synthetic.main.edit_item_schedule.view.*
import java.util.*

class EditScheduleAdapter(val schedule: MutableList<mItem>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(R.layout.edit_item_schedule, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun getItemCount() =
            schedule.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ItemViewHolder -> {
            holder.bind(position)
        }
        else -> throw IllegalStateException("bad holder")
    }


    abstract inner class TextChangedListener<T>(private val target: T) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            this.onTextChanged(target, s)
        }

        abstract fun onTextChanged(target: T, s: Editable)
    }

    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) = with(itemView) {

            val item = schedule[position]
            if (item != null) {
                etEditTime.setText(item.time)
                etEditInfo.setText(item.info)
                etEditTime.setOnClickListener() {
                    setTime(it)
                }

                etEditInfo.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {
                        item.info = etEditInfo.text.toString()
                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                })

                etEditTime.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {
                        item.time = etEditTime.text.toString()
                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                })

                btRemoveItem.visible()
                btRemoveItem.setOnClickListener {
                    //App.myRef.getReference("event").child(sectionsWithIdEvent.id).child("sections").child(position.toString()).removeValue()
                    schedule.removeAt((position))
                    notifyDataSetChanged()
                }

            }
            else itemView.gone()
        }


        var dateAndTime = Calendar.getInstance()
        fun setTime(v: View) {

            TimePickerDialog(itemView.context, t,
                    dateAndTime.get(Calendar.HOUR_OF_DAY),
                    dateAndTime.get(Calendar.MINUTE), true)
                    .show()
        }

        // установка начальных даты и времени
        fun setInitialDateTime() {

            // etWriteTime.text =
            DateUtils.formatDateTime(itemView.context,
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_TIME)
        }

        // установка обработчика выбора времени
        var t: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            dateAndTime.set(Calendar.MINUTE, minute)

            setInitialDateTime()
        }
    }
}