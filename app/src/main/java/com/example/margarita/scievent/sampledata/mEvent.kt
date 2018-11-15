package com.example.margarita.scievent.sampledata

import java.io.Serializable

class mEvent(
        val name: String,
        val description: String,
        var date: Long,
        var dateTo: Long,
        var sections: MutableList<mEvent>?,
        var schedule: MutableList<mItem>?) : Serializable {

    constructor() : this("", "", 0,0, null, null) {}
}

class mItem(var time: String, var info: String) : Serializable {
    constructor() : this("", "") {}
}

data class lItem(var schedule: MutableList<mItem>?) : Serializable

data class mapEvent(val id: String, var event: mEvent) : Serializable

data class mapSections(val id: String, val sections: MutableList<mEvent>?) : Serializable

data class mapSectionId(val id: String, var event: mEvent, val position: Int, val positionSub: Int?) : Serializable