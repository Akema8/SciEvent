package com.example.margarita.scievent

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.margarita.scievent.AddEvents.FragmentAddEvent
import com.example.margarita.scievent.Boockmarks.BookmarkFragment
import com.example.margarita.scievent.MyEventEdit.MyEventsFragment
import com.example.margarita.scievent.ShowEvents.AllEventFragment
import com.example.margarita.scievent.sampledata.mEvent
import com.example.margarita.scievent.sampledata.mUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*


class MainFragment : Fragment() {

    val mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            Log.w("TAG", "onAuthStateChanged:signed_in:" + user.uid)
        } else {
            Log.d("TAG", "onAuthStateChanged:signed_out")
        }
        updateUI(user)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.mAuth.addAuthStateListener(mAuthListener)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (mAuthListener != null) {
            App.mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //список все событий
        btAllEvent.setOnClickListener() {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.main_container, AllEventFragment())
                    .addToBackStack(null)
                    .commit()

        }

        //Добавить новое событие
        btAddEvent.setOnClickListener() {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.main_container, FragmentAddEvent())
                    .addToBackStack(null)
                    .commit()
        }

        //Список созданных пользователем событий
        btMyEvent.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.main_container, MyEventsFragment()) //AddEventFragment())
                    .addToBackStack(null)
                    .commit()
        }

        //Создать учеьную запись
        btEmailCreateAccountButton.setOnClickListener {
            createAccount(etFieldEmail.text.toString(), etFieldPassword.text.toString())
        }


        //Вход
        btEmailSignInButton.setOnClickListener {
            signIn(etFieldEmail.text.toString(), etFieldPassword.text.toString())
        }


        //Выход из аккаунта
        btSignOutButton.setOnClickListener {
            signOut()
        }

        //Закладки
        btBookmarks.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.main_container, BookmarkFragment())
                    .addToBackStack(null)
                    .commit()
        }


    }

    fun signOut() {
        App.mAuth.signOut()
        updateUI(null)
    }

    fun updateUI(user: FirebaseUser?) {
        //  hideProgressDialog()

        if (user != null) {
            //  getUsersEventId(user.uid)

            btSignOutButton.visible()
            try {
                val user = FirebaseAuth.getInstance().currentUser
                val referenceCurUserEvents = App.myRef.getReference("users").child(user!!.uid)

                referenceCurUserEvents.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        // newEvents.clear()
                        if (p0 != null) {
                            try {
                                val userbb = p0.getValue(mUser::class.java)!!
                                Log.w("TESTTEST", user.email)
                                if (userbb.admin == "true") {
                                    // updateUI(user)
                                    try {
                                        Log.w("TAG", userbb.email.toString())
                                        Log.w("TAG", userbb.admin.toString())
                                        llEmailPasswordButtons.gone()
                                        llEmailPasswordFields.gone()
                                        btAddEvent.visible()
                                        tvAdEv.visible()
                                        btMyEvent.visible()
                                        tvMyEvent.visible()
                                        btBookmarks.visible()
                                        tvBook.visible()
                                    } catch (e: Exception) {
                                        Log.w("TESTTEST", "ExceptionAdmin")
                                    }
                                } else {
                                    // updateUIsimple(user)
                                    try {


                                        Log.w("TAG", userbb.email.toString())
                                        Log.w("TAG", userbb.admin.toString())
                                        llEmailPasswordButtons.gone()
                                        llEmailPasswordFields.gone()
                                        btAddEvent.gone()
                                        tvAdEv.gone()
                                        btMyEvent.gone()
                                        tvMyEvent.gone()
                                        btBookmarks.visible()
                                        tvBook.visible()
                                    } catch (e: Exception) {
                                        Log.w("TESTTEST", "Exception")
                                    }
                                }
                            } catch (e: Exception) {
                                Log.w("TESTTEST", "Can't serialize")
                            }
                        }

                    }
                })
            } catch (e: Exception) {
                Log.w("TAG", "CATCH")
            }


        } else {
            llEmailPasswordButtons.visible()
            llEmailPasswordFields.visible()
            btSignOutButton.gone()
            btAddEvent.gone()
            tvAdEv.gone()
            btMyEvent.gone()
            tvMyEvent.gone()
            btBookmarks.gone()
            tvBook.gone()
            etFieldEmail.setText("")
            etFieldPassword.setText("")

        }
    }


    fun validateForm(): Boolean {
        var valid = true

        val email = etFieldEmail.text.toString()
        if (email.isEmpty()) {
            etFieldEmail.error = "Required."
            valid = false
        } else {
            etFieldEmail.error = null
        }

        val password = etFieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            etFieldPassword.error = "Required."
            valid = false
        } else {
            etFieldPassword.error = null
        }
        return valid
    }

    fun createAccount(email: String, password: String) {
        Log.w("TAG", "createAccount:$email")
        if (!validateForm()) {
            return
        }
        App.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                    Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful)
                    if (!task.isSuccessful) {
                        Toast.makeText(context, "auth_failed",
                                Toast.LENGTH_SHORT).show()

                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            Toast.makeText(context, "пароль должен быть не менее 6 символов",
                                    Toast.LENGTH_LONG).show()
                            etFieldPassword.requestFocus()
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(context, "некорректный email",
                                    Toast.LENGTH_LONG).show()
                            etFieldEmail.requestFocus()
                        } catch (e: FirebaseAuthUserCollisionException) {
                            Toast.makeText(context, "учетная запись с таким email уже существует",
                                    Toast.LENGTH_LONG).show()
                            etFieldEmail.requestFocus()
                        } catch (e: FirebaseException) {
                            Log.e(TAG, e.message)
                        }
                    }
                    if (task.isSuccessful) {
                        val t = App.myRef.getReference("users")
                        val user = FirebaseAuth.getInstance().currentUser
                        val us = mUser(user!!.email!!, mutableListOf(), "true", mutableListOf())
                        t.child(user.uid).setValue(us)
                    }

                })
    }


    fun signIn(email: String, password: String) {
        Log.d("TAG", "signIn:$email")
        if (!validateForm()) {
            return
        }
        //showProgressDialog()
        App.mAuth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                    Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful)

                    if (!task.isSuccessful) {
                        Log.w("TAG", "signInWithEmail:failed", task.exception)
                        //     Toast.makeText(context, "auth_failed",
                        //           Toast.LENGTH_SHORT).show()
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidUserException) {
                            Toast.makeText(context, "такого пользователя нет",
                                    Toast.LENGTH_LONG).show()
                            etFieldEmail.requestFocus()
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(context, "неверный логин или пароль",
                                    Toast.LENGTH_LONG).show()
                            etFieldPassword.requestFocus()
                        } catch (e: FirebaseException) {
                            Log.e(TAG, e.message)
                        }
                    }

                })
    }
}