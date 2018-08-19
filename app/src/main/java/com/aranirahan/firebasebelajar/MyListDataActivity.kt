package com.aranirahan.firebasebelajar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.toast
import com.google.firebase.database.DataSnapshot


class MyListDataActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var reference: DatabaseReference
    private lateinit var dataMahasiswa: ArrayList<DataMahasiswa>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)

        recyclerView = findViewById(R.id.rv_datalist)

        supportActionBar?.title = "Data Mahasiswa"
        auth = FirebaseAuth.getInstance()
        showRecyclerView()
        getData()
    }

    fun getData() {
        toast("Mohon tunggu sebentar...")
        reference = FirebaseDatabase.getInstance().reference
        auth.uid?.let { it ->
            reference.child("Admin").child(it).child("Mahasiswa")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            toast("Data gagal dimuat")
                            Log.e("MyListActivity ", "${databaseError.details} ${databaseError.message}")
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            try {
                                dataMahasiswa = ArrayList()
                                for (snapshot in dataSnapshot.children) {
                                    //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                                    val mahasiswa = snapshot.getValue(DataMahasiswa::class.java)

                                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                                    snapshot.key?.let { mahasiswa?.key = it }

                                    mahasiswa?.let { it -> dataMahasiswa.add(it) }
                                }

                                adapter = RecyclerViewAdapter(dataMahasiswa, this@MyListDataActivity)
                                recyclerView.adapter = adapter
                                toast("Data berhasil dimuat")
                            } catch (e: Exception) {
                                Log.e("MyListDataActivty", e.toString())
                            }
                        }

                    })
        }
    }

    fun showRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        val itemDecoration: DividerItemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(applicationContext, R.drawable.line)?.let { itemDecoration.setDrawable(it) }
        recyclerView.addItemDecoration(itemDecoration)
    }
}
