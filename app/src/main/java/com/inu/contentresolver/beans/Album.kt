package com.inu.contentresolver.beans

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

class Album (id: String, artist:String?, album:String?, albumArtBit: Bitmap?) {

    var id: String = "" // 음원 자체의 id
    var artist : String? = ""
    var album: String? = ""
    var albumArtBit: Bitmap?  = null // 앨범이미지  id

    init {
        this.id = id
        this.artist = artist
        this.album = album
        this.albumArtBit = albumArtBit
    }

    fun getMusicUri(): Uri {
        return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
    }

    fun  getAlbumUri(): Uri {
        //  val albumId: Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
        // val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
        //  val sAlbumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId!!)
        //
        return Uri.parse("content://media/external/audio/albumart/$id")
    }
}