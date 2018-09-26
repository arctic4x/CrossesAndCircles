package com.alsaev.myapps.crossesandcircles.ui.fragments.menu


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alsaev.myapps.crossesandcircles.App

import com.alsaev.myapps.crossesandcircles.R
import com.alsaev.myapps.crossesandcircles.view.DeclineRequestToPlayDialog
import com.alsaev.myapps.crossesandcircles.view.RequestToPlayDialog
import kotlinx.android.synthetic.main.client_item.view.*
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment(), MenuContract.Vview {

    private lateinit var presenter: MenuContract.Presenter

    private var fragmentInteraction: FragmentInteraction? = null
    private lateinit var adapter: ClientsRVAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        presenter = MenuPresenter(this)
//        NotificationCenter.getInstance().addObserver(this, )
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.init()
        rv_clients.layoutManager = LinearLayoutManager(context)
        adapter = ClientsRVAdapter(ArrayList())
        rv_clients.adapter = adapter
        //rv_clients.adapter = ClientsRVAdapter(arrayListOf("player 1", "player 2", "player 3", "player 4", "player 2", "player 3", "player 4", "player 2", "player 3", "player 4"))
    }

    override fun setItems(items: ArrayList<String>) {
        if (items.isNotEmpty())
            Handler(Looper.getMainLooper()).post {
                adapter.setItems(items)
            }
    }

    override fun addItem(item: String) {
        Handler(Looper.getMainLooper()).post {
            adapter.addItem(item)
        }
    }

    override fun removeItem(item: String) {
        Handler(Looper.getMainLooper()).post {
            adapter.removeItem(item)
        }
    }

    class ClientsRVAdapter(var clients: ArrayList<String>) : RecyclerView.Adapter<ClientsRVAdapter.ClientHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ClientHolder {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.client_item, p0, false)
            val holder = ClientHolder(view)
            view.btn_request_to_play.setOnClickListener {
                val adapterPostition = holder.adapterPosition
                if (adapterPostition != RecyclerView.NO_POSITION) {
                    App.instance.sendRequestToPlay(view.tv_client_name.text.toString())
                }
            }

            return holder
        }

        fun setItems(clients: ArrayList<String>) {
            this.clients = clients
            notifyDataSetChanged()
        }

        fun addItem(client: String) {
            clients.add(client)
            notifyItemInserted(clients.size - 1)
        }

        fun removeItem(client: String) {
            val id = clients.indexOf(client)
            clients.removeAt(id)
            notifyItemInserted(id)
        }

        override fun getItemCount(): Int {
            return clients.size
        }

        override fun onBindViewHolder(p0: ClientHolder, p1: Int) {
            p0.bind(clients[p1])
        }

        class ClientHolder(v: View) : RecyclerView.ViewHolder(v) {
            fun bind(clientName: String) {
                itemView.tv_client_name.text = clientName
                itemView.setOnClickListener {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentInteraction = context as FragmentInteraction
    }

    override fun onDetach() {
        super.onDetach()
        fragmentInteraction = null
    }

    override fun getLogin(): String {
        return if (fragmentInteraction != null) fragmentInteraction!!.getLogin() else "Unnamed"
    }

    override fun setLogin(login: String) {
        Handler(Looper.getMainLooper()).post {
            tv_your_name.text = login.toString()
        }
    }

    interface FragmentInteraction {
        fun getLogin(): String
    }

    override fun showRequestToPlay(opponentName: String) {
        Handler(Looper.getMainLooper()).post {
            RequestToPlayDialog(context!!).show(opponentName, object : RequestToPlayDialog.DialogClickListener {
                override fun accept(opponentName: String) {
                    App.instance.acceptRequestToPlay(opponentName)
                }

                override fun decline() {
                    App.instance.declineRequestToPlay(opponentName)
                }
            })
        }
    }

    override fun showDeclineRequestToPlay(opponentName: String) {
        Handler(Looper.getMainLooper()).post {
            DeclineRequestToPlayDialog(context!!).show(opponentName)
        }
    }

    override fun readyToPlay(opponentName: String) {
        Handler(Looper.getMainLooper()).post {
            Log.d("READY TO PLAY", "WITH $opponentName")
        }
    }
}
