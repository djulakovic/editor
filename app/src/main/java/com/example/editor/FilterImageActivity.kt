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

class FilterImageActivity : AppCompatActivity() {

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
        customAdapter = CustomAdapter(itemsList)
        val layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
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
        if (resultCode == Activity.RESULT_OK && requestCode == imagePickCode) {
            imageView.setImageURI(data?.data)

            //TODO izmeni ovo ispod
            data?.data?.let { FilterModel(it, "Filter 1") }?.let { itemsList.add(it) }
            data?.data?.let { FilterModel(it, "Filter 2") }?.let { itemsList.add(it) }
            data?.data?.let { FilterModel(it, "Filter 3") }?.let { itemsList.add(it) }
            data?.data?.let { FilterModel(it, "Filter 4") }?.let { itemsList.add(it) }
            data?.data?.let { FilterModel(it, "Filter 5") }?.let { itemsList.add(it) }

            customAdapter.notifyDataSetChanged()
        }
    }
}