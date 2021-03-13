package org.bubbble.andsplash.ui.pictures

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.FragmentPictureBinding
import org.bubbble.andsplash.ui.personal.PersonalActivity
import org.bubbble.andsplash.ui.preview.PreviewActivity


enum class Page {
    HOME_PHOTO,
    HOME_COLLECTION,
    PERSONAL_PHOTO,
    PERSONAL_LIKE,
    PERSONAL_COLLECTION
}

private const val ARG_PARAM = "param"

class PictureFragment : Fragment() {

    private var param: Page? = null
    
    private var currentViewType = ViewType.PHOTO

    private val photoViewTypes = arrayOf(
        ViewType.PHOTO,
        ViewType.FLOW,
        ViewType.FLOW2,
        ViewType.GALLERY
    )

    private val collectionViewTypes = arrayOf(
        ViewType.GRID,
        ViewType.LIST
    )

    private lateinit var adapter: PictureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getSerializable(ARG_PARAM) as Page
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param: Page) =
            PictureFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM, param)
                }
            }
    }
    private lateinit var binding: FragmentPictureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PictureAdapter { personal, picture ->
            if (personal != null) {
                val intent = Intent(context, PersonalActivity::class.java)
                startActivity(intent)
            } else if (picture != null) {
                val intent = Intent(context, PreviewActivity::class.java)
                intent.putExtra("photo", picture)
                startActivity(intent)
            }
        }

        initView()

//        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
//        binding.photosList.layoutManager = layoutManager
        binding.photosList.layoutManager = LinearLayoutManager(context)
//        binding.photosList.layoutManager = GridLayoutManager(context, 3)
        binding.photosList.adapter = adapter
        adapter.submitList(mutableListOf<PictureItem>().apply {
            add(PictureItem(R.drawable.photo_3))
            add(PictureItem(R.drawable.photo_4))
            add(PictureItem(R.drawable.photo_8))
            add(PictureItem(R.drawable.photo_9))
            add(PictureItem(R.drawable.photo_10))
            add(PictureItem(R.drawable.photo_5))
            add(PictureItem(R.drawable.photo_11))
            add(PictureItem(R.drawable.photo_6))
            add(PictureItem(R.drawable.photo_7))
            add(PictureItem(R.drawable.photo_1))
            add(PictureItem(R.drawable.photo_2))
            add(PictureItem(R.drawable.photo_12))
            add(PictureItem(R.drawable.photo_13))
            add(PictureItem(R.drawable.photo_14))
        })
    }

    private fun initView() {

        when (param) {
            Page.HOME_PHOTO,
            Page.PERSONAL_PHOTO,
            Page.PERSONAL_LIKE -> {

                currentViewType = ViewType.PHOTO
            }

            Page.HOME_COLLECTION,
            Page.PERSONAL_COLLECTION -> {

                currentViewType = ViewType.LIST
            }
        }

        adapter.setViewType(currentViewType)
    }

    fun changeViewType() {
        when (param) {
            Page.HOME_PHOTO,
            Page.PERSONAL_PHOTO,
            Page.PERSONAL_LIKE -> {
                for ((index, value) in photoViewTypes.withIndex()) {
                    if (currentViewType == value) {
                        currentViewType = if (index != photoViewTypes.size -1) {
                            photoViewTypes[index + 1]
                        } else {
                            photoViewTypes[0]
                        }
                        break
                    }
                }
            }
            
            Page.HOME_COLLECTION,
            Page.PERSONAL_COLLECTION -> {
                for ((index, value) in collectionViewTypes.withIndex()) {
                    if (currentViewType == value) {
                        currentViewType = if (index != collectionViewTypes.size -1) {
                            collectionViewTypes[index + 1]
                        } else {
                            collectionViewTypes[0]
                        }
                        break
                    }
                }
            }
        }

        when (currentViewType) {
            ViewType.PHOTO, ViewType.LIST -> {
                binding.photosList.layoutManager = LinearLayoutManager(context)
            }

            ViewType.FLOW, ViewType.FLOW2 -> {
                val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                binding.photosList.layoutManager = layoutManager
            }

            ViewType.GALLERY -> {
                binding.photosList.layoutManager = GridLayoutManager(context, 3)
            }

            ViewType.GRID -> {
                binding.photosList.layoutManager = GridLayoutManager(context, 2)
            }
        }
        adapter.setViewType(currentViewType)
        adapter.notifyDataSetChanged()
    }
}