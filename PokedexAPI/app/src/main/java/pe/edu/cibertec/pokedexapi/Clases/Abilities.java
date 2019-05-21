package pe.edu.cibertec.pokedexapi.Clases;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Abilities {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("effect_entries")
    @Expose
    private List<EffectEntry> effectEntries = null;
    @SerializedName("pokemon")
    @Expose
    private List<Poke> pokemon = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EffectEntry> getEffectEntries() {
        return effectEntries;
    }

    public void setEffectEntries(List<EffectEntry> effectEntries) {
        this.effectEntries = effectEntries;
    }

    public List<Poke> getPokemon() {
        return pokemon;
    }

    public void setPokemon(List<Poke> pokemon) {
        this.pokemon = pokemon;
    }

}