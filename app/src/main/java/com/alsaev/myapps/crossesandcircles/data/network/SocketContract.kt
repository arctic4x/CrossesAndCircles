package com.alsaev.myapps.crossesandcircles.data.network

interface SocketContract {
    fun out_signIn(login: String)
    fun out_exit()
    fun out_requestToPlay(targetClient: String)
    fun out_responseOnRequestToPlay(targetClient: String,isAccept: Boolean)
    fun out_getClientsList()
    fun out_declineRequestToPlay(opponentName: String)
    fun out_imReady()
    fun out_action(position: Int)
}