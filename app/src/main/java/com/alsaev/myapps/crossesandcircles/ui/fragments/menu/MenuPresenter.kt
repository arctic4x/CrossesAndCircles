package com.alsaev.myapps.crossesandcircles.ui.fragments.menu

import com.alsaev.myapps.crossesandcircles.App
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter.*

internal class MenuPresenter(val vview: MenuContract.Vview) : MenuContract.Presenter,
        NotificationCenter.NotificationCenterDelegate {

    private var login = "Unnamed"

    override fun init() {
        login = vview.getLogin()
        vview.setLogin(login)

        subscribeNotificationCenter()

        App.instance.getClientsList()
    }

    private fun subscribeNotificationCenter() {
        NotificationCenter.getInstance().addObserver(this, SetListOfClients)
        NotificationCenter.getInstance().addObserver(this, AddClient)
        NotificationCenter.getInstance().addObserver(this, RemoveClient)
        NotificationCenter.getInstance().addObserver(this, GetRequestToPlay)
        NotificationCenter.getInstance().addObserver(this, OpponentDeclineRequestToPlay)
    }

    private fun unSubscribeNotificationCenter() {
        NotificationCenter.getInstance().removeObserver(this, SetListOfClients)
        NotificationCenter.getInstance().removeObserver(this, RemoveClient)
        NotificationCenter.getInstance().removeObserver(this, AddClient)
        NotificationCenter.getInstance().removeObserver(this, GetRequestToPlay)
        NotificationCenter.getInstance().removeObserver(this, OpponentDeclineRequestToPlay)
    }

    override fun didReceivedNotification(id: Int, vararg args: Any?) {
        when (id) {
            SetListOfClients -> {
                vview.setItems(ArrayList(args[0] as List<String>))
            }
            RemoveClient -> {
                vview.removeItem(args[0].toString())
            }
            AddClient -> {
                vview.addItem(args[0].toString())
            }
            GetRequestToPlay -> {
                vview.showRequestToPlay(args[0].toString())
            }
            OpponentDeclineRequestToPlay -> {
                vview.showDeclineRequestToPlay(args[0].toString())
            }
        }
    }

    override fun onDestroy() {
        unSubscribeNotificationCenter()
    }
}