package com.example.unsplash.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.unsplash.R
import com.example.unsplash.data.Results
import com.example.unsplash.databinding.FragmentHomeBinding
import com.example.unsplash.utils.Constants.ID_PHOTO
import com.example.unsplash.utils.Constants.KEY_TOKEN
import com.example.unsplash.utils.Constants.TOKEN
import com.example.unsplash.utils.Constants.URL_PHOTOS
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), PhotoAdapter.OnItemClickListener {

    @Inject
    @ApplicationContext
    lateinit var applicationContext: Context
    private var _binding: FragmentHomeBinding? = null

    private val viewModel by viewModels<HomeViewModel>()

    private var token: String? = null

    private val binding get() = _binding!!

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recycleView.layoutManager = layoutManager
        val adapter = PhotoAdapter(this)
        binding.recycleView.adapter = adapter
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(
                viewLifecycleOwner.lifecycle,
                it
            )
        }
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        token = sharedPref!!.getString(KEY_TOKEN, null)
        if (token == null) {
            token = requireActivity().intent.getStringExtra(TOKEN)
            if (token != null) sharedPref.edit().putString(KEY_TOKEN, token).apply()
        }
        if (token != null) {
            getIntentPhoto()
            viewModel.searchPhotos("", token!!)
            binding.apply {
                recycleView.setHasFixedSize(true)
                recycleView.itemAnimator = null
                recycleView.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = PhotoLoadStateAdapter { adapter.retry() },
                    footer = PhotoLoadStateAdapter { adapter.retry() }
                )
                buttonRetry.setOnClickListener {
                    adapter.retry()
                }
            }
            adapter.addLoadStateListener { loadState ->
                binding.apply {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    recycleView.isVisible = loadState.source.refresh is LoadState.NotLoading
                    buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                    textError.isVisible = loadState.source.refresh is LoadState.Error
                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1
                    ) {
                        recycleView.isVisible = false
                        textViewEmpty.isVisible = true
                    } else {
                        textViewEmpty.isVisible = false
                    }
                }
            }
        } else {
            val intent = Intent(activity, com.example.unsplash.Authorization::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recycleView.scrollToPosition(0)
                    binding.topOnTodayText.isVisible = false
                    viewModel.searchPhotos(query, token!!)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(photo: Results) {
        val bundle = Bundle()
        bundle.putString(ID_PHOTO, photo.id)
        findNavController().navigate(
            R.id.action_navigation_home_to_detailsFragment,
            args = bundle
        )


    }

    override fun onClickOnLikes(id: String, b: Boolean) {
        viewModel.liked(id, token!!, b)
    }
    private fun getIntentPhoto() {
        val intent = activity?.intent
        val data = intent?.dataString?.split('/')
        if (data != null) {
            var dataSplit = data as MutableList
            val idPhoto = dataSplit.last()
            dataSplit.removeLast()
            if (dataSplit.joinToString("/") == URL_PHOTOS) {
                val bundle = Bundle()
                bundle.putString(ID_PHOTO, idPhoto)
                findNavController().navigate(
                    R.id.action_navigation_home_to_detailsFragment,
                    args = bundle
                )
            }
        }
    }

}
