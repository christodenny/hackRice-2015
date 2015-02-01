package com.hack.rice.pursonal;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

public class NewPurchaseActivity extends Activity {

    private static DataSnapshot lastSnap;
    public static void setSnap(DataSnapshot snap) {
        lastSnap = snap;
    }

    private EditText etCost, etInfo;
    private Spinner sCategory;
    private String category, username;
    private DataSnapshot data;
    private Firebase firebase;
    
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.purchase);
        
        data = lastSnap;
        username = getIntent().getExtras().getString("username", "");
        category = getIntent().getExtras().getString("category", "");
        firebase = new Firebase("https://intense-heat-8336.firebaseio.com/").child("users").child(username).child("Purchases");
        
        setupGUI();
    }
    
    private int getIndex(String cat) {
        String[] cats = {"Food", "Clothes", "Entertainment", "Rent"};
        for(int a = 0; a < cats.length; a++)
            if(cats[a] == cat)
                return a;
        return -1;
    }
    
    private void setupGUI() {
        ((TextView)findViewById(R.id.tvCat)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        ((TextView)findViewById(R.id.tvInfo)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        ((TextView)findViewById(R.id.tvCost)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        ((TextView)findViewById(R.id.tvPurchaseTitle)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        (etCost = (EditText)findViewById(R.id.etCost)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        (etInfo = (EditText)findViewById(R.id.etInfo)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        
        sCategory = (Spinner) findViewById(R.id.sCat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategory.setAdapter(adapter);
        if(!category.equals(""))
            sCategory.setSelection(getIndex(category));
        
        findViewById(R.id.bPurchase).setOnClickListener(listener);
    }
    
    private final OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            postPurchase();
            onBackPressed();
            // TODO should also update the firebase snapshot!!
        }
        
    };
    
    private void postPurchase() {
        DataSnapshot purchases = data.child("Purchases");
        Log.wtf("children", ""+purchases.getChildrenCount());
        firebase.child(purchases.getChildrenCount()+"")
            .setValue(sCategory.getSelectedItem()+","+etInfo.getText()+","+etCost.getText());
    }
    
}
