package com.example.storyapp.UI.Detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.storyapp.Data.StoryData.StoryResult
import com.example.storyapp.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private var _activityDetailBinding: ActivityDetailBinding? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private val binding get() = _activityDetailBinding!!
    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory.getInstance(application, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra(EXTRA_ID)

        if(id != null && detailViewModel.story.value == null) {
            detailViewModel.getDetailStories(id)
        }

        detailViewModel.story.observe(this){result ->
            when(result){
                is StoryResult.isSuccess -> {
                    binding.progressDetail.visibility = View.GONE
                    Glide.with(this)
                        .load(result.data.photoUrl)
                        .into(binding.imgStories)
                    binding.apply {
                        tvName.text = result.data.name
                        tvDescription.text = result.data.description
                    }
                    binding.tvName.text = result.data.name
                }
                is StoryResult.isError ->{
                    binding.progressDetail.visibility = View.GONE
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                }
                is StoryResult.isLoading ->{
                    binding.progressDetail.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityDetailBinding = null
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }
}