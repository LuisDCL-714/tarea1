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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pe.edu.cibertec.pokedexapi.Clases.Ability;
import pe.edu.cibertec.pokedexapi.Clases.Pokemon;
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
    private String selected, searched, abilities;
    private TextInputLayout inputSearch;
    private EditText editSearch;
    private Button buttonSearch;
    private Retrofit retrofit;
    private PokedexInterface service;
    private ProgressDialog progress;
    private ScrollView scrollSearch;
    private TextView textName, textWeight, textHeight, textAbilities, textTypes, textStats;
    private Pokemon pokemon;
    private Types types;

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

        selectSearchPokemon.setOnItemSelectedListener(this);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(MainActivity.this);
                progress.setMessage("Cargando");
                progress.setCancelable(false);
                progress.show();

                searched = editSearch.getText().toString();
                if(searched.equals("")){
                    scrollSearch.setVisibility(View.GONE);
                    progress.hide();
                    Toast.makeText(MainActivity.this, "¡Por favor escribe algo!", Toast.LENGTH_LONG).show();
                }else{
                    retrofit = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build();
                    service = retrofit.create(PokedexInterface.class);
                    if(selected.equals("pokemon")){
                        Call<Pokemon> method;
                        method = service.searchPokemon(selected, searched);
                        method.enqueue(new Callback<Pokemon>() {
                            @Override
                            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                                if(response.isSuccessful()){
                                    pokemon = response.body();
                                    textName.setText("Nombre: "+pokemon.getName()+"\n");
                                    textWeight.setText("Peso: "+pokemon.getWeight()+" Kg\n");
                                    textHeight.setText("Altura: "+pokemon.getHeight()+" cm\n");
                                    abilities = "Habilidades:\n";
                                    for(int i = 0; i < pokemon.getAbilities().size(); i++){
                                        abilities += "- "+pokemon.getAbilities().get(i)+"\n";
                                    }
                                    textAbilities.setText(abilities);
                                    /*textTypes, textStats;*/
                                    scrollSearch.setVisibility(View.VISIBLE);
                                    progress.hide();
                                }else{
                                    scrollSearch.setVisibility(View.GONE);
                                    progress.hide();
                                    Toast.makeText(MainActivity.this, "¡No se encontro ningun dato!", Toast.LENGTH_LONG).show();
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
                        Call<Types> method;
                        method = service.searchTypes(selected, searched);
                        method.enqueue(new Callback<Types>() {
                            @Override
                            public void onResponse(Call<Types> call, Response<Types> response) {
                                if(response.isSuccessful()){
                                    types = response.body();
                                    textName.setText("Nombre: ");
                                    scrollSearch.setVisibility(View.VISIBLE);
                                    progress.hide();
                                }else{
                                    scrollSearch.setVisibility(View.GONE);
                                    progress.hide();
                                    Toast.makeText(MainActivity.this, "¡No se encontro ningun dato!", Toast.LENGTH_LONG).show();
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
                        progress.hide();
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
