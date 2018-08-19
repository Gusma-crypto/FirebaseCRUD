package com.aranirahan.firebasebelajar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView


class RecyclerViewAdapter(val listMahasiswa: ArrayList<DataMahasiswa>,
                          val context: Context)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_design, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listMahasiswa.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nim: TextView = itemView.findViewById(R.id.tv_nim)
        val nama: TextView = itemView.findViewById(R.id.tv_nama)
        val jurusan: TextView = itemView.findViewById(R.id.tv_jurusan)
        val listItem: LinearLayout = itemView.findViewById(R.id.lv_list_item)

    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {

        val nim = listMahasiswa[position].nim
        val nama = listMahasiswa[position].nama
        val jurusan = listMahasiswa[position].jurusan

        holder.nim.setText("nim: $nim")
        holder.nama.setText("nama: $nama")
        holder.jurusan.setText("jurusan: $jurusan")

        holder.listItem.setOnLongClickListener {
            true
        }
    }
}