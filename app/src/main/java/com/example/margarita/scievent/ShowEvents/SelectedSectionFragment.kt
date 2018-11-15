package com.example.margarita.scievent.ShowEvents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.*
import com.example.margarita.scievent.sampledata.mEvent
import kotlinx.android.synthetic.main.item_event_ext.*

//Полная информация о секции или подсекции с расписанием
class SelectedSectionFragment: Fragment() {
    val event: mEvent by lazy { arguments!!.getSerializable(EVENT_KEY) as mEvent }

    companion object {
        const val EVENT_KEY = "event_key"

        fun newInstance(event: mEvent) = SelectedSectionFragment().apply {
            arguments = Bundle().apply { putSerializable(EVENT_KEY, event) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.item_event_ext, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNameEvent.text = event.name
        tvDateEvent.text = "Когда: c " + DateUtils.formatDateTime(context,
                event.date,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        if ( event.date==event.dateTo)
        {
            tvDateToEvent.gone()
            tv.gone()
        }
        tvDateToEvent.text= DateUtils.formatDateTime(context,
                event.dateTo,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)

        tvDescriptionEvent.text = "Описание cекции: " + event.description

        rvSections.layoutManager = LinearLayoutManager(context)
        if (event.schedule != null) {
            tvSchedule.visible()
            rvSections.adapter = ScheduleAdapter(event.schedule!!)
        }

        if (event.sections!= null){
            rvSections.adapter = SectionAdapter(event.sections!!, itemListener = { event ->
                fragmentManager!!.transaction {
                    replace(R.id.main_container, SelectedSectionFragment.newInstance(event))
                    addToBackStack(null)
                }
            })
        }


        btAddBookmark.text="Назад"
        btAddBookmark.visible()
        btAddBookmark.setOnClickListener{
            fragmentManager!!.transaction {
                replace(R.id.main_container, MainFragment())
            }
        }
    }
}