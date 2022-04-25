package com.example.kotlintest.common.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.common.model.Repository

class RecyclerViewAdapter(context: Context, data: MutableList<Repository>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var repositories: MutableList<Repository>
    private var inflater: LayoutInflater
    lateinit var listener: ItemClickListener
    lateinit var data: MutableList<Repository>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository: Repository = repositories[position]
        holder.textView1.text = repository.getName()
        holder.textView2.text = repository.getDescription()
    }


    override fun getItemCount(): Int {
        return repositories.size
    }

    fun addItems(list: List<Repository>) {
        repositories.addAll(list)
    }


    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView1: TextView
        var textView2: TextView
        override fun onClick(view: View) {
            listener.onItemClick(view, getAdapterPosition())
        }

        init {
            textView1 = itemView.findViewById(R.id.textView1)
            textView2 = itemView.findViewById(R.id.textView2)
            itemView.setOnClickListener(this)
        }
    }

    fun getItem(id: Int): Repository {
        return repositories[id]
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        listener = itemClickListener

    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    init {
        inflater = LayoutInflater.from(context)
        repositories = data
    }

}