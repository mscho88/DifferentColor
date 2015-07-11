package com.example.jmentertainment.differentcolor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Random;


public class NormalActivity extends Activity {
    private int normalModeNum;
    private int screenShorterSideLength;
    private String curColorCode;
    private int ansId;
    private CountDownTimer timer;
    float time = 15;
    int pauseTime;
    private int score;
    DecimalFormat dformat = new DecimalFormat(".#");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_normal);

        normalModeNum = 2;
        this.getScreenSize();
        this.initializeStageMap();
        this.setStageColor();
        this.setStageButton();
        this.setAnswerButton();

        timer = new CountDownTimer((int)time * 1000, 100){
            public void onTick(long millisUntilFinished){
                time = time - 0.1f;
                ((TextView)findViewById(R.id.normalModeTimer)).setText(dformat.format(time));
            }
            public void onFinish(){
                ((TextView)findViewById(R.id.normalModeTimer)).setText("0.0");
                disableButton();
            }
        }.start();
    }

    /*
     * The following function measures the length of the screen and store the length of the short-
     * er side.
     *
     * PARAM  : NONE
     * RETURN : NONE
     */
    private void getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenShorterSideLength = size.x > size.y ? size.y : size.x;
    }

    /*
     * The following function removes all the view(s) in the layout that contains the buttons.
     *
     * PARAM  : NONE
     * RETURN : NONE
     */
    private void initializeStageMap(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.normalModeLayout);
        layout.removeAllViews();
    }

    /*
     * The following function randomly pick a color to paint the background of the button. The co-
     * lor code is calculated in hexadeciaml (html) format and store the string in a variable.
     *
     * PARAM  : NONE
     * RETURN : NONE
     */
    private void setStageColor(){
        String colorCode = "#";
        int bit;
        Random r = new Random();
        for(int i = 0; i < 6; i++){
            bit = r.nextInt(16);
            if(bit >= 0 && bit <= 9){
                colorCode = colorCode + bit;
            }else{
                colorCode = colorCode + Character.toString((char)(bit + 55));
            }
        }
        curColorCode = colorCode;
    }

    private void setStageButton(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.normalModeLayout);
        for(int i = 0; i < normalModeNum; i++){
            LinearLayout layoutRow = new LinearLayout(this);
            //LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 4.0f);
            layoutRow.setOrientation(LinearLayout.HORIZONTAL);
            layoutRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for(int j = 0; j < normalModeNum; j++) {
                LinearLayout layoutBtn = new LinearLayout(this);
                layoutBtn.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams btnParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                btnParam.setMargins(5, 5, 5, 5);
                layoutBtn.setLayoutParams(btnParam);

                Button btn = new Button(this);
                btn.setBackgroundColor(Color.parseColor(curColorCode));
                btn.setWidth((screenShorterSideLength - 100) / normalModeNum);
                btn.setHeight((screenShorterSideLength - 100) / normalModeNum);
                btn.setId(Integer.parseInt((i * 10 + j) + ""));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((Button) v).getId() == ansId) {
                            addScore();
                            initializeStageMap();
                            setStageColor();
                            setStageButton();
                            setAnswerButton();
                            if(normalModeNum < 5) {
                                normalModeNum++;
                            }
                            timer.cancel();
                            time = 15;
                            timer.start();
                        } else {
                            timer.cancel();
                            time = time - 2;
                            timer = new CountDownTimer((int)(time * 1000), 100){
                                public void onTick(long millisUntilFinished){
                                    time = time - 0.1f;
                                    ((TextView)findViewById(R.id.normalModeTimer)).setText(dformat.format(time));
                                }
                                public void onFinish(){
                                    ((TextView)findViewById(R.id.normalModeTimer)).setText("0.0");
                                    disableButton();
                                }
                            }.start();

                        }
                    }
                });
                layoutBtn.addView(btn);
                layoutRow.addView(layoutBtn);
            }
            layout.addView(layoutRow);
        }
    }

    /*
     * The following function stores the cumulative score for one game and shows the score earned
     * on the screen.
     *
     * PARAM  : NONE
     * RETURN : NONE
     */
    private void addScore(){
        // Store the score cumulatively
        score += normalModeNum * normalModeNum;

        // Prints the score on the screen.
        TextView txt = (TextView) findViewById(R.id.normalModeScore);
        if(score >= 100000){
            txt.setText("Score : " + score);
        }else if(score >= 10000){
            txt.setText("Score : 0" + score);
        }else if(score >= 1000){
            txt.setText("Score : 00" + score);
        }else if(score >= 100){
            txt.setText("Score : 000" + score);
        }else if(score >= 10){
            txt.setText("Score : 0000" + score);
        }else{
            txt.setText("Score : 00000" + score);
        }
    }

    /*
     * The following function randomly selects two indices in the layout and changes the backgrou-
     * nd color of the selected button. Implementing the background color of the button depends on
     * the stage number. When the stage number increases, the background color is implemented less
     * times.
     *
     * PARAM  : NONE
     * RETURN : NONE
     */
    private void setAnswerButton(){
        Random r = new Random();
        int row = r.nextInt(normalModeNum);
        int col = r.nextInt(normalModeNum);
        Button btn = (Button)findViewById(Integer.parseInt((row * 10 + col)+ ""));

        String impColor = curColorCode.substring(1);

        for(int i = 0; i < 3; i++){
            int index = r.nextInt(6);
            if (impColor.charAt(index) == '0'){
                impColor = impColor.substring(0, index) + '1' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '1'){
                impColor = impColor.substring(0, index) + '2' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '2'){
                impColor = impColor.substring(0, index) + '3' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '3'){
                impColor = impColor.substring(0, index) + '4' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '4'){
                impColor = impColor.substring(0, index) + '5' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '5'){
                impColor = impColor.substring(0, index) + '6' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '6'){
                impColor = impColor.substring(0, index) + '7' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '7'){
                impColor = impColor.substring(0, index) + '8' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '8'){
                impColor = impColor.substring(0, index) + '9' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == '9'){
                impColor = impColor.substring(0, index) + 'A' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == 'A'){
                impColor = impColor.substring(0, index) + 'B' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == 'B'){
                impColor = impColor.substring(0, index) + 'C' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == 'C'){
                impColor = impColor.substring(0, index) + 'D' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == 'D'){
                impColor = impColor.substring(0, index) + 'E' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == 'E'){
                impColor = impColor.substring(0, index) + 'F' + impColor.substring(index + 1);
            }else if (impColor.charAt(index) == 'F'){
                impColor = impColor.substring(0, index) + '0' + impColor.substring(index + 1);
            }
          }

        btn.setBackgroundColor(Color.parseColor("#" + impColor));
        ansId = Integer.parseInt((row * 10 + col) + "");
    }

    /*
     * The following function disables all the buttons in the button layout. This function is bei-
     * ng called either when the game is paused or when the game is finished.
     *
     * PARAM  : NONE
     * RETURN : NONE
     */
    private void disableButton(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.normalModeLayout);
        for(int i = 0; i < normalModeNum; i++) {
            for (int j = 0; j < normalModeNum; j++) {
                Button btn = (Button)findViewById(Integer.parseInt((i * 10 + j) + ""));
                btn.setEnabled(false);
            }
        }
    }

    private void enableButton(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.normalModeLayout);
        for(int i = 0; i < normalModeNum; i++) {
            for (int j = 0; j < normalModeNum; j++) {
                Button btn = (Button)findViewById(Integer.parseInt((i * 10 + j) + ""));
                btn.setEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.disableButton();
        timer.cancel();

        //ImageView im = (ImageView) findViewById(R.id.normalModePauseView);
        //im.setVisibility(View.VISIBLE);
        LinearLayout layout = (LinearLayout)findViewById(R.id.normalModeLayoutBg);
        final int bg = layout.getDrawingCacheBackgroundColor();
        layout.setBackgroundColor(Color.parseColor("#59000000"));

        View popupView = getLayoutInflater().inflate(R.layout.pause_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView);

        popupWindow.setWidth((int)(screenShorterSideLength));
        popupWindow.setHeight((int)(screenShorterSideLength * 1.3));
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        Button exitBtn = (Button)popupView.findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                popupWindow.dismiss();
                startActivity(new Intent(NormalActivity.this, PregameActivity.class));
                finish();
            }
        });

        Button resumeBtn = (Button)popupView.findViewById(R.id.resumeBtn);
        resumeBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableButton();
                pauseTime = (int)((time - 2) * 1000);
                timer = new CountDownTimer((int)(time * 1000), 100){
                    public void onTick(long millisUntilFinished){
                        time = time - 0.1f;
                        ((TextView)findViewById(R.id.normalModeTimer)).setText(dformat.format(time));
                    }
                    public void onFinish(){
                        ((TextView)findViewById(R.id.normalModeTimer)).setText("0.0");
                        disableButton();
                    }
                }.start();
                popupWindow.dismiss();
                LinearLayout layout = (LinearLayout)findViewById(R.id.normalModeLayoutBg);
                layout.setBackgroundColor(bg);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
