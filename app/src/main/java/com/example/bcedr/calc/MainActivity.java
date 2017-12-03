package com.example.bcedr.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText operand1_text, operand2_text;
    private TextView result_text;
    private TextView Special_text1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View v) {
        int num1 = input_to_int(parenthesis_remover(operand1_text.getText().toString()));
        int num2 = input_to_int(parenthesis_remover(operand2_text.getText().toString()));
        switch (v.getId()) {
            case R.id.AddButton:
                int addition = num1 + num2;
                result_text.setText(String.valueOf(addition));
                break;
            case R.id.SubButton:
                int subtraction = num1 - num2;
                result_text.setText(String.valueOf(subtraction));
                break;
            case R.id.MultButton:
                int multiplication = num1 * num2;
                result_text.setText(String.valueOf(multiplication));
                break;
            case R.id.DivButton:
                if(num2 != 0) {
                    int division = num1 / num2;
                    result_text.setText(String.valueOf(division));
                }
                else{
                    result_text.setText("Cannot Divide by Zero");
                }
                break;
            case R.id.SqrtButton:
                if(num1 > 0) {
                    double squareroot = Math.sqrt(num1);
                    result_text.setText(String.valueOf(squareroot));
                }
                else{
                    result_text.setText("Cannot take Negatives");
                }
                break;
            case R.id.PowButton:
                double power = Math.pow(num1,num2);
                result_text.setText(String.valueOf(power));
                break;
            case R.id.ClearButton:
                result_text.setText("");
                operand1_text.setText("");
                operand2_text.setText("");
                break;
            default:
                break;
        }
    }
    private void init() {
        // Capture our button from layout
        Button AddButton = (Button) findViewById(R.id.AddButton);
        Button SubButton = (Button) findViewById(R.id.SubButton);
        Button MultButton = (Button)findViewById(R.id.MultButton);
        Button DivButton = (Button)findViewById(R.id.DivButton);
        Button SqrtButton = (Button)findViewById(R.id.SqrtButton);
        Button PowButton = (Button)findViewById(R.id.PowButton);
        Button ClearButton = (Button)findViewById(R.id.ClearButton);
        operand1_text = (EditText) findViewById(R.id.operand1);
        operand2_text = (EditText) findViewById(R.id.operand2);
        result_text = (TextView) findViewById(R.id.result);


        // Register the onClick listener with the implementation above

        AddButton.setOnClickListener(this);
        SubButton.setOnClickListener(this);
        MultButton.setOnClickListener(this);
        DivButton.setOnClickListener(this);
        SqrtButton.setOnClickListener(this);
        PowButton.setOnClickListener(this);
        ClearButton.setOnClickListener(this);
    }
        //function to convert and compute input value to the correct value
    private int input_to_int(String x){
        int num = 0;
        int sub_num = 0;
        int int_cnt = 0;
        int char_cnt = 0;
        boolean special_char = false;
        String input_str = x ;
        Pattern p = Pattern.compile("(?<=[-+*/()])|(?=[-+*/()])");
        Matcher m = p.matcher(input_str);
        // boolean b = m.matches();
        boolean b = m.find();
        //Array of numbers
        int[] split_array ;
        String regex = "(?<=[-+*/()])|(?=[-+*/()])";
        //System.out.println(input_str.split(regex));
        String char_num = "";
        if(b == false){
            for(int i = 0; i < x.length(); i++)
            {
                char_num += x.charAt(i);
            }
        }

        else{
            input_str.split(regex);
            char_num = operand_solver(input_str);
        }

        num = Integer.parseInt(char_num);

        return num;
    }
    private String operand_solver(String x) {
        int int_op1 = 0;
        int int_op2 = 0;
        String str_op1 = "";
        String str_op2 = "";
        int lencheck;
        char operation = '\0';
        boolean multi_operations = false;
        int final_value = 0;


        //Operator position
        //number builder
        for (int i = 0; i < x.length(); i++) {
            if (!multi_operations) {

                if (!isSpecial(x.charAt(i))) {
                    str_op1 += x.charAt(i);
                } else {
                    operation = x.charAt(i);
                    i++;

                    while (!isSpecial(x.charAt(i))) {

                        str_op2 += x.charAt(i);

                        lencheck = i + 1;
                        i++;

                        if (lencheck >= x.length()) {
                            break;
                        }
                    }

                    int_op1 = input_to_int(str_op1);
                    int_op2 = input_to_int(str_op2);

                    if (operation == '*') {
                        final_value = int_op1 * int_op2;
                    }
                    if (operation == '-') {
                        final_value = int_op1 - int_op2;
                    }
                    if (operation == '+') {
                        final_value = int_op1 + int_op2;
                    }
                    if (operation == '/') {
                        final_value = int_op1 / int_op2;
                    }

                    multi_operations = true;


                }
            }
            else {
                operation = x.charAt(i - 1);
                str_op2 = "";

                while (!isSpecial(x.charAt(i))) {

                    str_op2 += x.charAt(i);

                    i++;

                    if (i == x.length()) {
                        break;
                    }
                }

                int_op1 = final_value;
                int_op2 = input_to_int(str_op2);

                if (operation == '*') {
                    final_value = int_op1 * int_op2;
                }
                if (operation == '-') {
                    final_value = int_op1 - int_op2;
                }
                if (operation == '+') {
                    final_value = int_op1 + int_op2;
                }
                if (operation == '/') {
                    final_value = int_op1 / int_op2;
                }

            }

        }
        result_text.setText(Integer.toString(final_value));
        return Integer.toString(final_value);
    }
    //Function that deals with the parenthesis in the function by solving whats inside the parenthesis with
    //with meaningful string for the operand solver
    public String parenthesis_remover(String x){

        //Full input with parenthesis

        StringBuilder full_input = new StringBuilder(x);
        int start_pos = 0;
        int end_pos = 0;
        String sub_str = "";
        String part_input = "";

        while(hasParenthesis(full_input)){
            for(int i = 0 ; i <full_input.length(); i++){
                if(full_input.charAt(i) == '('){
                    start_pos = i;

                }
                if(full_input.charAt(i) == ')'){
                    end_pos = i;
                }
            }
            for(int i = 0 ; i <full_input.length(); i++){
                if(i == start_pos){
                    while(i != (end_pos + 1)){
                        sub_str += full_input.charAt(i);
                        i++;
                    }

                }
            }
            for(int i = 0 ; i <full_input.length(); i++){

                if(i == start_pos + 1 ){
                    while(i != (end_pos )){
                        part_input += full_input.charAt(i);
                        i++;
                    }

                }
            }


            sub_str = operand_solver(part_input);
            for(int i = 0 ; i <full_input.length(); i++){
                int j = 0;
                if(i == start_pos){
                    full_input.setCharAt(i,sub_str.charAt(j));
                    j++;
                    i++;
                    while(i <= (end_pos )){
                        int start_end_diff = (end_pos + 1) - start_pos;
                        int str_diff = start_end_diff - sub_str.length();
                        if(j < sub_str.length()) {
                            full_input.setCharAt(i, sub_str.charAt(j));
                            j++;
                            i++;
                        }
                        else{
                            i = i + str_diff;
                            while(i < full_input.length() ) {
                                full_input.setCharAt(i - str_diff, full_input.charAt(i ) );
                                j++;
                                i++;
                            }
                            full_input.setLength(full_input.length() - str_diff);
                        }

                    }

                }

            }


        }
        return full_input.toString();
    }

    //Function to check if character is a special character
    public boolean isSpecial(char x){
        boolean unique = false;

        if((x == '-' )|| (x == '+') || (x ==  '*') || (x == '/')){
            unique = true;
        }
        return unique;


    }
    public boolean hasParenthesis(StringBuilder x){
        int i = 0;
        Boolean hasParen = false;

        while(i < x.length()){
            if((x.charAt(i) == '(' )|| (x.charAt(i) == ')') ){
                hasParen = true;
            }
            i++;
        }
        return hasParen;
    }


}
