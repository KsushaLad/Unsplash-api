package com.codinginflow.imagesearchapp.ui.details

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
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

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.5f

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)
        val wallpaperManager = WallpaperManager.getInstance(context)
        binding.apply {
            val photo = args.photo

            Glide.with(this@DetailsFragment)
                .load(photo.urls.full)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewCreator.isVisible = true
                        textViewDescription.isVisible = photo.description != null
                        return false
                    }
                }) .into(imageView)

           //mScaleGestureDetector = ScaleGestureDetector(image_view.context, ScaleListener())

            imageView.setOnTouchListener( MyScaleGestures(context))

            set.setOnClickListener { view ->
                Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show()
                val bitmap = (image_view.getDrawable() as BitmapDrawable).bitmap
                try {
                    wallpaperManager.setBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            textViewDescription.text = photo.description

            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            textViewCreator.apply {
                text = "Photo by ${photo.user.name} on Unsplash"
                setOnClickListener {
                    context.startActivity(intent)
                }
                paint.isUnderlineText = true
            }
        }
    }


    fun onTouchEvent(event: MotionEvent): Boolean {
        return mScaleGestureDetector!!.onTouchEvent(event)
    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 1.5f))
            image_view.setScaleX(mScaleFactor)
            image_view.setScaleY(mScaleFactor)
            return true
        }
    }
}




