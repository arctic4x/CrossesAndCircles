package com.alsaev.myapps.crossesandcircles.view

import android.app.Dialog
import android.content.Context
import android.text.Html
import com.alsaev.myapps.crossesandcircles.R
import kotlinx.android.synthetic.main.dialog_request_to_play.*

class RequestToPlayDialog(context: Context) : Dialog(context) {

    var dialogClickListener: DialogClickListener? = null

    fun show(opponentName: String, dialogClickListener: DialogClickListener) {
        this.dialogClickListener = dialogClickListener
        setContentView(R.layout.dialog_request_to_play)

        tv_dialog.text = Html.fromHtml("Player <b>$opponentName</b><br>invite you to play</br>")

        btn_accept.setOnClickListener {
            dialogClickListener.accept(opponentName)
            cancel()
        }

        btn_decline.setOnClickListener {
            dialogClickListener.decline()
            cancel()
        }

        show()
    }

    override fun cancel() {
        super.cancel()
    }

    interface DialogClickListener {
        fun accept(opponentName: String)
        fun decline()
    }
}