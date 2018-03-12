package com.example.shruthinarayan.lab1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;
import java.lang.String;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    //Button
    private Button addButton;
    private Button subtractButton;
    private Button multiplyButton;
    private Button divideButton;
    private Button leftParenthesisButton;
    private Button rightParenthesisButton;
    private Button equalButton;
    private Button deleteButton;
    private Button oneButton;
    private Button twoButton;
    private Button threeButton;
    private Button fourButton;

    //Number Generation
    private int[] randArray = new int[4];
    private String buttonOneStr = "";
    private String buttonTwoStr = "";
    private String buttonThreeStr = "";
    private String buttonFourStr = "";

    private EditText displayText;
    private String totalOutput ="";

    //Counters
    private int successCounter = 0, attemptCounter = 1, skipCounter = 0, timeCounter = 0;
    private TextView attemptTextView;
    private TextView successTextview;
    private TextView skippedTextview;
    private TextView timerTextview;

    static Dialog d ;

    private android.support.v4.widget.DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initAlertEvent();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        android.support.design.widget.NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new android.support.design.widget.NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.sol:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                String answer = MakeNumber.getSolution(Integer.parseInt(buttonOneStr),Integer.parseInt(buttonTwoStr), Integer.parseInt(buttonThreeStr), Integer.parseInt(buttonFourStr));
                                Log.d("TAG", "ans" + answer+" =24");
                                if(answer != null) {
                                    builder1.setMessage("Solution: " + answer +" =24");
                                }
                                else {
                                    builder1.setMessage("Sorry, there are actually no solutions");
                                    resetNumber();
                                    skippedTextview.setText(String.valueOf(++skipCounter));
                                    timerSet();
                                }
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog soln = builder1.create();
                                soln.show();
                               // menuItem.setChecked(true);
                                return true;
                            case R.id.assignNumber:
                                dialogShow();
                                equalButton.setEnabled(false);
                               // menuItem.setChecked(true);
                                return true;
                            default:
                                mDrawerLayout.closeDrawers();
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(android.support.v4.view.GravityCompat.START);
                return true;

            case R.id.clr: {
                totalOutput ="";
                displayText.setText(totalOutput);
                oneButton.setEnabled(true);
                twoButton.setEnabled(true);
                threeButton.setEnabled(true);
                fourButton.setEnabled(true);
                return true;
            }

            case R.id.ff:
                resetNumber();
                totalOutput ="";
                displayText.setText(totalOutput);
                skippedTextview.setText(String.valueOf(++skipCounter));
                timerSet();
                return true;

            case R.id.mv:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.action, menu );
        return true;
    }


    private void initLayout() {
        oneButton = findViewById(R.id.num_one);
        twoButton = findViewById(R.id.num_two);
        threeButton = findViewById(R.id.num_three);
        fourButton = findViewById(R.id.num_four);

        addButton = findViewById(R.id.add);
        subtractButton = findViewById(R.id.subtract);
        multiplyButton = findViewById(R.id.multiply);
        divideButton = findViewById(R.id.divide);
        leftParenthesisButton = findViewById(R.id.left_parenthesis);
        rightParenthesisButton = findViewById(R.id.right_parenthesis);
        deleteButton = findViewById(R.id.delete);
        equalButton = findViewById(R.id.equal);

        attemptTextView = findViewById(R.id.attempt_num);
        attemptTextView.setText(String.valueOf(attemptCounter));

        successTextview = findViewById(R.id.succeed_num);
        successTextview.setText(String.valueOf(successCounter));

        skippedTextview = findViewById(R.id.skipped_num);
        skippedTextview.setText(String.valueOf(skipCounter));

        timerTextview = findViewById(R.id.time_num);

        Thread timerThread=new Thread() {
            @Override
            public void run() {
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeCounter++;
                                String time_count = String.format(Locale.US,"%02d:%02d", timeCounter / 60, timeCounter % 60);
                                timerTextview.setText(time_count);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timerThread.start();
        displayText =(EditText) findViewById(R.id.result_text);
        displayText.setCursorVisible(false);
    }

    private void initAlertEvent() {
        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);
        threeButton.setOnClickListener(this);
        fourButton.setOnClickListener(this);

        addButton.setOnClickListener(this);
        subtractButton.setOnClickListener(this);
        multiplyButton.setOnClickListener(this);
        divideButton.setOnClickListener(this);
        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCounter++;
                attemptTextView.setText(String.valueOf(attemptCounter));
                equalButton.setEnabled(false);
                ExpressionParsing obj = new ExpressionParsing();
                if(obj.check(totalOutput)){
                    AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);

                    builder.setMessage("Binggo! "+ totalOutput +"=24")
                            .setPositiveButton("Next Puzzle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    totalOutput ="";
                                    displayText.setText(totalOutput);
                                    resetNumber();
                                    successCounter++;
                                    successTextview.setText(String.valueOf(successCounter));
                                    attemptCounter =1;
                                    attemptTextView.setText(String.valueOf(attemptCounter));
                                    timerSet();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    android.support.design.widget.Snackbar.make(view, "Incorrect. Please try again!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            }
        });
        deleteButton.setOnClickListener(this);
        leftParenthesisButton.setOnClickListener(this);
        rightParenthesisButton.setOnClickListener(this);

        //about.setOnClickListener(this);
        displayText.setOnClickListener(this);
        attemptTextView.setOnClickListener(this);

        //generate 4 numbers at start
        resetNumber();
    }

    private void resetNumber() {
        int maxValue = 9, minValue = 1;
        randomGenerator(minValue, maxValue);
        buttonOneStr = Integer.toString(randArray[0]);
        buttonTwoStr = Integer.toString(randArray[1]);
        buttonThreeStr = Integer.toString(randArray[2]);
        buttonFourStr = Integer.toString(randArray[3]);

        oneButton.setText(buttonOneStr);
        twoButton.setText(buttonTwoStr);
        threeButton.setText(buttonThreeStr);
        fourButton.setText(buttonFourStr);

        oneButton.setEnabled(true);
        twoButton.setEnabled(true);
        threeButton.setEnabled(true);
        fourButton.setEnabled(true);
    }

    private void randomGenerator(int min, int max){
        int i = 0;
        while(i<4){
            int temp = (int)(Math.random() * (max - min + 1)) + min;
            if (temp != randArray[0] && temp != randArray[1] && temp != randArray[2] && temp != randArray[3]){
                randArray[i] = temp;
                i++;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.num_one:
                totalOutput =AddSum(String.valueOf(buttonOneStr).charAt(0));
                displayText.setText(totalOutput);
                break;
            case R.id.num_two:
                totalOutput =AddSum(String.valueOf(buttonTwoStr).charAt(0));
                displayText.setText(totalOutput);
                break;
            case R.id.num_three:
                totalOutput =AddSum(String.valueOf(buttonThreeStr).charAt(0));
                displayText.setText(totalOutput);
                break;
            case R.id.num_four:
                totalOutput =AddSum(String.valueOf(buttonFourStr).charAt(0));
                displayText.setText(totalOutput);
                break;

            case R.id.add:
                totalOutput =AddSum('+');
                displayText.setText(totalOutput);
                break;
            case R.id.subtract:
                totalOutput =AddSum('-');
                displayText.setText(totalOutput);
                break;
            case R.id.multiply:
                totalOutput =AddSum('*');
                displayText.setText(totalOutput);
                break;
            case R.id.divide:
                totalOutput =AddSum('/');
                displayText.setText(totalOutput);
                break;
            case R.id.left_parenthesis:
                totalOutput =AddSum('(');
                displayText.setText(totalOutput);
                break;
            case R.id.right_parenthesis:
                totalOutput =AddSum(')');
                displayText.setText(totalOutput);
                break;
            case R.id.delete:
                if(totalOutput.length()>=1)
                {
                    char c = totalOutput.charAt(totalOutput.length()-1);
                    if (Character.isDigit(c)) {
                        if (oneButton.getText().charAt(0) == c){
                            oneButton.setEnabled(true);
                        } else if (twoButton.getText().charAt(0) == c) {
                            twoButton.setEnabled(true);
                        } else if (threeButton.getText().charAt(0) == c) {
                            threeButton.setEnabled(true);
                        } else if (fourButton.getText().charAt(0) == c) {
                            fourButton.setEnabled(true);
                        }
                    }
                    totalOutput = totalOutput.substring(0, totalOutput.length()-1);
                }
                displayText.setText(totalOutput);
            default:
                break;
        }
    }

    public String AddSum(char c)
    {
        if (Character.isDigit(c)){
            if (oneButton.getText().charAt(0) == c){
                oneButton.setEnabled(false);
            } else if (twoButton.getText().charAt(0) == c) {
                twoButton.setEnabled(false);
            } else if (threeButton.getText().charAt(0) == c) {
                threeButton.setEnabled(false);
            } else if (fourButton.getText().charAt(0) == c) {
                fourButton.setEnabled(false);
            }
        }
        totalOutput = totalOutput +String.valueOf(c);
        equalButton.setEnabled(true);
        return totalOutput;
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Log.i("value is",""+i1);

    }
    public void dialogShow()
    {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np1 = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
        final NumberPicker np3 = (NumberPicker) d.findViewById(R.id.numberPicker3);
        final NumberPicker np4 = (NumberPicker) d.findViewById(R.id.numberPicker4);

        np1.setMaxValue(9);
        np1.setMinValue(1);
        np1.setWrapSelectorWheel(true);
        np1.setOnValueChangedListener(this);

        np2.setMaxValue(9);
        np2.setMinValue(1);
        np2.setWrapSelectorWheel(true);
        np2.setOnValueChangedListener(this);

        np3.setMaxValue(9);
        np3.setMinValue(1);
        np3.setWrapSelectorWheel(true);
        np3.setOnValueChangedListener(this);

        np4.setMaxValue(9);
        np4.setMinValue(1);
        np4.setWrapSelectorWheel(true);
        np4.setOnValueChangedListener(this);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                buttonOneStr = Integer.toString(np1.getValue());
                buttonTwoStr = Integer.toString(np2.getValue());
                buttonThreeStr = Integer.toString(np3.getValue());
                buttonFourStr = Integer.toString(np4.getValue());

                oneButton.setText(buttonOneStr);
                twoButton.setText(buttonTwoStr);
                threeButton.setText(buttonThreeStr);
                fourButton.setText(buttonFourStr);

                totalOutput ="";
                displayText.setText(totalOutput);
                oneButton.setEnabled(true);
                twoButton.setEnabled(true);
                threeButton.setEnabled(true);
                fourButton.setEnabled(true);

                timerSet();

                attemptCounter =1;
                attemptTextView.setText(String.valueOf(attemptCounter));
                skippedTextview.setText(String.valueOf(++skipCounter));

                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void timerSet(){
        timeCounter =0;
        String time_count = String.format(Locale.US,"%02d:%02d", timeCounter / 60, timeCounter % 60);
        timerTextview.setText(time_count);
    }
}
