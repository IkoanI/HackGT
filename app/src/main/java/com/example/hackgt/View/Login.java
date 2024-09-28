package com.example.hackgt.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hackgt.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText usernameInput = findViewById(R.id.username_input);
        EditText stepGoalInput = findViewById(R.id.step_goal_input);
        Button submit = findViewById(R.id.login_button);

        stepGoalInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String stepGoal = stepGoalInput.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(Login.this, "Enter a username!", Toast.LENGTH_SHORT).show();
                }
                else if(stepGoal.isEmpty()){
                    Toast.makeText(Login.this, "Enter a step goal!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("Username", username);
                    intent.putExtra("Goal", stepGoal);
                    startActivity(intent);
                }
            }
        });

    }
}