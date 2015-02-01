package com.hack.rice.pursonal;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GoalBlock extends RelativeLayout {

    private Context context;
    private String name;
    private double budget, spent;
    private View spentBlock, leftBlock;
    
    public GoalBlock(Context context, String name, double budget, double spent) {
        super(context);
        this.context = context;
        this.name = name;
        this.budget = budget;
        this.spent = spent;
        
        setupGUI();
    }
    
    public GoalBlock(Context context) {
        this(context, "stuff", 100, 50);
    }

    private void setupGUI() {
        setPadding(10, 10, 10, 10);
        setBackgroundColor(getResources().getColor(R.color.grey));
        
        TextView text = new TextView(context), 
                percent = new TextView(context);
        text.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf"));
        text.setTextSize(30);
        text.setText(name);
        percent.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf"));
        percent.setTextSize(30);
        percent.setText((int)(100 * spent / budget)+"%");
        
        spentBlock = new View(context);
        spentBlock.setBackgroundColor(getResources().getColor(R.color.red));
        
        leftBlock = new View(context);
        leftBlock.setBackgroundColor(getResources().getColor(R.color.green));
        
        RelativeLayout.LayoutParams params[] = { 
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT),
                new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT),
                new RelativeLayout.LayoutParams(0, 0),
                new RelativeLayout.LayoutParams(0, 0),
        };

        params[0].addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params[0].addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        text.setLayoutParams(params[0]);

        params[1].addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params[1].addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        percent.setLayoutParams(params[1]);
        
        params[2].addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        spentBlock.setLayoutParams(params[2]);
        
        params[3].addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftBlock.setLayoutParams(params[3]);
        
        // text is on top
        addView(spentBlock);
        addView(leftBlock);
        addView(text);
        addView(percent);
        
        startUpdate();
    }
    
    private void startUpdate() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                while(getHeight() == 0)
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                updateBlock();
                return null;
            }

            
        };
        
        task.execute();
    }
    
    public void updateBlock() {
        spentBlock.getLayoutParams().width = (int) (getWidth() * spent / budget);
        spentBlock.getLayoutParams().height = getHeight();
        
        if(spent < budget) {
            leftBlock.getLayoutParams().width = getWidth() - spentBlock.getLayoutParams().width;
            leftBlock.getLayoutParams().height = getHeight();
        } else {
            leftBlock.getLayoutParams().width = 0;
            leftBlock.getLayoutParams().height = 0;
        }
        
    }
    
}
