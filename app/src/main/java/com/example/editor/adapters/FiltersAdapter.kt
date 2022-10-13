package com.example.editor.adapters

import FilterModel
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.editor.R
import com.zomato.photofilters.imageprocessors.Filter

internal class FiltersAdapter(
    private var itemsList: List<FilterModel>,
    private var imageView: ImageView
) :
    RecyclerView.Adapter<FiltersAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemImageView: ImageView = view.findViewById(R.id.itemImageView)
        var itemTitleView: TextView = view.findViewById(R.id.itemTitleView)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_filter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.itemImageView.setImageURI(item.uri)
        holder.itemTitleView.setText(item.title)
        filterImage(holder.itemImageView, item.filter)

        holder.itemImageView.setOnClickListener { onFilterClick(holder.itemImageView) }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    private fun getFilteredBitmap(filter: Filter, imageView: ImageView): Bitmap {
        val limeStufferBitmapDrawable: BitmapDrawable = imageView.drawable as BitmapDrawable
        val limeStufferBitmap = limeStufferBitmapDrawable.bitmap
        val image: Bitmap = limeStufferBitmap.copy(Bitmap.Config.ARGB_8888, true)
        return filter.processFilter(image)
    }

    private fun filterImage(itemImageView: ImageView, filter: Filter) {
        val bitmap: Bitmap = getFilteredBitmap(filter, itemImageView)
        itemImageView.setImageBitmap(bitmap)
    }

    private fun onFilterClick(itemImageView: ImageView) {
        val drawable: Drawable = itemImageView.drawable
        imageView.setImageDrawable(drawable)
    }
}