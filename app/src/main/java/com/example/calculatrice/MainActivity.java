package com.example.calculatrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    Button percentageButton;
    private TextView newNumberTextView;
    private Double operand1 = null;
    private String pendingOperation = "=";
    private Button clearButton,deleteButton,exitButton,commaButton;


    private static final String STATE_PENDING_OPERATION = "PendingOperation";
    private static final String STATE_OPERAND1 = "Operand1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.result);
        newNumberTextView = findViewById(R.id.textView);
        clearButton = findViewById(R.id.clearButton);
        deleteButton = findViewById(R.id.deleteButton);
        exitButton = findViewById(R.id.exitButton);
        commaButton = findViewById(R.id.Comma);
        percentageButton = findViewById(R.id.pourcentage);

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

        // Operation Buttons
        Button buttonEquals = findViewById(R.id.equale);
        Button buttonPlus = findViewById(R.id.plus);
        Button buttonMinus = findViewById(R.id.minece);
        Button buttonMultiply = findViewById(R.id.multiplication);
        Button buttonDivide = findViewById(R.id.division);

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

        View.OnClickListener opListener = v -> {
            Button b = (Button) v;
            String op = b.getText().toString();
            String value = newNumberTextView.getText().toString();
            if (value.isEmpty()) value = "0"; // Default to zero if empty
            try {
                Double doubleValue = Double.valueOf(value);
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

    }

    private void performOperation(Double value, String operation) {
        if (null == operand1) {
            operand1 = value;
        } else {
            if (pendingOperation.equals("=")) {
                pendingOperation = operation;
            }

            switch (pendingOperation) {
                case "=":
                    operand1 = value;
                    break;
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

        resultTextView.setText(operand1.toString());
        newNumberTextView.setText("");

        clearButton.setOnClickListener(v ->
        {
            operand1 = null;
            pendingOperation = "=";
            resultTextView.setText("");
            newNumberTextView.setText("");
        });

        clearButton.setOnClickListener(v -> {
            operand1 = null;
            pendingOperation = "=";
            resultTextView.setText("");
            newNumberTextView.setText("0"); // Start with "0" to indicate a clear screen
        });

        percentageButton.setOnClickListener(v -> {
            String currentText = newNumberTextView.getText().toString();
            if (!currentText.isEmpty()) {
                double val= Double.parseDouble(currentText) / 100;
                newNumberTextView.setText(String.valueOf(val));
            }
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

        // Correcting Comma (Decimal) button functionality
        commaButton.setOnClickListener(v -> {
            String currentText = newNumberTextView.getText().toString();
            if (!currentText.contains(".")) {
                newNumberTextView.append(".");
            }
        });

        exitButton.setOnClickListener(v -> finish());


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_PENDING_OPERATION, pendingOperation);
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
        operand1 = savedInstanceState.getDouble(STATE_OPERAND1);
        resultTextView.setText(operand1.toString());
    }

    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, MainActivityLandspace.class);
            startActivity(intent);
        }
    }

}