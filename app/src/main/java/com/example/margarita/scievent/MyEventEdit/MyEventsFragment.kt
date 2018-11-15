package com.example.margarita.scievent.MyEventEdit

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.App
import com.example.margarita.scievent.R
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.all_event_fragment.*

class MyEventsFragment : Fragment() {

    lateinit var newEvents2: MutableList<mEvent>
    lateinit var adapter: AllEventsAdapterId

    lateinit var mapIdEvent: HashMap<String, mEvent>

    val referenceUser = App.myRef.getReference("users")
    val user = FirebaseAuth.getInstance().currentUser
    val referenceCurUserEvents = referenceUser.child(user!!.uid).child("events")
    val referenceEvent = App.myRef.getReference("event")
    //     FirebaseDatabase.getInstance().reference.child("event")

    val listOfEvents = mutableListOf<String>()

    override fun onStart() {
        super.onStart()
        getUsersEventId()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.all_event_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newEvents2 = mutableListOf()
        mapIdEvent = HashMap()
        rvAllEvents.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
        adapter = AllEventsAdapterId(mapIdEvent, itemListener = { event ->
            fragmentManager!!.transaction {
                replace(R.id.main_container, EditableEventFragment.newInstance(event))
                addToBackStack(null)
            }
        })

        rvAllEvents.adapter = adapter
        adapter.notifyDataSetChanged()
        try {
            getUsersEventId()
            getAllUsersEvent()
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.w("FB", "ОШИБКА с базой")
        }
    }

    //Получить ссылки на все события пользователя
    fun getUsersEventId() {
        referenceCurUserEvents.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                // newEvents.clear()
                if (p0!!.exists()) {
                    for (e in p0.children) {
                        val ev = e.getValue(String::class.java)
                        listOfEvents.add(ev!!)
                        //  Log.w("LIST", ev)
                        adapter.notifyDataSetChanged()
                    }
                }

            }
        })
    }

    //Получить сами события пользователя по ссылкам
    fun getAllUsersEvent() {
        referenceEvent.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.exists()) {
                    for (e in listOfEvents) {
                        val event = p0.child(e).getValue(mEvent::class.java)
                        if (event != null) {
                            newEvents2.add(event)
                            mapIdEvent.put(e, event)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}