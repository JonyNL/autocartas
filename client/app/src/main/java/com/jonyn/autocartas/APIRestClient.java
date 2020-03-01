package com.jonyn.autocartas;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRestClient {
   private static APIRestClient instance;
   private String baseUrl;
   private Retrofit retrofit;


   //Constructor privado para evitar que puedan construirse objetos de esta forma
   private APIRestClient() {
   }

   private APIRestClient(String baseUrl) {
      this.baseUrl = baseUrl;
      retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
   }

   public static APIRestClient getInstance(String baseUrl) {
      if(instance == null) {
         synchronized (APIRestClient.class) {
            if(instance == null) {
               instance = new APIRestClient(baseUrl);
            }
         }
      }
      return instance;
   }

   public static boolean removeInstance(){
      instance = null;
      return true;
   }

   public Retrofit getRetrofit() {
      return retrofit;
   }
}