package ams.com.eattendance.data.network.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by WeConnect on 4/12/2017.
 */
public interface RestAPI {

	@GET("/companies/validatelogin")
	Call<Boolean> validateLogin(@Query("name") String companyName, @Query("phoneNumber") String phoneNumber);
}
