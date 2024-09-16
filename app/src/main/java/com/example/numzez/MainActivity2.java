package com.example.numzez;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private Button btnBasicas, btnIntermedias, btnAvanzadas;
    private ImageView imagePitagoritas;
    private SharedPreferences sharedPreferences;
    private AlertDialog currentDialog;  // Guardar referencia al diálogo actual para cerrarlo antes de abrir el siguiente
    private boolean isTutorialRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnBasicas = findViewById(R.id.button);
        btnIntermedias = findViewById(R.id.button2);
        btnAvanzadas = findViewById(R.id.button3);
        imagePitagoritas = findViewById(R.id.imagePitagoritas);

        // Cargar las preferencias
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        // Mostrar tour si es la primera vez o si el usuario no ha desactivado el tutorial
        if (shouldShowTutorial() && !isTutorialRunning) {
            isTutorialRunning = true;
            showIntroduction();
        }
    }

    private boolean shouldShowTutorial() {
        return sharedPreferences.getBoolean("showTutorial", true);
    }

    private void setShowTutorial(boolean show) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("showTutorial", show);
        editor.apply();
    }

    private void showIntroduction() {
        movePitagoritasToWelcome(); // Mueve Pitagoritas a la bienvenida
        showDialog("¡Hola! Soy Pitagoritas, tu asistente para ayudarte en operaciones aritméticas.", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movePitagoritasToButton(btnBasicas);
                showDialog("Aquí puedes comenzar con Operaciones Básicas.", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePitagoritasToButton(btnIntermedias);
                        showDialog("Este botón es para Operaciones Intermedias.", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                movePitagoritasToButton(btnAvanzadas);
                                showDialog("Y aquí están las Operaciones Avanzadas.", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        movePitagoritasToBottomRight();
                                        showFinalMessage();
                                    }
                                }, false);  // No mostrar el checkbox aquí
                            }
                        }, false);
                    }
                }, false);
            }
        }, true);  // Mostrar el checkbox en la bienvenida
    }

    private void movePitagoritasToWelcome() {
        imagePitagoritas.setVisibility(View.VISIBLE);
        imagePitagoritas.setX(50);  // Ajustar valores según el tamaño de pantalla
        imagePitagoritas.setY(200); // Ajustar valores según el tamaño de pantalla
    }

    private void movePitagoritasToButton(View button) {
        // Verifica si es el botón de operaciones intermedias
        if (button == btnIntermedias) {
            // Coloca la alerta más arriba del botón de operaciones intermedias
            imagePitagoritas.setX(button.getX() - 150); // Ajustar posición horizontal
            imagePitagoritas.setY(button.getY() - 200);  // Ajustar posición vertical para que esté arriba
        } else {
            // Para otros botones, usa la posición predeterminada
            imagePitagoritas.setX(button.getX() - 150); // Ajustar posición horizontal
            imagePitagoritas.setY(button.getY() - 50);  // Ajustar posición vertical
        }
    }

    private void movePitagoritasToBottomRight() {
        imagePitagoritas.setX(800);  // Ajustar valores según el tamaño de pantalla
        imagePitagoritas.setY(1500); // Ajustar valores según el tamaño de pantalla
    }

    private void showDialog(String message, View.OnClickListener listener, boolean showCheckBox) {
        // Cerrar cualquier diálogo anterior
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pensamiento, null);
        builder.setView(dialogView);

        TextView dialogText = dialogView.findViewById(R.id.dialogText);
        Button dialogButton = dialogView.findViewById(R.id.dialogButton);
        CheckBox tutorialCheckBox = dialogView.findViewById(R.id.tutorialCheckBox);

        dialogText.setText(message);
        tutorialCheckBox.setVisibility(showCheckBox ? View.VISIBLE : View.GONE);  // Mostrar o esconder el checkbox

        dialogButton.setOnClickListener(v -> {
            if (showCheckBox && tutorialCheckBox.isChecked()) {
                setShowTutorial(false);  // Guardar preferencia si el checkbox está marcado
            }
            listener.onClick(v);  // Avanzar en el tutorial
        });

        currentDialog = builder.create();
        currentDialog.show();  // Mostrar el diálogo actual
    }

    private void showFinalMessage() {
        showDialog("¡Gracias por seguir el tutorial! Estaré aquí en la esquina por si necesitas ayuda.", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el último diálogo
                if (currentDialog != null && currentDialog.isShowing()) {
                    currentDialog.dismiss();
                }
                // Fin del tutorial, se cierra todo
                isTutorialRunning = false;
            }
        }, false);
    }
}
