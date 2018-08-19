package com.aranirahan.firebasebelajar

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    companion object {
        const val REQUEST_CODE_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress.visibility = View.GONE
        auth = FirebaseAuth.getInstance()

        Log.d("MainAuth : ", auth.currentUser.toString())

        if (auth.currentUser == null) {
            defaultUI()
        } else {
            updateUI()
        }

        login.setOnClickListener {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Collections.singletonList(AuthUI.IdpConfig.GoogleBuilder().build()))
                    .setIsSmartLockEnabled(false)
                    .build(),
                    REQUEST_CODE_SIGN_IN)
            progress.visibility = View.VISIBLE
        }

        logout.setOnClickListener { it ->
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        toast("Logout berhasil")
                        finish()
                    }
        }

        save.setOnClickListener { it ->
            //Mendapatkan UserID dari pengguna yang Terautentikasi
            val getUserID = auth.currentUser?.uid

            val database = FirebaseDatabase.getInstance()
            val getReference = database.reference

            val nim = tv_nim.text.toString()
            val nama = tv_nama.text.toString()
            val jurusan = tv_jurusan.text.toString()

            if (nim.isEmpty() && nama.isEmpty() && jurusan.isEmpty())
                toast("Data tidak boleh ada yang kosong")
            else if (getUserID != null)
                getReference.child("Admin").child(getUserID).child("Mahasiswa").push()
                        .setValue(DataMahasiswa(nim, nama, jurusan))
                        .addOnSuccessListener {
                            tv_nim.setText("")
                            tv_nama.setText("")
                            tv_jurusan.setText("")
                            toast("Data tersimpan")
                        }
        }

        showdata.setOnClickListener {
            startActivity<MyListDataActivity>()
        }
    }

    private fun defaultUI() {
        logout.isEnabled = false
        save.isEnabled = false
        showdata.isEnabled = false
        login.isEnabled = true
        tv_nim.isEnabled = false
        tv_nama.isEnabled = false
        tv_jurusan.isEnabled = false
    }

    private fun updateUI() {
        logout.isEnabled = true
        save.isEnabled = true
        showdata.isEnabled = true
        login.isEnabled = false
        tv_nim.isEnabled = true
        tv_nama.isEnabled = true
        tv_jurusan.isEnabled = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                toast("Login berhasil")
                updateUI()
                progress.visibility = View.GONE
            } else {
                progress.visibility = View.GONE
                toast("Login dibatalkan")
            }
        }
    }

    private fun Activity.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
