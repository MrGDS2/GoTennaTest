package build.free.mrgds2.gotennaproject.PinDataHolder



import android.os.Bundle

import android.support.design.widget.Snackbar

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager

import android.support.v7.widget.RecyclerView

import build.free.mrgds2.gotennaproject.R

import build.free.mrgds2.gotennaproject.api.JsonHelperApi



import kotlinx.android.synthetic.main.activity_pin_data_list.*

import retrofit2.converter.gson.GsonConverterFactory



import retrofit2.Retrofit

import android.widget.Toast

import android.util.Log

import build.free.mrgds2.gotennaproject.api.PinDataModel

import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response

import android.R.attr.name

import android.content.*

import android.database.Cursor

import android.database.sqlite.SQLiteDatabase

import android.preference.PreferenceManager

import android.support.v7.app.AlertDialog

import build.free.mrgds2.gotennaproject.SQLITE.SqLiteDBHelper

import android.widget.SimpleCursorAdapter

import build.free.mrgds2.gotennaproject.MapActivity





class PinDataListActivity : AppCompatActivity() {

    private lateinit var pinListArray : List<PinDataModel>

    private  lateinit var mrecyclerView : RecyclerView





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pin_data_list)

        setSupportActionBar(toolbar)

        //Set up DB for pulling data

        val mdbHelper = SqLiteDBHelper(this@PinDataListActivity)



        val pinDataModel = PinDataModel()//model pin obj



        //set recycler view

        mrecyclerView = findViewById(R.id.rvPinData)

        mrecyclerView.setHasFixedSize(true)



        //manage the layout and set adapter

        val linearlayoutManager = LinearLayoutManager(this)

        linearlayoutManager.orientation = LinearLayoutManager.VERTICAL

        mrecyclerView.layoutManager = linearlayoutManager





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
                /**check 404 error***/
                if (!response.isSuccessful()) {
                    Toast.makeText(this@PinDataListActivity, "code:" + response.code(), Toast.LENGTH_LONG).show()

                    return

                }

                //Toast.makeText(this@PinDataListActivity, "code:" + response.code(), Toast.LENGTH_LONG).show()

                pinListArray = response.body()!!
                val posts = response.body()

                for (pinDlist in posts!!) {

                    pinDataModel.name = pinDlist.name
                    pinDataModel.des = pinDlist.des
                    pinDataModel.lat = pinDlist.lat
                    pinDataModel.lng = pinDlist.lng

                    mdbHelper.insertPin(
                        pinDataModel.name,
                        pinDataModel.des,
                        pinDataModel.lat.toString(),
                        pinDataModel.lng.toString()

                    )// inserts data into the phones local memory db//push to phone db
                }

                val pinListAdapter = PinListAdapter(applicationContext, pinListArray, mdbHelper)

                mrecyclerView.adapter = pinListAdapter//set java class adapter

            }

            override fun onFailure(call: Call<List<PinDataModel>>, t: Throwable) {

                Toast.makeText(this@PinDataListActivity, "failed=>" + t.message, Toast.LENGTH_LONG).show()

                Log.e("ERROR:=>", t.message)
            }

        })
    }

    fun showMessage(title: String, message: String) {

        val alert = AlertDialog.Builder(this@PinDataListActivity)
        alert.setCancelable(true)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.show()

    }

    override fun onBackPressed() {

        super.onBackPressed()
        Toast.makeText(this@PinDataListActivity, "failed=>" + "back", Toast.LENGTH_LONG).show()

    }

    override fun onResume() {

        super.onResume()

    }

    override fun onStop() {

        super.onStop()

    }

}