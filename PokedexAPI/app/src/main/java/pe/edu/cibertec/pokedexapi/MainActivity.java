package pe.edu.cibertec.pokedexapi;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pe.edu.cibertec.pokedexapi.Clases.Abilities;
import pe.edu.cibertec.pokedexapi.Clases.Ability;
import pe.edu.cibertec.pokedexapi.Clases.Ability_;
import pe.edu.cibertec.pokedexapi.Clases.EffectEntry;
import pe.edu.cibertec.pokedexapi.Clases.Move;
import pe.edu.cibertec.pokedexapi.Clases.Poke;
import pe.edu.cibertec.pokedexapi.Clases.Poke_;
import pe.edu.cibertec.pokedexapi.Clases.Pokemon;
import pe.edu.cibertec.pokedexapi.Clases.Sprites;
import pe.edu.cibertec.pokedexapi.Clases.Stat;
import pe.edu.cibertec.pokedexapi.Clases.Stat_;
import pe.edu.cibertec.pokedexapi.Clases.Type;
import pe.edu.cibertec.pokedexapi.Clases.Type_;
import pe.edu.cibertec.pokedexapi.Clases.Types;
import pe.edu.cibertec.pokedexapi.retrofit.PokedexInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner selectSearchPokemon;
    private static List<String> paths;
    private ArrayAdapter<String> adapter;
    private int listsize;
    private String selected, searched, stringAbilities, type, stat, moves, pokemones, effects;
    private TextInputLayout inputSearch;
    private EditText editSearch;
    private Button buttonSearch;
    private ImageView imageFrontPokemon, imageBackPokemon, imageFrontShiny, imageBackShiny;
    private LinearLayout layoutLeft, layoutRight;
    private LinearLayout.LayoutParams param;
    private Retrofit retrofit;
    private PokedexInterface service;
    private ProgressDialog progress;
    private ScrollView scrollSearch;
    private TextView textName, textWeight, textHeight, textAbilities, textTypes, textStats, textImage, textShiny;
    private Pokemon pokemon;
    private List<Ability> listAbility;
    private List<Type> listType;
    private List<Stat> listStat;
    private Ability_ ability_;
    private Type_ type_;
    private Stat_ stat_;
    private Sprites sprites;
    private Types types;
    private List<Move> move;
    private List<Poke> listPoke;
    private Poke_ poke_;
    private Abilities abilities;
    private List<EffectEntry> listEffectEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paths = new ArrayList<String>();
        paths.add("Pokémon");
        paths.add("Tipo");
        paths.add("Habilidad");
        paths.add("Seleccione");
        listsize = paths.size() - 1;

        selectSearchPokemon = findViewById(R.id.selectSearchPokemon);
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, paths){
            @Override
            public int getCount() {
                return(listsize);
            }
        };
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        selectSearchPokemon.setAdapter(adapter);
        selectSearchPokemon.setSelection(listsize);

        inputSearch = findViewById(R.id.inputSearch);
        editSearch = findViewById(R.id.editSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        scrollSearch = findViewById(R.id.scrollSearch);
        textName = findViewById(R.id.textName);
        textWeight = findViewById(R.id.textWeight);
        textHeight = findViewById(R.id.textHeight);
        textAbilities = findViewById(R.id.textAbilities);
        textTypes = findViewById(R.id.textTypes);
        textStats = findViewById(R.id.textStats);
        textImage = findViewById(R.id.imagePokemon);
        textShiny = findViewById(R.id.imageShiny);
        layoutLeft = findViewById(R.id.layoutLeft);
        layoutRight = findViewById(R.id.layoutRight);
        imageFrontPokemon = findViewById(R.id.imageFrontPokemon);
        imageBackPokemon = findViewById(R.id.imageBackPokemon);
        imageFrontShiny = findViewById(R.id.imageFrontShiny);
        imageBackShiny = findViewById(R.id.imageBackShiny);

        selectSearchPokemon.setOnItemSelectedListener(this);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(MainActivity.this);
                progress.setMessage("Cargando");
                progress.setCancelable(false);
                progress.show();

                textName.setText("");
                textWeight.setText("");
                textHeight.setText("");
                textAbilities.setText("");
                textTypes.setText("");
                textStats.setText("");
                imageFrontPokemon.setImageResource(0);
                imageBackPokemon.setImageResource(0);
                imageFrontShiny.setImageResource(0);
                imageBackShiny.setImageResource(0);

                searched = editSearch.getText().toString();
                if(searched.equals("")){
                    scrollSearch.setVisibility(View.GONE);
                    progress.hide();
                    Toast.makeText(MainActivity.this, "¡Por favor escribe algo!", Toast.LENGTH_LONG).show();
                }else{
                    retrofit = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build();
                    service = retrofit.create(PokedexInterface.class);
                    if(selected.equals("pokemon")){
                        textImage.setVisibility(View.VISIBLE);
                        textShiny.setVisibility(View.VISIBLE);
                        param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT,1);
                        layoutLeft.setLayoutParams(param);
                        layoutRight.setLayoutParams(param);
                        Call<Pokemon> method;
                        method = service.searchPokemon(selected, searched);
                        method.enqueue(new Callback<Pokemon>() {
                            @Override
                            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                                if(response.isSuccessful()){
                                    pokemon = response.body();
                                    textName.setText("Name: "+pokemon.getName()+".\n");
                                    textWeight.setText("Weight: "+pokemon.getWeight()+" lb\n");
                                    textHeight.setText("Height: "+pokemon.getHeight()+" ft\n");
                                    stringAbilities = "Abilities:\n";
                                    type = "Type:\n";
                                    stat = "Statistics:\n";
                                    listAbility = pokemon.getAbilities();
                                    listType = pokemon.getTypes();
                                    listStat = pokemon.getStats();
                                    for(int i = 0; i < listAbility.size(); i++){
                                        ability_ = listAbility.get(i).getAbility();
                                        stringAbilities += "- "+ability_.getName()+".\n";
                                    }
                                    for(int i = 0; i < listType.size(); i++){
                                        type_ = listType.get(i).getType();
                                        type += "- "+type_.getName()+".\n";
                                    }
                                    for(int i = 0; i < listStat.size(); i++){
                                        stat_ = listStat.get(i).getStat();
                                        stat += "- "+stat_.getName()+": "+listStat.get(i).getBaseStat()+"%\n";
                                    }
                                    textAbilities.setText(stringAbilities);
                                    textTypes.setText(type);
                                    textStats.setText(stat);
                                    sprites = pokemon.getSprites();
                                    Glide.with(MainActivity.this).load(sprites.getFrontDefault()).into(imageFrontPokemon);
                                    Glide.with(MainActivity.this).load(sprites.getBackDefault()).into(imageBackPokemon);
                                    Glide.with(MainActivity.this).load(sprites.getFrontShiny()).into(imageFrontShiny);
                                    Glide.with(MainActivity.this).load(sprites.getBackShiny()).into(imageBackShiny);
                                    scrollSearch.setVisibility(View.VISIBLE);
                                    progress.hide();
                                }else{
                                    scrollSearch.setVisibility(View.GONE);
                                    progress.hide();
                                    Toast.makeText(MainActivity.this, "¡No se encontro al pokémon "+searched+"!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Pokemon> call, Throwable t) {
                                scrollSearch.setVisibility(View.GONE);
                                Log.e("Error", "Causa: "+t.getCause()+", Mensaje: "+t.getMessage());
                                progress.hide();
                                Toast.makeText(MainActivity.this, "¡Se presento un error!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else if(selected.equals("type")){
                        textImage.setVisibility(View.GONE);
                        textShiny.setVisibility(View.GONE);
                        param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT,1);
                        layoutLeft.setLayoutParams(param);
                        param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT,0);
                        layoutRight.setLayoutParams(param);
                        Call<Types> method;
                        method = service.searchTypes(selected, searched);
                        method.enqueue(new Callback<Types>() {
                            @Override
                            public void onResponse(Call<Types> call, Response<Types> response) {
                                if(response.isSuccessful()){
                                    types = response.body();
                                    textName.setText("Type: "+types.getName()+".\n");
                                    move = types.getMoves();
                                    listPoke = types.getPokemon();
                                    moves = "Moves:\n";
                                    pokemones = "List of Pokemon:\n";
                                    for(int i = 0; i < move.size(); i++){
                                        moves += "- "+move.get(i).getName()+".\n";
                                    }
                                    for(int i = 0; i < listPoke.size(); i++){
                                        poke_ = listPoke.get(i).getPokemon();
                                        pokemones += "- "+poke_.getName()+".\n";
                                    }
                                    textWeight.setText(moves);
                                    textHeight.setText(pokemones);
                                    scrollSearch.setVisibility(View.VISIBLE);
                                    progress.hide();
                                }else{
                                    scrollSearch.setVisibility(View.GONE);
                                    progress.hide();
                                    Toast.makeText(MainActivity.this, "¡No se encontro ningún pokemon de tipo "+searched+"!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Types> call, Throwable t) {
                                scrollSearch.setVisibility(View.GONE);
                                Log.e("Error", "Causa: "+t.getCause()+", Mensaje: "+t.getMessage());
                                progress.hide();
                                Toast.makeText(MainActivity.this, "¡Se presento un error!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else if(selected.equals("ability")){
                        textImage.setVisibility(View.GONE);
                        textShiny.setVisibility(View.GONE);
                        param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT,1);
                        layoutLeft.setLayoutParams(param);
                        param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT,0);
                        layoutRight.setLayoutParams(param);
                        Call<Abilities> method;
                        method = service.searchAbilities(selected, searched);
                        method.enqueue(new Callback<Abilities>() {
                            @Override
                            public void onResponse(Call<Abilities> call, Response<Abilities> response) {
                                if(response.isSuccessful()){
                                    abilities = response.body();
                                    textName.setText("Ability: "+abilities.getName()+".\n");
                                    listEffectEntry = abilities.getEffectEntries();
                                    listPoke = abilities.getPokemon();
                                    effects = "Effects:\n";
                                    pokemones = "List of Pokemon:\n";
                                    for(int i = 0; i < listEffectEntry.size(); i++){
                                        effects += "- Description: "+listEffectEntry.get(i).getEffect()+". Summary: "+listEffectEntry.get(i).getShortEffect()+".\n";
                                    }
                                    for(int i = 0; i < listPoke.size(); i++){
                                        poke_ = listPoke.get(i).getPokemon();
                                        pokemones += "- "+poke_.getName()+".\n";
                                    }
                                    textWeight.setText(effects);
                                    textHeight.setText(pokemones);
                                    scrollSearch.setVisibility(View.VISIBLE);
                                    progress.hide();
                                }else{
                                    scrollSearch.setVisibility(View.GONE);
                                    progress.hide();
                                    Toast.makeText(MainActivity.this, "¡No se encontro ningún pokemon con la habilidad "+searched+"!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Abilities> call, Throwable t) {
                                scrollSearch.setVisibility(View.GONE);
                                Log.e("Error", "Causa: "+t.getCause()+", Mensaje: "+t.getMessage());
                                progress.hide();
                                Toast.makeText(MainActivity.this, "¡Se presento un error!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                selected = "pokemon";
                editSearch.setText("");
                scrollSearch.setVisibility(View.GONE);
                inputSearch.setVisibility(View.VISIBLE);
                buttonSearch.setVisibility(View.VISIBLE);
                break;
            case 1:
                selected = "type";
                editSearch.setText("");
                scrollSearch.setVisibility(View.GONE);
                inputSearch.setVisibility(View.VISIBLE);
                buttonSearch.setVisibility(View.VISIBLE);
                break;
            case 2:
                selected = "ability";
                editSearch.setText("");
                scrollSearch.setVisibility(View.GONE);
                inputSearch.setVisibility(View.VISIBLE);
                buttonSearch.setVisibility(View.VISIBLE);
                break;
            default:
                inputSearch.setVisibility(View.GONE);
                buttonSearch.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.e("Error", "No se seleccionó nada");
    }
}
