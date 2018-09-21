package com.alsaev.myapps.crossesandcircles.ui.activities.main

internal interface MainContract {
    interface Vview {
        fun openRegistrationFragment()
        fun openMenuFragment()
        fun setLogin(login: String)
        fun openGameFragment(opponent: String)
    }

    interface Presenter {
        fun init()
        fun onDestroy()
    }
}