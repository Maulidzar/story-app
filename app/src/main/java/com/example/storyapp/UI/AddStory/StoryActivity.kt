package com.example.storyapp.UI.AddStory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.Data.StoryData.StoryResult
import com.example.storyapp.Others.rotateFile
import com.example.storyapp.Others.uriToFile
import com.example.storyapp.R
import com.example.storyapp.UI.Camera.CamActivity
import com.example.storyapp.UI.Main.MainActivity
import com.example.storyapp.databinding.ActivityStoryBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class StoryActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private var _activityStoryBinding : ActivityStoryBinding? = null
    private lateinit var fusedLocation: FusedLocationProviderClient
    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory.getInstance(application, dataStore)
    }
    private val binding get() = _activityStoryBinding!!
    private var getFile: File? = null
    private var loc: Location? = null
    private val intenCamX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.imgPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }
    private val intentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@StoryActivity)
                getFile = myFile
                binding.imgPreview.setImageURI(uri)
            }
        }
    }
    private val camPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startCam()
        }else{
            Toast.makeText(this, getString(R.string.permission), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityStoryBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }
        binding.btnCamera.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA))  startCam()
            else camPermission.launch(Manifest.permission.CAMERA)
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.switchLocation.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked){
                getLastLocation()
            }else{
                loc = null
            }
        }
    }
    private fun getLastLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            fusedLocation.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.loc = location
                } else {
                    Toast.makeText(this@StoryActivity, getString(R.string.loc_error), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getLastLocation()
                }
                else -> {}
            }
        }
    private fun uploadImage() {
        val description = binding.etDescription.text.toString().trim()
        if (description.isNotBlank() && getFile != null) {
            val file = getFile as File

            storyViewModel.uploadStory(description, file,loc?.latitude, loc?.longitude).observe(this){
                when(it){
                    is StoryResult.isLoading -> {
                        binding.progressBarStory.visibility = View.VISIBLE
                    }
                    is StoryResult.isSuccess -> {
                        binding.progressBarStory.visibility = View.GONE
                        setResult(MainActivity.RESULT_OK, Intent())
                        finish()
                    }
                    is StoryResult.isError -> {
                        binding.progressBarStory.visibility = View.GONE
                        Toast.makeText(this@StoryActivity, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            Toast.makeText(this@StoryActivity, getString(R.string.story_incorrect), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        intentGallery.launch(chooser)
    }

    private fun startCam() {
        val intent = Intent(this, CamActivity::class.java)
        intenCamX.launch(intent)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityStoryBinding = null
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}