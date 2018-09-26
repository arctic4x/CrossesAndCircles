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
                getAction(args[0] as Int,args[1] as Int)
            }
        }
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
        NotificationCenter.getInstance().addObserver(this, MyTurn)
        NotificationCenter.getInstance().addObserver(this, GetAction)
    }

    private fun subscribeNotificationCenter() {
        NotificationCenter.getInstance().removeObserver(this, MyTurn)
        NotificationCenter.getInstance().removeObserver(this, GetAction)
    }

    override fun makeAction(position: Int) {
        App.instance.sendAction(position)
    }

    override fun finish(winFigure: Int) {

    }


}