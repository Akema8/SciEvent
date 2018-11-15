package com.example.margarita.scievent.MyEventEdit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.*
import com.example.margarita.scievent.AddEvents.AddScheduleFragment
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mItem
import com.example.margarita.scievent.sampledata.mapSectionId
import com.example.margarita.scievent.sampledata.mapSections
import kotlinx.android.synthetic.main.fragment_edit_event.*
import java.time.temporal.ChronoUnit
import java.util.*

//Фрагмент редактирования конкретной секции
class EditableSectionFragment : Fragment() {

    //idEventWithSectionPosition события, сама секция и номер этой секции
    val sectionWithIdEvent: mapSectionId by lazy { arguments!!.getSerializable(EVENT_ID) as mapSectionId }

    companion object {
        const val EVENT_ID = "event_id"

        fun newInstance(eventId: mapSectionId) = EditableSectionFragment().apply {
            arguments = Bundle().apply { putSerializable(EVENT_ID, eventId) }
        }
    }

    var editDate: Long = 0
    var editDateTo: Long = 0

    val referenceEvent = App.myRef.getReference("event")


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_edit_event, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btBack.setOnClickListener {
            fragmentManager!!.transaction {
                replace(R.id.main_container, MainFragment())
            }
        }


        etEditNameEvent.setText(sectionWithIdEvent.event.name)
        etEditDescriptionEvent.setText(sectionWithIdEvent.event.description)
        etEditDateEvent.text = DateUtils.formatDateTime(context,
                sectionWithIdEvent.event.date,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        etEditDateToEvent.text=DateUtils.formatDateTime(context,
                sectionWithIdEvent.event.dateTo,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        editDate = sectionWithIdEvent.event.date
        editDateTo=sectionWithIdEvent.event.dateTo

        val cal1 = GregorianCalendar()
        val cal2 = GregorianCalendar()

        val dat = Date(editDate)
        val dat2 = Date(editDateTo)
        cal1.time = dat
        cal2.time = dat2

        val ddd: Int = Math.abs(((cal2.timeInMillis - cal1.timeInMillis)/86400000.toDouble()).toInt())+1 //Получим количество дней на которые необходимы расписания

        Log.w("DATEMINUS", ddd.toString())


        //Устанавливаем в поля текущие значения
        etEditDateEvent.setOnClickListener() {
            setDate(it)
        }

        etEditDateToEvent.setOnClickListener {
            setDateTo(it)
        }

        rvSections.layoutManager = LinearLayoutManager(context)

        //если у секции есть только расписание
        if (sectionWithIdEvent.event.schedule != null) {
            rvSchedule.visible()
            rvSchedule.layoutManager = LinearLayoutManager(context)
            val adapter = EditScheduleAdapter(sectionWithIdEvent.event.schedule!!)
            rvSchedule.adapter = adapter

            btAddSched.text = "Добавить пункт расписания"
            btAddSched.visible()
            btAddSched.setOnClickListener {
                clAddScheduleItem.visible()

                etAddEditTime.setOnClickListener { setTime(it) }
                btAddNewItem.setOnClickListener {
                    if (!etAddEditTime.text.isNullOrEmpty() && !etAddEditInfo.text.isNullOrEmpty()) {
                        val newItem = mItem(etAddEditTime.text.toString(), etAddEditInfo.text.toString())

                        if (sectionWithIdEvent.event.schedule == null) {
                            val ns = mutableListOf(newItem)
                            sectionWithIdEvent.event.schedule = ns
                        } else {
                            sectionWithIdEvent.event.schedule!!.add(newItem)
                        }
                        if (sectionWithIdEvent.positionSub != null) {
                            referenceEvent
                                    .child(sectionWithIdEvent.id)
                                    .child("sections")
                                    .child(sectionWithIdEvent.position.toString())
                                    .child("sections")
                                    .child(sectionWithIdEvent.positionSub.toString())
                                    .child("schedule")
                                    .setValue(sectionWithIdEvent.event.schedule!!)
                        } else {
                            referenceEvent
                                    .child(sectionWithIdEvent.id)
                                    .child("sections")
                                    .child(sectionWithIdEvent.position.toString())
                                    .child("schedule")
                                    .setValue(sectionWithIdEvent.event.schedule!!)
                        }
                        adapter.notifyDataSetChanged()
                        etAddEditInfo.setText("")
                        etAddEditInfo.hint = "Инфо"
                        etAddEditTime.setText("")
                        clAddScheduleItem.gone()
                    }
                }
            }
        } else {
            rvSchedule.visible()
            rvSchedule.layoutManager = LinearLayoutManager(context)

            btAddSched.text = "Добавить расписаниe"
            btAddSched.visible()
            btAddSched.setOnClickListener {
                clAddScheduleItem.visible()

                etAddEditTime.setOnClickListener { setTime(it) }
                btAddNewItem.setOnClickListener {
                    if (!etAddEditTime.text.isNullOrEmpty() && !etAddEditInfo.text.isNullOrEmpty()) {
                        val newItem = mItem(etAddEditTime.text.toString(), etAddEditInfo.text.toString())

                        if (sectionWithIdEvent.event.schedule == null) {
                            val ns = mutableListOf(newItem)
                            sectionWithIdEvent.event.schedule = ns
                        } else {
                            sectionWithIdEvent.event.schedule!!.add(newItem)
                        }
                        val adapter = EditScheduleAdapter(sectionWithIdEvent.event.schedule!!)
                        rvSchedule.adapter = adapter
                        if (sectionWithIdEvent.positionSub != null) {
                            referenceEvent
                                    .child(sectionWithIdEvent.id)
                                    .child("sections")
                                    .child(sectionWithIdEvent.position.toString())
                                    .child("sections")
                                    .child(sectionWithIdEvent.positionSub.toString())
                                    .child("schedule")
                                    .setValue(sectionWithIdEvent.event.schedule!!)
                        } else {
                            referenceEvent
                                    .child(sectionWithIdEvent.id)
                                    .child("sections")
                                    .child(sectionWithIdEvent.position.toString())
                                    .child("schedule")
                                    .setValue(sectionWithIdEvent.event.schedule!!)
                        }
                        adapter.notifyDataSetChanged()
                        etAddEditInfo.setText("")
                        etAddEditInfo.hint = "Инфо"
                        etAddEditTime.setText("")
                        clAddScheduleItem.gone()
                    }
                }
            }
        }


        if (sectionWithIdEvent.event.sections != null) {

            tvDescriptionRecycler.text = "Подсекции:"
            val adapterSection = EditSubSectionAdapter(mapSectionId(
                    sectionWithIdEvent.id,
                    sectionWithIdEvent.event,
                    sectionWithIdEvent.position,
                    null),
                    itemListener = { event ->
                        fragmentManager!!.transaction {
                            replace(R.id.main_container, EditableSectionFragment.newInstance(event))
                            addToBackStack(null)
                        }
                    })
            rvSections.adapter = adapterSection

            if (sectionWithIdEvent.positionSub != null) {
                btAddSect.gone()
                tvDescriptionRecycler.gone()
            }

            btAddSect.text = "Добавить подсекцию"
            btAddSect.setOnClickListener {

                clAddSectionItem.visible()

                btAddNewSectionItem.setOnClickListener {
                    if (!etAddSectionName.text.isNullOrEmpty() && !etAddSectionDescription.text.isNullOrEmpty()) {
                        if (sectionWithIdEvent.event.sections != null) {
                            val newSection = mEvent(etAddSectionName.text.toString(),
                                    etAddSectionDescription.text.toString(),
                                    editDate,
                                    editDateTo,
                                    mutableListOf(),
                                    mutableListOf())
                            sectionWithIdEvent.event.sections!!.add(newSection)

                            referenceEvent
                                    .child(sectionWithIdEvent.id)
                                    .child("sections")
                                    .child(sectionWithIdEvent.position.toString())
                                    .child("sections")
                                    .setValue(sectionWithIdEvent.event.sections)
                            adapterSection.notifyDataSetChanged()
                            etAddSectionName.setText("")
                            etAddSectionDescription.setText("")
                            clAddSectionItem.gone()
                        }

                    }
                }
            }
        } else {
            val newListSections = mutableListOf<mEvent>()

            if (sectionWithIdEvent.positionSub != null) {
                btAddSect.gone()
                tvDescriptionRecycler.gone()
            }

            btAddSect.text = "Добавить подсекцию"
            btAddSect.setOnClickListener {
                clAddSectionItem.visible()

                btAddNewSectionItem.setOnClickListener {
                    if (!etAddSectionName.text.isNullOrEmpty() && !etAddSectionDescription.text.isNullOrEmpty()) {
                        val newSection = mEvent(etAddSectionName.text.toString(),
                                etAddSectionDescription.text.toString(),
                                editDate,
                                editDateTo,
                                mutableListOf(),
                                mutableListOf())
                        newListSections.add(newSection)
                        sectionWithIdEvent.event.sections = newListSections

                        referenceEvent.child(sectionWithIdEvent.id)
                                .child("sections")
                                .child(sectionWithIdEvent.position.toString())
                                .child("sections")
                                .setValue(sectionWithIdEvent.event.sections)

                        tvDescriptionRecycler.text = "Секции:"


                        val adapterSection = EditSubSectionAdapter(mapSectionId(
                                sectionWithIdEvent.id,
                                sectionWithIdEvent.event,
                                sectionWithIdEvent.position,
                                null),
                                itemListener = { event ->
                                    fragmentManager!!.transaction {
                                        replace(R.id.main_container, EditableSectionFragment.newInstance(event))
                                        addToBackStack(null)
                                    }
                                })
                        rvSections.adapter = adapterSection

                        adapterSection.notifyDataSetChanged()
                        etAddSectionName.setText("")
                        etAddSectionDescription.setText("")
                        clAddSectionItem.gone()


                    }
                }
            }
        }


        btSaveChanges.setOnClickListener {
            val editEvent = mEvent(etEditNameEvent.text.toString(),
                    etEditDescriptionEvent.text.toString(),
                   // etEditDateEvent.text.toString(),
                    editDate,
                    editDateTo,
                    sectionWithIdEvent.event.sections,
                    sectionWithIdEvent.event.schedule)

            if (sectionWithIdEvent.positionSub == null) {
                referenceEvent.child(sectionWithIdEvent.id).child("sections").child(sectionWithIdEvent.position.toString()).setValue(editEvent)
                sectionWithIdEvent.event = editEvent
            } else {
                referenceEvent.child(sectionWithIdEvent.id)
                        .child("sections")
                        .child(sectionWithIdEvent.position.toString())
                        .child("sections")
                        .child(sectionWithIdEvent.positionSub.toString())
                        .setValue(editEvent)

                if (sectionWithIdEvent.event.sections != null)
                    if (sectionWithIdEvent.positionSub != null)

                        try {
                        sectionWithIdEvent.event.sections!![sectionWithIdEvent.positionSub!!] = editEvent}
                        catch (e: Exception)
                        {
                            Log.w("ERROR", "Not local save")
                        }
            }
            //  fragmentManager!!.popBackStack()
        }
    }


    var dateAndTime = Calendar.getInstance()
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
        t.datePicker.minDate = sectionWithIdEvent.event.date
        t.show()
    }

    fun setDateTo(v: View) {
        val t = DatePickerDialog(context, dTo,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
        //  .show()
        t.datePicker.minDate = sectionWithIdEvent.event.date
        t.show()
    }

    private fun setInitialDateTime() {
        etEditDateEvent.setText(DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR))
    }

    private fun setInitialDateTimeTo() {
        etEditDateToEvent.setText(DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR))
    }

    fun setTime(v: View) {

        TimePickerDialog(context, t2,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show()
    }

    // установка начальных даты и времени
    private fun setInitialTime() {

        etAddEditTime.text =
                DateUtils.formatDateTime(context,
                        dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_TIME)
    }

    // установка обработчика выбора времени
    var t2: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateAndTime.set(Calendar.MINUTE, minute)

        setInitialTime()
    }
}