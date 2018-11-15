package com.example.margarita.scievent.sampledata

import java.io.Serializable

class mUser (val email: String, val events: MutableList<String>, val admin: String, val bookmarks : MutableList<String>) : Serializable
{
    constructor() : this("", mutableListOf(), "false", mutableListOf()) {}
}

