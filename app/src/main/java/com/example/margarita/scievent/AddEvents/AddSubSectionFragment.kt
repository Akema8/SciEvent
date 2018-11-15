package com.example.margarita.scievent.AddEvents

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.App
import com.example.margarita.scievent.R
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mItem
import com.example.margarita.scievent.sampledata.mapSectionId
import com.example.margarita.scievent.transaction
import kotlinx.android.synthetic.main.fragment_add_section.*
import java.util.*

class AddSubSectionFragment : Fragment() {

    lateinit var idEvent: String

    //ссылка на событие, сама секция в которую вкладывать подсекцию и номер этой секции
    val idEventWithSectionPosition: mapSectionId by lazy { arguments!!.getSerializable(ITEMID) as mapSectionId }

    companion object {
        const val ITEMID = "item_key"

        fun newInstance(item: mapSectionId) = AddSubSectionFragment().apply {
            arguments = Bundle().apply { putSerializable(ITEMID, item) }
        }
    }

    var date: Long = 0
    var dateTo: Long = 0
    val schedule: MutableList<mItem>? = null
    val referenceEvent = App.myRef.getReference("event")

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_add_section, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etDateEvent.setOnClickListener() {
            setDate(it)
        }

        etDateToEvent.setOnClickListener {
            setDateTo(it)
        }

        if (idEventWithSectionPosition.event.sections != null) {
            for (e in idEventWithSectionPosition.event.sections!!) {
                val tv = TextureView(context)
                llContainerSubExist.addView(tv)
            }
        }

        btPlus.setOnClickListener() {
            if (!etNameEventSection.text.toString().isEmpty() && !etDescriptionEventSection.text.toString().isEmpty()) {

                if (date == 0.toLong()) {
                    date = idEventWithSectionPosition.event.date
                    etDateEvent.text = DateUtils.formatDateTime(context,
                            date,
                            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
                }

                if (dateTo==0.toLong() && date!=0.toLong())
                    dateTo=date

                val newSubSection = mEvent(etNameEventSection.text.toString(),
                        etDescriptionEventSection.text.toString(),
                        date,
                        dateTo,
                        null,
                        schedule)
                if (idEventWithSectionPosition.event.sections == null) {
                    idEventWithSectionPosition.event.sections = mutableListOf()
                    idEventWithSectionPosition.event.sections!!.add(newSubSection)
                } else idEventWithSectionPosition.event.sections!!.add(newSubSection)

                fragmentManager!!.transaction {
                    add(R.id.llContainerSub, AddScheduleItem.newInstance(
                            mItem(etNameEventSection.text.toString(),
                                    etDescriptionEventSection.text.toString())
                    ))
                }

                val editSection = idEventWithSectionPosition.event.sections
                referenceEvent.child(idEventWithSectionPosition.id)
                        .child("sections")
                        .child(idEventWithSectionPosition.position.toString())
                        .child("sections")
                        .setValue(editSection)

                etNameEventSection.setText("")
                etDescriptionEventSection.setText("")
            }

        }

        //Записать секцию в мероприятие в базу
        btAddEventSectionSave.setOnClickListener() {
            val st: Array<String>
            idEvent = idEventWithSectionPosition.id
            st = arrayOf(idEvent)

            fragmentManager!!.transaction {
                replace(R.id.main_container, AllNewSection.newInstance(st))
            }
        }


    }

    var dateAndTime = Calendar.getInstance()
    val today = dateAndTime.timeInMillis

    var d: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        dateAndTime.set(Calendar.YEAR, year)
        dateAndTime.set(Calendar.MONTH, monthOfYear)
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        setInitialDateTime()
    }

    var dTo: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        dateAndTime.set(Calendar.YEAR, year)
        dateAndTime.set(Calendar.MONTH, monthOfYear)
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        setInitialDateTimeTo()
    }

    fun setDate(v: View) {
        val t = DatePickerDialog(context, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
        //  .show()
        t.datePicker.minDate = today
        t.show()
    }

    fun setDateTo(v: View) {
        val t = DatePickerDialog(context, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
        //  .show()
        t.datePicker.minDate = today
        t.show()

    }

    private fun setInitialDateTimeTo() {
        etDateToEvent.text = DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)

        dateTo = dateAndTime.timeInMillis
        Log.w("SetDate", dateAndTime.timeInMillis.toString())
    }

    private fun setInitialDateTime() {
        etDateEvent.text = DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)

        date = dateAndTime.timeInMillis
        Log.w("SetDate", dateAndTime.timeInMillis.toString())
    }

}