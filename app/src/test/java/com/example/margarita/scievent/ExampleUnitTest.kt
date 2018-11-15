package com.example.margarita.scievent

import com.example.margarita.scievent.AddEvents.AddScheduleFragment
import com.example.margarita.scievent.AddEvents.FragmentAddEvent
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mItem
import org.junit.Assert
import org.junit.Test
import kotlin.math.E


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    //Сохранен ли ивент
    @Test
    fun testEventSave1() {
        val t = FragmentAddEvent().eventSave("Test", "Test", "Test", null, null)
        val ev = mEvent("Test", "Test", "Test", null, null)
        //   Assert.assertEquals(ev,referenceUser)
        Assert.assertEquals(true, t)
    }

    @Test
    fun testEventSave2() {
        val t = FragmentAddEvent().eventSave("", "", "Test", null, null)
        val ev = mEvent("Test", "Test", "Test", null, null)
        //   Assert.assertEquals(ev,referenceUser)
        Assert.assertEquals(false, t)
    }

    @Test
    fun testEventSave3() {
        val t = FragmentAddEvent().eventSave("Testпрвоырвоырвлплплплплплплплплплплплалплалвлалплплалвлала", "Test", "Test", null, null)
        val ev = mEvent("Test", "Test", "Test", null, null)
        //   Assert.assertEquals(ev,referenceUser)
        Assert.assertEquals(false, t)
    }






}

