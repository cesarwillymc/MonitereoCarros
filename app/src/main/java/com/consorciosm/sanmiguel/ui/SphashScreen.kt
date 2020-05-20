package com.consorciosm.sanmiguel.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.consorciosm.sanmiguel.R
import com.consorciosm.sanmiguel.base.BaseActivity
import com.consorciosm.sanmiguel.ui.auth.view.LoginActivity
import kotlinx.android.synthetic.main.activity_sphash_screen.*

class SphashScreen : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim)
        imgLogoSplash.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                navigationToPrincipal()
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
    }

    private fun navigationToPrincipal() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getLayout(): Int = R.layout.activity_sphash_screen
}