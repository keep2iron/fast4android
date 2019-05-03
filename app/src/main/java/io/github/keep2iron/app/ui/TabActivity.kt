package io.github.keep2iron.app.ui

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import io.github.keep2iron.app.R
import io.github.keep2iron.app.widget.TextTabLayout

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/30
 */
class TabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, TestFragment3())
                .commit()
//        setContentView(R.layout.activity_tab)
//
//        val tabLayout = findViewById<TextTabLayout>(R.id.tabLayout)
//        val viewPager = findViewById<ViewPager>(R.id.viewPager)
//        tabLayout.setTexts(arrayListOf("现金", "U币U币U币", "测试"))
//        tabLayout.setupWithViewPager(viewPager)
//
//        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
//            val framgents = arrayListOf<Fragment>(ColorFragment.getInstance(R.color.colorPrimary), ColorFragment.getInstance(R.color.blue), ColorFragment.getInstance(R.color.white))
//
//            override fun getItem(position: Int): Fragment = framgents[position]
//
//            override fun getCount(): Int = 3
//
//        }
//
//        viewPager.currentItem = 1
    }

}