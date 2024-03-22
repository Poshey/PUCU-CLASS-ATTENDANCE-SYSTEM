package com.poshey.upuasatt2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScannedDataAdapter(private var dataList: List<ScannedData>) :
    RecyclerView.Adapter<ScannedDataAdapter.ScannedDataViewHolder>() {





    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedDataViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scanned_data, parent, false)
        return ScannedDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScannedDataViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.nameTextView.text = currentItem.fname
        holder.lNameTextView.text = currentItem.lname
        holder.numberTextView.text = currentItem.number
        holder.timeTextView.text = currentItem.time

        holder.editButton.setOnClickListener {
            listener?.onEditClick(position)
        }

        holder.deleteButton.setOnClickListener {
            listener?.onDeleteClick(position)
        }
    }

    override fun getItemCount() = dataList.size

    inner class ScannedDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.fName)
        val lNameTextView: TextView = itemView.findViewById(R.id.lName)
        val numberTextView: TextView = itemView.findViewById(R.id.numS)
        val timeTextView: TextView = itemView.findViewById(R.id.time)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }


    fun setScannedDataList(newList: List<ScannedData>) {
        dataList = newList // Update the data list directly
        notifyDataSetChanged()
    }

}
