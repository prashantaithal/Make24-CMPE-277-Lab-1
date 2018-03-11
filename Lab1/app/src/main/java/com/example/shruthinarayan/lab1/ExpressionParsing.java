package com.example.shruthinarayan.lab1;

/**
 * Created by prash on 3/11/2018.
 */

import org.mariuszgromada.math.mxparser.*;

public class ExpressionParsing {

    public boolean check(String exp){
        Expression eh = new Expression(exp);
        Double d  =  eh.calculate();
        if(d.equals(24.0)) {
            return true;
        }
        else{
            return false;
        }
    }
}
