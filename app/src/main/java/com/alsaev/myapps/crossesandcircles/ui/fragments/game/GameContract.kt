package com.alsaev.myapps.crossesandcircles.ui.fragments.game

class GameContract {
    interface Vview {
        fun myTurn()
        fun getAction(figure: Int, position: Int)
        fun winGame(i1: Int, i2: Int)
        fun loseGame(i1: Int, i2: Int)

    }

    interface Presenter {
        fun init()
        fun onDestroy()
        fun makeAction(position: Int)
        fun finish(winFigure: Int)
    }
}