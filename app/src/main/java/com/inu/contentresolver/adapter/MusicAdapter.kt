package com.inu.contentresolver.adapter

import android.R
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
import android.graphics.BitmapFactory.*
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.inu.contentresolver.activity.MainActivity
import com.inu.contentresolver.beans.Album
import com.inu.contentresolver.beans.Music
import com.inu.contentresolver.player.Player
import com.inu.contentresolver.R.drawable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class MusicAdapter: // (private val onClick: (Music) -> Unit):
    RecyclerView.Adapter<MusicAdapter.Holder>() {

    val musicList = mutableListOf<Music>()
    val albumList = mutableListOf<Album>()
    var mediaPlayer:MediaPlayer? = null
    var player: Player? = null
    val uriLocal = Uri.parse("android.resource://com.inu.contentresolver/drawable/mp3logo")

    private var onSongClicked: SongClicked? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder  {
       // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = musicList[position]
        holder.setMusic(music)
    }

    interface SongClicked {
        fun onSongClicked(song: Music)
    }

    override fun getItemCount(): Int {
        return musicList.size
      //  return albumList.size
    }

    private val listener = View.OnClickListener {
        Log.d("클릭: ", "리스너")
        onSongClicked?.onSongClicked(musicList[1])
        //     intent.putExtra("position", position)
        //    context.startActivity(intent)
    }

    inner class Holder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    //    var cardView: CardView? = null
        var musicUri: Uri? = null
      //  var musicBean: Music? = null
        val mainItem : Unit = binding.itemMain.setOnClickListener(listener)
/*
        binding.root.setOnClickListener {
            Log.d("클릭:", "adapter")
            onSongClicked?.onSongClicked(music)
        } */
/*
        init {
            //    player?.musicPlayer(musicUri, binding.root.context)
            binding.root.setOnClickListener {
                Log.d("클릭:", "adapter Holder")
           //     onSongClicked?.onSongClicked(musicBean!!)

                if(mediaPlayer != null) {
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
                mediaPlayer = MediaPlayer.create(binding.root.context, musicUri)
                mediaPlayer?.start()
            }
        } */

        fun setMusic(music: Music) {
            /*
            try {
                val url = URL(music.getAlbumUri().toString())
                val stream = url.openStream()

                bitmap =  decodeStream(stream)

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            // 코루틴 구현
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = withContext(Dispatchers.IO){
                    bitmapLoader(music.getAlbumUri())  // 원본
                }
            binding.imageAlbum.setImageBitmap(bitmap) // 원본
*/
//                1. 로드할 대상 Uri    2. 입력될 이미지뷰
                with(binding) {
                 //     imageAlbum.setImageBitmap(Utils.songArt(music.path, binding.root.context))
              //      imageAlbum.setImageBitmap(decodeSampledBitmapFromResource(, music.id.toInt(), 250, 250));
                    textTitle.text = music.title
                    texArtist.text = music.artist
                    val sdf = SimpleDateFormat("mm:ss")
                    textDuration.text = sdf.format(music.duration)
                }

                Glide.with(binding.root.context)
                    .load(music.getAlbumUri())
                //    .placeholder(R.drawable.ic_menu_close_clear_cancel).into(binding.imageAlbum)
                    .placeholder(com.inu.contentresolver.R.drawable.outline_music_note_24).into(binding.imageAlbum)
          //      Log.d("duration 길이:", "${music.duration}")
          //  } // 코루틴 마지막 줄
            this.musicUri = music.getMusicUri()
        }

        fun bitmapLoader(musicUri : Uri): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    return ImageDecoder.decodeBitmap(ImageDecoder.createSource(
                        binding.root.context.contentResolver, musicUri))
                } else {
                    //     val source = ImageDecoder.createSource(binding.root.context.contentResolver, uriLocal)
                    //   ImageDecoder.decodeBitmap(source)
                    return MediaStore.Images.Media.getBitmap(
                        binding.root.context.contentResolver, musicUri)
                }
            } catch (e: IOException) {
                // Log.d("무존재 : ", "${music.path}")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    return ImageDecoder.decodeBitmap(ImageDecoder.createSource(
                        binding.root.context.contentResolver, uriLocal))
                } else {
                    return MediaStore.Images.Media.getBitmap(
                        binding.root.context.contentResolver, uriLocal
                    )
                }
            }
        }

        fun setAlbum(album: Album) {
            with(binding) {
              //  imageAlbum.setImageURI(music.albumUUri)
                imageAlbum.setImageBitmap(album.albumArtBit)
                texArtist.text = album.artist
                textTitle.text = album.title
             //   val sdf = SimpleDateFormat("mm:ss")
              //  textDuration.text = sdf.format(album.duration)
            }
            this.musicUri = album.getMusicUri()
        }
    }

    object ImageLoader{
        suspend fun loadImage(imageUrl: String): Bitmap? {
            val bmp: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val stream = url.openStream()

                return BitmapFactory.decodeStream(stream)

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bmp
        }
    }

    fun decodeSampledBitmapFromResource(
        res: Resources?, resId: Int,
        reqWidth: Int, reqHeight: Int
    ): Bitmap? {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = Options()
        options.inJustDecodeBounds = true
        decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return decodeResource(res, resId, options)
    }

    fun calculateInSampleSize(
        options: Options, reqWidth: Int, reqHeight: Int
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
