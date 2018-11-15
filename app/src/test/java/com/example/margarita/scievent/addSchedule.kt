package com.example.margarita.scievent

import com.example.margarita.scievent.AddEvents.AddScheduleFragment
import com.example.margarita.scievent.sampledata.mItem
import org.junit.Assert
import org.junit.Test

class addSchedule {


    //правильно ли сохранено расписание
    @Test
    fun testSchedule1() {
        val t = AddScheduleFragment().addScheduleItem("test", "9:00")
        val s = mItem("9:00", "test")
        val se = s.info
        val te = t.info
        Assert.assertEquals("Error", se, te)
    }

    @Test
    fun testSchedule2() {
        val t = AddScheduleFragment().addScheduleItem("", "")
        val s = mItem("9:00", "null")
        val se = s.info
        val te = t.info
        Assert.assertEquals("Error", se, te)
    }

    @Test
    fun testSchedule3() {
        val t = AddScheduleFragment().addScheduleItem("это очень длинное описание происходящего которое расстягивается в длиннющую строку, " +
                "поэтому ее тяжело будет уместить на экране, но это ничего, для этого и существуют проверки, ведь так?", "9:00")
        val s = mItem("9:00", "null")
        val se = s.info
        val te = t.info
        Assert.assertEquals("Error", se, te)
    }
}