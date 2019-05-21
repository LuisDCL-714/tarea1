package pe.edu.cibertec.pokedexapi.retrofit;

import pe.edu.cibertec.pokedexapi.Clases.Abilities;
import pe.edu.cibertec.pokedexapi.Clases.Pokemon;
import pe.edu.cibertec.pokedexapi.Clases.Types;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokedexInterface {
    @GET("{endpoint}/{name}")
    Call<Pokemon> searchPokemon(@Path("endpoint")String selected, @Path("name") String wrote);

    @GET("{endpoint}/{name}")
    Call<Types> searchTypes(@Path("endpoint")String selected, @Path("name") String wrote);

    @GET("{endpoint}/{name}")
    Call<Abilities> searchAbilities(@Path("endpoint")String selected, @Path("name") String wrote);
}
