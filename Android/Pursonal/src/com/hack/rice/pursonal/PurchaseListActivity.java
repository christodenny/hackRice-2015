package com.hack.rice.pursonal;

import com.firebase.client.DataSnapshot;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PurchaseListActivity extends Activity{

    private static DataSnapshot lastSnap;
    public static void setSnap(DataSnapshot snap) {
        lastSnap = snap;
    }
    
//    private String category
    
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.purchase_list);
        
        setupGUI(lastSnap);
    }

    private void setupGUI(DataSnapshot data) {
        ((TextView)findViewById(R.id.tvPurchases))
            .setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        loadGoals(data);
    }
    
    private void loadGoals(DataSnapshot data) {
        ViewGroup list = (ViewGroup) findViewById(R.id.llPurchases);
        for(int a = list.getChildCount() - 1; a >= 0; a--)
            list.removeView(list.getChildAt(a));
        for(DataSnapshot child : data.child("Purchases").getChildren()) {
            String s = child.getKey();
            String val[] = (""+data.child("Purchases").child(s).getValue()).split(",");
            
            Purchase purchase = new Purchase(this, val[0], val[1], Double.parseDouble(val[2]));
            LinearLayout.LayoutParams params 
                = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            purchase.setLayoutParams(params);
            list.addView(purchase);
        }
    }
    
}
