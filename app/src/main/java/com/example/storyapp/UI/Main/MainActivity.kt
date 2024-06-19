package com.example.storyapp.UI.Main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.Adapter.LoadingAdapter
import com.example.storyapp.Adapter.StoryAdapter
import com.example.storyapp.Data.StoryData.StoryResult
import com.example.storyapp.R
import com.example.storyapp.UI.AddStory.StoryActivity
import com.example.storyapp.UI.Authentication.AuthenActivity
import com.example.storyapp.UI.Detail.DetailActivity
import com.example.storyapp.UI.Location.MapActivity
import com.example.storyapp.databinding.ActivityMainBinding

class  MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(application, dataStore)
    }
    private val launchStoryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                mainViewModel.getAllStories()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storyAdapter = StoryAdapter { story, optionsCompat ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, story.id)
            startActivity(intent, optionsCompat.toBundle())
        }
        binding.btnMap.setOnClickListener {
            launchStoryActivity.launch(Intent(this, MapActivity::class.java))
        }
        binding.btnStory.setOnClickListener {
            launchStoryActivity.launch(Intent(this, StoryActivity::class.java))
        }
        binding.listStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter =
                storyAdapter.withLoadStateFooter(footer = LoadingAdapter { storyAdapter.retry() })
        }
        mainViewModel.listStory.observe(this) {
            storyAdapter.submitData(lifecycle, it)
            if (it == null) Toast.makeText(
                this@MainActivity,
                getString(R.string.empty),
                Toast.LENGTH_SHORT
            ).show()
        }
        mainViewModel.isLogin().observe(this) {
            if (it.isNullOrEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.log_out),
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, AuthenActivity::class.java))
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting_languange -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.logout -> {
                mainViewModel.logout().observe(this) {
                    when (it) {
                        is StoryResult.isLoading -> {
                            binding.progressMain.visibility = View.VISIBLE
                        }
                        is StoryResult.isSuccess -> {
                            binding.progressMain.visibility = View.GONE
                        }
                        is StoryResult.isError -> {
                            binding.progressMain.visibility = View.GONE
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.error_logout),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                true
            }
            else -> true
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }
    companion object {
        const val RESULT_OK = 110
    }
}