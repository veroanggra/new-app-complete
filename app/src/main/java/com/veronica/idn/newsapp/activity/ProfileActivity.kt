package com.veronica.idn.newsapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.veronica.idn.newsapp.R
import com.veronica.idn.newsapp.model.Users
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    companion object {
        fun getLaunchService(from: Context) = Intent(from, ProfileActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()
        tv_logout.setOnClickListener(this)
        iv_back_profile.setOnClickListener(this)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (p0 in snapshot.children) {
                    val name = snapshot.child("fullName").value.toString()
                    val email = snapshot.child("email").value.toString()
                    tv_name_profile.text = name
                    tv_emai_profile.text = email
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_logout -> logOut()
            R.id.iv_back_profile -> startActivity(Intent(MainActivity.getLaunchService(this)))
        }
    }

    private fun logOut() {
        startActivity(Intent(SignInActivity.getLaunchService(this)))
        FirebaseAuth.getInstance().signOut()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(MainActivity.getLaunchService(this)))
    }
}