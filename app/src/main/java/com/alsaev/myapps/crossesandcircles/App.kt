package com.alsaev.myapps.crossesandcircles

import android.app.Application
import com.alsaev.myapps.crossesandcircles.data.network.SocketWorker
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter

class App : Application(),SocketWorker.SocketListener, NotificationCenter.NotificationCenterDelegate {

    private lateinit var socketWorker: SocketWorker

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        //NotificationCenter.getInstance().addObserver(this)

        socketWorker = SocketWorker()
        socketWorker.setSocketListener(this)
    }

    override fun setLogin(login: String) {

    }

    override fun loginAlreadyExist() {

    }

    override fun setListOfClients(list: List<String>) {

    }

    override fun addClient(connectedClientName: String) {

    }

    override fun removeClient(removedClientName: String) {

    }

    override fun requestToPlay(opponentName: String) {

    }

    override fun letsPlay() {

    }

    override fun didReceivedNotification(id: Int, vararg args: Any?) {
    }
}