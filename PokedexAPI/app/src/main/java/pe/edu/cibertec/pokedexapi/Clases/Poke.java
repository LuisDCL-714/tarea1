package pe.edu.cibertec.pokedexapi.Clases;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Poke {

    @SerializedName("pokemon")
    @Expose
    private Poke_ pokemon;

    public Poke_ getPokemon() {
        return pokemon;
    }

    public void setPokemon(Poke_ pokemon) {
        this.pokemon = pokemon;
    }

}

class Poke_ {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}