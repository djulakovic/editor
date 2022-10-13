package com.example.editor

import FilterModel
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zomato.photofilters.SampleFilters
import com.zomato.photofilters.imageprocessors.Filter


class FilterImageActivity : AppCompatActivity() {

    init {
        System.loadLibrary("NativeImageProcessor")
    }

    private val itemsList = ArrayList<FilterModel>()
    private lateinit var customAdapter: CustomAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageView: ImageView;


    private val intentType = "image/*";
    private val imagePickCode = 100
    private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_image)
        fetchViewChildren()
        initRecycleView()
        pickImage()
    }

    private fun initRecycleView() {
        customAdapter = CustomAdapter(itemsList,imageView)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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

    private fun fillImageFilters(data: Intent?){
        data?.data?.let { FilterModel(it, R.string.originalFilter, Filter()) }?.let { itemsList.add(it) }
        data?.data?.let { FilterModel(it, R.string.starLitFiler, SampleFilters.getStarLitFilter()) }?.let { itemsList.add(it) }
        data?.data?.let { FilterModel(it, R.string.blueMessFilter, SampleFilters.getBlueMessFilter()) }?.let { itemsList.add(it) }
        data?.data?.let { FilterModel(it, R.string.stuckVibeFilter, SampleFilters.getAweStruckVibeFilter()) }?.let { itemsList.add(it) }
        data?.data?.let { FilterModel(it, R.string.limeStutterFilter, SampleFilters.getLimeStutterFilter()) }?.let { itemsList.add(it) }
        data?.data?.let { FilterModel(it, R.string.nightWhisperFilter, SampleFilters.getNightWhisperFilter()) }?.let { itemsList.add(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imagePickCode && data != null) {
            imageView.setImageURI(data.data)
            fillImageFilters(data)
            customAdapter.notifyDataSetChanged()
        }
    }
}