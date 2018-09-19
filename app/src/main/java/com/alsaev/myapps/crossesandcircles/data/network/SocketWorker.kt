package com.alsaev.myapps.crossesandcircles.data.network

import android.util.Log
import java.io.*
import java.net.InetAddress
import java.net.Socket

const val MA_PORT = 6969

private const val ACCEPT = "accept"
private const val DECLINE = "decline"

private const val OUT_SIGN_IN = "SIGN_IN"
private const val OUT_EXIT = "EXIT"
private const val OUT_REQUEST_TO_PLAY = "REQUEST_TO_PLAY"
private const val OUT_RESPONSE_ON_REQUEST_TO_PLAY = "RESPONSE_ON_REQUEST_TO_PLAY"
private const val OUT_READY_TO_PLAY = "READY_TO_PLAY"

private const val IN_SEND_LOGIN_RESULT = "SEND_LOGIN_RESULT"
private const val IN_SEND_LIST_OF_CLIENT = "SEND_LIST_OF_CLIENT"
private const val IN_SEND_CLIENT_CONNECTED = "SEND_CLIENT_CONNECTED"
private const val IN_SEND_CLIENT_REMOVED = "SEND_CLIENT_REMOVED"
private const val IN_REQUEST_TO_PLAY = "REQUEST_TO_PLAY"
private const val IN_CONNECT_PLAYER_TO_GAME = "CONNECT_PLAYER_TO_GAME"

class SocketWorker : Thread(), SocketContract {

    private var isRunning = false

    private var writerStream: PrintWriter? = null
    private var readerStream: BufferedReader? = null
    private var listener: SocketListener? = null

    override fun run() {
        try {
            Log.d("socket", "try to find server")
            val address = InetAddress.getByName("192.168.0.112")

            try {
                Log.d("socket", "try to connect")
                val socket = Socket(address, MA_PORT)
                isRunning = true

                writerStream = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())))
                readerStream = BufferedReader(InputStreamReader(socket.getInputStream()))

                Log.d("socket", "connected")

                Log.d("socket", "listen to commands...")

                while (isRunning) {
                    if (writerStream!!.checkError()) {
                        isRunning = false
                    }
                    val command = readerStream!!.readLine()
                    if (command != null && command.isNotBlank()) {
                        Log.d("socket", command)
                        when (command) {
                            IN_SEND_LOGIN_RESULT -> {
                                in_sendLoginRequest()
                            }
                            IN_SEND_LIST_OF_CLIENT -> {
                                in_sendListOfClient()
                            }
                            IN_SEND_CLIENT_CONNECTED -> {
                                in_sendClientConnected()
                            }
                            IN_SEND_CLIENT_REMOVED -> {
                                in_sendCleintRemoved()
                            }
                            IN_REQUEST_TO_PLAY -> {
                                in_requestToPlay()
                            }
                            IN_CONNECT_PLAYER_TO_GAME -> {
                                in_connectPlayerToGame()
                            }
                        }
                    }
                }

                Log.d("socket", "disconnected")
                writerStream = null
                readerStream = null
                listener = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun in_sendLoginRequest() {
        with(readerStream) {
            val request = readLine()
            val login = readLine()
            if (request == ACCEPT) {
                listener?.setLogin(login!!)
            } else {
                listener?.loginAlreadyExist()
            }
        }
    }

    override fun in_sendListOfClient() {
        with(readerStream) {
            val listOfClients = readLine()
            val list = listOfClients!!.split(" ")
            listener?.setListOfClients(list)
        }
    }

    override fun in_sendClientConnected() {
        with(readerStream) {
            val connectedClientName = readLine()
            listener?.addClient(connectedClientName!!)
        }
    }

    override fun in_sendCleintRemoved() {
        with(readerStream) {
            val removedClientName = readLine()!!
            listener?.removeClient(removedClientName)
        }
    }

    override fun in_requestToPlay() {
        with(readerStream) {
            val opponentName = readLine()!!
            listener?.requestToPlay(opponentName)
        }
    }

    override fun in_connectPlayerToGame() {
        with(readerStream) {
            listener?.letsPlay()
        }
    }

    override fun out_signIn(login: String) {
        writerStream?.println(OUT_SIGN_IN)
        writerStream?.println(login)
        writerStream?.flush()
    }

    override fun out_exit() {
        writerStream?.println(OUT_EXIT)
        writerStream?.flush()
    }

    override fun out_requestToPlay(targetClient: String) {
        writerStream?.println(OUT_REQUEST_TO_PLAY)
        writerStream?.println(targetClient)
        writerStream?.flush()
    }

    override fun out_responseOnRequestToPlay(targetClient: String, isAccept: Boolean) {
        writerStream?.println(OUT_RESPONSE_ON_REQUEST_TO_PLAY)
        writerStream?.println(targetClient)
        writerStream?.println(if (isAccept) ACCEPT else DECLINE)
        writerStream?.flush()
    }

    override fun out_readyToPlay() {
        writerStream?.println(OUT_READY_TO_PLAY)
        writerStream?.flush()
    }

    interface SocketListener {
        fun setLogin(login: String)
        fun loginAlreadyExist()
        fun setListOfClients(list: List<String>)
        fun addClient(connectedClientName: String)
        fun removeClient(removedClientName: String)
        fun requestToPlay(opponentName: String)
        fun letsPlay()
    }
}
