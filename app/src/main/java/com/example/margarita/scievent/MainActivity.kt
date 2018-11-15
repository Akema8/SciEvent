package com.example.margarita.scievent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    protected var onBackPressedListener: OnBackPressedListener? = null




    fun setOnBackPressedLostener(onBackPressedListener: OnBackPressedListener){
        this.onBackPressedListener = onBackPressedListener
    }

    override fun onBackPressed() {
        if (onBackPressedListener!=null)
            onBackPressedListener!!.doBack()
        else
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment()) //AddEventFragment())
                //  .addToBackStack(null)
                .commit()

    }
}



