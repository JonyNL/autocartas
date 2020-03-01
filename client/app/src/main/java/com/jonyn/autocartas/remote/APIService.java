package com.jonyn.autocartas.remote;

import com.jonyn.autocartas.modelos.Coche;
import com.jonyn.autocartas.modelos.ResPartida;
import com.jonyn.autocartas.modelos.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    /**
     *  METODOS CRUD
     */

    /**
     * Metodos get para leer los usuarios
     */
    @GET("users")
    Call<List<User>> readUsers();

    @GET("users/{user}")
    Call<User> readUser(@Path("user") String user);

    /**
     * Metodo para insertar un usuario
     */
    @POST("users")
    @FormUrlEncoded
    Call<String> createUser(@Field("user") String user,
                            @Field("nombre") String nombre,
                            @Field("passwd") String passwd);

    /**
     * Metodo para editar un usuario
     */
    @PUT("users/{user}/{attr}/{newvalue}")
    Call<String> editUser(@Path("user") String user,
                          @Path("attr") String attr,
                          @Path("newvalue") String newvalue);

    /**
     * Metodo para borrar un usuario
     */
    @DELETE("users/{user}")
    Call<String> deleteUser(@Path("user") String user);

    /**
     * Metodos get para leer los coches
     */
    @GET("coches")
    Call<List<Coche>> readCoches();

    @GET("coches/{id}")
    Call<Coche> readCoche(@Path("id") String idCoche);

    /**
     * Metodo para insertar un usuario
     */
    @POST("coches")
    @FormUrlEncoded
    Call<String> createCoche(@Field("id") String id, @Field("modelo") String modelo,
                             @Field("pais") String pais, @Field("motor") int motor,
                             @Field("cilindros") int cilindros, @Field("potencia") int potencia,
                             @Field("revxmin") int revxmin, @Field("velocidad") int velocidad,
                             @Field("consAt100km") int consAt100km);

    /**
     * Metodo para editar un usuario
     */
    @PUT("coches/{idCoche}/{attr}/{newvalue}")
    Call<String> editCoche(@Path("idCoche") String idCoche,
                          @Path("attr") String attr,
                          @Path("newvalue") String newvalue);

    /**
     * Metodo para borrar un usuario
     */
    @DELETE("coches/{idCoche}")
    Call<String> deleteCoche(@Path("idCoche") String idCoche);

    /**
     * Metodo get para recibir el listado de usuarios ordenado en base a sus estadisticas
     */
    @GET("stats")
    Call<List<User>> getStats();

    /**
     * Metodos para hacer login y logout
     */
    @POST("login")
    @FormUrlEncoded
    Call<String> login(@Field("user") String user,
                       @Field("passwd") String passwd);

    @POST("logout")
    @FormUrlEncoded
    Call<String> logout(@Field("user") String user,
                        @Field("idSesion") String idSesion);

    @POST("newgame")
    @FormUrlEncoded
    Call<ResPartida> getNewGame(@Field("idSesion") String idSesion);

    @POST("resetgame")
    @FormUrlEncoded
    Call<ResPartida> getResetGame(@Field("idSesion") String idSesion);

    @POST("raffle")
    @FormUrlEncoded
    Call<String> makeRaffle(@Field("idSesion") String idSesion);

    @POST("playcard")
    @FormUrlEncoded
    Call<ResPartida> playCard(@Field("idSesion") String idSesion, @Field("idCocheP1")
            String idCocheP1, @Field("Caracteristica") String caracteristica);

    @POST("ready")
    @FormUrlEncoded
    Call<ResPartida> ready(@Field("idSesion") String idSesion);
}
