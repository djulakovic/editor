package com.example.editor.activities

import com.example.editor.adapters.GridViewAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.editor.R
import com.example.editor.helpers.StaticText

class SavedImagesActivity : AppCompatActivity() {
    private val itemsList = ArrayList<String>()
    lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_images)
        val numberOfSavedImages: Int = getNumberOfSavedImages()
        if (numberOfSavedImages > 0) {
            for (i in 1..numberOfSavedImages) {
                itemsList.add(getImagePathFromSharedPreferences(StaticText.sharedPreferencesImage+i))
            }
        }
        gridView = findViewById(R.id.gridView)
        val mainAdapter = GridViewAdapter(this, itemsList)
        gridView.adapter = mainAdapter
    }

    private fun getNumberOfSavedImages(): Int {
        val sharedPreferences = getSharedPreferences(StaticText.sharedPreferencesName, MODE_PRIVATE)
        val numberOfSavedImages = sharedPreferences.getString(StaticText.sharedPreferencesNumberOfSavedImages, "0")
        return numberOfSavedImages?.toInt() ?: 0
    }

    private fun getImagePathFromSharedPreferences(path: String): String {
        val sharedPreferences = getSharedPreferences(StaticText.sharedPreferencesName, MODE_PRIVATE)
        val numberOfSavedImages = sharedPreferences.getString(path, "")
        return numberOfSavedImages ?: ""
    }
}