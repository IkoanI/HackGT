package com.example.hackgt.View;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hackgt.R;

import java.util.Locale;

public class AnimalActivity extends AppCompatActivity {
    TextView item1_amount, item2_amount, item3_amount, levelCurr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.animalXML), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ProgressBar progress = findViewById(R.id.animal_level_pb);
        progress.setProgress(Player.animalProgress);

        TextView aniName = findViewById(R.id.animal_name);
        aniName.setText(Player.animalName);

        levelCurr = findViewById(R.id.level_text);

        item1_amount = findViewById(R.id.animal_food1_text);

        item2_amount = findViewById(R.id.animal_food2_text);

        item3_amount = findViewById(R.id.animal_food3_text);

        progressChecker();
        updateItems();

        Button backMain = findViewById(R.id.back_button_animal);
        backMain.setOnClickListener((v -> {
            Intent intent = new Intent(AnimalActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }));

        Button item1bt = findViewById(R.id.animal_food1_bt);
        item1bt.setOnClickListener(v -> {
            if (Player.items[0] > 0) {
                Player.items[0]  -= 1;
                Player.animalProgress += 5;
                progressChecker();
                updateItems();
                progress.setProgress(Player.animalProgress);

            }
        });

        Button item2bt = findViewById(R.id.animal_food2_bt);
        item2bt.setOnClickListener(v -> {
            if (Player.items[1] > 0) {
                Player.items[1] -= 1;
                Player.animalProgress += 40;
                progressChecker();
                updateItems();
                progress.setProgress(Player.animalProgress);

            }
        });

        Button item3bt = findViewById(R.id.animal_food3_bt);
        item3bt.setOnClickListener(v -> {
            if (Player.items[2] > 0) {
                Player.items[2] -= 1;
                Player.animalProgress += 100;
                progressChecker();
                updateItems();
                progress.setProgress(Player.animalProgress);
            }
        });
    }

    private void progressChecker() {
        if (Player.animalProgress >= 100) {
            Player.animalProgress -= 100;
            Player.animalLevel += 1;
        }
        levelCurr.setText(String.format(Locale.ENGLISH, "Level: %d", Player.animalLevel));
    }

    private void updateItems(){
        item1_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[0]));
        item2_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[1]));
        item3_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[2]));
    }
}