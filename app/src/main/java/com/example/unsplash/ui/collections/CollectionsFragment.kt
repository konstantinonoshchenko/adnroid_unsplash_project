package com.example.unsplash.ui.collections

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.unsplash.R
import com.example.unsplash.data.collections.Collections
import com.example.unsplash.databinding.FragmentCollectionsBinding
import com.example.unsplash.utils.Constants.KEY_COVER_URL
import com.example.unsplash.utils.Constants.KEY_DESCRIPTION_COLLECTION
import com.example.unsplash.utils.Constants.KEY_ID_COLLECTION
import com.example.unsplash.utils.Constants.KEY_SIGN_COLLECTION
import com.example.unsplash.utils.Constants.KEY_TAGS_COLLECTION
import com.example.unsplash.utils.Constants.KEY_TITLE_COLLECTION
import com.example.unsplash.utils.Constants.KEY_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class CollectionsFragment : Fragment(), CollectionAdapter.OnItemClickListener {

    @Inject
    @ApplicationContext
    lateinit var applicationContext: Context

    private var _binding: FragmentCollectionsBinding? = null

    private var token: String? = null

    private val viewModel by viewModels<CollectionsViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        token = sharedPref!!.getString(KEY_TOKEN, null)
        viewModel.getCollections(token!!)
        val adapter = CollectionAdapter(this)
        binding.recycleView.adapter = adapter
        viewModel.collections.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(collection: Collections) {
        var tags = ""
        collection.tags.forEach {
            tags += "#${it.title} "
        }

        val sign =
            "${collection.totalPhotos ?: 0} ${requireContext().getString(R.string.imagesBY)}${collection.user!!.username ?: ""}"

        val bundle = Bundle()
        bundle.putString(KEY_ID_COLLECTION, collection.id)
        bundle.putString(KEY_TITLE_COLLECTION, collection.title)
        bundle.putString(KEY_TAGS_COLLECTION, tags)
        bundle.putString(KEY_DESCRIPTION_COLLECTION, collection.description ?: "")
        bundle.putString(KEY_SIGN_COLLECTION, sign)
        bundle.putString(KEY_COVER_URL,collection.coverPhoto!!.urls!!.regular?:"")

        findNavController().navigate(
            R.id.action_navigation_dashboard_to_collectionsOpen,
            args = bundle
        )
    }


}