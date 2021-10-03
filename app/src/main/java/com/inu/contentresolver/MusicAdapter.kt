package com.inu.contentresolver

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.inu.contentresolver.databinding.ItemLayoutBinding
import java.text.SimpleDateFormat

class MusicAdapter: RecyclerView.Adapter<MusicAdapter.Holder>() {

    val musicList = mutableListOf<Music>()
    var mediaPlayer:MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = musicList[position]
        holder.setMusic(music)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    inner class Holder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        var musicUri: Uri? = null

        init {
            binding.root.setOnClickListener {
                Log.d("음악", "asdd")
                if(mediaPlayer != null) {
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
                mediaPlayer = MediaPlayer.create(binding.root.context, musicUri)
                mediaPlayer?.start()
            }
        }
        fun setMusic(music:Music) {
            binding.imageAlbum.setImageURI(music.getAlbumUri())
            binding.texArtist.text = music.artist
            binding.textTitle.text = music.title
            val sdf = SimpleDateFormat("mm:ss")
            binding.textDuration.text = sdf.format(music.duration)
            this.musicUri = music.getMusicUri()
        }
    }
}
