package build.free.mrgds2.gotennaproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import build.free.mrgds2.gotennaproject.PinDataHolder.PinDataListActivity
import build.free.mrgds2.gotennaproject.SQLITE.SqLiteDBHelper
import build.free.mrgds2.gotennaproject.api.JsonHelperApi
import build.free.mrgds2.gotennaproject.api.PinDataModel
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_map_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//Save the data to persistent storage of your choice. TODO:fix db
//Display the user’s location on the map.
//Display the Pin data on the map.       TODO:fix from db
//:Display the Pin data in a list.


class MapActivity : AppCompatActivity(),OnMapReadyCallback, PermissionsListener {

    private lateinit var mapView: MapView
    private lateinit var  permissionsManager :PermissionsManager
    private lateinit var mapboxMap: MapboxMap

    private lateinit var pinDataModel :PinDataModel

    private   var originLocation :Location? = null
    private  var pinListArray : List<PinDataModel>?=null


    private val mdbHelper= SqLiteDBHelper(this@MapActivity)//Set up DB for pulling data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pinDataModel = PinDataModel()//model pin obj

        //mdbHelper.
        // Mapbox Access token
        Mapbox.getInstance(applicationContext, getString(R.string.accessToken))
        setContentView(R.layout.activity_map_view)






        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)//save instance on screen change
        mapView.getMapAsync(this)
//        mapView.getMapAsync({
//            it.setStyle(Style.TRAFFIC_DAY)
//
//
//            // Customize map with markers, polylines, etc.
//        })


        /* RETROFIT IMPLEMENTATION to get post **/
        val rf = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.GET_map_pins_js))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonapi = rf.create<JsonHelperApi>(JsonHelperApi::class.java!!)


        val call = jsonapi.posts

        /*excute the call**/
        call.enqueue(object : Callback<List<PinDataModel>> {
            override fun onResponse(call: Call<List<PinDataModel>>, response: Response<List<PinDataModel>>) {
                /**check 404 error**
                 */
                if (!response.isSuccessful()) {
                    Toast.makeText(this@MapActivity, "code:" + response.code(), Toast.LENGTH_LONG).show()
                    return
                }
                //  Toast.makeText(this@PinDataListActivity, "code:" + response.code(), Toast.LENGTH_LONG).show()
                pinListArray = response.body()!!

                val posts = response.body()

                for (pinDlist  in posts!!) {

                    pinDataModel.name = pinDlist.name
                    pinDataModel.des = pinDlist.des
                    pinDataModel.lat=pinDlist.lat
                    pinDataModel.lng=pinDlist.lng

                    mdbHelper.insertPin(pinDataModel.name ,
                        pinDataModel.des,
                        pinDataModel.lat.toString(),
                        pinDataModel.lng.toString())// inserts data into the phones local memory db//push to phone db


                    placeMakers(
                        pinDataModel.name,
                        pinDataModel.des,
                        pinDataModel.lat.toDouble(),pinDataModel.lng.toDouble())

                }


            }

            override fun onFailure(call: Call<List<PinDataModel>>, t: Throwable) {
                Toast.makeText(this@MapActivity, "failed=>" + t.message, Toast.LENGTH_LONG).show()
                Log.e("ERROR:=>", t.message)

            }
        })
        /**FLOATING ACTION BUTTONS**/
        location_fab.setOnClickListener { view ->
            //set camera to user location
            val lastLocation=mapboxMap.locationComponent.lastKnownLocation
           // originLocation=lastLocation//it's not going to be!!*message to complier(!!)
            setCameraPosition(lastLocation)

        }




    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.Builder().fromUrl(
                "mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"
            )
        ) {
            // MapActivity is set up and the style has loaded. Now you can add data or make other map adjustments
            enableLocation(it);
        }


        val position  = LatLng(pinDataModel.lat.toDouble(),pinDataModel.lng.toDouble())

        val mapPosition = CameraPosition.Builder()
            .target(LatLng(position))
            .zoom(25.0)
            .tilt(20.0)
            .build()

    }

    private  fun enableLocation(loadedMapStyle: Style){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initLocationEngine(loadedMapStyle)
        }
        else{
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }

    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this,"becuase",Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocation(mapboxMap.style!!)
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show()
            finish()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            200->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //   Toast.makeText(this, "Showing Pace campus near you", Toast.LENGTH_LONG).show()
                }
                else

                    Toast.makeText(this, "For full app functions these premission are needed", Toast.LENGTH_LONG).show();
            // return


        }

    }

    @SuppressLint("MissingPermission")
    private  fun initLocationEngine(loadedMapStyle: Style) {
        //// Create and customize the LocationComponent's options
        val customLocationComponentOptions = LocationComponentOptions.builder(this)
            .trackingGesturesManagement(true)
            .accuracyColor(ContextCompat.getColor(this, R.color.mapboxGreen))
            .build()
        val locationComponentActivationOptions =
            LocationComponentActivationOptions.builder(this, loadedMapStyle)
                .locationComponentOptions(customLocationComponentOptions)
                .build()

////// Get an instance of the LocationComponent and then adjust its settings
        mapboxMap.locationComponent.apply {

            //            // Activate the LocationComponent with options
            activateLocationComponent(locationComponentActivationOptions)
//
////// Enable to make the LocationComponent visible
            isLocationComponentEnabled = true
////
////// Set the LocationComponent's camera mode
            cameraMode = CameraMode.NONE_GPS
////
////// Set the LocationComponent's render mode
            renderMode = RenderMode.COMPASS
        }
    }


    private fun setCameraPosition(location: Location?){
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude,location.longitude),13.0))
    }



    fun placeMakers(title: String,description: String,lat :Double,lng : Double){
        var op= MarkerOptions()
        op.title=title
        op.snippet= description
        op.position= LatLng(lat,lng)
        mapboxMap.addMarker(op)

    }

    fun showMessage(title: String, message: String) {

        val alert = AlertDialog.Builder(this@MapActivity)
        alert.setCancelable(true)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.show()

    }

    public override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }




}
