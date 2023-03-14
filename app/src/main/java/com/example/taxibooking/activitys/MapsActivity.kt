package com.example.taxibooking.activitys


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.taxibooking.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.taxibooking.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsRoute
import com.google.maps.model.TravelMode


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val DEFAULT_ZOOM = 15f
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var polyline: Polyline
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        polyline = mMap.addPolyline(PolylineOptions().width(5f).color(Color.RED))
        geocoder = Geocoder(this)
        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title("select"))
            geocoder.getFromLocation(it.latitude, it.longitude, 1) { data ->
                val address: ArrayList<Address> = ArrayList()
                address.addAll(data)
                Log.d("ANKIT", address[0].getAddressLine(0))
            }
        }


        getCurrentLocationAndSetMarkerOnMap()
    }

//    private fun getDistanceBetweenPoints(point1: LatLng, point2: LatLng): Float {
//        val results = FloatArray(1)
//        Location.distanceBetween(
//            point1.latitude, point1.longitude,
//            point2.latitude, point2.longitude, results
//        )
//        return results[0]
//    }

    private fun getCurrentLocationAndSetMarkerOnMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val currentMarkerOptions =
                        MarkerOptions().position(currentLatLng).title("My Location")
                    mMap.addMarker(currentMarkerOptions)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
                    val destinationLatLng = LatLng(30.760618, 76.765388)
                    val destinationMarkerOptions =
                        MarkerOptions().position(destinationLatLng).title("Destination Location")
                    mMap.addMarker(destinationMarkerOptions)


                    // Draw a line between the two points
                    val points = listOf(currentLatLng, destinationLatLng)
                    polyline.points = points

                    val bounds =
                        LatLngBounds.Builder().include(currentLatLng).include(destinationLatLng)
                            .build()
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                } else {
                    // Location not available, handle the error
                }
            }
        } else {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 123
            )
        }

        // Step 4: Override the onRequestPermissionsResult method to handle the user's response to the permission request

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndSetMarkerOnMap()
            } else {
                // Permission denied, handle the error
            }
        }
    }


//    private fun getDistanceBetweenPoints(origin: LatLng, destination: LatLng) {
//        val api = GeoApiContext.Builder()
//            .apiKey(getString(R.string.google_maps_key))
//            .build()
//        val req = DirectionsApi.newRequest(api)
//            .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
//            .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
//            .mode(TravelMode.DRIVING) // Set the travel mode to driving
//            .await()
//
//        // Draw the route on the map
//        drawRouteOnMap(req.routes[0])
//    }
//    private fun drawRouteOnMap(route: DirectionsRoute) {
//        val lineOptions = PolylineOptions()
//        val legs = route.legs
//        for (i in legs.indices) {
//            val steps = legs[i].steps
//            for (j in steps.indices) {
//                val points = steps[j].polyline.decodePath()
//                for (point in points) {
//                    lineOptions.add(LatLng(point.lat, point.lng))
//                }
//            }
//        }
//        lineOptions.width(10f)
//        lineOptions.color(Color.BLUE)
//        mMap.addPolyline(lineOptions)
//    }
}