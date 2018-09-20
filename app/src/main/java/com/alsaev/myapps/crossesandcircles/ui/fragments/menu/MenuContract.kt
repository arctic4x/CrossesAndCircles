package com.alsaev.myapps.crossesandcircles.ui.fragments.menu

internal interface MenuContract {
    interface Vview {
        fun getLogin(): String
        fun setLogin(login: String)
        fun setItems(items: ArrayList<String>)
        fun addItem(item: String)
        fun removeItem(item: String)
    }

    interface Presenter {
        fun init()
        fun onDestroy()
    }
}