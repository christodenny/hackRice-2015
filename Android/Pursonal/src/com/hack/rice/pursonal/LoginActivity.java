package com.hack.rice.pursonal;

import android.app.Activity;
import android.content.Intent;
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
            postLoginData();
            
            // go to main if we have demographic data
            // go to data input otherwise
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", ""+((TextView)findViewById(R.id.etUsername)).getText());
            intent.putExtra("password", ""+((TextView)findViewById(R.id.etPassword)).getText());
            startActivity(intent);
        }
    };
    
    private void postLoginData() {
        
    }
    
}
