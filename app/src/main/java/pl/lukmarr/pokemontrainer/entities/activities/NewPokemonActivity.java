package pl.lukmarr.pokemontrainer.entities.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.connection.RxDetails;
import pl.lukmarr.pokemontrainer.database.AchievementsManager;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.model.PokemonDescription;
import rx.functions.Action1;

public class NewPokemonActivity extends AppCompatActivity {

    @Bind(R.id.description)
    TextView description;

    @Bind(R.id.mainTitle)
    TextView mtitle;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.pokemonImage)
    ImageView pokemonImage;

    @OnClick(R.id.pokemonImage)
    public void zzx() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pokemon);
        ButterKnife.bind(this);

        Intent i = getIntent();
        int pokemonId = i.getIntExtra("POKEMON_ID", -1);
        String message = i.getStringExtra("TITLE");


        Realm realm = Realm.getInstance(this);
        final RealmPoke poke = realm.where(RealmPoke.class).equalTo("id", pokemonId).findFirst();

        if (pokemonId == -1 || poke == null) {
            finish();
        } else {
            RxDetails.fetchDescriptionForId(pokemonId, new Action1<PokemonDescription>() {
                @Override
                public void call(final PokemonDescription pokemonDescription) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String descriptionMessage = pokemonDescription.description;
                            description.setText(descriptionMessage);
                        }
                    });
                }
            });

            mtitle.setText(message == null ? "New wild pokemon appeared!" : message);
            title.setText(poke.getName());
            Picasso.with(this).load(poke.getImage()).into(pokemonImage);
        }
        AchievementsManager.unlockPokemon(this, pokemonId);
        AchievementsManager.checkAchievements(this);
    }

    public static Intent buildIntent(RealmPoke pokemon,String message, Activity activity) {
        Intent intent = new Intent(activity, NewPokemonActivity.class);
        intent.putExtra("POKEMON_ID", pokemon.getId());
        intent.putExtra("TITLE", message);
        return intent;
    }
}
