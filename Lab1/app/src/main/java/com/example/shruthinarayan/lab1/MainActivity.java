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
import java.util.Stack;
import java.lang.String;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener {
    //Button
    private Button one;
    private Button two;
    private Button three;
    private Button four;

    private Button add;
    private Button subtract;
    private Button multiply;
    private Button divide;
    private Button left_parenthesis;
    private Button right_parenthesis;
    private Button equal;
    private Button delete;

    //EditText
    private EditText showtext;
    private String OperateSum="";

    //Number Gen
    private int[] numberStore = new int[4];
    private int max = 9, min = 1;
    private String bt_one = "";
    private String bt_two = "";
    private String bt_three = "";
    private String bt_four = "";

    //Count
    private int succee_count = 0, attempt_count = 1, skip_count = 0, sec = 0;
    private TextView show_attempt;
    private TextView show_succee;
    private TextView show_skipped;
    private TextView show_timer;
    Thread timer;

    static Dialog d ;

    private android.support.v4.widget.DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

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
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.sol:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                MakeNumber obj = new MakeNumber();
                                String answer = obj.getSolution(Integer.parseInt(bt_one),Integer.parseInt(bt_two), Integer.parseInt(bt_three), Integer.parseInt(bt_four));
                                Log.d("TAG", "ans" + answer+" =24");
                                if(answer != null) {
                                    builder1.setMessage("Solution: " + answer +" =24");
                                }
                                else {
                                    builder1.setMessage("Sorry, there are actually no solutions");
                                    resetNumber();
                                    show_skipped.setText(String.valueOf(++skip_count));
                                    sec=0;
                                    String time_count = String.format("%02d:%02d", sec / 100, sec % 100);
                                    show_timer.setText(time_count);
                                }
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                return true;
                            case R.id.assignNumber:
                                dialogShow();
                                show_skipped.setText(String.valueOf(++skip_count));
                                equal.setEnabled(false);
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
                OperateSum="";
                showtext.setText(OperateSum);
                one.setEnabled(true);
                two.setEnabled(true);
                three.setEnabled(true);
                four.setEnabled(true);
                return true;
            }

            case R.id.ff:
                resetNumber();
                show_skipped.setText(String.valueOf(++skip_count));
                sec=0;
                String time_count = String.format("%02d:%02d", sec / 100, sec % 100);
                show_timer.setText(time_count);
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


    private void initView() {
        one=(Button) findViewById(R.id.num_one);
        two=(Button) findViewById(R.id.num_two);
        three=(Button) findViewById(R.id.num_three);
        four=(Button) findViewById(R.id.num_four);

        add=(Button) findViewById(R.id.add);
        subtract=(Button) findViewById(R.id.subtract);
        multiply=(Button) findViewById(R.id.multiply);
        divide=(Button) findViewById(R.id.divide);
        left_parenthesis=(Button) findViewById(R.id.left_parenthesis);
        right_parenthesis=(Button) findViewById(R.id.right_parenthesis);
        delete=(Button) findViewById(R.id.delete);
        equal=(Button) findViewById(R.id.equal);

        show_attempt=(TextView) findViewById(R.id.attempt_num);
        show_attempt.setText(String.valueOf(attempt_count));

        show_succee=(TextView) findViewById(R.id.succeed_num);
        show_succee.setText(String.valueOf(succee_count));

        show_skipped=(TextView) findViewById(R.id.skipped_num);
        show_skipped.setText(String.valueOf(skip_count));

        show_timer=(TextView) findViewById(R.id.time_num);

        Thread timer=new Thread() {
            @Override
            public void run() {
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sec++;
                                String time_count = String.format("%02d:%02d", sec / 100, sec % 100);
                                show_timer.setText(time_count);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.start();
        showtext=(EditText) findViewById(R.id.result_text);
        showtext.setCursorVisible(false);
    }

    private void initEvent() {
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        add.setOnClickListener(this);
        subtract.setOnClickListener(this);
        multiply.setOnClickListener(this);
        divide.setOnClickListener(this);
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempt_count++;
                show_attempt.setText(String.valueOf(attempt_count));
                equal.setEnabled(false);
                if(!evresult(OperateSum)){
                   android.support.design.widget.Snackbar.make(view, "Incorrect. Please try again!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                           .show();
                }
                else {
                    AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);

                    builder.setMessage("Binggo! "+OperateSum+"=24")
                            .setPositiveButton("Next Puzzle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OperateSum="";
                                    showtext.setText(OperateSum);
                                    resetNumber();
                                    succee_count++;
                                    show_succee.setText(String.valueOf(succee_count));
                                    attempt_count=1;
                                    show_attempt.setText(String.valueOf(attempt_count));
                                    sec = 0;
                                    String time_count = String.format("%02d:%02d", sec / 100, sec % 100);
                                    show_timer.setText(time_count);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        delete.setOnClickListener(this);
        left_parenthesis.setOnClickListener(this);
        right_parenthesis.setOnClickListener(this);

        //about.setOnClickListener(this);
        showtext.setOnClickListener(this);
        show_attempt.setOnClickListener(this);

        //generate 4 numbers at start
        resetNumber();
    }

    private void resetNumber() {
        randomGenerator(min,max);
        bt_one = Integer.toString(numberStore[0]);
        bt_two = Integer.toString(numberStore[1]);
        bt_three = Integer.toString(numberStore[2]);
        bt_four = Integer.toString(numberStore[3]);

        one.setText(bt_one);
        two.setText(bt_two);
        three.setText(bt_three);
        four.setText(bt_four);

        one.setEnabled(true);
        two.setEnabled(true);
        three.setEnabled(true);
        four.setEnabled(true);
    }

    private void randomGenerator(int min, int max){
        int[] map = new int[10];
        for(int i = 0; i < 4; i++){
            int temp = (int)(Math.random() * ((max - min) + 1)) + min;
            if (map[temp] == 0){
                numberStore[i] = temp;
                map[temp] = 1;
            } else {
                i--;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.num_one:
                OperateSum=AddSum(String.valueOf(bt_one).charAt(0));
                showtext.setText(OperateSum);
                break;
            case R.id.num_two:
                OperateSum=AddSum(String.valueOf(bt_two).charAt(0));
                showtext.setText(OperateSum);
                break;
            case R.id.num_three:
                OperateSum=AddSum(String.valueOf(bt_three).charAt(0));
                showtext.setText(OperateSum);
                break;
            case R.id.num_four:
                OperateSum=AddSum(String.valueOf(bt_four).charAt(0));
                showtext.setText(OperateSum);
                break;

            case R.id.add:
                OperateSum=AddSum('+');
                showtext.setText(OperateSum);
                break;
            case R.id.subtract:
                OperateSum=AddSum('-');
                showtext.setText(OperateSum);
                break;
            case R.id.multiply:
                OperateSum=AddSum('*');
                showtext.setText(OperateSum);
                break;
            case R.id.divide:
                OperateSum=AddSum('/');
                showtext.setText(OperateSum);
                break;
            case R.id.left_parenthesis:
                OperateSum=AddSum('(');
                showtext.setText(OperateSum);
                break;
            case R.id.right_parenthesis:
                OperateSum=AddSum(')');
                showtext.setText(OperateSum);
                break;
            case R.id.delete:
                if(OperateSum.length()>=1)
                {
                    char c = OperateSum.charAt(OperateSum.length()-1);
                    if (Character.isDigit(c)) {
                        if (one.getText().charAt(0) == c){
                            one.setEnabled(true);
                        } else if (two.getText().charAt(0) == c) {
                            two.setEnabled(true);
                        } else if (three.getText().charAt(0) == c) {
                            three.setEnabled(true);
                        } else if (four.getText().charAt(0) == c) {
                            four.setEnabled(true);
                        }
                    }
                    OperateSum=OperateSum.substring(0,OperateSum.length()-1);
                }
                showtext.setText(OperateSum);
            default:
                break;
        }
    }

    public String AddSum(char c)
    {
        if (Character.isDigit(c)){
            if (one.getText().charAt(0) == c){
                one.setEnabled(false);
            } else if (two.getText().charAt(0) == c) {
                two.setEnabled(false);
            } else if (three.getText().charAt(0) == c) {
                three.setEnabled(false);
            } else if (four.getText().charAt(0) == c) {
                four.setEnabled(false);
            }
        }
        OperateSum=OperateSum+String.valueOf(c);
        equal.setEnabled(true);
        return OperateSum;
    }

    public int calc(int op2, int op1, char ch) {
        switch(ch) {
            case '-': return op1 - op2;
            case '+': return op1 + op2;
            case '/': return op1 / op2;
            case '*': return op1 * op2;
        }
        return 0;
    }

    public boolean higherPriority(char op1, char op2) {
        if ((op1 =='*') || (op1 =='/')) {
            return true;
        }

        if ((op2 =='+') || (op2 =='-')) {
            return true;
        }
        return false;
    }

    public boolean evresult(String exp) {
        if(exp.length() == 0) {
            return false;
        }
        Stack<Integer> st = new Stack<>();
        Stack<Character> op = new Stack<>();
        int digit = 0;
        boolean hasDigit = false;
        for (int i = 0; i < exp.length(); i++) {
            if (Character.isDigit(exp.charAt(i))) {
                hasDigit = true;
                digit = digit*10 + (exp.charAt(i) - '0');
            } else {
                if(hasDigit) {
                    hasDigit = false;
                    st.push(digit);
                    digit = 0;
                }
                if (exp.charAt(i) == '(') {
                    op.push('(');
                } else if(exp.charAt(i) == ')') {
                    while (op.peek() != '(') {
                        st.push(calc(st.pop(), st.pop(), op.pop()));
                    }
                    op.pop();

                } else {
                    while (!op.isEmpty() && op.peek() != '(' && higherPriority(op.peek(), exp.charAt(i))) {
                        st.push(calc(st.pop(), st.pop(), op.pop()));
                    }

                    op.push(exp.charAt(i));
                }
            }
        }
        if(hasDigit)
            st.push(digit);
        while(!op.isEmpty()) {
            st.push(calc(st.pop(), st.pop(), op.pop()));
        }
        return st.peek() == 24;
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
                bt_one = Integer.toString(np1.getValue());
                bt_two = Integer.toString(np2.getValue());
                bt_three = Integer.toString(np3.getValue());
                bt_four = Integer.toString(np4.getValue());

                one.setText(bt_one);
                two.setText(bt_two);
                three.setText(bt_three);
                four.setText(bt_four);

                OperateSum="";
                showtext.setText(OperateSum);
                one.setEnabled(true);
                two.setEnabled(true);
                three.setEnabled(true);
                four.setEnabled(true);

                sec=0;
                String time_count = String.format("%02d:%02d", sec / 100, sec % 100);
                show_timer.setText(time_count);

                attempt_count=1;
                show_attempt.setText(String.valueOf(attempt_count));

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

}
