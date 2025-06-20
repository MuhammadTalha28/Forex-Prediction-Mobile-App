package com.example.Forex

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class TestingActivity : BaseActivity() {

    private lateinit var buttonSelectImage: MaterialButton
    private lateinit var buttonOpenCamera: MaterialButton
    private lateinit var buttonStartTest: MaterialButton
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewResult: TextView
    private var imageUri: Uri? = null

    private val REQUEST_IMAGE_PICK = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_testing, findViewById(R.id.content_frame))

        setNavigationViewItemChecked(R.id.nav_testing)

        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonOpenCamera = findViewById(R.id.buttonOpenCamera)
        buttonStartTest = findViewById(R.id.buttonStartTest)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)
        textViewResult = findViewById(R.id.textViewResult)

        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        buttonOpenCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }

        buttonStartTest.isEnabled = false
        buttonStartTest.setBackgroundColor(resources.getColor(R.color.grey))

        buttonStartTest.setOnClickListener {
            imageUri?.let { uri ->
                progressBar.visibility = ProgressBar.VISIBLE
                textViewResult.text = ""
                uploadImage(uri)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    imageUri = data?.data
                    imageView.setImageURI(imageUri)
                    onImageAttached()
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageUri = getImageUri(imageBitmap)
                    imageView.setImageBitmap(imageBitmap)
                    onImageAttached()
                }
            }
        }
    }

    private fun onImageAttached() {
        buttonStartTest.isEnabled = true
        buttonStartTest.setBackgroundColor(resources.getColor(R.color.blue))
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "temp_image", null)
        return Uri.parse(path)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex: Int = cursor?.getColumnIndex(filePathColumn[0]) ?: 0
        val filePath: String = cursor?.getString(columnIndex) ?: ""
        cursor?.close()

        return if (filePath.isNotEmpty()) {
            File(filePath)
        } else {
            // Create a temporary file from the URI
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val tempFile = File(context.cacheDir, "temp_image")
            val outputStream: OutputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            tempFile
        }
    }

    private fun uploadImage(uri: Uri) {
        val file = getFileFromUri(this, uri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.4.28.89:5000")  // Replace with your Flask server's IP
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FlaskApiService::class.java)
        service.predict(body).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                progressBar.visibility = ProgressBar.GONE
                if (response.isSuccessful) {
                    val result = response.body()?.prediction
                    textViewResult.text = "Prediction: $result"
                } else {
                    Toast.makeText(this@TestingActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(this@TestingActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

interface FlaskApiService {
    @Multipart
    @POST("/predict")
    fun predict(@Part file: MultipartBody.Part): Call<PredictionResponse>
}

data class PredictionResponse(val prediction: String)
