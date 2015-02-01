package com.hack.rice.pursonal;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;

public class GoalActivity extends Activity{

    /* 
     * Need: 
     * spending limit/so-far
     * editing options
     * add a purchase
     * 
     */
    
    private static final String[] options = { "Add a Payment", "View Payments" };

    private static DataSnapshot lastSnap;
    public static void setSnap(DataSnapshot snap) {
        lastSnap = snap;
    }
    
    private DataSnapshot data;
    private String goalName, username;
    
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.goal);
        
        Bundle extras = getIntent().getExtras();
        
        goalName = extras.getString("name", "");
        username = extras.getString("username", "");
        data = lastSnap;
        
        setupGUI(extras.getDouble("budget"));
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
        
        Log.wtf("id", ""+item.getItemId());
        
        int index = item.getTitle().equals("Add a Payment") ? 0 : 1;
        Intent intent;
        
        switch(index){
        case 0:
            intent = new Intent(this, NewPurchaseActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("category", goalName);
            NewPurchaseActivity.setSnap(data);
            startActivity(intent);
            return true;
            
        case 1:
            intent = new Intent(this, PurchaseListActivity.class);
            PurchaseListActivity.setSnap(data);
            startActivity(intent);
            return true;
        }
        
        return true;
    };
    
    private void setupGUI(double budget) {
        TextView tvGoalName = (TextView) findViewById(R.id.tvGoalName);
        tvGoalName.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        tvGoalName.setText(goalName);
        
        ((TextView)findViewById(R.id.tvBudget)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        ((TextView)findViewById(R.id.tvSpent)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        ((TextView)findViewById(R.id.tvChartTitle)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        ((TextView)findViewById(R.id.tvUpdateBudget)).setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        
        TextView userBudget = (TextView)findViewById(R.id.tvUserBudget), 
                userSpent = (TextView)findViewById(R.id.tvUserSpent);
        userBudget.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        userSpent.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));
        userBudget.setText(String.format("$%.2f", budget));
        userSpent.setText(String.format("$%.2f", computeSpent()));
        
        findViewById(R.id.rlUpdateBudget).setOnClickListener(listener);
        
        setupChart();
    }
    
    private double computeSpent() {
        double total = 0;
        for(DataSnapshot s : data.child("Purchases").getChildren())
        {
            String[] args = s.getValue().toString().split(",");
            if(goalName.equals(args[0]))
                total += Double.parseDouble(args[args.length-1]);
        }
        return total;
    }
    
    private static int[] COLORS = new int[] { 0xff99cc00, 0xff33b5e5, 0xffffbb33, 0xffcc0000 };
    private static double[] VALUES;
    private static String[] NAME_LIST = new String[] { "$0-$5", "$5-$20", "$20-$50", "$50+" };
    private CategorySeries series = new CategorySeries("");
    private DefaultRenderer renderer = new DefaultRenderer();
    private GraphicalView chart;

    private void setupChart() {
        VALUES = new double[4];
        for(DataSnapshot ds : data.child("Purchases").getChildren()) {
            String args[] = ds.getValue().toString().split(",");
            if(!args[0].equals(goalName))
                continue;
            double price = Double.parseDouble(args[args.length - 1]);
            if(price <= 5)
                VALUES[0]++;
            else if(price <= 20)
                VALUES[1]++;
            else if(price <= 50)
                VALUES[2]++;
            else
                VALUES[3]++;
        }
        
        renderer.setApplyBackgroundColor(true);
        renderer.setLabelsTextSize(50);
        renderer.setShowLegend(false);
        renderer.setScale(0.85f);
        renderer.setLabelsColor(0xff000000);
        renderer.setZoomEnabled(false);
        renderer.setPanEnabled(false);
        renderer.setTextTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf"));

        for (int i = 0; i < VALUES.length; i++) {
            if(VALUES[i] == 0)
                continue;
            series.add(NAME_LIST[i], VALUES[i]);
            SimpleSeriesRenderer ssr = new SimpleSeriesRenderer();
            ssr.setColor(COLORS[i]);
            renderer.addSeriesRenderer(ssr);
        }

        if (chart != null) {
            chart.repaint();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (chart == null) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlMain);
            chart = ChartFactory.getPieChartView(this, series, renderer);
            RelativeLayout.LayoutParams params = 
                    new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.tvChartTitle);
            params.addRule(RelativeLayout.ABOVE, R.id.rlUpdateBudget);
            layout.addView(chart, params);
        }
        else {
        chart.repaint();
        }
    }
    
    private final OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GoalActivity.this, UpdateBudgetActivity.class);
            intent.putExtra("category", goalName);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    };
    
}
