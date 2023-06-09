package com.example.geoquest_app.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.geoquest_app.R
import com.example.geoquest_app.databinding.PopupmarkerBinding
import com.example.geoquest_app.utils.round
import com.example.geoquest_app.view.MapFragmentDirections
import com.example.geoquest_app.viewmodel.GeoViewModel
import com.example.models.Treasures
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(context: Context, val viewModel: GeoViewModel): GoogleMap.InfoWindowAdapter {
    var view: View = LayoutInflater.from(context).inflate(R.layout.popupmarker, null)

    private fun setInfoWindowText(marker: Marker) {
        val binding = PopupmarkerBinding.bind(view)
        binding.titleTreasure.text = marker.title
        binding.scoreTV.text = marker.snippet!!.split(",")[1].toDouble().round(2).toString()
        binding.distanceTV.text = "Aproximate ${marker.snippet!!.split(",")[2].toDouble().toInt()} m"
        binding.mapImage.setImageBitmap(viewModel.treasureImages[marker.snippet!!.split(",")[0].toInt()])
    }


    override fun getInfoContents(marker: Marker): View? {
        setInfoWindowText(marker)
        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        setInfoWindowText(marker)
        return view
    }
}