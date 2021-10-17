package com.inu.contentresolver

import android.net.Uri
import android.provider.MediaStore
import android.content.ContentUris




class Music(id: String, title:String?, artist:String?, albumId:String?, albumUri: Uri, duration:Long?) {

    var id: String = "" // 음원 자체의 id
    var title: String? = ""
    var artist : String? = ""
    var albumId: String? = ""
    var albumUUri: Uri?  = Uri.parse("android.resource://com.inu.contentresolver/drawable/next_thin")  // 앨범이미지  id
    //var albumUri: Uri = Uri.parse("android.resource://your.package.here/drawable/image_name")
    var duration : Long? = 0

    init {
        this.id = id
        this.title = title
        this.artist = artist
        this.albumId = albumId
        this.albumUUri = albumUri
        this.duration = duration
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