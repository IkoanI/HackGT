package com.example.hackgt.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hackgt.R;
import com.example.hackgt.View.helloar.HelloArActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.fitness.FitnessLocal;
import com.google.android.gms.fitness.LocalRecordingClient;
import com.google.android.gms.fitness.data.LocalBucket;
import com.google.android.gms.fitness.data.LocalDataPoint;
import com.google.android.gms.fitness.data.LocalDataSet;
import com.google.android.gms.fitness.data.LocalDataType;
import com.google.android.gms.fitness.data.LocalField;
import com.google.android.gms.fitness.request.LocalDataReadRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final int ACTIVITY_REQUEST = 1;
    public static int steps = 0;
    String stepGoal;
    TextView stepsTaken;
    ProgressBar progressBar;
    LocalRecordingClient fitnessClient;
    LocalDataReadRequest readRequest;
    TextView amountOfCoins;
    int chestThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.noar_gameplay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String username = intent.getStringExtra("Username");
        stepGoal = intent.getStringExtra("Goal");

        chestThreshold = (int) (Math.random() * 499 + 1);

        TextView usernameTextView = findViewById(R.id.username_pb);
        stepsTaken = findViewById(R.id.stepsTaken);
        progressBar = findViewById(R.id.progressBar);

        amountOfCoins = findViewById(R.id.amountCoins);

        usernameTextView.setText(username);
        checkGooglePlayVersion();

        findViewById(R.id.armode_button).setOnClickListener(v -> {
            Intent arIntent = new Intent(MainActivity.this, HelloArActivity.class);
            startActivity(arIntent);
        });

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    getFitnessData();
                }
                readStepsData();
                handler.postDelayed(this,1000);
            }
        },1000);

        Button shopMove = findViewById(R.id.shop_button);
        shopMove.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, ShopActivity.class);
            startActivity(intent1);
        });

        Button chestMove = findViewById(R.id.chestscreen_button);
        chestMove.setOnClickListener(v -> {
            Intent intent12 = new Intent(MainActivity.this, ChestActivity.class);
            startActivity(intent12);
        });

        Button animalMove = findViewById(R.id.animalscreen_button);
        animalMove.setOnClickListener(v -> {
            Intent intent13 = new Intent(MainActivity.this, AnimalActivity.class);
            startActivity(intent13);
        });

    }



    public void checkGooglePlayVersion(){
        int hasMinPlayServices = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (hasMinPlayServices != ConnectionResult.SUCCESS) {
            // Prompt user to update their device's Google Play services app and return
            Toast.makeText(this, "Update device Google Play services", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void getFitnessData(){
        fitnessClient = FitnessLocal.getLocalRecordingClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = {Manifest.permission.ACTIVITY_RECOGNITION};
            ActivityCompat.requestPermissions(this, permissions, ACTIVITY_REQUEST);
        }

        fitnessClient.subscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
                .addOnFailureListener(r -> Log.e("Fitness Subscription", r.getMessage(), r.getCause()));

        ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        ZonedDateTime startTime = endTime.minusWeeks(1);
        readRequest = new LocalDataReadRequest.Builder()
                .aggregate(LocalDataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build();

    }

    public void readStepsData(){
        fitnessClient.readData(readRequest)
                .addOnSuccessListener(r -> {
                    for(LocalBucket bucket : r.getBuckets()){
                        for(LocalDataSet dataSet : bucket.getDataSets()){
                            dumpDataSet(dataSet);
                        }
                    }
                })
                .addOnFailureListener(r -> Log.e("Fitness Client", r.getMessage(), r.getCause()));
    }

    public void dumpDataSet(LocalDataSet dataSet){

        for (LocalDataPoint dp : dataSet.getDataPoints()) {
            for (LocalField field : dp.getDataType().getFields()) {
                steps = dp.getValue(field).asInt();

                if(Player.baseSteps == 0){
                    Player.baseSteps = steps;
                }

                steps -= Player.baseSteps;
                stepsTaken.setText(String.format(Locale.ENGLISH, "Steps taken: %d / %s", steps, stepGoal));

                progressBar.setProgress(steps * 100 / Integer.parseInt(stepGoal));

                Log.d("TEST", String.valueOf(chestThreshold));
                if (steps >= chestThreshold && Player.chestInventory.size() < 5) {
                    Chests newChest = new Chests(steps);
                    Player.chestInventory.add(newChest);
                    chestThreshold += (int) (Math.random() * 499 + 1);
                    Toast.makeText(this, "You found a chest!", Toast.LENGTH_SHORT).show();
                }

                if (!Player.chestInventory.isEmpty() && steps >= Player.chestInventory.get(0).getEndCount()) {
                    Toast.makeText(this, "Chest opened!", Toast.LENGTH_SHORT).show();
                    Player.spent -= Player.chestInventory.get(0).getReward();
                    Player.chestInventory.removeFirst();
                }

                Player.coins = Math.floorDiv(steps, 1) - Player.spent;
                amountOfCoins.setText(String.format(Locale.ENGLISH, "Coins: %d", Player.coins));
            }
        }
    }


}