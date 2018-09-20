package com.alsaev.myapps.crossesandcircles.ui.activities.main

import com.alsaev.myapps.crossesandcircles.data.network.SocketWorker
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter

internal class MainPresenter(val vview: MainContract.Vview) : MainContract.Presenter,
        NotificationCenter.NotificationCenterDelegate{
    override fun init() {
        subscribeNotificationCenter()
        vview.openRegistrationFragment()
    }

    override fun didReceivedNotification(id: Int, vararg args: Any?) {
        when (id) {
            NotificationCenter.OpenMenuFragment -> vview.openMenuFragment()
            NotificationCenter.SetLogin -> vview.setLogin(args[0].toString())
        }
    }

    private fun subscribeNotificationCenter() {
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.OpenMenuFragment)
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.SetLogin)
    }

    private fun unSubscribeNotificationCenter() {
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.OpenMenuFragment)
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.SetLogin)
    }

    override fun onDestroy() {
        unSubscribeNotificationCenter()
    }
}