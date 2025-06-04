package a.randomjokes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private String punchline = "";
    private TextView jokeSetup;
    private TextView jokePunchline;
    private JokeApi jokeApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        jokeSetup = findViewById(R.id.joke_setup);
        jokePunchline = findViewById(R.id.joke_punchline);
        Button jokeButton = findViewById(R.id.button_show);
        Button nextJokeButton = findViewById(R.id.next_joke);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://official-joke-api.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jokeApi = retrofit.create(JokeApi.class);

        loadJoke();

        jokeButton.setOnClickListener(v -> {
            jokePunchline.setText(punchline);
            jokePunchline.setVisibility(View.VISIBLE);
        });

        nextJokeButton.setOnClickListener(v -> {
            loadJoke();
        });
    }

    private void loadJoke() {
        jokeApi.getRandomJoke().enqueue(new Callback<Joke>() {
            @Override
            public void onResponse(Call<Joke> call, Response<Joke> response) {
                if (response.isSuccessful() && response.body() != null) {
                    jokeSetup.setText(response.body().setup);
                    punchline = response.body().punchline;
                    jokePunchline.setVisibility(View.GONE);
                } else {
                    jokeSetup.setText("Failed to load joke.");
                    jokePunchline.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Joke> call, Throwable t) {
                jokeSetup.setText("Error: " + t.getMessage());
                jokePunchline.setVisibility(View.GONE);
            }
        });
    }
}