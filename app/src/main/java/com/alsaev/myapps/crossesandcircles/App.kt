package com.alsaev.myapps.crossesandcircles

import android.app.Application
import com.alsaev.myapps.crossesandcircles.data.network.SocketContract
import com.alsaev.myapps.crossesandcircles.data.network.SocketWorker
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter.*

class App : Application(), SocketWorker.SocketListener {

    private lateinit var socketWorker: SocketContract

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        //NotificationCenter.getInstance().addObserver(this)

        val sw = SocketWorker()
        sw.setSocketListener(this)
        sw.start()
        socketWorker = sw
    }

    fun tryToLogin(login: String) {
        socketWorker.out_signIn(login)
    }

    override fun setLogin(login: String) {
        NotificationCenter.getInstance().postNotificationName(SetLogin, login)
    }

    override fun loginAlreadyExist() {
        NotificationCenter.getInstance().postNotificationName(LoginAlreadyExist)
    }

    override fun setListOfClients(list: List<String>) {
        NotificationCenter.getInstance().postNotificationName(SetListOfClients, list)
    }

    override fun addClient(connectedClientName: String) {
        NotificationCenter.getInstance().postNotificationName(AddClient, connectedClientName)
    }

    override fun removeClient(removedClientName: String) {
        NotificationCenter.getInstance().postNotificationName(RemoveClient, removedClientName)
    }

    override fun requestToPlay(opponentName: String) {

    }

    override fun letsPlay() {

    }

    fun getClientsList() {
        socketWorker.out_getClientsList()
    }
}