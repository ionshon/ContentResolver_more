package com.inu.contentresolver

import android.app.PendingIntent.getActivity
import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inu.contentresolver.databinding.ItemLayoutBinding
import java.text.SimpleDateFormat
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.os.ParcelFileDescriptor







class MusicAdapter: // (private val onClick: (Music) -> Unit):
    RecyclerView.Adapter<MusicAdapter.Holder>() {

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
                if(mediaPlayer != null) {
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
                mediaPlayer = MediaPlayer.create(binding.root.context, musicUri)
                mediaPlayer?.start()
            }
        }
        fun setMusic(music:Music) {
            with(binding) {
                imageAlbum.setImageURI(music.albumUUri)

              //  imageAlbum.setImageBitmap(decodeSampledBitmapFromResource(imageAlbum.resources, music.id.toInt(), 250, 250));
                texArtist.text = music.artist
                textTitle.text = music.title
                val sdf = SimpleDateFormat("mm:ss")
                textDuration.text = sdf.format(music.duration)
            }
            this.musicUri = music.getMusicUri()

        }
    }

    fun decodeSampledBitmapFromResource(
        res: Resources?, resId: Int,
        reqWidth: Int, reqHeight: Int
    ): Bitmap? {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight
                && halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
