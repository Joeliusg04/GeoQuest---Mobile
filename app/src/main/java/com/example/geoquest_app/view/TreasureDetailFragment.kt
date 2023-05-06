package com.example.geoquest_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geoquest_app.databinding.FragmentTreasureDetailBinding
import com.example.geoquest_app.model.OnClickListenerReview
import com.example.geoquest_app.model.ReviewAdapter
import com.example.geoquest_app.model.Reviews
import com.example.geoquest_app.viewmodel.GeoViewModel
import com.example.models.Treasures


class TreasureDetailFragment : Fragment(), OnClickListenerReview {

    lateinit var binding: FragmentTreasureDetailBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private val viewModel: GeoViewModel by activityViewModels()
    var isFav = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTreasureDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        activity.setBottomNavigationVisible(true)

        val treasureID = arguments?.getInt("treasureID")!!
        val userID = viewModel.userData.value?.idUser!!
        viewModel.getTreasureByID(treasureID)
        viewModel.getTreasureImage(treasureID)
        viewModel.getAllReviews(treasureID)
        viewModel.checkIfTreasureIsFav(userID, treasureID)

        viewModel.isFav.observe(viewLifecycleOwner){
            isFav = it
            binding.favorite.isChecked = isFav
        }


        binding.favorite.setOnClickListener{
            if (!isFav){
                viewModel.addFavTreasure(userID, treasureID)
                viewModel.checkIfTreasureIsFav(userID, treasureID)
            } else{
                viewModel.deleteFavTreasure(userID, treasureID)
                viewModel.checkIfTreasureIsFav(userID, treasureID)
            }
        }


        viewModel.reviewListData.observe(viewLifecycleOwner) { reviewList ->
            setUpRecyclerView(reviewList!!)
        }

        viewModel.treasureData.observe(viewLifecycleOwner){ treasure ->
            println(treasure.name)
            setTreasureInfo(treasure)
        }

        viewModel.treasureImage.observe(viewLifecycleOwner){ treasureImage ->
            binding.treasureImg.setImageBitmap(treasureImage)
        }

        binding.play.setOnClickListener {
            val toPlay = TreasureDetailFragmentDirections.actionTreasureDetailFragmentToStartGameFragment(treasureID)
            findNavController().navigate(toPlay)
        }


    }

    fun setTreasureInfo(treasure: Treasures){
        binding.treasureName.text = treasure.name
        binding.dificulty.text = treasure.difficulty
        binding.location.text = treasure.location
    }

    private fun setUpRecyclerView(list: List<Reviews>) {
        reviewAdapter = ReviewAdapter(list, this)
        linearLayoutManager = LinearLayoutManager(context)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = reviewAdapter
        }
    }

    override fun onClick(review: Reviews) {

    }
}