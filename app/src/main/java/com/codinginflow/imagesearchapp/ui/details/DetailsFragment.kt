package com.codinginflow.imagesearchapp.ui.details

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import java.io.IOException
import com.codinginflow.imagesearchapp.MyScaleGestures
import com.codinginflow.imagesearchapp.R.string.photo_by_on_Unsplash

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailsBinding.bind(view)
        val wallpaperManager = WallpaperManager.getInstance(requireContext())
        binding.apply {
            val photo = args.photo
            detailsPhotoText()
            imgImagePreview.setOnTouchListener(MyScaleGestures(requireContext()))
            setWallpaper(wallpaperManager)
            textViewDescription.text = photo.description
            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            createLinkToUnsplash(intent)
        }
    }

    private fun setWallpaper(wallpaperManager: WallpaperManager) {
        set.setOnClickListener { view ->
            Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show()
            val bitmap = (imgImagePreview.getDrawable() as BitmapDrawable).bitmap
            try {
                wallpaperManager.setBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    private fun createLinkToUnsplash(intent: Intent) {
        text_view_creator.apply {
            text =
                resources.getString(photo_by_on_Unsplash) + " " + args.photo.user.name + " " + resources.getString(
                    R.string.photo_by_on_Unsplash1
                )
            setOnClickListener {
                context.startActivity(intent)
            }
            paint.isUnderlineText = true
        }
    }

    private fun detailsPhotoText() {
        Glide.with(this@DetailsFragment)
            .load(args.photo.urls.full)
            .error(R.drawable.ic_error)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress_bar.isVisible = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress_bar.isVisible = false
                    text_view_creator.isVisible = true
                    text_view_description.isVisible = args.photo.description != null
                    return false
                }
            }).into(imgImagePreview)
    }


}




