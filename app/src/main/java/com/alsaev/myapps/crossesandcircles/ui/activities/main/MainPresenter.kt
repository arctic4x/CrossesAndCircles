package com.alsaev.myapps.crossesandcircles.ui.activities.main

import com.alsaev.myapps.crossesandcircles.data.network.SocketWorker
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter.*

internal class MainPresenter(val vview: MainContract.Vview) : MainContract.Presenter,
        NotificationCenterDelegate {
    override fun init() {
        subscribeNotificationCenter()
        vview.openRegistrationFragment()
    }

    override fun didReceivedNotification(id: Int, vararg args: Any?) {
        when (id) {
            OpenMenuFragment ->
                vview.openMenuFragment()
            SetLogin ->
                vview.setLogin(args[0].toString())
            ReadyToPlay ->
                vview.openGameFragment(args[0].toString())

        }
    }

    private fun subscribeNotificationCenter() {
        getInstance().addObserver(this, OpenMenuFragment)
        getInstance().addObserver(this, SetLogin)
        getInstance().addObserver(this, ReadyToPlay)
    }

    private fun unSubscribeNotificationCenter() {
        getInstance().removeObserver(this, OpenMenuFragment)
        getInstance().removeObserver(this, SetLogin)
        getInstance().removeObserver(this, ReadyToPlay)
    }

    override fun onDestroy() {
        unSubscribeNotificationCenter()
    }
}