package com.example.hackgt.View;

import static com.example.hackgt.View.MainActivity.steps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hackgt.R;

import java.util.Locale;

public class ChestActivity extends AppCompatActivity {
    TextView chest1text;
    TextView chest2text;
    TextView chest3text;
    TextView chest4text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chestXML), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backMain = findViewById(R.id.back_button_chest);
        backMain.setOnClickListener(v -> {
            Intent intent = new Intent(ChestActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        chest1text = findViewById(R.id.step_chest1);
        chest2text = findViewById(R.id.step_chest2);
        chest3text = findViewById(R.id.step_chest3);
        chest4text = findViewById(R.id.step_chest4);

        chest1text.setText(String.format(Locale.ENGLISH, "Steps taken: %d / %s", steps, Player.chestInventory.getHead().getData().getEndCount()));
        chest2text.setText(String.format(Locale.ENGLISH, "Steps taken: %d / %s", steps, Player.chestInventory.getHead().getNext().getData().getEndCount()));
        chest3text.setText(String.format(Locale.ENGLISH, "Steps taken: %d / %s", steps, Player.chestInventory.getHead().getNext().getNext().getData().getEndCount()));
        chest4text.setText(String.format(Locale.ENGLISH, "Steps taken: %d / %s", steps, Player.chestInventory.getHead().getNext().getNext().getNext().getData().getEndCount()));




    }
}
