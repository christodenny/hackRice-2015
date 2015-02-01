package com.hack.rice.pursonal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

public class UpdateBudgetActivity extends Activity {

    public static DataSnapshot snapshot;
    
    private String goalName, username;
    private Firebase firebase;
    private double propBudget, orig;
    
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.update_budget);
        
        firebase = new Firebase("https://intense-heat-8336.firebaseio.com/");
        
        goalName = getIntent().getExtras().getString("category", "");
        username = getIntent().getExtras().getString("username", "");
        orig = Double.parseDouble(""+snapshot.child(username).child("Budget").child(goalName).getValue());
        propBudget = getPropBudget();
        
        setupGUI();
    }
    
    private void setupGUI() {
        double budgetDiff = orig - propBudget;
        
        TextView report = (TextView) findViewById(R.id.tvBudgetChange), 
                change = (TextView)findViewById(R.id.tvChangeTo),
                keep = (TextView)findViewById(R.id.tvKeep),
                orig = (TextView)findViewById(R.id.tvOriginal);

        if(Math.abs(budgetDiff) < 50){
            report.setText(getResources().getString(R.string.same_budget));
            findViewById(R.id.rlChangeTo).setVisibility(View.INVISIBLE);
        }
        else if(budgetDiff > 0)
            report.setText(getResources().getString(R.string.lower_budget));
        else
            report.setText(getResources().getString(R.string.higher_budget));
        
        report.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        change.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        keep.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        orig.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        
        change.setText(String.format("Change to $%.2f", propBudget));
        orig.setText(String.format("Original: $%.2f", 
                Double.parseDouble(""+snapshot.child(username).child("Budget").child(goalName).getValue())));
        
        ((RelativeLayout)findViewById(R.id.rlChangeTo)).setOnClickListener(listener);
        ((RelativeLayout)findViewById(R.id.rlKeep)).setOnClickListener(listener);
    }
    
    private final OnClickListener listener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
            case R.id.rlChangeTo:
                firebase.child("users").child(username).child("Budget").child(goalName).setValue(propBudget);
            case R.id.rlKeep:
                onBackPressed();
                break;
            }
        }
    };
    
    private static final HashSet<String> demos = 
            new HashSet<String>(Arrays.asList(new String[]{"age", "gender", "location", "student"}));
    
    private double getPropBudget() {
        HashMap<String, String> vals = new HashMap<String, String>();
        DataSnapshot user = snapshot.child(username);
        
        for(DataSnapshot shot : user.getChildren()) {
            if(demos.contains(shot.getKey()))
                vals.put(shot.getKey(), ""+shot.getValue());
        }
        
        long totalCount = 0;
        double totalBudget = 0;
        
        for(DataSnapshot u : snapshot.getChildren()) {
            for(String dem : demos)
                if(u.hasChild(dem) && u.child(dem).getValue().equals(vals.get(dem))
                        && u.child("Budget").hasChild(goalName))
                {
                    totalCount++;
                    totalBudget += Double.parseDouble(""+u.child("Budget").child(goalName).getValue());
                }
        }
        
        return totalCount == 0 ? orig : totalBudget / totalCount;
    }
    
}
