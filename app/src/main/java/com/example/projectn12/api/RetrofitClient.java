package com.example.projectn12.api;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://provinces.open-api.vn/api/";
    private static Retrofit retrofit;
    private static JsonPlaceHolderAPI jsonPlaceHolderApi;

    public static JsonPlaceHolderAPI getJsonPlaceHolderApi() {
        if (jsonPlaceHolderApi == null) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            Request newRequest = originalRequest.newBuilder()
                                    .header("Interceptor-Header", "abcxyz")
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();

            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);
        }

        return jsonPlaceHolderApi;
    }
}
