package build.free.mrgds2.gotennaproject.api;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A data model that represents a chat log message fetched from the D & A Technologies Web Server.
 */
public class PinDataModel {

//    public List<PinDataModel> getData() {
//        return data;
//    }
//
//    public void setData(ArrayList<PinDataModel> data) {
//        this.data = data;
//    }



//    //@SerializedName("data")
//    private List<PinDataModel> data;


    @SerializedName("id")
    private String  id;
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private float lat;



    @SerializedName("longitude")
    private float lng;
    @SerializedName("description")
    private String des;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }


    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

}
