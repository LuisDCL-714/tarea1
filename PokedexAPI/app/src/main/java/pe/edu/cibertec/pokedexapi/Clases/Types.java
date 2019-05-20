package pe.edu.cibertec.pokedexapi.Clases;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Types {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pokemon")
    @Expose
    private List<Poke> pokemon = null;
    @SerializedName("moves")
    @Expose
    private List<Move> moves = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Poke> getPokemon() {
        return pokemon;
    }

    public void setPokemon(List<Poke> pokemon) {
        this.pokemon = pokemon;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

}