package com.example.margarita.scievent


import android.test.MoreAsserts.assertEquals
import com.example.margarita.scievent.ShowEvents.AllEventsAdapter
import com.example.margarita.scievent.sampledata.mEvent
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class FdbTest {

    val mFireBase = Mockito.mock(FireBaseHelper::class.java)

    val mFb = FireBaseHelper()
    val event = mEvent("test1","test2","test3",null,null)

    @Test
    @Throws(Exception::class)
    fun testGetEvent(){
        val t = mFb.getAllEvents()
        Fdb.addElement(event,{})

        Assert.assertEquals(event,mFb.getAllEvents().last())
    }

    val lst: MutableList<mEvent> = mutableListOf()
    @Test
    @Throws(Exception::class)
    fun testMainScreenOpened() {
        Assert.assertEquals(lst,FireBaseHelper().getAllEvents())
    }
}