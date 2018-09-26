package com.alsaev.myapps.crossesandcircles.ui.fragments.game

class GameContract {
    interface Vview {
        fun myTurn()
        fun getAction(figure: Int, position: Int)

    }

    interface Presenter {
        fun init()
        fun onDestroy()
        fun makeAction(position: Int)
        fun finish(winFigure: Int)
    }
}