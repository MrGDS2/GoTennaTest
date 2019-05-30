package build.free.mrgds2.gotennaproject.api;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonHelperApi {

    @GET("get_map_pins.php")
    Call<List<PinDataModel>> getPosts();


}

