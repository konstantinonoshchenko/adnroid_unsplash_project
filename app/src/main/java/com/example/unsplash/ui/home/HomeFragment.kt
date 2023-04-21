package com.example.unsplash.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.unsplash.R
import com.example.unsplash.api.Scope
import com.example.unsplash.data.Results
import com.example.unsplash.data.Token
import com.example.unsplash.databinding.FragmentHomeBinding
import com.example.unsplash.utils.Constants.ACCESS_KEY
import com.example.unsplash.utils.Constants.ID_PHOTO
import com.example.unsplash.utils.Constants.KEY_AUTORIZATION
import com.example.unsplash.utils.Constants.KEY_CLICK
import com.example.unsplash.utils.Constants.KEY_STARTING
import com.example.unsplash.utils.Constants.KEY_TOKEN
import com.example.unsplash.utils.Constants.URL_PHOTOS
import com.example.unsplash.utils.Constants.redirectURI
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        token = sharedPref!!.getString(KEY_TOKEN, null)
        sharedPref.getBoolean(KEY_CLICK, false)
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
       var startAutorization= requireActivity().intent.getBooleanExtra(KEY_CLICK,false)
        if (!startAutorization) startAutorization = sharedPref.getBoolean(KEY_STARTING,false)
       // val SA = sharedPref.getBoolean("startAutorization", false)
        Log.d("startAutorization", "SA fragment=$startAutorization")
        Log.d("startAutorization", "1st token=$token")
        // if (clickAuto) {


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
            if(!startAutorization){
                val intent = Intent(activity, com.example.unsplash.Authorization::class.java)
                startActivity(intent)
                activity?.finish()
            }
            if (startAutorization){
            authorize()
            sharedPref.edit().putBoolean(KEY_STARTING,true).apply()}
            val code = handleAuthCallback()
            if (code != null) {
                viewModel.getToken(handleAuthCallback()).enqueue(object : Callback<Token> {
                    override fun onResponse(call: Call<Token>, response: Response<Token>) {
                        val tokenResponse = response.body() ?: return
                        token = tokenResponse.accessToken.toString()
                        sharedPref.edit()
                            .putString(KEY_TOKEN, token).apply()
                        sharedPref.edit().putBoolean(KEY_CLICK, true).apply()
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
                        viewModel.searchPhotos("", token!!)
                        adapter.addLoadStateListener { loadState ->
                            binding.apply {
                                progressBar.isVisible =
                                    loadState.source.refresh is LoadState.Loading
                                recycleView.isVisible =
                                    loadState.source.refresh is LoadState.NotLoading
                                buttonRetry.isVisible =
                                    loadState.source.refresh is LoadState.Error
                                textError.isVisible =
                                    loadState.source.refresh is LoadState.Error

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
                    }

                    override fun onFailure(call: Call<Token>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
           // }
        }
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

    private fun authorize() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val firstAuto = sharedPref!!.getBoolean(KEY_AUTORIZATION, true)
        if (firstAuto) {
            sharedPref.edit().putBoolean(KEY_AUTORIZATION, false).apply()
            val scopeList =
                listOf(Scope.PUBLIC, Scope.READ_USER, Scope.WRITE_USER, Scope.WRITE_LIKES)
            var scopes = StringBuilder()
            for (scope in scopeList) {
                scopes.append(scope.scope).append("+")
            }
            scopes = scopes.deleteCharAt(scopes.length - 1)
            val url =
                "https://unsplash.com/oauth/authorize?client_id=$ACCESS_KEY&redirect_uri=$redirectURI&response_type=code&scope=$scopes"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
        }
    }

    private fun handleAuthCallback(): String? {
        val data = activity?.intent?.data
        return data?.query?.replace("code=", "")
    }

    private fun getIntentPhoto(){
        val intent= activity?.intent
        val data = intent?.dataString?.split('/')
        if (data!=null){
            var dataSplit = data as MutableList
            val idPhoto = dataSplit.last()
            dataSplit.removeLast()
            if (dataSplit.joinToString("/")==URL_PHOTOS){
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
