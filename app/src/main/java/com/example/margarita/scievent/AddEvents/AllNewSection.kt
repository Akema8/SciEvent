package com.example.margarita.scievent.AddEvents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.*
import com.example.margarita.scievent.MyEventEdit.EditableEventFragment
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mItem
import com.example.margarita.scievent.sampledata.mapSections
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.all_event_fragment.*

class AllNewSection : Fragment() {

    lateinit var eventIdAndSection: mapSections
    lateinit var sections: MutableList<mEvent>
    lateinit var adapter: AllNewSectionAdapter
    val idArray: Array<String> by lazy { arguments!!.getStringArray(AllNewSection.ITEMID) }

    var event: mEvent? = null
    lateinit var mapp: mapSections
    lateinit var st: Array<String>

    companion object {
        const val ITEMID = "event_section"

        fun newInstance(idArray: Array<String>) = AllNewSection().apply {
            arguments = Bundle().apply { putStringArray(ITEMID, idArray) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        var activity = getActivity()
        (activity as MainActivity).setOnBackPressedLostener(BaseBackPressedListener(this))
        return inflater.inflate(R.layout.all_event_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvDateNow.text = "Выберите секцию для добавления расписания:"
        tvDateNow.visible()
        getEventSections() //заполнит mapp и получит мероприятие
        rvAllEvents.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?

        btBack.visible()
        btBack.setOnClickListener {
            fragmentManager!!.transaction {
                replace(R.id.main_container, MainFragment())
            }
        }
    }

    fun getEventSections() {
        App.myRef.getReference("event").child(idArray.first()).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    event = p0.getValue(mEvent::class.java)
                    Log.w("TAG", event!!.name)
                    mapp = mapSections(idArray.first(), event!!.sections)

                    try {
                        adapter = AllNewSectionAdapter(mapp,
                                itemListener = { event ->
                                    fragmentManager!!.transaction {
                                        replace(R.id.main_container, AddScheduleFragment.newInstance(event))
                                        addToBackStack(null)
                                    }
                                },
                                itemListener2 = { event2 ->
                                    fragmentManager!!.transaction {
                                        //   replace(R.idEventWithSectionPosition.main_container, EditableEventFragment.newInstance(event))
                                        replace(R.id.main_container, AddSubSectionFragment.newInstance(event2))
                                        addToBackStack(null)
                                    }
                                })
                        rvAllEvents.adapter = adapter
                        adapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.w("TAG", "error adapter")
                    }
                }
            }
        })
    }
}