package com.example.cosigo.BindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.cosigo.R

class RowBinding {
    companion object{
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            if(imageUrl == null||imageUrl.isEmpty()){
                imageView.setImageResource(R.drawable.ic_baseline_sentiment_dissatisfied_24)
            }
            else{
                imageView.load(imageUrl) {
                    crossfade(600)
                }
            }

        }
    }
}