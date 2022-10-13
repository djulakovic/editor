package com.example.editor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.editor.R

class MainActivity : AppCompatActivity() {

    private lateinit var editNewImageButton: Button;
    private lateinit var savedImagesButton: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchViewChildren();
        initClickListeners();
    }

    private fun fetchViewChildren() {
        editNewImageButton = findViewById(R.id.editImageButton);
        savedImagesButton = findViewById(R.id.savedImagesButton);
    }

    private fun initClickListeners() {
        editNewImageButton.setOnClickListener { editNewImageButtonAction() };
        savedImagesButton.setOnClickListener { savedImagesButtonAction() }
    }

    private fun editNewImageButtonAction() {
        val intent = Intent(this@MainActivity, FilterImageActivity::class.java)
        startActivity(intent)
    }

    private fun savedImagesButtonAction() {
        val intent = Intent(this@MainActivity, SavedImagesActivity::class.java)
        startActivity(intent)
    }
}