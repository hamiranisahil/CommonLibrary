package com.example.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.library.R
import kotlinx.android.synthetic.main.dialog_list.*
import kotlinx.android.synthetic.main.list_item.view.*

class ListDialog(
    context: Context,
    var title: String,
    var list: List<String>,
    val onListItemClickListener: OnListItemClickListener
) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_list)

        dialog = this
        tvTitle.setText(title)
        rvList.adapter = ListAdapter(list, onListItemClickListener)
        rvList.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        var dialog: Dialog? = null
    }

    private class ListAdapter(val list: List<String>, val onListItemClickListener: OnListItemClickListener) :
        RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.tvData.setText(list[position])

            viewHolder.itemView.tvData.setOnClickListener({
                onListItemClickListener.onListItemClick(ListDialog.dialog!!, position)
            })
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

    interface OnListItemClickListener {
        fun onListItemClick(dialog: Dialog, position: Int)
    }
}
