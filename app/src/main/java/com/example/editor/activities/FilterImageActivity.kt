package com.example.editor.activities

import FilterModel
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.editor.adapters.FiltersAdapter
import com.example.editor.R
import com.example.editor.helpers.StaticText
import com.zomato.photofilters.SampleFilters
import com.zomato.photofilters.imageprocessors.Filter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FilterImageActivity : AppCompatActivity() {

    init {
        System.loadLibrary("NativeImageProcessor")
    }

    private val itemsList = ArrayList<FilterModel>()
    private lateinit var customAdapter: FiltersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageView: ImageView;


    private val intentType = "image/*";
    private val imagePickCode = 100
    private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        editActionBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_image)
        fetchViewChildren()
        initRecycleView()
        pickImage()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, R.string.permissions, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imagePickCode && data != null) {
            imageView.setImageURI(data.data)
            fillImageFilters(data)
            customAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_icons, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveImage(imageView.drawable)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun fillImageFilters(data: Intent?) {
        data?.data?.let { FilterModel(it, R.string.originalFilter, Filter()) }?.let { itemsList.add(it) }
        data?.data?.let { FilterModel(it, R.string.starLitFiler, SampleFilters.getStarLitFilter()) }?.let { itemsList.add(it) }
        data?.data?.let {FilterModel(it, R.string.blueMessFilter, SampleFilters.getBlueMessFilter()) }?.let { itemsList.add(it) }
        data?.data?.let {FilterModel(it, R.string.stuckVibeFilter, SampleFilters.getAweStruckVibeFilter()) }?.let { itemsList.add(it) }
        data?.data?.let {FilterModel(it, R.string.limeStutterFilter, SampleFilters.getLimeStutterFilter()) }?.let { itemsList.add(it) }
        data?.data?.let {FilterModel(it, R.string.nightWhisperFilter, SampleFilters.getNightWhisperFilter()) }?.let { itemsList.add(it) }
    }

    private fun getDisc(): File {
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(file, StaticText.albumName)
    }

    private fun saveImage(drawable: Drawable) {
        val newFile = getFileFromDrawable()
        saveFileToSharedPreferences(newFile)

        try {
            saveImageToGallery(drawable, newFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getFileFromDrawable(): File {
        val file = getDisc()

        if (!file.exists() && !file.mkdirs()) {
            file.mkdir()
        }

        val simpleDateFormat = SimpleDateFormat("yyyymmsshhmmss")
        val date = simpleDateFormat.format(Date())
        val name = "IMG$date.jpg"
        val fileName = file.absolutePath + "/" + name
        return File(fileName)
    }

    private fun saveImageToGallery(drawable: Drawable, newFile: File) {
        val draw = drawable as BitmapDrawable
        val bitmap = draw.bitmap
        val fileOutPutStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutPutStream)
        Toast.makeText(this, "File saved successfully", Toast.LENGTH_SHORT).show()
        fileOutPutStream.flush()
        fileOutPutStream.close()
        finish()
    }

    private fun saveFileToSharedPreferences(newFile: File) {
        var numberOfSavedImages: Int = getNumberOfSavedImages()
        numberOfSavedImages++
        saveStringToSharedPreferences(newFile.absolutePath, StaticText.sharedPreferencesImage + numberOfSavedImages)
        saveStringToSharedPreferences(numberOfSavedImages.toString(), StaticText.sharedPreferencesNumberOfSavedImages)
    }

    private fun saveStringToSharedPreferences(path: String, field: String) {
        val sharedPreferences = getSharedPreferences(StaticText.sharedPreferencesName, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(field, path)
        editor.apply()
    }

    private fun getNumberOfSavedImages(): Int {
        val sharedPreferences = getSharedPreferences(StaticText.sharedPreferencesName, MODE_PRIVATE)
        val numberOfSavedImages = sharedPreferences.getString(StaticText.sharedPreferencesNumberOfSavedImages, "0")
        return numberOfSavedImages?.toInt() ?: 0
    }

    private fun initRecycleView() {
        customAdapter = FiltersAdapter(itemsList, imageView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
    }

    private fun fetchViewChildren() {
        imageView = findViewById(R.id.imageView)
        recyclerView = findViewById(R.id.recycler_view)
    }

    private fun pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, permissionCode);
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = intentType
        startActivityForResult(intent, imagePickCode)
    }

    private fun editActionBar() {
        supportActionBar?.title = "Filters"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.DKGRAY))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }
}