package com.example.test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.model.Success

class ListTopicAdapter() :
    RecyclerView.Adapter<ListTopicAdapter.ViewHolder>() {
    private var postsList: MutableList<Success> = ArrayList()

    fun setData(postsList: List<Success>?) {
        this.postsList = postsList as MutableList<Success>;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_topic_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val success = postsList!![position]
        if (success == null) {
            return
        }
        holder.tvTitle.text = success.name
        holder.tvMount.text = success.soluong.toString()
    }

    override fun getItemCount(): Int {
        return if (postsList != null) {
            postsList!!.size
        } else 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val tvMount: TextView

        init {
            tvTitle = itemView.findViewById(R.id.topic)
            tvMount = itemView.findViewById(R.id.amount)
        }
    }
}