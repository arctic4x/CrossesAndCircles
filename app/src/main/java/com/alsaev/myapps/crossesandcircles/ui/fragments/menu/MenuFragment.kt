package com.alsaev.myapps.crossesandcircles.ui.fragments.menu


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alsaev.myapps.crossesandcircles.R
import com.alsaev.myapps.crossesandcircles.utils.NotificationCenter
import kotlinx.android.synthetic.main.client_item.view.*
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment(), NotificationCenter.NotificationCenterDelegate {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

//        NotificationCenter.getInstance().addObserver(this, )
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_clients.layoutManager = LinearLayoutManager(context)
    }

    override fun didReceivedNotification(id: Int, vararg args: Any?) {
    }

    class ClientsRVAdapter(var clients: ArrayList<String>) : RecyclerView.Adapter<ClientsRVAdapter.ClientHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ClientHolder {
            return ClientHolder(LayoutInflater.from(p0.context).inflate(R.layout.client_item, p0, false))
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
            }
        }
    }
}
