package com.hack.rice.pursonal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String[] options = { "Add a Payment" };

    private String username = "", password = "";

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        try {
            username = getIntent().getExtras().getString("username", "");
            password = getIntent().getExtras().getString("password", "");
        } catch (NullPointerException e) {
            Log.wtf("Error", e.toString());
        }

//        if (username == "" || password == "") {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//        }
        
        setupGUI();
    }

    private void setupGUI() {
        ((TextView)findViewById(R.id.tvGoals)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        loadGoals();
    }
    
    private static final String[] goals = {"Things", "Other Things"};
    
    private void loadGoals() {
        ViewGroup list = (ViewGroup) findViewById(R.id.llGoals);
        for(String s : goals) {
            double budget = 100, // get budget
                    spent = 30; // get spent
            
            GoalBlock block = new GoalBlock(this, s, budget, spent);
            LinearLayout.LayoutParams params 
                = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            block.setLayoutParams(params);
            block.setOnClickListener(getListener(s, budget, spent));
            list.addView(block);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        for (String s : options)
            menu.add(s);
        return super.onCreateOptionsMenu(menu);
    }

    private OnClickListener getListener(final String name, final double budget, final double spent){
        return new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoalActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("budget", budget);
                intent.putExtra("spent", spent);
                startActivity(intent);
            }
            
        };
    }
}