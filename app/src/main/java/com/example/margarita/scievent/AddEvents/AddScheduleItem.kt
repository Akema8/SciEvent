package com.example.margarita.scievent.AddEvents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.R
import com.example.margarita.scievent.ShowEvents.SelectedEventFragment
import com.example.margarita.scievent.sampledata.mItem
import kotlinx.android.synthetic.main.item_shedule.*

class AddScheduleItem: Fragment() {

    val itemScedule: mItem by lazy { arguments!!.getSerializable(ITEM_SCHEDULE) as mItem }

    companion object {
        const val ITEM_SCHEDULE = "item_key"

        fun newInstance(item: mItem) = AddScheduleItem().apply {
            arguments = Bundle().apply { putSerializable(ITEM_SCHEDULE, item) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.item_shedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvDescription.text=itemScedule.info
        tvTime.text=itemScedule.time

    }
}