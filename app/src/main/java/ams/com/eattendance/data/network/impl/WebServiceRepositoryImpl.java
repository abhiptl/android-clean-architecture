package ams.com.eattendance.data.network.impl;

import java.io.IOException;

import ams.com.eattendance.data.network.RestAPIClient;
import ams.com.eattendance.data.network.api.RestAPI;
import ams.com.eattendance.domain.repository.WebServiceRepository;
import retrofit2.Call;
import retrofit2.Response;

public class WebServiceRepositoryImpl implements WebServiceRepository {

	public Boolean getCompanyByName(final String name, final String phoneNumber) throws IOException {
		RestAPI api = RestAPIClient.getApi(RestAPI.class);
		Call<Boolean> booleanCall = api.validateLogin(name, phoneNumber);
		Response<Boolean> response = booleanCall.execute();

		if (response.isSuccessful()) {
			return response.body();
		} else {
			return null;
		}

	}
}
