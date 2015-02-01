package com.hack.rice.pursonal;

import org.shaded.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends Activity {

    /*
     There are plenty of budget apps that keep the user on track to a health financial plan, but all of them start with a huge mistake. If you've ever used Mint.com, you know how uncertain you can feel as you punch in your guess at a budget in every category. Pursonal takes this doubt away. Using data on average spending over multiple demographics, Pursonal is able to give an accurate budget suggestion for every spending category. Of course, users are able to adjust this budget as they see fit, but they're not left in the dark about how much they should spend. After inputting some basic data and your preferences, you can be confident that you're working towards the right goals.
     */
    
    private static final String[] options = { "Add a Payment", "View Payments", "Log out" };
    
    private String username = "", password = "";
    private Firebase firebase;
    private DataSnapshot data;
    
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
        
        username = new String(Base64.encodeBase64(username.getBytes()));

        setupGUI();
        
        
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://intense-heat-8336.firebaseio.com/");
        firebase = firebase.child("users");
        
        firebase.addValueEventListener(new ValueEventListener() {
            
            @Override
            public void onDataChange(DataSnapshot data) {
                firebase = firebase.child(username);
                UpdateBudgetActivity.snapshot = MainActivity.this.data = data;
                loadGoals(data.child(username));
            }
            
            @Override
            public void onCancelled(FirebaseError error) {
                Log.wtf("Error", error+"");
            }
        });
        
        
    }

    @Override
    public void onResume() {
        super.onResume();
//        try{
//            loadGoals(data);
//        }catch(Exception e){}
    }
    
    private double computeSpent(String name, DataSnapshot data) {
        double total = 0;
        for(DataSnapshot s : data.child("Purchases").getChildren())
        {
            String[] args = s.getValue().toString().split(",");
            if(name.equals(args[0]))
                total += Double.parseDouble(args[args.length-1]);
        }
        return total;
    }

    private void setupGUI() {
        ((TextView)findViewById(R.id.tvGoals)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
    }
    
    private void loadGoals(DataSnapshot data) {
        Log.wtf("load called", "hi");
        ViewGroup list = (ViewGroup) findViewById(R.id.llGoals);
        for(int a = list.getChildCount() - 1; a >= 0; a--)
            list.removeView(list.getChildAt(a));
        for(DataSnapshot child : data.child("Budget").getChildren()) {
            String s = child.getKey();
            double budget = Double.parseDouble(""+data.child("Budget").child(s).getValue()), // get budget
                    spent = computeSpent(s, data); // get spent
            
            GoalBlock block = new GoalBlock(this, s, budget, spent);
            LinearLayout.LayoutParams params 
                = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            block.setLayoutParams(params);
            block.setOnClickListener(getListener(data, s, budget, spent));
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(data == null)
            return true;
        
        int index = 1;
        if(item.getTitle().equals("Add a Payment"))
            index = 0;
        else if(item.getTitle().equals("Log out"))
            index = 2;
        Intent intent;
        
        switch(index){
        case 0:
            intent = new Intent(this, NewPurchaseActivity.class);
            intent.putExtra("username", username);
            NewPurchaseActivity.setSnap(data.child(username));
            startActivity(intent);
            return true;
            
        case 1:
            intent = new Intent(this, PurchaseListActivity.class);
            PurchaseListActivity.setSnap(data.child(username));
            startActivity(intent);
            return true;
            
        case 2:
            getSharedPreferences("com.hack.rice.pursonal", Context.MODE_PRIVATE).edit().clear().commit();
            intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            return true;
        }
        
        return true;
    };
    
    private OnClickListener getListener(final DataSnapshot data, final String name, 
            final double budget, final double spent){
        return new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoalActivity.class);
                GoalActivity.setSnap(data);
                intent.putExtra("name", name);
                intent.putExtra("username", username);
                intent.putExtra("budget", budget);
                intent.putExtra("spent", spent);
                startActivity(intent);
            }
            
        };
    }
}