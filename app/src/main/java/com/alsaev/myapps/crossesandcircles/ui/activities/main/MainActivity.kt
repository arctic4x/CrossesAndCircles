package com.alsaev.myapps.crossesandcircles.ui.activities.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alsaev.myapps.crossesandcircles.R

class MainActivity : AppCompatActivity(), MainContract.Vview {

    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)
    }

    override fun onStart() {
        super.onStart()

        presenter.init()
    }
}
