package com.example.margarita.scievent.AddEvents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.R
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mItem
import kotlinx.android.synthetic.main.item_shedule.*

class AddSectionItem:Fragment() {

    val itemSection: mEvent by lazy { arguments!!.getSerializable(ITEM_SECTION) as mEvent }

    companion object {
        const val ITEM_SECTION = "item_event"

        fun newInstance(item: mEvent) = AddSectionItem().apply {
            arguments = Bundle().apply { putSerializable(ITEM_SECTION, item) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.item_shedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvDescription.text=itemSection.description
        tvTime.text=itemSection.name

    }
}