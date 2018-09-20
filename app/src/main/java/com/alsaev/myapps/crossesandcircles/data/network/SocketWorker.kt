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
private const val OUT_GET_CLIENTS_LIST = "GET_CLIENTS_LIST"

private const val IN_SEND_LOGIN_RESULT = "SEND_LOGIN_RESULT"
private const val IN_SEND_LIST_OF_CLIENT = "SEND_LIST_OF_CLIENT"
private const val IN_SEND_CLIENT_CONNECTED = "SEND_CLIENT_CONNECTED"
private const val IN_SEND_CLIENT_REMOVED = "SEND_CLIENT_REMOVED"
private const val IN_REQUEST_TO_PLAY = "REQUEST_TO_PLAY"
private const val IN_CONNECT_PLAYER_TO_GAME = "CONNECT_PLAYER_TO_GAME"

class SocketWorker : Thread(), SocketContract {

    private var isRunning = false

    private lateinit var writerStream: PrintWriter
    private lateinit var readerStream: BufferedReader
    private var listener: SocketListener? = null

    override fun run() {
        try {
            Log.d("socket_worker", "try to find server")
            val address = InetAddress.getByName("192.168.0.112")

            try {
                Log.d("socket_worker", "try to connect")
                val socket = Socket(address, MA_PORT)
                isRunning = true

                writerStream = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())))
                readerStream = BufferedReader(InputStreamReader(socket.getInputStream()))

                Log.d("socket_worker", "connected")

                Log.d("socket_worker", "listen to commands...")

                while (isRunning) {
                    if (writerStream.checkError()) {
                        isRunning = false
                    }
                    val command = readerStream.readLine()
                    if (command != null && command.isNotBlank()) {
                        Log.d("socket_worker", "command: $command")
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

                Log.d("socket_worker", "disconnected")
                listener = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setSocketListener(socketListener: SocketListener?) {
        listener = socketListener
    }

    private fun in_sendLoginRequest() {
        //with(readerStream) {
        Log.d("socket_worker", "in_sendLoginRequest")
        //Thread {
            val request = readerStream.readLine()
            val login = readerStream.readLine()
            if (request == ACCEPT) {
                listener?.setLogin(login!!)
            } else {
                listener?.loginAlreadyExist()
            }
        //}.start()
        //}
    }

    private fun in_sendListOfClient() {
        Log.d("socket_worker", "in_sendListOfClient")
            val listOfClients = readerStream.readLine()
            val list = listOfClients!!.split(" ")
            listener?.setListOfClients(list)
    }

    private fun in_sendClientConnected() {
            val connectedClientName = readerStream.readLine()
            Log.d("socket_worker", "in_sendClientConnected: $connectedClientName")
            listener?.addClient(connectedClientName!!)
    }

    private fun in_sendCleintRemoved() {
            val removedClientName = readerStream.readLine()!!
            Log.d("socket_worker", "in_sendCleintRemoved: $removedClientName")
            listener?.removeClient(removedClientName)
    }

    private fun in_requestToPlay() {
            val opponentName = readerStream.readLine()!!
            Log.d("socket_worker", "in_requestToPlay $opponentName")
            listener?.requestToPlay(opponentName)
    }

    private fun in_connectPlayerToGame() {
        Log.d("socket_worker", "in_connectPlayerToGame")
            listener?.letsPlay()
    }

    override fun out_signIn(login: String) {
        Log.d("socket_worker", "out_signIn")
        Thread {
            writerStream?.println(OUT_SIGN_IN)
            writerStream?.println(login)
            writerStream?.flush()
        }.start()
    }

    override fun out_exit() {
        Log.d("socket_worker", "out_exit")
        Thread {
            writerStream?.println(OUT_EXIT)
            writerStream?.flush()
        }.start()
    }

    override fun out_requestToPlay(targetClient: String) {
        Log.d("socket_worker", "out_requestToPlay to $targetClient")
        Thread {
            writerStream?.println(OUT_REQUEST_TO_PLAY)
            writerStream?.println(targetClient)
            writerStream?.flush()
        }.start()
    }

    override fun out_responseOnRequestToPlay(targetClient: String, isAccept: Boolean) {
        Log.d("socket_worker", "out_responseOnRequestToPlay to $targetClient $isAccept")
        Thread {
            writerStream?.println(OUT_RESPONSE_ON_REQUEST_TO_PLAY)
            writerStream?.println(targetClient)
            writerStream?.println(if (isAccept) ACCEPT else DECLINE)
            //asd
            writerStream?.flush()
        }.start()
    }

    override fun out_readyToPlay() {
        Log.d("socket_worker", "out_readyToPlay")
        Thread {
            writerStream?.println(OUT_READY_TO_PLAY)
            writerStream?.flush()
        }.start()
    }

    override fun out_getClientsList() {
        Log.d("socket_worker", "out_getClientsList")
        Thread {
            writerStream?.println(OUT_GET_CLIENTS_LIST)
            writerStream?.flush()
        }.start()
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
