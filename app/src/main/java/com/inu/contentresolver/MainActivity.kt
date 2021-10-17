package com.inu.contentresolver

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contentresolver.BaseActivity
import com.inu.contentresolver.databinding.ActivityMainBinding
import java.io.FileNotFoundException


class MainActivity : BaseActivity() {

//    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    val FLAG_REQ_STORAGE = 99
    val FLAG_PERM_STORAGE = 102
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    //    requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), FLAG_PERM_STORAGE)

        if(isPermitted()) {
            startProcess()
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), FLAG_REQ_STORAGE)
        }

    }

    fun startProcess() {
        val adapter = MusicAdapter()
        adapter.musicList.addAll(getMusicList())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("Recycle")
    fun getMusicList(): List<Music> {
        // 컨텐트 리졸버로 음원 목록 가져오기
        // 1. 데이터 테이블 주소
        val musicListUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        // 2. 가져올 데이터 컬컴 정의
        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )
        //3.  컨텐트 리졸버에 해당 데이터 요청
        val cursor = contentResolver.query(musicListUri, proj, null, null, null)
        // 4. 커서로 전달받은 데이터를 꺼내서 저장
        val musicList = mutableListOf<Music>()
        var i=0
        while (cursor?.moveToNext() == true) {
            val id = cursor.getString(0)
            val title = cursor.getString(1)
            val artist = cursor.getString(2)
            val albumId = cursor.getString(3) //Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)) //cursor!!.getString(3)
            val albumUri = Uri.parse("content://media/external/audio/albumart/$albumId")
            val duration = cursor.getLong(4)

            val defaultUri = Uri.parse("android.resource://com.inu.contentresolver/drawable/next_thin")
            if( contentResolver.openFileDescriptor(albumUri, "r") == FileNotFoundException())
            {
                i+=1
                val albumUri = defaultUri
                Log.w("파일디스크립터", "something error!! $i");
            }
            // var fd: ParcelFileDescriptor?  = contentResolver.openFileDescriptor(albumUri, "r") ?: contentResolver.openFileDescriptor(defaultUri, "r")

            //  val bitmap = BitmapFactory.decodeFileDescriptor(fd?.fileDescriptor, null, null)

            val music = Music(id, title, artist, albumId, albumUri, duration)
            musicList.add(music)
        }
        return  musicList
    }

    fun isPermitted() : Boolean { // 책에는 checkPermission, 조건이 하나일 때 한줄로
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == FLAG_REQ_STORAGE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startProcess()
            } else {
                Toast.makeText(this, "권한 요청 실행해야지 앱 실행", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    override fun permissionGranted(requestCode: Int) {
        when(requestCode) {
            FLAG_PERM_STORAGE -> startProcess()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode) {
            FLAG_PERM_STORAGE -> {
                Toast.makeText(this, "공용 저장소 권한을 승인해야 앱을 사용 가능합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


}