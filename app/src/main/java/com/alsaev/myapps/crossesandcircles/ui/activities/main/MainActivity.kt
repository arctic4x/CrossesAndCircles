package com.alsaev.myapps.crossesandcircles.ui.activities.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alsaev.myapps.crossesandcircles.R
import com.alsaev.myapps.crossesandcircles.ui.fragments.game.GameFragment
import com.alsaev.myapps.crossesandcircles.ui.fragments.menu.MenuFragment
import com.alsaev.myapps.crossesandcircles.ui.fragments.registration.RegistrationFragment
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter.*
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.Vview,
        RegistrationFragment.FragmentInteraction,
        MenuFragment.FragmentInteraction, GameFragment.FragmentInteraction {

    private lateinit var presenter: MainContract.Presenter
    private var login = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)
    }

    override fun onStart() {
        super.onStart()

        presenter.init()
    }

    override fun openRegistrationFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_contrainer, RegistrationFragment()).addToBackStack(null).commit()
    }

    override fun openMenuFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_contrainer, MenuFragment()).addToBackStack(null).commit()
    }

    override fun setLogin(login: String) {
        this.login = login
    }

    override fun getLogin(): String {
        return login
    }

    override fun openGameFragment(opponent: String) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_contrainer, GameFragment.newInstance(opponent)).addToBackStack(null).commit()
    }
}
