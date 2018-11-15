package com.example.margarita.scievent.Boockmarks

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
import com.example.margarita.scievent.ShowEvents.AllEventsAdapter
import com.example.margarita.scievent.ShowEvents.SelectedEventFragment
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mapEvent
import com.example.margarita.scievent.transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.all_event_fragment.*
import java.util.*

class BookmarkFragment : Fragment() {
    lateinit var adapter: AllEventsAdapter

    lateinit var mapIdEvent: HashMap<String, mEvent>

    val referenceUser = App.myRef.getReference("users")
    val user = FirebaseAuth.getInstance().currentUser
    val referenceCurUserEvents = referenceUser.child(user!!.uid).child("bookmarks")
    val referenceEvent = App.myRef.getReference("event")

    lateinit var newEvents: HashMap<Long, MutableList<mapEvent>>
    lateinit var treeMap: TreeMap<Long, MutableList<mapEvent>>
    val listOfEvents = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.all_event_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newEvents = HashMap()
        mapIdEvent = HashMap()
        treeMap = TreeMap()

        rvAllEvents.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?


        adapter = AllEventsAdapter(treeMap, itemListener = { event ->
            fragmentManager!!.transaction {
                replace(R.id.main_container, SelectedEventFragment.newInstance(event))
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
        listOfEvents.clear()
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
                        Log.w("LIST", ev)
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
                            // newEvents2.add(event)
                            mapIdEvent.put(e, event)
                            Log.w("EVENT", event.name)
                            adapter.notifyDataSetChanged()


                            //  val ev = e.getValue(mEvent::class.java)
                            val date = event!!.date
                            val frequency = treeMap.get(date)//newEvents.get(date)
                            val referenceUser = mutableListOf<mapEvent>()
                            treeMap.put(date, if (frequency == null) {
                                //newEvents.put(date, if (frequency == null) {
                                referenceUser.add(mapEvent(e, event))
                                referenceUser
                            } else {
                                frequency.add(mapEvent(e, event))
                                frequency
                            })
                            //  treeMap = TreeMap<Long, MutableList<mapEvent>>(newEvents)
                            adapter.notifyDataSetChanged()

                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}