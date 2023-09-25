package com.example.playlistmaker.player.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.model.Playlist

class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.playlist_title)
    private val size = itemView.findViewById<TextView>(R.id.playlist_size)
    private val image = itemView.findViewById<ImageView>(R.id.playlist_image)

    fun bind(model: Playlist) {
        title.text = model.title
        size.text = itemView.context.getString(R.string.track_count, pluralizeWord(model.size, TRACK_NAME))

        Glide.with(itemView.context)
            .load(model.imageUri)
            .transform(CenterCrop(), RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
            .placeholder(R.drawable.placeholder)
            .into(image)

    }

    private fun pluralizeWord(number: Int, word: String): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number $word"
            number % 10 in 2..4 && (number % 100 < 10 || number % 100 >= 20) -> "$number $word${if (word.endsWith('а')) "и" else "а"}"
            else -> "$number $word${if (word.endsWith('а')) "" else "ов"}"
        }
    }

    companion object {
        private const val TRACK_NAME = "трек"
    }
}