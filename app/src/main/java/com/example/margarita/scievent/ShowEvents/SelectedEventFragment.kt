package com.example.margarita.scievent.ShowEvents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.sampledata.mEvent
import kotlinx.android.synthetic.main.item_event_ext.*
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import com.example.margarita.scievent.*
import com.example.margarita.scievent.sampledata.mapEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//Полная информация о событии без редактирования
class SelectedEventFragment : Fragment() {

    val eventM: mapEvent by lazy { arguments!!.getSerializable(EVENT_KEY) as mapEvent }

    companion object {
        const val EVENT_KEY = "event_key"

        fun newInstance(eventM: mapEvent) = SelectedEventFragment().apply {
            arguments = Bundle().apply { putSerializable(EVENT_KEY, eventM) }
        }
    }

    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //   var activity = getActivity()
        // (activity as MainActivity).setOnBackPressedLostener(BaseBackPressedListener(activity))

        return inflater.inflate(R.layout.item_event_ext, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (user != null) {
            val referenceUser = App.myRef.getReference("users")

            val referenceCurUserEvents = referenceUser.child(user!!.uid).child("bookmarks")
            var lstBookmarks = mutableListOf<String>()
            referenceCurUserEvents.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    Log.w("FBDB", user!!.uid)
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    //  newEvents = HashMap()
                    if (p0 != null) {
                        for (e in p0.children) {
                            val ev = e.getValue(String::class.java)
                            if (ev != null) {
                                lstBookmarks.add(ev)
                            }
                        }
                    }
                }
            })

            btAddBookmark.visible()
            btAddBookmark.setOnClickListener {
                if (lstBookmarks.contains(eventM.id)) {
                    Toast.makeText(context, "Данное мероприятие уже добавлено в закладки", Toast.LENGTH_SHORT).show()

                } else {
                    lstBookmarks.add(eventM.id)
                    referenceCurUserEvents
                            .setValue(lstBookmarks)

                    // referenceCurUserEvents.child(eventM.id).setValue(lstBookmarks)
                }
            }
        }
        val event = eventM.event

        tvNameEvent.text = event.name
        tvDateEvent.text = "Когда: c " + DateUtils.formatDateTime(context,
                event.date,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        tvDescriptionEvent.text = "Описание события: " + event.description
        if ( event.date==event.dateTo)
        {
            tvDateToEvent.gone()
            tv.gone()
        }
        tvDateToEvent.text= DateUtils.formatDateTime(context,
                event.dateTo,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)


        rvSections.layoutManager = LinearLayoutManager(context)


        if (event.sections != null) {

            rvSections.adapter = SectionAdapter(event.sections!!, itemListener = { event ->
                fragmentManager!!.transaction {
                    replace(R.id.main_container, SelectedSectionFragment.newInstance(event))
                    addToBackStack(null)
                }
            })

        }
        if (event.schedule != null) {
            rvSchedule.visible()
            rvSchedule.layoutManager = LinearLayoutManager(context)
            rvSchedule.adapter = ScheduleAdapter(event.schedule!!)
        }
        if (event.sections != null) {

        }
    }
}