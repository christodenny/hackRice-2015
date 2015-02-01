package com.hack.rice.pursonal;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Purchase extends RelativeLayout {
    
    private Context context;
    private String category, info;
    private double cost;
    
    public Purchase(Context context, String category, String info, double cost){
        super(context);
        this.context = context;
        this.category = category;
        this.info = info;
        this.cost = cost;
        
        setupGUI();
    }
    
    public Purchase(Context context) {
        this(context, "Food", "Tacos!", 6.00);
    }
    
    private void setupGUI() {
        setPadding(10, 10, 10, 10);
        setBackgroundColor(getResources().getColor(R.color.grey));
    
        TextView tvCat = new TextView(context), 
                tvInfo = new TextView(context), 
                tvCost = new TextView(context);
        
        tvCat.setText(category);
        tvCat.setTextSize(25);
        tvCat.setId(R.id.tvCat);
        tvCat.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf"));
        
        tvInfo.setText(info);
        tvInfo.setTextSize(15);
        tvInfo.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf"));

        tvCost.setText(String.format("$%.2f", cost));
        tvCost.setTextSize(25);
        tvCost.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf"));
        
        RelativeLayout.LayoutParams params[] = {
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT), 
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT), 
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT), 
        };
        
        params[0].addRule(ALIGN_PARENT_LEFT, TRUE);
        tvCat.setLayoutParams(params[0]);
        
        params[1].addRule(ALIGN_PARENT_LEFT, TRUE);
        params[1].addRule(BELOW, R.id.tvCat);
        tvInfo.setLayoutParams(params[1]);
        
        params[2].addRule(ALIGN_PARENT_RIGHT, TRUE);
        tvCost.setLayoutParams(params[2]);
        
        addView(tvCat);
        addView(tvCost);
        addView(tvInfo);
    }
    
}
