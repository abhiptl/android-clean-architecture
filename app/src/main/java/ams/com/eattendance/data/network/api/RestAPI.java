package ams.com.eattendance.data.network.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestAPI {

	@GET("/companies/validatelogin")
	Call<Boolean> validateLogin(@Query("name") String companyName, @Query("phoneNumber") String phoneNumber);
}
