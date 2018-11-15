package com.example.margarita.scievent.AddEvents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.margarita.scievent.sampledata.lItem
import com.example.margarita.scievent.sampledata.mItem
import kotlinx.android.synthetic.main.add_schedule_fragment.*
import android.widget.DatePicker
import android.app.DatePickerDialog
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.text.format.DateUtils
import com.example.margarita.scievent.ShowEvents.SelectedEventFragment
import java.util.*
import javax.xml.datatype.DatatypeConstants.SECONDS
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.margarita.scievent.*
import com.example.margarita.scievent.sampledata.mEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class AddScheduleFragment : Fragment() {

    lateinit var newSchedule: MutableList<mItem>

    val id: Array<String> by lazy { arguments!!.getStringArray(ITEMID) }
    lateinit var event: mEvent

    companion object {
        const val ITEMID = "schedule"

        fun newInstance(eventId: Array<String>) = AddScheduleFragment().apply {
            arguments = Bundle().apply { putStringArray(ITEMID, eventId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        var activity = getActivity()
        (activity as MainActivity).setOnBackPressedLostener(BaseBackPressedListener(this))
        return inflater.inflate(R.layout.add_schedule_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btBackS.setOnClickListener {
            fragmentManager!!.transaction {
                replace(R.id.main_container, MainFragment())
            }
        }
        newSchedule = mutableListOf()

        etWriteTime.setOnClickListener() {
            setTime(it)
        }

        btAddScheduleItem.setOnClickListener() {
            if (!etWriteTime.text.toString().isEmpty() && !etWriteDescription.text.toString().isEmpty()) {
                val itm = mItem(etWriteTime.text.toString(), etWriteDescription.text.toString())
                newSchedule.add(itm)
                fragmentManager!!.transaction {
                    add(R.id.llContainer, AddScheduleItem.newInstance(itm))
                }
            }
            etWriteTime.text = "____________"
            etWriteDescription.text.clear()
        }

        App.myRef.getReference("event").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                event = (p0!!.child(id[0]).getValue(mEvent::class.java))!!
            }

        })

        btSaveSchedule.setOnClickListener() {

            if (newSchedule.isNotEmpty()) {

                setSchedule(id.size)
                Log.w("TAG", id.size.toString())
                Toast.makeText(context,"Сохранено", Toast.LENGTH_SHORT)
                event.schedule = newSchedule
            }

           // fragmentManager!!.popBackStack()
        }
    }
    //test
    fun addScheduleItem(info: String, time: String): mItem {
        try {
            if (info.isEmpty() or time.isEmpty() or (info.length > 100))
                return mItem("null", "null")
            val s = mItem(time, info)
            return s
        } catch (e: Exception) {
            return mItem("null", "null")
        }
    }

    fun setSchedule(quantityId: Int) = when (quantityId) {
        1 -> Fdb.addSchedule(newSchedule, id[0])
        2 -> Fdb.addScheduleToSection(newSchedule, id[0], id[1])
        3 -> Fdb.addScheduleToSubSection(newSchedule, id[0], id[1], id[2])
        else -> {
        }
    }

    var dateAndTime = Calendar.getInstance()

    // отображаем диалоговое окно для выбора времени
    fun setTime(v: View) {

        TimePickerDialog(context, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show()
    }

    // установка начальных даты и времени
    private fun setInitialDateTime() {

        etWriteTime.text = DateUtils.formatDateTime(context,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME)
    }

    // установка обработчика выбора времени
    var t: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateAndTime.set(Calendar.MINUTE, minute)

        setInitialDateTime()
    }

}