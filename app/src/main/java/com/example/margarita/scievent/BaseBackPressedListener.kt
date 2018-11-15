package com.example.margarita.scievent

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.text.TextUtils.replace
import com.example.margarita.scievent.ShowEvents.SelectedEventFragment


class BaseBackPressedListener(private val activity: Fragment): OnBackPressedListener {
    override fun doBack() {

     /*   activity.supportFragmentManager.transaction {
            replace(R.id.main_container,MainFragment())
        }*/

//        activity.fragmentManager!!.transaction {
  //          replace(R.id.main_container,MainFragment())
    //    }


      /* activity.supportFragmentManager.popBackStack()*/
    }


    /*
    public class BaseBackPressedListener implements OnBackPressedListener {
    private final FragmentActivity activity;

    public BaseBackPressedListener(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void doBack() {
        activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
     */

  /*  inner class BaseBackPressedListener(private val activity: FragmentActivity) : OnBackPressedListener {

        override fun doBack() {
            activity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }*/
}