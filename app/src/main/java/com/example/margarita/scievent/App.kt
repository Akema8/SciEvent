package com.example.margarita.scievent

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class App: Application() {

    companion object {
        lateinit var instance: App
            private set

        val myRef = FirebaseDatabase.getInstance()
        val mAuth = FirebaseAuth.getInstance()
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}