package com.uxi.bambupay.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uxi.bambupay.R
import com.uxi.bambupay.view.activity.MainActivity

class AboutFragment : BaseFragment() {

//    private lateinit var galleryViewModel: GalleryViewModel

//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
////        galleryViewModel =
////                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_about, container, false)
////        val textView: TextView = root.findViewById(R.id.text_gallery)
////        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
////        })
//        return root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context is MainActivity) {
            (context as MainActivity).setToolbarBgColor(R.color.white)
        }
    }

    override fun getLayoutId() = R.layout.fragment_about
}