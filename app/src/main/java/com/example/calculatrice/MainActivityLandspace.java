package com.example.calculatrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;


public class MainActivityLandspace extends AppCompatActivity {

    private TextView resultTextView;
    private TextView newNumberTextView;
    private Double operand1 = null;
    private String pendingOperation = "=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_landspace);

        resultTextView = findViewById(R.id.result);
        newNumberTextView = findViewById(R.id.textView);




        Button[] numberButtons = {
                findViewById(R.id.zero),
                findViewById(R.id.one),
                findViewById(R.id.two),
                findViewById(R.id.tree),
                findViewById(R.id.four),
                findViewById(R.id.five),
                findViewById(R.id.sex),
                findViewById(R.id.seven),
                findViewById(R.id.eight),
                findViewById(R.id.nine)
        };

        Button[] functionButtons = {
                findViewById(R.id.sin),
                findViewById(R.id.cos),
                findViewById(R.id.tang),
                findViewById(R.id.arcsin),
                findViewById(R.id.arcos),
                findViewById(R.id.arctg),
                findViewById(R.id.squared),
                findViewById(R.id.racine),
                findViewById(R.id.inverse),
                findViewById(R.id.expolanciel),
                findViewById(R.id.logarithme),
                findViewById(R.id.Pi)
        };

        // Operation Buttons
        Button buttonEquals = findViewById(R.id.equale);
        Button buttonPlus = findViewById(R.id.plus);
        Button buttonMinus = findViewById(R.id.minece);
        Button buttonMultiply = findViewById(R.id.multiplication);
        Button buttonDivide = findViewById(R.id.divisionLand);
        Button clearButton = findViewById(R.id.clearButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button percentageButton = findViewById(R.id.pourcentage);
        Button commaButton = findViewById(R.id.Comma);
        Button exitButton = findViewById(R.id.exitButton);

        if (getIntent().hasExtra("operand1")) {
            operand1 = getIntent().getDoubleExtra("operand1", 0.0);
            resultTextView.setText(operand1.toString());
        }
        pendingOperation = getIntent().getStringExtra("pendingOperation");

        View.OnClickListener numberListener = v -> {
            Button b = (Button) v;
            String currentText = newNumberTextView.getText().toString();
            if(currentText.equals("0")) {
                newNumberTextView.setText(b.getText().toString());
            } else {
                newNumberTextView.append(b.getText().toString());
            }
        };

        for (Button button : numberButtons) {
            button.setOnClickListener(numberListener);
        }

        View.OnClickListener functionListener = v -> {
            Button b = (Button) v;
            String function = b.getText().toString();
            String value = newNumberTextView.getText().toString();
            if (!value.isEmpty()) {
                // If there's a value in newNumberTextView, use it
                try {
                    double doubleValue = Double.parseDouble(value);
                    double result = performFunction(doubleValue, function);
                    newNumberTextView.setText(String.valueOf(result)); // Set the result to newNumberTextView
                } catch (NumberFormatException e) {
                    newNumberTextView.setText(""); // Clear newNumberTextView if there's an error
                }
            } else {
                // If newNumberTextView is empty, use the value from resultTextView
                value = resultTextView.getText().toString();
                if (!value.isEmpty()) {
                    try {
                        double doubleValue = Double.parseDouble(value);
                        double result = performFunction(doubleValue, function);
                        resultTextView.setText(String.valueOf(result)); // Set the result to resultTextView
                    } catch (NumberFormatException e) {
                        resultTextView.setText(""); // Clear resultTextView if there's an error
                    }
                }
            }
        };



        for (Button button : functionButtons) {
            button.setOnClickListener(functionListener);
        }

        View.OnClickListener opListener = v -> {
            Button b = (Button) v;
            String op = b.getText().toString();
            String value = newNumberTextView.getText().toString();
            if (value.isEmpty()) value = "0"; // Default to zero if empty
            try {
                double doubleValue = Double.parseDouble(value);
                performOperation(doubleValue, op);
            } catch (NumberFormatException e) {
                newNumberTextView.setText("");
            }
            pendingOperation = op;
        };

        buttonEquals.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);
        buttonMinus.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);
        buttonDivide.setOnClickListener(opListener);

        clearButton.setOnClickListener(v -> {
            operand1 = null;
            pendingOperation = "=";
            resultTextView.setText("");
            newNumberTextView.setText("0"); // Start with "0" to indicate a clear screen
        });

        deleteButton.setOnClickListener(v -> {
            String currentText = newNumberTextView.getText().toString();
            if (!currentText.isEmpty() && !currentText.equals("0")) {
                currentText = currentText.substring(0, currentText.length() - 1);
                if(currentText.isEmpty()) {
                    currentText = "0"; // Reset to 0 if all digits are deleted
                }
                newNumberTextView.setText(currentText);
            }
        });

        percentageButton.setOnClickListener(v -> {
            String currentText = newNumberTextView.getText().toString();
            if (!currentText.isEmpty()) {
                double val= Double.parseDouble(currentText) / 100;
                newNumberTextView.setText(String.valueOf(val));
            }
        });

        commaButton.setOnClickListener(v -> {
            String currentText = newNumberTextView.getText().toString();
            if (!currentText.contains(".")) {
                newNumberTextView.append(".");
            }
        });

        exitButton.setOnClickListener(v -> finish());
    }

    private double performFunction(double value, String function) {
        switch (function) {
            case "sin":
                return Math.sin(Math.toRadians(value));
            case "cos":
                return Math.cos(Math.toRadians(value));
            case "tang":
                return Math.tan(Math.toRadians(value));
            case "arcsin":
                return Math.toDegrees(Math.asin(value));
            case "arcos":
                return Math.toDegrees(Math.acos(value));
            case "arctg":
                return Math.toDegrees(Math.atan(value));
            case "x²":
                return Math.pow(value, 2);
            case "√x":
                return Math.sqrt(value);
            case "1/x":
                return 1 / value;
            case "exp":
                return Math.exp(value);
            case "ln":
                return Math.log(value);
            case "π":
                return Math.PI * value;
            default:
                return value;
        }
    }

    private void performOperation(Double value, String operation) {
        if (null == operand1) {
            operand1 = value;
        } else {
            if (!pendingOperation.equals("=")) {
                switch (pendingOperation) {
                    case "/":
                        if (value == 0) {
                            operand1 = 0.0; // Prevent division by zero
                        } else {
                            operand1 /= value;
                        }
                        break;
                    case "×":
                        operand1 *= value;
                        break;
                    case "-":
                        operand1 -= value;
                        break;
                    case "+":
                        operand1 += value;
                        break;
                }
            }
        }

        resultTextView.setText(operand1.toString());
        newNumberTextView.setText("");
        pendingOperation = operation;
    }

    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, MainActivity.class);
            if (operand1 != null) {
                intent.putExtra("operand1", operand1);
            }
            intent.putExtra("pendingOperation", pendingOperation);
            startActivity(intent);
        }
    }
}



