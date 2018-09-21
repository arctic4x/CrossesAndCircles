package com.alsaev.myapps.crossesandcircles.view

import android.app.Dialog
import android.content.Context
import android.text.Html
import com.alsaev.myapps.crossesandcircles.R
import kotlinx.android.synthetic.main.dialog_decline_request.*

class DeclineRequestToPlayDialog(context: Context) : Dialog(context) {

    fun show(opponentName: String) {
        setContentView(R.layout.dialog_decline_request)
        tv_decline_requset.text = Html.fromHtml("Player <b>$opponentName</b><br>decline your request to play</br>")

        btn_ok.setOnClickListener {
            cancel()
        }

        show()
    }
}