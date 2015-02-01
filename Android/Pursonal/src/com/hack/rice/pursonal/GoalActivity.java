package com.hack.rice.pursonal;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class GoalActivity extends Activity{

    /* 
     * Need: 
     * spending limit/so-far
     * editing options
     * add a purchase
     * 
     */
    
    private String goalName;
    
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.goal);
        
        goalName = getIntent().getExtras().getString("name", "");
        
        setupGUI();
    }
    
    private void setupGUI() {
        TextView tvGoalName = (TextView) findViewById(R.id.tvGoalName);
        tvGoalName.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        tvGoalName.setText(goalName);
        
        
    }
    
}
