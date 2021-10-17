package com.inu.contentresolver.beans

import android.net.Uri
import android.provider.MediaStore
import android.content.ContentUris
import android.graphics.Bitmap


class Music(id: String, title:String?, artist:String?, albumId:String?, duration:Long?, path: String) { //, albumArtBit: Bitmap?) {

    var id: String = "" // 음원 자체의 id
    var title: String? = ""
    var artist : String? = ""
    var albumId: String? = ""
 //   var albumUUri: Uri?  = Uri.parse("android.resource://com.inu.contentresolver/drawable/next_thin")  // 앨범이미지  id
    var duration : Long? = 0
    var path : String =""
 //   var albumArtBit: Bitmap?  = null // 앨범이미지  id

    init {
        this.id = id
        this.title = title
        this.artist = artist
        this.albumId = albumId
     //   this.albumUUri = albumUri
        this.duration = duration
        this.path = path
    //    this.albumArtBit = albumArtBit
    }

    fun getMusicUri(): Uri {
        return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
    }

    fun  getAlbumUri(): Uri {
      //  val albumId: Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
       // val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
      //  val sAlbumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId!!)
     //
        return Uri.parse("content://media/external/audio/albumart/$albumId")
    }
}