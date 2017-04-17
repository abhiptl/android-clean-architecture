package ams.com.eattendance.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is the main entry point for network communication. Use this class for instancing REST services which do the
 * actual communication.
 */
public class RestAPIClient {

	/* Base URL for Backend API
	*
	* 10.0.2.2 for default AVD to connect localhost API
	*
	* */
	public static final String REST_API_URL = "http://10.0.2.2:8080/";

	private static Retrofit retrofit;

	static {

		// enable logging
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
				// .addNetworkInterceptor(new StethoInterceptor())
				.build();

		retrofit = new Retrofit.Builder().baseUrl(REST_API_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build();
	}

	public static <T> T getApi(Class<T> serviceClass) {
		return retrofit.create(serviceClass);
	}
}
