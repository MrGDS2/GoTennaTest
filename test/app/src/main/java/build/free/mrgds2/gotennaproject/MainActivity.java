package build.free.mrgds2.gotennaproject;


import android.content.DialogInterface;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import build.free.mrgds2.gotennaproject.MapActivity;
import build.free.mrgds2.gotennaproject.PinDataHolder.PinDataListActivity;
import build.free.mrgds2.gotennaproject.R;


/**
 * The main screen that lets you navigate to all other screens in the app.
 *
 */

public class MainActivity extends AppCompatActivity {
    AlertDialog ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_main_title);
        setContentView(R.layout.activity_main);
    }

    public void onMapClicked(View v)
    {

        this.startActivity(new Intent(MainActivity.this, MapActivity.class));
    }

    public void onListClicked(View v)
    {
        this.startActivity(new Intent(MainActivity.this, PinDataListActivity.class));
    }



    @Override
    public void onBackPressed()
    {
        Alert();
    }
    public void Alert() {

        ad = new AlertDialog.Builder(this).setIcon(R.drawable.ic_wait_exit).setTitle(
                "Are you sure you want to exit?").setMessage(
                "But we just met?").setCancelable(false)
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                ad.cancel();   // cancel
                            }


                        }).setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();// close app


                            }
                        }).show();


    }



}
