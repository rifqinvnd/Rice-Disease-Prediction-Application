package com.bangkit.sb.apppadi.scanning

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.sb.apppadi.R
import com.bangkit.sb.apppadi.databinding.ActivityScanBinding
import com.bangkit.sb.apppadi.ml.ModelQuant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Suppress("DEPRECATION")
class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private lateinit var result: TextView
    private lateinit var bitmap: Bitmap
    private lateinit var imageView: ImageView
    private lateinit var btnGallery: Button
    private lateinit var btnCamera: Button
    private lateinit var btnScan: Button
//    private lateinit var image:Bitmap
//    private var imageSize = 224


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        result = findViewById(R.id.result)
        imageView = findViewById(R.id.imageView)
        btnGallery = findViewById(R.id.to_gallery)
        btnCamera = findViewById(R.id.to_camera)
        btnScan = findViewById(R.id.scan)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        btnGallery.setOnClickListener {
            Log.d("Msg", "Button Pressed")
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 150)
        }

        btnCamera.setOnClickListener {
            val toCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(toCamera, 100)
        }

        btnScan.setOnClickListener {
            val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = ModelQuant.newInstance(this)

            val tBuffer = TensorImage.fromBitmap(resized)
            val byteBuffer = tBuffer.buffer

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray)

            val classes = arrayOf("Brown Spot", "Healthy", "Hispa", "Leaf Blast")

            result.text = classes[max]
// Releases model resources if no longer used.
            model.close()

        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 150){
            imageView.setImageURI(data?.data)

            val uri : Uri?= data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
        else if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
        }

    }

    private fun getMax(arr:FloatArray) : Int{
        var ind = 0
        var min = 0.0f

        for(i in 0..1000)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i
            }
        }
        return ind
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}