package com.inu.contentresolver.activity

import android.R
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.inu.contentresolver.adapter.PlayerAdapter
import com.inu.contentresolver.beans.Music
import com.inu.contentresolver.databinding.ActivityMainBinding
import com.inu.contentresolver.databinding.ActivityPlayerBinding
import java.lang.Exception

class PlayerActivity : AppCompatActivity()  {

    val binding by lazy { ActivityPlayerBinding.inflate(layoutInflater)}

    var datas: List<Music>? = null
    var adapter: PlayerAdapter? = null

    var viewPager: ViewPager? = null
    //var btnPlay: ImageButton? = null
   // var btnFf:ImageButton? = null
  //  var btnRew:ImageButton? = null
    var txtDuration: TextView? = null
    var txtCurrent:TextView? = null
    var seekBar: SeekBar? = null

    var player: MediaPlayer? = null
    var position = 0 // 현재 음악 위치


    private val PLAY = 0
    private val PAUSE = 1
    private val STOP = 2
    private var playStatus = STOP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }


/*
        volumeControlStream = AudioManager.STREAM_MUSIC /*
        seekBar = findViewById(R.id.seekBar) as SeekBar
        txtCurrent = findViewById(R.id.txtCurrent) as TextView
        txtDuration = findViewById(R.id.txtDuration) as TextView
        btnPlay = findViewById(R.id.btnPlay) as ImageButton
        btnFf = findViewById(R.id.btnFf) as ImageButton
        btnRew = findViewById(R.id.btnRew) as ImageButton */
     //   btnPlay!!.setOnClickListener(clickListener)
      //  btnFf?.setOnClickListener(clickListener)
      //  btnRew?.setOnClickListener(clickListener)
        seekBar!!.setOnSeekBarChangeListener(seekBarChangeListener) // SeekBar 리스너 연결

        // 0. 데이터 가져오기
    //    datas = MainActivity.taLoader.get(this)

        viewPagerSetting()

        // 5. 특정 페이지 호출되서 받는다.
        val intent = intent
        if (intent != null) {
            val bundle = intent.extras
            position = bundle!!.getInt("position")
            // 첫페이지일 경우만 init 호출
            // 이유 : 첫페이지가 아닌경우 setCurrentItem에 의해서 ViewPager의 onPageSelected가 호출된다. 즉 1페이지가 호출되고 나서 선택된 페이지로 넘어가니까 1페이지의 init이 두번 반복됨
            if (position == 0) {
                // 음원길이 같은 음악정보를 미리 세팅한다.
                init()
            } else {
                // 페이지 이동
                viewPager?.setCurrentItem(position)
            }
        }
    }

    private fun viewPagerSetting() {
        // 1. 뷰페이저 가져오기
        viewPager = binding.viewPager as ViewPager?
        // 2. 뷰페이저용 아답터 생성
        adapter = PlayerAdapter(datas, this)
        // 3. MusicAdapter는 RecyclerView.Adapter에서 왔기 때문에 맞지 않다.
        viewPager?.setAdapter(adapter)
        // 4. 뷰페이저 리스너 연결
        viewPager?.addOnPageChangeListener(viewPageListener)
        // * 페이지 트랜스 포머 연결
        viewPager?.setPageTransformer(false, pageTransformer)
    }


    // seekBar 리스너;
    var seekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (player != null && fromUser == true) {
                player!!.seekTo(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }

    // 뷰페이저 리스너
    var viewPageListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {  // 바뀐 포지션이 넘어온다.
            this@PlayerActivity.position = position
            init()
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
    private fun init() {
        // 뷰페이저로 이동할 경우 플레이어에 세팅된 값을 해제한 후 로직을 실행한다.
        if (player != null) {
            // 플레어 상태를 STOP으로 변경
            playStatus = STOP
            // 아이콘을 플레이 버튼으로 변경
            binding.btnPlay!!.setImageResource(R.drawable.ic_media_play)
            player!!.release()
        }
        playerInit()
        controllerInit()
    }

    var clickListener = View.OnClickListener { v ->
        when (v.id) {
                with(binding) {
                binding.btnPlay -> play()
                btnFw -> next()
                btnRew -> prev()
            }
        }
    }




    private fun playerInit() {
        val musicUri: Uri = datas!![position].uri
        // 플레이어에 음원 세팅
        player = MediaPlayer.create(this, musicUri)
        player.setLooping(false) // 반복 여부
        // 미디어 플레이어에 완료체크 리스너를 등록한다.
        player.setOnCompletionListener(OnCompletionListener { next() })
    }

    private fun controllerInit() {
        // seekBar 길이
        seekBar!!.max = player!!.duration
        // SeekBar 현재값 0으로
        seekBar!!.progress = 0
        // SeekBar 전체 플레이 시간 설정
        txtCurrent.setText("00:00")
        // SeekBar  현재 플레이 시간 설정
        txtDuration!!.text = convertMiliToTime(player!!.duration.toLong())
    }

    private fun convertMiliToTime(mili: Long): String? {
        val min = mili / 1000 / 60
        val sec = mili / 1000 % 60
        return String.format("%02d", min) + ":" + String.format("%02d", sec)
    }

    private fun play() {
        // 플래이중이 아니면 실행
        when (playStatus) {
            STOP -> playStop()
            PLAY -> playPlay()
            PAUSE -> playPause()
        }
    }

    private fun playStop() {
        player!!.start()
        playStatus = PLAY
        btnPlay!!.setImageResource(R.drawable.ic_media_pause)
        // sub thread 를 생성해서 mediaplayer 의 현재 포지션 값으로 seekbar 를 변경해준다. 매 1초마다
        val thread: Thread = TimerThread()
        thread.start()
    }

    private fun playPlay() {
        player!!.pause()
        playStatus = PAUSE
        btnPlay!!.setImageResource(R.drawable.ic_media_play)
    }

    private fun playPause() {
        player!!.seekTo(player!!.currentPosition)
        player!!.start()
        playStatus = PLAY
        btnPlay!!.setImageResource(R.drawable.ic_media_pause)
    }

    private operator fun next() {
        if (position > 0) viewPager.setCurrentItem(position + 1)
    }

    private fun prev() {
        if (position < datas!!.size) viewPager.setCurrentItem(position - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player != null) {
            player!!.release() // 사용이 끝나면 해제해야만 한다.
        }
        playStatus = STOP
    }

    var pageTransformer: ViewPager.PageTransformer = object : PageTransformer() {
        fun transformPage(page: View, position: Float) {
            //현재 Page의 위치가 조금이라도 바뀔때마다 호출되는 메소드
            //첫번째 파라미터 : 현재 존재하는 View 객체들 중에서 위치가 변경되고 있는 View들
            //두번째 파라미터 : 각 View 들의 상대적 위치( 0.0 ~ 1.0 : 화면 하나의 백분율)

            //           1.현재 보여지는 Page의 위치가 0.0
            //           Page가 왼쪽으로 이동하면 값이 -됨. (완전 왼쪽으로 빠지면 -1.0)
            //           Page가 오른쪽으로 이동하면 값이 +됨. (완전 오른쪽으로 빠지면 1.0)

            //주의할 것은 현재 Page가 이동하면 동시에 옆에 있는 Page(View)도 이동함.
            //첫번째와 마지막 Page 일때는 총 2개의 View가 메모리에 만들어져 잇음.
            //나머지 Page가 보여질 때는 앞뒤로 2개의 View가 메모리에 만들어져 총 3개의 View가 instance 되어 있음.
            //ViewPager 한번에 1장의 Page를 보여준다면 최대 View는 3개까지만 만들어지며
            //나머지는 메모리에서 삭제됨.-리소스관리 차원.

            //position 값이 왼쪽, 오른쪽 이동방향에 따라 음수와 양수가 나오므로 절대값 Math.abs()으로 계산
            //position의 변동폭이 (-2.0 ~ +2.0) 사이이기에 부호 상관없이 (0.0~1.0)으로 변경폭 조절
            //주석으로 수학적 연산을 설명하기에는 한계가 있으니 코드를 보고 잘 생각해 보시기 바랍니다.
            val normalizedposition = Math.abs(1 - Math.abs(position))
            page.alpha = normalizedposition //View의 투명도 조절
            page.scaleX = normalizedposition / 2 + 0.5f //View의 x축 크기조절
            page.scaleY = normalizedposition / 2 + 0.5f //View의 y축 크기조절
            page.rotationY = position * 80 //View의 Y축(세로축) 회전 각도
        }
    }


    internal class TimerThread : Thread() {
        override fun run() {
            while (playStatus < STOP) {
                if (player != null) {
                    // 이부분은 메인 쓰레드에서 동작하도록 Runnable 객체를 메인쓰레드로 던져준다.
                    runOnUiThread(Runnable
                    // 메인 쓰레드에게 UI 메세지를 던진다.
                    {
                        try { // 플레이어가 도중에 종료되면 예외가 발생한다.
                            seekBar.setProgress(player.getCurrentPosition())
                            txtCurrent.setText(
                                convertMiliToTime(
                                    player.getCurrentPosition().toLong()
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    })
                }
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }
    }
*/
}
