package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.os.Handler
import android.provider.Contacts
import android.provider.Contacts.Intents.UI
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.os.CountDownTimer
import android.util.Log
import kotlinx.coroutines.Runnable


@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    UnsplashPhotoAdapter.OnItemClickListener{

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding

    // TODO Зачем тебе эти переменные, если ты просто можешь обращаться к элементам через binding
    var d_3: CardView? = null
    var textures: CardView? = null
    var nature: CardView? = null
    var food: CardView? = null
    var travel: CardView? = null
    var animals: CardView? = null

    private lateinit var textChangeCountDownJob: Job

    // TODO Как я вижу, это не используемые переменные. Убери их, чтобы не захломлять код
    var mQuery : String? = null
    val handler : Handler? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)
        val adapter = UnsplashPhotoAdapter(this)
        bindingGalleryFragment(adapter)
        observe(adapter)
        load(adapter)
        setHasOptionsMenu(true)
        initialization()
        buttonsCategory()
    }

    private fun bindingGalleryFragment(unsplashPhotoAdapter: UnsplashPhotoAdapter) {
        binding?.apply {
            recyclerView?.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = unsplashPhotoAdapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { unsplashPhotoAdapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { unsplashPhotoAdapter.retry() }
            )
            buttonRetry.setOnClickListener { unsplashPhotoAdapter.retry() }
        }
    }

    // TODO Избавься пожалуйста тут от this!! (!!)
    private fun load(unsplashPhotoAdapter: UnsplashPhotoAdapter) {
        unsplashPhotoAdapter.addLoadStateListener { loadState ->
            binding.apply {
                this!!.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                this.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    unsplashPhotoAdapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
    }

    // TODO сделай пожалуйста свои типы быстрого поиска как enum. Так будет проавильнее
    private fun buttonsCategory() {
        d_3?.setOnClickListener {
            binding?.recyclerView?.scrollToPosition(0)
            viewModel.searchPhotos("3d")
        }

        textures?.setOnClickListener {
            binding?.recyclerView?.scrollToPosition(0)
            viewModel.searchPhotos("textures")
        }

        nature?.setOnClickListener {
            binding?.recyclerView?.scrollToPosition(0)
            viewModel.searchPhotos("nature")
        }

        food?.setOnClickListener {
            binding?.recyclerView?.scrollToPosition(0)
            viewModel.searchPhotos("food")
        }

        travel?.setOnClickListener {
            binding?.recyclerView?.scrollToPosition(0)
            viewModel.searchPhotos("travel")
        }

        animals?.setOnClickListener {
            binding?.recyclerView?.scrollToPosition(0)
            viewModel.searchPhotos("animals")
        }
    }

    private fun observe(unsplashPhotoAdapter: UnsplashPhotoAdapter) {
        viewModel.photos.observe(viewLifecycleOwner) {
            unsplashPhotoAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(::textChangeCountDownJob.isInitialized)
                    textChangeCountDownJob.cancel()

                textChangeCountDownJob = lifecycleScope.launch {
                    delay(1500)
                    if (query != null) {
                        viewModel.searchPhotos(query)
                        searchView.clearFocus()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(::textChangeCountDownJob.isInitialized)
                     textChangeCountDownJob.cancel()
                     textChangeCountDownJob = lifecycleScope.launch {
                    delay(1500)
                         if (newText != null) {
                             if (newText.isEmpty()) {
                                 viewModel.searchPhotos("new")
                                 searchView.clearFocus()
                             }
                         }
                }
                return false
            }
        })
   }

    private fun initialization() {
        d_3 = binding?.nature
        textures = binding?.bus
        nature = binding?.car
        food = binding?.train
        travel = binding?.trending
        animals = binding?.animals
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}