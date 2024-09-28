package com.example.hackgt.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hackgt.R;

import java.util.Locale;

public class ShopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shopXML), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backMain = findViewById(R.id.backMainBt);
        backMain.setOnClickListener((v -> {
            Intent intent = new Intent(ShopActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }));

        TextView coinWord = findViewById(R.id.num_coins_shop);
        coinWord.setText(String.format(Locale.ENGLISH, "Coins: %d", Player.coins));

        TextView item1_amount = findViewById(R.id.shop_amount1);
        item1_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[0]));

        TextView item2_amount = findViewById(R.id.shop_amount2);
        item2_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[1]));

        TextView item3_amount = findViewById(R.id.shop_amount3);
        item3_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[2]));

        Button item1bt = findViewById(R.id.item_bt1);
        item1bt.setOnClickListener(v -> {
            if (Player.coins >= 10) {
                Player.spent += 10;
                Player.items[0] += 1;
                item1_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[0]));
                Player.coins -= 10;
                coinWord.setText(String.format(Locale.ENGLISH, "Coins: %d", Player.coins));
            }
        });

        Button item2bt = findViewById(R.id.item_bt2);
        item2bt.setOnClickListener(v -> {
            if (Player.coins >= 35) {
                Player.spent += 35;
                Player.items[1] += 1;
                item2_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[1]));
                Player.coins -= 35;
                coinWord.setText(String.format(Locale.ENGLISH, "Coins: %d", Player.coins));
            }
        });

        Button item3bt = findViewById(R.id.item_bt3);
        item3bt.setOnClickListener(v -> {
            if (Player.coins > 75) {
                Player.spent += 75;
                Player.items[2] += 1;
                item3_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", Player.items[2]));
                Player.coins -= 75;
                coinWord.setText(String.format(Locale.ENGLISH, "Coins: %d", Player.coins));
            }
        });

    }
}
