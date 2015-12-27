package pl.lukmarr.pokemontrainer.entities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.database.RealmPoke;

public class NewPokemonActivity extends AppCompatActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.pokemonImage)
    ImageView pokemonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pokemon);
        ButterKnife.bind(this);
        setTitle("New wild pokemon appeared!");

        Intent i = getIntent();
        int pokemonId = i.getIntExtra("POKEMON_ID", -1);
        Realm realm = Realm.getInstance(this);
        RealmPoke poke = realm.where(RealmPoke.class).equalTo("id", pokemonId).findFirst();

        if (pokemonId == -1 || poke == null) {
            finish();
        } else {
            title.setText(poke.getName());
            Picasso.with(this).load(poke.getImage()).into(pokemonImage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_pokemon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_pokedex) {
            startMainActivity(Config.POKEDEX_FRAGMENT);
            return true;
        } else if (id == R.id.action_details) {
            startMainActivity(Config.DETAILS_FRAGMENT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void startMainActivity(int fragmentId) {

    }
}
