package com.example.shesyncdashboard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class SelfCareFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_selfcare, container, false)

        // Load thumbnails and set click listeners
        setupVideoItem(view, R.id.videoItem1, R.id.videoThumbnail1, "Ohv9zyu3ndM")
        setupVideoItem(view, R.id.videoItem2, R.id.videoThumbnail2, "iJhD6NatBik")
        setupVideoItem(view, R.id.videoItem3, R.id.videoThumbnail3, "l6DpGarDj_M")
        setupVideoItem(view, R.id.videoItem4, R.id.videoThumbnail4, "G6M4HBc3_wo")

        return view
    }

    private fun setupVideoItem(view: View, itemId: Int, thumbnailId: Int, videoId: String) {
        val itemView = view.findViewById<LinearLayout>(itemId)
        val thumbnailView = view.findViewById<ImageView>(thumbnailId)

        // Load thumbnail
        val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
        loadImageFromUrl(thumbnailUrl, thumbnailView)

        // Set click listener
        itemView.setOnClickListener {
            openYouTubeVideo(videoId)
        }
    }

    private fun loadImageFromUrl(url: String, imageView: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun openYouTubeVideo(videoId: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
        try {
            startActivity(appIntent)
        } catch (ex: Exception) {
            startActivity(webIntent)
        }
    }
}
