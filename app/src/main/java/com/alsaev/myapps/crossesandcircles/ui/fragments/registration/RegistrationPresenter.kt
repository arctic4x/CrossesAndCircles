package com.alsaev.myapps.crossesandcircles.ui.fragments.registration

import com.alsaev.myapps.crossesandcircles.App
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter.*

internal class RegistrationPresenter(val vview: RegistrationContract.Vview) : RegistrationContract.Presenter
        , NotificationCenter.NotificationCenterDelegate {
    override fun init() {
        NotificationCenter.getInstance().addObserver(this, LoginAlreadyExist)
        NotificationCenter.getInstance().addObserver(this, SetLogin)
    }

    override fun btnLoginClick() {
        val login = vview.getLogin()

        App.instance.tryToLogin(login)

        //vview.setLogin(login)
        //vview.openMenu()
    }

    override fun didReceivedNotification(id: Int, vararg args: Any?) {
        when (id) {
            LoginAlreadyExist -> {
                vview.loginAlreadyExist()
            }
            SetLogin -> {
                vview.setLogin(args[0].toString())
                NotificationCenter.getInstance().postNotificationName(NotificationCenter.OpenMenuFragment)
            }
        }
    }

    override fun onDestroy() {
        NotificationCenter.getInstance().removeObserver(this, LoginAlreadyExist)
        NotificationCenter.getInstance().removeObserver(this, SetLogin)
    }
}