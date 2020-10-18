package com.example.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gallery.presentation.GalleryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view_tag, GalleryFragment.newInstance())
            .commit()
    }
}
