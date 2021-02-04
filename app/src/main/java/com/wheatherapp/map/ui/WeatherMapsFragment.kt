package com.wheatherapp.map.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.wheatherapp.BackPressListner
import com.wheatherapp.WEBVIW_PATH
import com.wheatherapp.map.viewmodel.WeatherMapViewModel
import com.wheatherapp.model.LocationWeatherModel
import com.wheatherapp.openweathermap.R
import com.wheatherapp.openweathermap.databinding.BottomSheetCitydetailBinding
import com.wheatherapp.openweathermap.databinding.FragmentWeatherMapsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_citydetail.*
import kotlinx.android.synthetic.main.fragment_weather_maps.*
import javax.inject.Inject


@AndroidEntryPoint
class WeatherMapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnMapClickListener, View.OnClickListener,
        BackPressListner {
    @Inject
    lateinit var weatherMapViewModel: WeatherMapViewModel

    private var isLocateCity: Boolean = true

    private lateinit var sheetBehavior: BottomSheetBehavior<MaterialCardView>
    private lateinit var mMap: GoogleMap

    private lateinit var bottomSheetDataBinding: BottomSheetCitydetailBinding
    private lateinit var mapFragmentDataBinding: FragmentWeatherMapsBinding


    override fun onCreateView(

            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mapFragmentDataBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_weather_maps, container, false)
        return mapFragmentDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setBottomSHeet()
        addMap()

    }

    private fun setData() {

        ivBookMark.setOnClickListener(this)
        ivExploreWeather.setOnClickListener(this)
        ivHelp.setOnClickListener(
                this
        )
    }

    private fun setBottomSHeet() {
        DataBindingUtil.getBinding<BottomSheetCitydetailBinding>(
                mapFragmentDataBinding.root.findViewById(
                        R.id.cityBottomDetailSheetLayout
                )

        )?.let { bottomSheetDataBinding = it }

        sheetBehavior = BottomSheetBehavior.from(bottomSheetDataBinding.cityBottomDetailSheetLayout)
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }


            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0) {
                    cardVIewWhetherMenu.visibility = View.GONE
                } else {
                    cardVIewWhetherMenu.visibility = View.VISIBLE
                }

            }
        }
        sheetBehavior.addBottomSheetCallback(bottomSheetCallback)


    }

    private fun addMap() {
        val mapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.mapContainerLayout, mapFragment)
        fragmentTransaction.commit()
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnCameraMoveListener(this)
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMapClickListener(this)
        mMap.setOnInfoWindowClickListener {

            if (it.tag is LocationWeatherModel) {
                expandBottomSheet(it.tag as LocationWeatherModel)
            }
        }

        weatherMapViewModel.pinWeatherLiveData.observe(this, Observer { locationWeatherModel ->
            addMarker(locationWeatherModel)
        })

        weatherMapViewModel.bookmarksWeatherLiveData.observe(this, Observer {
            mMap.clear()
            it.forEach { locationWeather ->
                addMarker(locationWeather)
            }
        })

    }

    private fun addMarker(locationWeatherModel: LocationWeatherModel) {
        val markerOptions = MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                .position(LatLng(locationWeatherModel.lat, locationWeatherModel.lon))
                .title(locationWeatherModel.name)
        mMap.addMarker(markerOptions).apply {
            tag = locationWeatherModel
            showInfoWindow()
        }
    }

    private fun expandBottomSheet(locationWeather: LocationWeatherModel? = null, isWebView: Boolean = false) {

        sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        if (isWebView) {
            webview.loadUrl(WEBVIW_PATH)
            sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            bottomSheetDataBinding.webview.visibility = View.VISIBLE
            bottomSheetDataBinding.weatherDataScrollView.visibility = View.GONE

        } else {
            bottomSheetDataBinding.weatherDataScrollView.visibility = View.VISIBLE
            bottomSheetDataBinding.webview.visibility = View.GONE

            bottomSheetDataBinding.locationWeather = locationWeather


            bottomSheetDataBinding.btnAddBookmark.setOnClickListener {
                weatherMapViewModel.bookmarkLocation(locationWeather!!)
                if (bottomSheetDataBinding.btnAddBookmark.text.toString().contentEquals(StringBuilder().append(getString(R.string.bookmark)))) {
                    bottomSheetDataBinding.btnAddBookmark.text = getString(R.string.remove_bookmark)
                    addMarker(locationWeather)
                } else {
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    weatherMapViewModel.removeBookMark(locationWeather,!isLocateCity)
                    bottomSheetDataBinding.btnAddBookmark.text = getString(R.string.bookmark)
                }
            }
            weatherMapViewModel.containBookMark(locationWeather!!.id)

            weatherMapViewModel.containBookMark.observe(this, Observer {
                if (!isLocateCity || it.size > 0) {

                    bottomSheetDataBinding.btnAddBookmark.text = getString(R.string.remove_bookmark)
                } else {
                    bottomSheetDataBinding.btnAddBookmark.text = getString(R.string.bookmark)
                }
            })

        }

    }


    override fun onCameraMove() {
        if (isLocateCity) {
            mMap.clear()
            mapFragmentDataBinding.pinImageView.visibility = View.VISIBLE
        } else {
            mapFragmentDataBinding.pinImageView.visibility = View.GONE
        }
    }

    override fun onCameraIdle() {
        if (!isLocateCity) return
        weatherMapViewModel.refreshPin(
                mMap.cameraPosition.target.latitude,
                mMap.cameraPosition.target.longitude
        )


    }


    override fun onMapClick(latLng: LatLng?) {
        if (isLocateCity) {
            mMap.clear()
            mapFragmentDataBinding.pinImageView.visibility = View.VISIBLE
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        } else {
            mapFragmentDataBinding.pinImageView.visibility = View.GONE
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ivExploreWeather -> {
                mapFragmentDataBinding.pinImageView.visibility = View.VISIBLE

                resetData()

                if (!isLocateCity)
                    mMap.clear()
                isLocateCity = true
                resetData()
                ivExploreWeather.setColorFilter(
                        ContextCompat.getColor(
                                activity!!.applicationContext,
                                R.color.yellow
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                );

            }
            R.id.ivBookMark -> {
                mapFragmentDataBinding.pinImageView.visibility = View.GONE

                resetData()
                isLocateCity = false
                mMap.clear()
                weatherMapViewModel.loadBookmarks()
                ivBookMark.setColorFilter(
                        ContextCompat.getColor(
                                activity!!.applicationContext,
                                R.color.yellow
                        ), android.graphics.PorterDuff.Mode.MULTIPLY
                );

            }
            R.id.ivHelp -> {
                expandBottomSheet(isWebView = true)


            }
        }
    }

    private fun resetData() {
        ivHelp.setColorFilter(
                ContextCompat.getColor(activity!!.applicationContext, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
        );
        ivBookMark.setColorFilter(
                ContextCompat.getColor(
                        activity!!.applicationContext,
                        R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
        );
        ivExploreWeather.setColorFilter(
                ContextCompat.getColor(
                        activity!!.applicationContext,
                        R.color.white
                ), android.graphics.PorterDuff.Mode.MULTIPLY
        );

    }

    override fun onBackPressed(): Boolean {
        return if (sheetBehavior.state == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            true
        } else {
            false
        }
    }
}
