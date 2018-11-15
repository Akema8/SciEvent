package com.example.margarita.scievent

import com.example.margarita.scievent.ShowEvents.AllEventsAdapter
import com.example.margarita.scievent.sampledata.mEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FireBaseHelper() {

    companion object {
        val mRef = FirebaseDatabase.getInstance().getReference("event")
    }

    fun getAllEvents(): MutableList<mEvent>{
        val newEvents:MutableList<mEvent> = mutableListOf()
        mRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                // newEvents.clear()
                if (p0!!.exists()){
                    for (e in p0.children) {
                        val ev = e.getValue(mEvent::class.java)
                        newEvents.add(ev!!)
                    }
                }
            }
        })
        return newEvents
    }

    fun addEv(events:mEvent): Boolean{

        val reference = Fdb.fireBase.getReference("event")
        val eventId = reference.push().key
        try {


        reference.child(eventId).setValue(events)
        return true}
        catch (e: Exception) {
            return false
        }
    }



    fun getEvents(newEvents:MutableList<mEvent>, adapter:AllEventsAdapter){
        mRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                // newEvents.clear()
                if (p0!!.exists()){
                    for (e in p0.children) {
                        val ev = e.getValue(mEvent::class.java)

                        newEvents.add(ev!!)

                        adapter.updateAllMessages(newEvents)
                    }
                }
            }

        })
    }




}