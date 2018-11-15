package com.example.margarita.scievent.AddEvents


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.margarita.scievent.sampledata.mEvent
import kotlinx.android.synthetic.main.fragment_add_event.*
import android.app.DatePickerDialog
import java.util.*
import android.text.format.DateUtils
import android.util.Log
import com.example.margarita.scievent.*
import com.example.margarita.scievent.sampledata.mItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FragmentAddEvent : Fragment() {

    lateinit var idEvent: String
    var usersListEvent = mutableListOf<String>()
    val referenceUser = App.myRef.getReference("users")
    val user = FirebaseAuth.getInstance().currentUser
    val referenceUsersEvents = referenceUser.child(user!!.uid).child("events")

    var date: Long = 0
    var dateTo: Long = 0


    var section: MutableList<mEvent> = mutableListOf()
    var sectionPast: MutableList<mEvent> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_add_event, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btAddEventSave.isClickable = false

        usersListEvent.clear()
        etDateEvent.setOnClickListener() {
            setDate(it)
        }

        etDateEventTo.setOnClickListener() {
            setDateTo(it)
        }

        btAddSection.setOnClickListener {
            etSectionName.visible()
            etSectionDescription.visible()
            btPlusSection.visible()
        }

        btPlusSection.setOnClickListener {
            if (!etSectionName.text.toString().isEmpty() && !etSectionDescription.text.toString().isEmpty()) {
                val subsection = mutableListOf<mEvent>()
                val itm = mEvent(etSectionName.text.toString(), etSectionDescription.text.toString(), date, dateTo, subsection, null)
                section.add(itm)
                fragmentManager!!.transaction {
                    add(R.id.llContainerSect, AddSectionItem.newInstance(itm))
                }
            } else
                Toast.makeText(context, "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show()
            etSectionName.setText("")
            etSectionDescription.setText("")
        }

        var flag = false
        var flagSect = false

        btSave.setOnClickListener {
            if (!flag) {
                if (etSectionName.text.isNullOrEmpty() || etSectionName.text.toString() == "") {
                    if (date == 0.toLong()) {
                        date = dateAndTime.timeInMillis
                        etDateEvent.text = DateUtils.formatDateTime(context,
                                date,
                                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
                    }
                    if (dateTo == 0.toLong() && date != 0.toLong()) {
                        dateTo = date
                    }
                    for (sect in section){
                        sect.date=date
                        sect.dateTo=dateTo
                    }
                    saveEvent()
                    flag = true
                } else {
                    Toast.makeText(context, "Нажмите + для добавления введенной секции", Toast.LENGTH_SHORT).show()
                }
            } else {
                for (sect in section){
                    sect.date=date
                    sect.dateTo=dateTo
                }
                if (etSectionName.text.isNullOrEmpty() || etSectionName.text.toString() == "") {
                    Fdb.fireBase.getReference("event").child(idEvent).child("sections").setValue(section).addOnCompleteListener() {
                        Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()
                    }
                    sectionPast = section
                } else {
                    Toast.makeText(context, "Нажмите + для добавления введенной секции", Toast.LENGTH_SHORT).show()
                }
            }

        }

        //Добавить мероприятие (сохранить)
        btAddEventSave.setOnClickListener() {
            if (flag) {
                val st: Array<String>
                st = arrayOf(idEvent)

                if (section.isEmpty()) {

                    fragmentManager!!.transaction {
                        replace(R.id.main_container, AddScheduleFragment.newInstance(st))
                    }
                    Log.w("TAG", "ADD_SCHEDULE_FRAGMENT")
                } else {

                    fragmentManager!!.transaction {
                        replace(R.id.main_container, AllNewSection.newInstance(st))
                    }
                    Log.w("TAG", "ALL_NEW_SECTION")
                }
            } else {
                Toast.makeText(context, "Для начала сохраните мероприятие", Toast.LENGTH_SHORT).show()
            }

        }

        referenceUsersEvents.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                // newEvents.clear()
                if (p0!!.exists()) {
                    for (e in p0.children) {
                        val link = e.getValue(String::class.java)
                        // if (!link.isNullOrEmpty())
                        usersListEvent.add(link!!)
                    }
                }

            }
        })
    }

    private fun saveEvent() {

        if (etNameEvent.text.toString().isEmpty())
            Toast.makeText(context, "Название мероприятия не может быть пустым", Toast.LENGTH_LONG).show()
        else {


            val ev = mEvent(//eventWithId,
                    etNameEvent.text.toString(),
                    etDescriptionEvent.text.toString(),
                    date,
                    dateTo,
                    section,
                    null)
            Log.w("SaveEvent", DateUtils.formatDateTime(context,
                    date,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR))
            idEvent = Fdb.addElement(ev, { Toast.makeText(context, "Загружено успешно", Toast.LENGTH_SHORT).show() })

            usersListEvent.add(idEvent)

            referenceUsersEvents.setValue(usersListEvent)//usersListEvent)
            usersListEvent.clear()
        }
    }

    fun eventSave(nameEvent: String, descriptionEvent: String, dateEvent: String, section: MutableList<mEvent>?, schedule: MutableList<mItem>?):
            Boolean {

        if (nameEvent.isEmpty() or descriptionEvent.isEmpty() or dateEvent.isEmpty()
                or (nameEvent.length > 50)
                or (descriptionEvent.length > 200)) {
            return false
        } else {
            val ev = mEvent(
                    nameEvent,
                    descriptionEvent,
                    date,
                    dateTo,
                    section,
                    schedule)
            return true
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
        val t = DatePickerDialog(context, dTo,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
        //  .show()
        t.datePicker.minDate = today
        t.show()
    }

    private fun setInitialDateTimeTo() {
        etDateEventTo.text = DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        dateTo=dateAndTime.timeInMillis
    }

    private fun setInitialDateTime() {
        etDateEvent.text = DateUtils.formatDateTime(context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)

        date = dateAndTime.timeInMillis
        Log.w("SetDate", dateAndTime.timeInMillis.toString())
    }

}
