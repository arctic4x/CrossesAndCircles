package com.alsaev.myapps.crossesandcircles.ui.fragments.registration


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alsaev.myapps.crossesandcircles.R
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter.*
import kotlinx.android.synthetic.main.fragment_regisration.*

class RegistrationFragment : Fragment(), RegistrationContract.Vview {

    private lateinit var presenter: RegistrationContract.Presenter

    private var fragmentInteraction: FragmentInteraction? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        presenter = RegistrationPresenter(this)

        return inflater.inflate(R.layout.fragment_regisration, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.init()
        btn_login.isEnabled = false

        et_login.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                btn_login.isEnabled = et_login.text.isNotBlank()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        btn_login.setOnClickListener {
            presenter.btnLoginClick()
        }
    }

    override fun getLogin(): String {
        return et_login.text.toString()
    }

    override fun openMenu() {
        NotificationCenter.getInstance().postNotificationName(OpenMenuFragment)
    }

    override fun setLogin(login: String) {
        Handler(Looper.getMainLooper()).post {
            fragmentInteraction?.setLogin(login)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentInteraction = context as FragmentInteraction
    }

    override fun onDetach() {
        super.onDetach()
        fragmentInteraction = null
    }

    interface FragmentInteraction {
        fun setLogin(login: String)
    }

    override fun loginAlreadyExist() {
        Handler(Looper.getMainLooper()).post {
            et_login.error = "Login already exist"
        }
    }
}
