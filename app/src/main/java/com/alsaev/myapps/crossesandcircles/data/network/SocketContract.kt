package com.alsaev.myapps.crossesandcircles.data.network

interface SocketContract {
    fun out_signIn(login: String)
    fun out_exit()
    fun out_requestToPlay(targetClient: String)
    fun out_responseOnRequestToPlay(targetClient: String,isAccept: Boolean)
    fun out_readyToPlay()
    fun in_sendLoginRequest()
    fun in_sendListOfClient()
    fun in_sendClientConnected()
    fun in_sendCleintRemoved()
    fun in_requestToPlay()
    fun in_connectPlayerToGame()
}