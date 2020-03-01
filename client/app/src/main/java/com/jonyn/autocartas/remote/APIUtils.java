package com.jonyn.autocartas.remote;

import com.jonyn.autocartas.APIRestClient;

public class APIUtils {
    //Evitamos que se puedan crear objetos ya que queremos que la clase sea estática
    private APIUtils() {
    }

    //public static final String BASE_URL = "http://192.168.1.141:8081/AUTOCARTASREST/rest/restcoches/";


    private static String ip = "192.168.1.141";
    private static String port = "8081";
    private static final String BASE_URL = "http://" + ip + ":" + port + "/AUTOCARTASREST/rest/restcoches/";

    public static APIService getAPIService() {
        return APIRestClient.getInstance(BASE_URL).getRetrofit().create(APIService.class);
    }
   /*public static APIService getAPIService(String ip, String port) {
      return APIRestClient.getInstance("http://"+ip+":"+port+"/AUTOCARTASREST/rest/restcoches/").getRetrofit().create(APIService.class);
   }*/

   public static boolean removeInstance(){
       return APIRestClient.removeInstance();
   }

   public static void setIp(String newIp){
      ip = newIp;
   }

   public static void setPort(String newPort){
      port = newPort;
   }

}
