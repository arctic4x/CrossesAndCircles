package com.alsaev.myapps.crossesandcircles.ui.fragments.registration

internal interface RegistrationContract {
    interface Vview {
        fun getLogin(): String
        fun openMenu()
        fun setLogin(login: String)
        fun loginAlreadyExist()
    }

    interface Presenter {
        fun init()
        fun btnLoginClick()
        fun onDestroy()
    }
}