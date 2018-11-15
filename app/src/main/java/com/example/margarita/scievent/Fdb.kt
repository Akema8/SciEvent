package com.example.margarita.scievent

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object Fdb {

    //private
    val fireBase: FirebaseDatabase by lazy {
        val db = FirebaseDatabase.getInstance()
//        db.setPersistenceEnabled(true)
        db
    }

    fun removeList(function: () -> Unit) {
        val reference = fireBase.getReference("event")
        reference.removeValue({ databaseError, databaseReference -> function() })
    }

    fun addElement(mevent: mEvent, callError: () -> Unit): String {
        try {
            val reference = fireBase.getReference("event")
            val eventId = reference.push().key
            reference.child(eventId).setValue(mevent).addOnCompleteListener() { callError() }
            return eventId
        } catch (e: Exception) {
            println("ERRor")
            return ""
        }
    }

    //Добавить секцию в событие по его ключу
    fun addSection(section: mEvent, id: String): String {
        val reference = Fdb.fireBase.getReference("event")
        val sectionId = reference.child(id).child("section").push().key
        // sections.add(mEvent("llaala","",555,null,null))
        // reference.child(idEventWithSectionPosition).child("section").setValue(sections)//добавит с idEventWithSectionPosition по номерам списка
        reference.child(id).child("sections").child(sectionId).setValue(section)
        return sectionId

    }

    fun addScheduleToSection(schedule: MutableList<mItem>, idEvent: String, idSection: String) {
        val reference = fireBase.getReference("event")

        reference.child(idEvent).child("sections").child(idSection).child("schedule").setValue(schedule)
    }

    fun addSchedule(schedule: MutableList<mItem>, idEvent: String) {
        val reference = fireBase.getReference("event")

        reference.child(idEvent).child("schedule").setValue(schedule).isComplete
    }

    fun addScheduleToSubSection(schedule: MutableList<mItem>, idEvent: String, idSection: String, idSubsection: String) {
        val reference = Fdb.fireBase.getReference("event")

        reference.child(idEvent).child(idSection).child(idSubsection).child("schedule").setValue(schedule)
    }


    //Получить событие по ключу
    fun getEvent(id: String): mEvent {
        val reference = fireBase.getReference("event")
        var event: mEvent? = null
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                event = (p0!!.child(id).getValue(mEvent::class.java))!!


                //получить ключи событий секций
                /*  for (e in p0.child(idEventWithSectionPosition).child("section").children) {
                      val ev = e.getValue(mEvent::class.java)
                      // newEvents.add(ev!!)
                  }
                  */
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
        if (event == null) {
            return mEvent("test", "test", 0,0, null, null)
        } else
            return event!!
    }


    fun showElement(): MutableList<mEvent> {
        val reference = fireBase.getReference("event")
        val list: MutableList<mEvent> = mutableListOf()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                //callError(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot?) {
                // val list = mutableListOf<mEvent>()
                //p0?.children?.forEach {
                //    element -> list.add(element.getValue(mEvent::class.java))
                //  }
                if (p0!!.exists()) {
                    for (e in p0.children) {
                        val ev = e.getValue(mEvent::class.java)
                        list.add(ev!!)

                        //  adapter.updateAllMessages(newEvents)
                    }
                }
                // call(list)
            }
        })
        return list
    }
}