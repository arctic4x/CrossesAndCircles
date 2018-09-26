package com.alsaev.myapps.crossesandcircles.ui.fragments.game

import android.util.Log
import com.alsaev.myapps.crossesandcircles.App
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter.*

class GamePresenter(val vview: GameContract.Vview) : GameContract.Presenter, NotificationCenter.NotificationCenterDelegate {
    override fun init() {
        subscribeNotificationCenter()
    }

    override fun didReceivedNotification(id: Int, vararg args: Any?) {
        when (id) {
            MyTurn -> {
                myTurn()
            }
            GetAction -> {
                getAction(args[0] as Int, args[1] as Int)
            }
            EndOfGame -> {
                endOfGame(args[0] as Boolean, args[1] as Int, args[2] as Int)
            }
        }
    }

    private fun endOfGame(isWin: Boolean, i1: Int, i2: Int) {
        if (isWin) vview.winGame(i1, i2)
        else vview.loseGame(i1, i2)
    }

    private fun getAction(figure: Int, position: Int) {
        vview.getAction(figure, position)
    }

    private fun myTurn() {
        Log.d("MyTurn", "myTurn")
        vview.myTurn()
    }

    override fun onDestroy() {
        unSubscribeNotificationCenter()
    }

    private fun unSubscribeNotificationCenter() {
        NotificationCenter.getInstance().removeObserver(this, MyTurn)
        NotificationCenter.getInstance().removeObserver(this, GetAction)
        NotificationCenter.getInstance().removeObserver(this, EndOfGame)
    }

    private fun subscribeNotificationCenter() {
        NotificationCenter.getInstance().addObserver(this, MyTurn)
        NotificationCenter.getInstance().addObserver(this, GetAction)
        NotificationCenter.getInstance().addObserver(this, EndOfGame)
    }

    override fun makeAction(position: Int) {
        App.instance.sendAction(position)
    }

    override fun finish(winFigure: Int) {

    }


}