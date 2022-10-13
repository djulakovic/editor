package com.example.editor.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.editor.R
import java.io.File

internal class GridViewAdapter(private val context: Context, private val numbersInWords: ArrayList<String>) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView

    override fun getCount(): Int {
        return numbersInWords.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view

        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.grid_item, null)
        }

        imageView = convertView!!.findViewById(R.id.gridImage)
        getFileFromFilePath(numbersInWords[position],imageView)
        return convertView
    }
    private fun getFileFromFilePath(path:String,imageView:ImageView){
        val imgFile = File(path)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imageView.setImageBitmap(myBitmap)
        }
    }
}