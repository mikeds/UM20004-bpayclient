package com.uxi.bambupay.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.uxi.bambupay.R
import com.uxi.bambupay.view.activity.MainActivity
import com.uxi.bambupay.view.slideshow.SlideshowViewModel

class FAQFragment : BaseFragment() {

//    private lateinit var slideshowViewModel: SlideshowViewModel
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        slideshowViewModel =
//                ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_faq, container, false)
//        val textView: TextView = root.findViewById(R.id.text_slideshow)
//        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is MainActivity) {
            (context as MainActivity).setToolbarBgColor(R.color.white)
        }
    }

    override fun getLayoutId() = R.layout.fragment_faq
}