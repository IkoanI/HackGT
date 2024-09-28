package com.example.hackgt.View;

import static com.example.hackgt.View.ShopActivity.item1;
import static com.example.hackgt.View.ShopActivity.item2;
import static com.example.hackgt.View.ShopActivity.item3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hackgt.R;

import java.util.Locale;

public class AnimalActivity extends AppCompatActivity {
    private Button backMain;
    private TextView item1_amount;
    private TextView item2_amount;
    private TextView item3_amount;
    private TextView levelCurr;
    private int level = 0;
    private Button item1bt;
    private Button item2bt;
    private Button item3bt;
    private ProgressBar progress;
    private int progressPercent;

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

        progress = findViewById(R.id.animal_level_pb);
        progress.setProgress(progressPercent/100);

        levelCurr = findViewById(R.id.level_text);
        levelCurr.setText(String.format(Locale.ENGLISH, "Level: %d", level));

        item1_amount = findViewById(R.id.animal_food1_text);
        item1_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", item1));

        item1_amount = findViewById(R.id.animal_food1_text);
        item1_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", item1));

        item2_amount = findViewById(R.id.animal_food2_text);
        item2_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", item2));

        item3_amount = findViewById(R.id.animal_food3_text);
        item3_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", item3));

        backMain = findViewById(R.id.back_button_animal);
        backMain.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }));

        item1bt = findViewById(R.id.animal_food1_bt);
        item1bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item1 > 0) {
                    item1 -= 1;
                    progressPercent += 5;
                    progressChecker();
                }
            }
        });

        item2bt = findViewById(R.id.animal_food2_bt);
        item2bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item2 > 0) {
                    item2 -= 1;
                    progressPercent += 40;
                    progressChecker();
                }
            }
        });

        item3bt = findViewById(R.id.animal_food3_bt);
        item3bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item3 > 0) {
                    item3 -= 1;
                    progressPercent += 100;
                    progressChecker();
                }
            }
        });
    }

    private void progressChecker() {
        if (progressPercent >= 100) {
            progressPercent -= 100;
            level += 1;
        }
    }

}