package org.bubbble.andsplash.ui.pictures

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.FragmentPictureBinding
import org.bubbble.andsplash.ui.info.InfoActivity
import org.bubbble.andsplash.ui.personal.PersonalActivity

/**
 * A simple [Fragment] subclass.
 * Use the [PictureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PictureFragment : Fragment() {

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

        val adapter = PictureAdapter { personal, picture ->
            if (personal != null) {
                val intent = Intent(context, PersonalActivity::class.java)
                startActivity(intent)
            } else if (picture != null) {
                val intent = Intent(context, InfoActivity::class.java)
                intent.putExtra("photo", picture)
                startActivity(intent)
            }
        }
//        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
//        binding.photosList.layoutManager = layoutManager
        binding.photosList.layoutManager = LinearLayoutManager(context)
//        binding.photosList.layoutManager = GridLayoutManager(context, 2)
        binding.photosList.adapter = adapter
        adapter.submitList(mutableListOf<PictureItem>().apply {
            add(PictureItem(R.drawable.photo_1))
            add(PictureItem(R.drawable.photo_2))
            add(PictureItem(R.drawable.photo_3))
            add(PictureItem(R.drawable.photo_4))
            add(PictureItem(R.drawable.photo_5))
            add(PictureItem(R.drawable.photo_6))
            add(PictureItem(R.drawable.photo_7))
            add(PictureItem(R.drawable.photo_8))
            add(PictureItem(R.drawable.photo_9))
            add(PictureItem(R.drawable.photo_10))
            add(PictureItem(R.drawable.photo_11))
            add(PictureItem(R.drawable.photo_12))
            add(PictureItem(R.drawable.photo_13))
            add(PictureItem(R.drawable.photo_14))
        })
    }
}