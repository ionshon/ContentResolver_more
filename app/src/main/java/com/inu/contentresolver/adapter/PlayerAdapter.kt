package com.inu.contentresolver.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.inu.contentresolver.beans.Music
import com.inu.contentresolver.databinding.ItemLayoutBinding
import com.inu.contentresolver.databinding.ItemPlayerLayoutBinding

class PlayerAdapter(datas: List<Music>, context: Context) : PagerAdapter() {
    var datas: List<Music>? = null
    var context: Context? = null
    var viewList = ArrayList<View>()
    var inflater: LayoutInflater? = null

    public fun PlayerAdapter(datas: List<Music>?, context: Context) {
        this.datas = datas
        this.context = context
     //   viewList.add(inflater.inflate(R.layout.pager_layout1, null))

        //   inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return datas!!.size
    }

   // val binding = ItemPlayerLayoutBinding.inflate(LayoutInflater.from(context))
    // listView의 getView와 같은 역할
    // listView의 getView와 같은 역할

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = null
         val binding = ItemPlayerLayoutBinding.inflate(LayoutInflater.from(context))
       // val view: View = inflater?.inflate(R.layout.item, null)
        /*val imageView = view.findViewById<View>(R.id.imgView) as ImageView
        val txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView
        val txtArtist = view.findViewById<View>(R.id.txtArtist) as TextView */
        if (context != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            val inflater : LayoutInflater =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //    view = inflater.inflate(R.layout.item, container, false)
            val textView = view?.findViewById(R.id.title) as TextView
            textView.text = "TEXT $position"
        }

        // 실제 음악 데이터 가져오기
        val music = datas!!.get(position)
        with(binding) {
            binding.textTitleInPlayer.text = music.title
            binding.textArtistInPlayer.text = music.artist
            Glide.with(context!!).load(music.getAlbumUri())
                 .placeholder(R.drawable.ic_menu_close_clear_cancel).into(imageInPlayer)
       }
        // 생성한 뷰를 컨테이너에 담아준다. 컨테이너 = 뷰페이저를 생성한 최외곽 레이아웃 개념
        container.addView(view)
        return view!!
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        TODO("Not yet implemented")
    }

}

