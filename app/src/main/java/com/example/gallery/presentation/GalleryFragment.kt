package com.example.gallery.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.R

import com.example.gallery.databinding.GalleryFragmentBinding

//@AndroidEntryPoint
class GalleryFragment : Fragment() {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    private lateinit var binding: GalleryFragmentBinding
    private lateinit var viewModel: GalleryViewModelInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // View Binding
//        binding = GalleryFragmentBinding.inflate(inflater)

        // Data Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.gallery_fragment, container, false)

        // ViewのLifeCycleOwnerを指定しないとリークする https://note.com/d_forest/n/n9c2dcf155f34
        // Fragmentは残ったままでViewのみ（もしくは子Fragmentなども）が破棄されて再生成される場合もあるため http://nein37.hatenablog.com/entry/2018/05/06/204943
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

        // Without Binding
//        return inflater.inflate(R.layout.gallery_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        binding.viewModel = viewModel
        binding.adapter = GalleryItemAdapter()
        binding.itemListRecyclerView.layoutManager = GridLayoutManager(context, 3)

        // 手動でBindする場合
//        viewModel.listItems.observe(viewLifecycleOwner, Observer {
//            binding.textViewHoge.text = it.firstOrNull()?.displayName
//        })

        if (viewModel.hasRequiredPermissions(context)) {
            viewModel.fetchGalleryItemsIfNeeded(activity!!.application.contentResolver)
        } else {
            requestGalleryPermissionsIfNeeded()
        }
    }

    private fun requestGalleryPermissionsIfNeeded() {
        requestPermissions(viewModel.requiredPermissions(), 100)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                viewModel.fetchGalleryItemsIfNeeded(activity!!.application.contentResolver)
            }
        }
    }

}