package com.hack.rice.pursonal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.login);

        SharedPreferences prefs = LoginActivity.this.getSharedPreferences("com.hack.rice.pursonal", Context.MODE_PRIVATE);

        if(prefs.contains("username")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", prefs.getString("username", "").toLowerCase().trim());
            intent.putExtra("password", "");
            startActivity(intent);
        
        }
        
        findViewById(R.id.bLogin).setOnClickListener(listener);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    private OnClickListener listener = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            
            String username = ""+((TextView)findViewById(R.id.etUsername)).getText();
            SharedPreferences prefs = LoginActivity.this.getSharedPreferences("com.hack.rice.pursonal", Context.MODE_PRIVATE);
            prefs.edit().putString("username", username).commit();
            
            intent.putExtra("username", username.toLowerCase().trim());
            intent.putExtra("password", ""+((TextView)findViewById(R.id.etPassword)).getText());
            startActivity(intent);
        }
    };
    
}
