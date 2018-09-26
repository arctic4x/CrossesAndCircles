package com.alsaev.myapps.crossesandcircles.ui.fragments.game


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alsaev.myapps.crossesandcircles.App

import com.alsaev.myapps.crossesandcircles.R
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import com.alsaev.myapps.crossesandcircles.view.GameFieldView
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment(), GameContract.Vview {

    private lateinit var presenter: GameContract.Presenter

    private var fragmentInteraction: FragmentInteraction? = null
    private var opponent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        opponent = arguments!!.getString("opponent", "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        presenter = GamePresenter(this)
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.init()

        tv_your_name.text = fragmentInteraction?.getLogin()
        tv_opponent_name.text = opponent

        game_field.fieldInteraction = object : GameFieldView.FieldInteraction {
            override fun onFinish(winFigure: Int) {
                presenter.finish(winFigure)
            }

            override fun makeAction(position: Int) {
                presenter.makeAction(position)
            }
        }

        App.instance.imReady()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun myTurn() {
        Log.d("turn", "my")
        game_field.canTouch = true
    }

    override fun getAction(figure: Int, position: Int) {
        Handler(Looper.getMainLooper()).post {
            game_field.setFigureInSpan(figure, position)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentInteraction = (context as FragmentInteraction)
    }

    override fun onDetach() {
        fragmentInteraction = null
        super.onDetach()
    }

    interface FragmentInteraction {
        fun getLogin(): String
    }

    override fun winGame(i1: Int, i2: Int) {
        Handler(Looper.getMainLooper()).post {
            game_field.drawWinLine(i1, i2)
            Toast.makeText(context, "Вы победили", Toast.LENGTH_LONG).show()
        }
    }

    override fun loseGame(i1: Int, i2: Int) {
        Handler(Looper.getMainLooper()).post {
            game_field.drawWinLine(i1, i2)
            Toast.makeText(context, "Вы проиграли", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(opponent: String): Fragment {
            val fragment = GameFragment()
            val args = Bundle()
            args.putString("opponent", opponent)
            fragment.arguments = args
            return fragment
        }
    }
}
