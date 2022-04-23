package com.dicoding.picodiploma.submission_story_app.ui.postStory

import android.Manifest
import android.content.Intent
import android.content.Intent.EXTRA_USER
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.util.Util
import com.dicoding.picodiploma.submission_story_app.R
import com.dicoding.picodiploma.submission_story_app.databinding.ActivityCameraBinding
import com.dicoding.picodiploma.submission_story_app.databinding.ActivityPostStoryBinding
import com.dicoding.picodiploma.submission_story_app.databinding.ActivitySignUpBinding
import com.dicoding.picodiploma.submission_story_app.model.UserModel
import com.dicoding.picodiploma.submission_story_app.ui.Utils
import com.dicoding.picodiploma.submission_story_app.ui.camera.CameraActivity
import com.dicoding.picodiploma.submission_story_app.ui.login.LoginViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PostStoryActivity : AppCompatActivity() {
    private lateinit var user: UserModel
    private var getFile: File? = null
    private var result: Bitmap? = null
    private val binding: ActivityPostStoryBinding by lazy {
        ActivityPostStoryBinding.inflate(layoutInflater)
    }

    private val viewModel: PostStoryViewModel by viewModels()

    companion object {
        const val EXTRA_DATA = "data"
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }


        user = intent.getParcelableExtra(EXTRA_DATA)!!

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.camBtn.setOnClickListener { startCameraX() }
        binding.galleryBtn.setOnClickListener { startGallery() }
        binding.btnPost.setOnClickListener { uploadImage() }

    }

    private fun startCameraX() {
        launcherIntentCameraX.launch(Intent(this, CameraActivity::class.java))
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result =
                Utils.rotateBitmap(
                    BitmapFactory.decodeFile(getFile?.path),
                    isBackCamera
                )
        }
        binding.imgPost.setImageBitmap(result)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = Utils.uriToFile(selectedImg, this@PostStoryActivity)
            getFile = myFile
            binding.imgPost.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {

        if (getFile != null) {
            val file = Utils.reduceFileImage(getFile as File)

            val description = binding.etCaption.text.toString()
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            // upload image
            viewModel.uploadImage(user, description, imageMultipart, object : Utils.ApiCallbackString {
                override fun onResponse(success: Boolean, message: String) {
                    showAlertDialog(success, message)
                }
            })

        } else {
            Utils.showToast(this@PostStoryActivity, getString(R.string.input_file_first))
        }
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.success))
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    // Do nothing
                }
                finish()
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.failed) + ", $message")
                setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}