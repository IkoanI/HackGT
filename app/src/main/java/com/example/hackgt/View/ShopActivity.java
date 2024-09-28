package com.example.hackgt.View;

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

import org.w3c.dom.Text;

import java.util.Locale;

public class ShopActivity extends AppCompatActivity {
    private Button backMain;
    private TextView coinWord;
    public static int item1 = 0;
    public static int item2 = 0;
    public static int item3 = 0;
    private TextView item1_amount;
    private TextView item2_amount;
    private TextView item3_amount;
    private Button item1bt;
    private Button item2bt;
    private Button item3bt;

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

        backMain = findViewById(R.id.backMainBt);
        backMain.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }));

        coinWord = findViewById(R.id.num_coins_shop);
        coinWord.setText(String.format(Locale.ENGLISH, "Coins: %d", MainActivity.coins));

        item1_amount = findViewById(R.id.shop_amount1);
        item1_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", item1));

        item2_amount = findViewById(R.id.shop_amount2);
        item2_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", item2));

        item3_amount = findViewById(R.id.shop_amount3);
        item3_amount.setText(String.format(Locale.ENGLISH, "Amount: %d", item3));

        item1bt = findViewById(R.id.item_bt1);
        item1bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.coins >= 10) {
                    MainActivity.coins -= 10;
                    item1 += 1;
                }
            }
        });

        item2bt = findViewById(R.id.item_bt2);
        item2bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.coins >= 35) {
                    MainActivity.coins -= 35;
                    item2 += 1;
                }
            }
        });

        item3bt = findViewById(R.id.item_bt3);
        item3bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.coins > 75) {
                    MainActivity.coins -= 75;
                    item3 += 1;
                }
            }
        });

    }
}
