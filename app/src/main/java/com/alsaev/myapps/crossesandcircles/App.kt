package com.alsaev.myapps.crossesandcircles

import android.app.Application
import android.util.Log
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

    override fun getRequestToPlay(opponentName: String) {
        NotificationCenter.getInstance().postNotificationName(GetRequestToPlay, opponentName)
    }

    override fun opponentDeclineRequset(opponentName: String) {
        NotificationCenter.getInstance().postNotificationName(OpponentDeclineRequestToPlay, opponentName)
    }

    override fun letsPlay(opponentName: String) {
        NotificationCenter.getInstance().postNotificationName(ReadyToPlay, opponentName)
    }

    override fun myTurn() {
        NotificationCenter.getInstance().postNotificationName(MyTurn)
    }

    override fun getAction(figure: Int, position: Int) {
        NotificationCenter.getInstance().postNotificationName(GetAction, figure, position)
    }

    override fun endOfGame(isWin: Boolean, index1: Int, index2: Int) {
        NotificationCenter.getInstance().postNotificationName(EndOfGame, isWin, index1, index2)
    }

    fun acceptRequestToPlay(opponentName: String) {
        socketWorker.out_responseOnRequestToPlay(opponentName, true)
    }

    fun tryToLogin(login: String) {
        socketWorker.out_signIn(login)
    }

    fun sendRequestToPlay(targetName: String) {
        socketWorker.out_requestToPlay(targetName)
    }

    fun getClientsList() {
        socketWorker.out_getClientsList()
    }

    fun declineRequestToPlay(opponentName: String) {
        socketWorker.out_responseOnRequestToPlay(opponentName, false)
    }

    fun imReady() {
        socketWorker.out_imReady()
    }

    fun sendAction(pos: Int) {
        socketWorker.out_action(pos)
    }
}