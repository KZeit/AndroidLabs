package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> characterNames;
    private ArrayList<String> characterDetails;
    private ExecutorService executorService;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        characterNames = new ArrayList<>();
        characterDetails = new ArrayList<>();

        listView = findViewById(R.id.listView);
        if (listView == null) {
            throw new NullPointerException("ListView is null. Please check the layout file for a ListView with the ID 'listView'.");
        }

        CharacterAdapter adapter = new CharacterAdapter(this, characterNames);
        listView.setAdapter(adapter);

        executorService = Executors.newSingleThreadExecutor();

        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String details = characterDetails.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("details", details);

                if (findViewById(R.id.detailFragmentContainer) != null) {
                    DetailsFragment detailsFragment = new DetailsFragment();
                    detailsFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.detailFragmentContainer, detailsFragment)
                            .commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void fetchData() {
        String urlString = "https://swapi.dev/api/people/?format=json";
        executorService.submit(() -> {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result.toString());
                        JSONArray characters = jsonObject.getJSONArray("results");
                        for (int i = 0; i < characters.length(); i++) {
                            JSONObject character = characters.getJSONObject(i);
                            String name = character.getString("name");
                            characterNames.add(name);
                            characterDetails.add(character.toString());
                        }
                        ((CharacterAdapter) listView.getAdapter()).notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed to parse data", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
