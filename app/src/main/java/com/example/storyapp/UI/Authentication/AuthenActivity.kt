package com.example.storyapp.UI.Authentication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.commit
import com.example.storyapp.R
import com.example.storyapp.UI.Main.MainActivity

class AuthenActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private val mFragManager = supportFragmentManager
    private val mLoginFrag = LoginFragment()
    private val fragment = mFragManager.findFragmentByTag(LoginFragment::class.java.simpleName)
    private val authenViewModel: AuthenViewModel by viewModels {
        AuthViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authen)

        authenViewModel.isLogin().observe(this){
            if(!it.isNullOrEmpty()){
                startActivity(Intent(this@AuthenActivity, MainActivity::class.java))
                finish()
            }
        }

        if (fragment !is LoginFragment){
            mFragManager.commit {
                add(R.id.frame_authen, mLoginFrag, LoginFragment::class.java.simpleName)
            }
        }
    }
}