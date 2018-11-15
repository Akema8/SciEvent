package com.example.margarita.scievent.MyEventEdit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.margarita.scievent.*
import com.example.margarita.scievent.AddEvents.AddScheduleFragment
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mItem
import com.example.margarita.scievent.sampledata.mapEvent
import com.example.margarita.scievent.sampledata.mapSections
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_edit_event.*
import java.util.*


//Фрагмент редактирования конкретного события
class EditableEventFragment : Fragment() {

    val eventWithId: mapEvent by lazy { arguments!!.getSerializable(EVENT_ID) as mapEvent }

    companion object {
        const val EVENT_ID = "event_id"

        fun newInstance(eventId: mapEvent) = EditableEventFragment().apply {
            arguments = Bundle().apply { putSerializable(EVENT_ID, eventId) }
        }
    }

    var editDate: Long = 0
    var editDateTo: Long = 0
    lateinit var ev: mEvent
    lateinit var adapterS: EditScheduleAdapter
    val referenceEvent = App.myRef.getReference("event")

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_edit_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEditNameEvent.setText(eventWithId.event.name)
        etEditDescriptionEvent.setText(eventWithId.event.description)
        etEditDateEvent.text = DateUtils.formatDateTime(context,
                eventWithId.event.date,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        etEditDateEvent.setPaintFlags(etEditDateEvent.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        etEditDateToEvent.text = DateUtils.formatDateTime(context,
                eventWithId.event.dateTo,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        etEditDateToEvent.setPaintFlags(etEditDateEvent.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        editDate=eventWithId.event.date
        editDateTo=eventWithId.event.dateTo

        btBack.setOnClickListener {
            fragmentManager!!.transaction {
                replace(R.id.main_container, MainFragment())
            }
        }


        etEditDateEvent.setOnClickListener() {
            setDate(it)
        }

        etEditDateToEvent.setOnClickListener {
            setDateTo(it)
        }


        rvSections.layoutManager = LinearLayoutManager(context)

        var eventA: mEvent? = null
        App.myRef.getReference("event").child(eventWithId.id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    eventA = p0.getValue(mEvent::class.java)!!
                    Log.w("TAG", eventWithId.event!!.name)

                    try {
                        if (eventA!!.schedule != null) {
                            rvSchedule.visible()
                            rvSchedule.layoutManager = LinearLayoutManager(context)
                            val adapter = EditScheduleAdapter(eventA!!.schedule!!)
                            rvSchedule.adapter = adapter

                            btAddSched.text = "Добавить пункт расписания"
                            btAddSched.visible()
                            btAddSched.setOnClickListener {
                                clAddScheduleItem.visible()

                                etAddEditTime.setOnClickListener { setTime(it) }
                                btAddNewItem.setOnClickListener {
                                    if (!etAddEditTime.text.isNullOrEmpty() && !etAddEditInfo.text.isNullOrEmpty()) {
                                        val newItem = mItem(etAddEditTime.text.toString(), etAddEditInfo.text.toString())
                                        eventA!!.schedule!!.add(newItem)
                                        referenceEvent.child(eventWithId.id).child("schedule").setValue(eventA!!.schedule!!)
                                        adapter.notifyDataSetChanged()
                                        etAddEditInfo.setText("")
                                        etAddEditTime.setText("")
                                        etAddEditInfo.hint = "Инфо"
                                        clAddScheduleItem.gone()

                                    }
                                }
                            }
                        } else {
                            btAddSched.visible()
                            btAddSched.setOnClickListener {
                                val st: Array<String>
                                st = arrayOf(eventWithId.id)
                                fragmentManager!!.transaction {
                                    replace(R.id.main_container, AddScheduleFragment.newInstance(st))
                                    addToBackStack(null)
                                }

                            }
                        }

                    } catch (e: Exception) {
                        Log.w("TAG", "error adapter")
                    }
                }
            }
        })

        //Вывод списка всех секций
        if (eventWithId.event.sections != null) {

            tvDescriptionRecycler.text = "Секции:"
            val adapterSection = EditSectionAdapter(mapSections(eventWithId.id, eventWithId.event.sections!!), itemListener = { event ->
                fragmentManager!!.transaction {
                    replace(R.id.main_container, EditableSectionFragment.newInstance(event))
                    addToBackStack(null)
                }
            })
            rvSections.adapter = adapterSection

            btAddSect.setOnClickListener {

                clAddSectionItem.visible()

                btAddNewSectionItem.setOnClickListener {
                    if (!etAddSectionName.text.isNullOrEmpty() && !etAddSectionDescription.text.isNullOrEmpty()) {
                        if (eventWithId.event.sections != null) {
                            val newSection = mEvent(etAddSectionName.text.toString(),
                                    etAddSectionDescription.text.toString(),
                                    editDate,
                                    editDateTo,
                                    mutableListOf(),
                                    mutableListOf())
                            eventWithId.event.sections!!.add(newSection)

                            referenceEvent.child(eventWithId.id).child("sections").setValue(eventWithId.event.sections)
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
                        eventWithId.event.sections = newListSections

                        referenceEvent.child(eventWithId.id).child("sections").setValue(eventWithId.event.sections)

                        tvDescriptionRecycler.text = "Секции:"
                        val adapterSection = EditSectionAdapter(mapSections(eventWithId.id, eventWithId.event.sections!!), itemListener = { event ->
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

        //сохранить все изменения этого экрана
        btSaveChanges.setOnClickListener {
            if (editDate == 0.toLong())
                editDate = eventWithId.event.date

            if (etEditNameEvent.text.isNullOrEmpty() or etEditDescriptionEvent.text.isNullOrEmpty())
                Toast.makeText(context, "Поля не могут остаться пустыми", Toast.LENGTH_SHORT).show()
            else {
                val editEvent = mEvent(etEditNameEvent.text.toString(),
                        etEditDescriptionEvent.text.toString(),
                        editDate,
                        editDateTo,
                        eventWithId.event.sections,
                        eventA!!.schedule)

                referenceEvent.child(eventWithId.id).setValue(editEvent).addOnCompleteListener {
                    Toast.makeText(context, "Сохранено", Toast.LENGTH_LONG).show()
                }
            }
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
        t.datePicker.minDate = eventWithId.event.date
        t.show()
    }

    fun setDateTo(v: View) {
        val t = DatePickerDialog(context, dTo,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
        //  .show()
        t.datePicker.minDate = eventWithId.event.date
        t.show()
    }

    private fun setInitialDateTimeTo() {
        etEditDateToEvent.setText(DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR))
        editDateTo = dateAndTime.timeInMillis

    }

    private fun setInitialDateTime() {
        etEditDateEvent.setText(DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR))
        editDate = dateAndTime.timeInMillis
    }

    // отображаем диалоговое окно для выбора времени
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

        editDate = dateAndTime.timeInMillis
    }

    // установка обработчика выбора времени
    var t2: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateAndTime.set(Calendar.MINUTE, minute)

        setInitialTime()
    }
}