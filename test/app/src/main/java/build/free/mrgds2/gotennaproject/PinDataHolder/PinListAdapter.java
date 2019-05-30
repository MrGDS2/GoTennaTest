package build.free.mrgds2.gotennaproject.PinDataHolder;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import build.free.mrgds2.gotennaproject.R;
import build.free.mrgds2.gotennaproject.SQLITE.SqLiteDBHelper;
import build.free.mrgds2.gotennaproject.api.PinDataModel;

import java.util.ArrayList;
import java.util.List;

public class PinListAdapter  extends RecyclerView.Adapter<PinListAdapter.PinViewHolder> {

    private Context context;
    private List<PinDataModel> dataList;
    private SqLiteDBHelper mdbhelper;
    //==============================================================================================
    // Constructor
    //==============================================================================================
    public PinListAdapter(Context context, List<PinDataModel> pindataList, SqLiteDBHelper mdbhelper) {
        this.context = context;
        this.dataList = pindataList;
        this.mdbhelper=mdbhelper;
    }
    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================
    @NonNull
    @Override
    public PinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_data_item,parent,false);
        return new PinViewHolder(view);
    }
    //==============================================================================================
    // RecyclerView.Adapter Methods
    //==============================================================================================
    @Override
    public void onBindViewHolder(@NonNull PinViewHolder pinViewHolder, int position) {

/*position starts at 0 so i need to add 1
 to position for
 correct pin data
 retrieval*/

        Cursor cursor = mdbhelper.getPin(position+1 );

        if(cursor.getCount()==0)
            Toast.makeText(context, "nothing in database", Toast.LENGTH_LONG).show();
        if (cursor.moveToFirst()) {
            do {
                pinViewHolder.tvName.setText(cursor.getString(1)); // get the data into viewholder
                pinViewHolder.tvDescription.setText(cursor.getString(2));
                pinViewHolder.tvLat.setText(cursor.getString(3));

            }
            while (cursor.moveToNext());
        }

        cursor.close();//release resources

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }




    class PinViewHolder extends RecyclerView.ViewHolder{

    public TextView tvName, tvDescription,tvLat,tvLng;

    public PinViewHolder(View itemView) {
        super(itemView);
        tvName= (TextView) itemView.findViewById(R.id.pin_name);
        tvDescription = (TextView) itemView.findViewById(R.id.pin_des);
        tvLat = (TextView) itemView.findViewById(R.id.pin_lat_lng);


    }






}
}