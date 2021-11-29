package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    // TODO Избавься пожалуйста от !!
    private val binding get() = _binding!!

    var d_3: CardView? = null
    var textures: CardView? = null
    var nature: CardView? = null
    var food: CardView? = null
    var travel: CardView? = null
    var animals: CardView? = null

    // TODO Та же проблема что и в предыдущем фрагменте. Очень большой получился у тебя метод. Надо подробить его на более мелкие
    //  Тут можно инициализацию адаптера вынести отдельно, и разна маленькие методы с зонами ответственности
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashPhotoAdapter(this)

        binding.apply {
            setRecyclerView()
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener { adapter.retry() }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                // TODO Вынес бы эти 4 строки в отдельный метод
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)

        initialization()

        d_3?.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
            viewModel.searchPhotos("3d")
        }

        textures?.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
            viewModel.searchPhotos("textures")
        }

        nature?.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
            viewModel.searchPhotos("nature")
        }

        food?.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
            viewModel.searchPhotos("food")
        }

        travel?.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
            viewModel.searchPhotos("travel")
        }

        animals?.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
            viewModel.searchPhotos("animals")
        }

    }

    private fun setRecyclerView() {
        binding.recyclerView.apply {
            // TODO Это можно задавать в xml верстке, при том у тебя задано, но не то
            //            <androidx.recyclerview.widget.RecyclerView
            //             ...
            //            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
            //            tools:listitem="@layout/item_unsplash_photo" />
            //  А можно так:
            //            <androidx.recyclerview.widget.RecyclerView
            //              ...
            //            app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
            //            app:spanCount="2"
            //            tools:listitem="@layout/item_unsplash_photo" />
            //  Теперь этот метод вообще не надо
            layoutManager = GridLayoutManager(requireContext(), 2)
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
                // TODO в Kotlin для таких вещей есть query.isNullOrEmpty()
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    // TODO в Kotlin для таких вещей есть newText.isEmpty или newText.isNotEmpty
                    if(newText.length == 0){
                        binding.recyclerView.scrollToPosition(0)
                        viewModel.searchPhotos("popular")
                        searchView.clearFocus()
                    }
                }
                return true
            }
        })
    }

    private fun initialization() {
        d_3 = binding.nature
        textures = binding.bus
        nature = binding.car
        food = binding.train
        travel = binding.trending
        animals = binding.animals
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}