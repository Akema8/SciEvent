package com.example.margarita.scievent.ShowEvents

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.*
import com.example.margarita.scievent.R
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mapEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.add_event_fragment.*
import kotlinx.android.synthetic.main.all_event_fragment.*
import kotlinx.android.synthetic.main.fragment_add_event.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AllEventFragment : Fragment() {

    lateinit var newEvents: HashMap<Long, MutableList<mapEvent>>
   // lateinit var newEvents2: MutableList<mEvent>
    lateinit var adapter: AllEventsAdapter

    lateinit var treeMap : TreeMap<Long, MutableList<mapEvent>>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.all_event_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  newEvents2 = mutableListOf()

        newEvents = HashMap()
        treeMap = TreeMap()

        val layoutManager = LinearLayoutManager(context)

        layoutManager.reverseLayout = true
        rvAllEvents.layoutManager = layoutManager


        adapter = AllEventsAdapter(treeMap, itemListener = { event ->
            fragmentManager!!.transaction {
                replace(R.id.main_container, SelectedEventFragment.newInstance(event))
                addToBackStack(null)
            }
        })

        rvAllEvents.adapter = adapter

        val dateAndTime = Calendar.getInstance()
            dateAndTime.add(Calendar.DATE,1)

        val referenceUser = DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        val user = FirebaseAuth.getInstance().currentUser
        //Получение элементов из базы
        val ref = FirebaseDatabase.getInstance().reference.child("event")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.w("FBDB",user!!.uid)
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
               //  newEvents = HashMap()
                if (p0!!.exists()) {
                    for (e in p0.children) {
                        val ev = e.getValue(mEvent::class.java)
                        val date = ev!!.date
                        val frequency = treeMap.get(date)//newEvents.get(date)
                        val referenceUser = mutableListOf<mapEvent>()
                        treeMap.put(date, if (frequency == null) {
                        //newEvents.put(date, if (frequency == null) {
                            referenceUser.add(mapEvent(e.key,ev))
                            referenceUser
                        } else {
                            frequency.add(mapEvent(e.key,ev))
                            frequency
                        })
                      //  treeMap = TreeMap<Long, MutableList<mapEvent>>(newEvents)
                        adapter.notifyDataSetChanged()

                    }
                }
            }
        })

        var d: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime.set(Calendar.YEAR, year)
            dateAndTime.set(Calendar.MONTH, monthOfYear)
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            //   setInitialDateTime()
        }

        fun setDate(v: View) {
            val t = DatePickerDialog(context, d,
                    dateAndTime.get(Calendar.YEAR),
                    dateAndTime.get(Calendar.MONTH),
                    dateAndTime.get(Calendar.DAY_OF_MONTH))
            //  .show()
            t.datePicker.minDate = dateAndTime.timeInMillis
            t.show()
        }

        fun setInitialDateTime() {
            //  etDateEvent.text =
            DateUtils.formatDateTime(context,
                    dateAndTime.timeInMillis,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        }
    }
}