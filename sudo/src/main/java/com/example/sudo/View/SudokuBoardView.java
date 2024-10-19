package com.example.sudo.View;

import com.example.sudo.Model.Sudoku;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
/**
 * Esta clase representa la vista del tablero del Sudoku, manejando la interfaz
 * gráfica y las interacciones del usuario con el tablero.
 * Hereda de {@code Pane}, lo que permite agregar componentes visuales como {@code TextField}.
 */


public class SudokuBoardView extends Pane {

    private TextField[][] listTxt;
    private int txWidth;
    private int txtHeight;
    private int txtMargin;
    private int txtLetterSize;
    private Color panelBackground;
    private Color txtBackground1;
    private Color textForeground1;
    private Color txtBackground2;
    private Color textForeground2;
    private Color txtBackground3;
    private Color textForeground3;
    private ArrayList <TextField> listTxtAux;
    private Sudoku sudoku;
    private ArrayList<TextField> generatedTxtList;

    /**
     * Constructor de la clase. Inicializa los componentes y crea un nuevo objeto de Sudoku.
     */

    public SudokuBoardView() {
        startComponents();
        this.sudoku = new Sudoku();
    }
    /**
     * Inicializa los componentes gráficos y las propiedades del tablero.
     */

    public void startComponents() {
        listTxt = new TextField[6][6];
        txWidth =23;
        txtHeight = 24;
        txtMargin = 4;
        txtLetterSize = 3;
        panelBackground = Color.BLACK;
        txtBackground1 = Color.WHITE;
        textForeground1 = Color.BLACK;
        txtBackground2 = Color.WHITE;
        textForeground2 = Color.BLACK;
        txtBackground3 = Color.WHITE;
        textForeground3 = Color.BLACK;
        listTxtAux = new ArrayList();
        Sudoku sudoku;
        generatedTxtList = new ArrayList<TextField>();
    }
    /**
     * Crea y configura los campos de texto que representan cada celda del Sudoku,
     * ubicándolos en el tablero con márgenes y tamaños específicos.
     */

    public void createTxtField() {
        int x = txtMargin;
        int y = txtMargin;

        for (int i = 0; i < listTxt.length; i++) {
            for (int j = 0; j < listTxt[0].length; j++) {
                TextField txt = new TextField();
                this.getChildren().add(txt);
                txt.setLayoutX(x);
                txt.setLayoutY(y);
                txt.setPrefWidth(txWidth);
                txt.setPrefHeight(txtHeight);
                txt.setEditable(false);
                x += 2 + txWidth;
                if ((j + 1) % 3 == 0) {
                    x +=  2* txtMargin;
                }
                listTxt[i][j] = txt;
                generateEvents(txt,i,j);
            }
            x = txtMargin;
            y += 2 + txtHeight;
            if ((i + 1) % 2 == 0) {
                y += 2* txtMargin;
            }
        }
    }
    /**
     * Comprueba si un campo de texto fue generado automáticamente por el Sudoku.
     *
     * @param txt El campo de texto a verificar.
     * @return {@code true} si el campo fue generado, {@code false} de lo contrario.
     */

    public boolean txtGenerated(TextField txt) {
        for (TextField jTxt : generatedTxtList) {
            if (txt == jTxt && !txt.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Genera un nuevo tablero de Sudoku y lo muestra en los campos de texto.
     * Los números generados se añaden a la lista {@code generatedTxtList}.
     */

    public void generateSudoku() {
        clearTxt();
        sudoku.generateSudoku(listTxt);
        int [][] sudokuGenerated = sudoku.getSudoku();
        for(int i = 0; i < sudokuGenerated.length; i++) {
            for (int j = 0; j < sudokuGenerated[0].length; j++) {
                if (sudokuGenerated[i][j] != 0) {
                    listTxt[i][j].setText(String.valueOf(sudokuGenerated[i][j]));
                    generatedTxtList.add(listTxt[i][j]);
                    listTxt[i][j].setEditable(false);
                }
            }
        }
    }



    /**
     * Limpia todos los campos de texto del tablero.
     */


    public void clearTxt(){
        for(int i = 0; i < listTxt.length; i++){
            for (int j = 0; j < listTxt[0].length; j++){
                listTxt[i][j].setText("");
            }
        }

    }

    /**
     * Valida la entrada del usuario en un campo de texto específico, verificando si el número
     * es válido para la fila y columna correspondientes.
     *
     * @param txt El campo de texto que contiene la entrada del usuario.
     * @param fila La fila del tablero.
     * @param columna La columna del tablero.
     * @param num El número ingresado por el usuario.
     */
    private void validateInput(TextField txt, int fila, int columna, int num) {
        boolean isValid = sudoku.isNumberValid(fila, columna, num, listTxt);

        if (!isValid) {
            txt.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        } else {
            txt.setStyle(""); // Restaurar el estilo predeterminado si el número es válido
        }
    }

    /**
     * Genera eventos para los campos de texto, controlando la entrada de datos del usuario,
     * la validación y el comportamiento de foco.
     *
     * @param txt El campo de texto al que se le añaden los eventos.
     * @param fila La fila del tablero asociada al campo de texto.
     * @param columna La columna del tablero asociada al campo de texto.
     */


    public void generateEvents(TextField txt, int fila, int columna) {
        txt.setOnMousePressed(event -> pressed(txt));
        txt.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();


            if (txtGenerated(txt)) {
                event.consume();
                return;
            }


            if (code == KeyCode.BACK_SPACE || code == KeyCode.DELETE) {
                txt.clear();
                validateInput(txt, fila, columna, -1);
            }

            else if (code.isDigitKey()) {
                String input = event.getText();
                if (input.matches("[1-6]")) {
                    txt.setText(input);
                    validateInput(txt, fila, columna, Integer.parseInt(input));
                } else {
                    event.consume();
                }
            } else {
                event.consume();
            }
        });


        txt.setOnKeyTyped(event -> {
            if (txtGenerated(txt)) {
                event.consume();
                return;
            }
            String input = event.getCharacter();

            if (!input.matches("[1-6]") || txt.getText().length() >= 1) {
                event.consume();
            }
        });


        txt.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (txt.getText().isEmpty()) {
                    txt.setStyle("");
                } else {
                    validateInput(txt, fila, columna, Integer.parseInt(txt.getText()));
                }
            } else {
                if (!txtGenerated(txt)) {
                    txt.clear();
                }
            }
        });
    }


   /* public void resolver(){
        sudoku.limpiarSudoku();
        for(int i = 0; i < listTxt.length; i++){
            for (int j = 0; j < listTxt[0].length; j++){
                if (!listTxt[i][j].getText().isEmpty()){
                    sudoku.getSudoku()[i][j] = Integer.parseInt(listTxt[i][j].getText());
                }
            }
        }
        if (sudoku.resolveSudoku()){
            for (int i = 0; i < listTxt.length; i++){
                for (int j = 0; j < listTxt[0].length; j++){
                    listTxt[i][j].setText(String.valueOf(sudoku.getSudoku()[i][j]));
                }
            }
        } else {
            System.out.println("sin solucion");
        }
    } */
    /**
     * Valida el estado actual del tablero de Sudoku verificando si todas las celdas están llenas.
     * Si el tablero está incompleto, imprime un mensaje indicando que faltan datos.
     * Si el tablero está completo, verifica si la solución del Sudoku es correcta.
     * Si la solución es válida, imprime un mensaje de éxito; de lo contrario, muestra un mensaje de error.
     */

    public void validate(){
        int sudo[][] = new int[6][6];
        for (int i = 0; i < listTxt.length; i++) {
            for (int j = 0; j < listTxt[0].length; j++) {
                if(listTxt[i][j].getText().isEmpty()){
                    System.out.println("Eso esta incompleto mano");
                    return;
                } else {
                    sudo[i][j] = Integer.parseInt(listTxt[i][j].getText());
                }
            }
        }
        sudoku.setSudoku(sudo);
        sudoku.showSudoku();
        if (sudoku.winCheck()){
            System.out.println("completado exitosamente, felicidades menor");
        } else {
            System.out.println("No hay solución pa");
        }
    }

    /**
     * Resalta la celda seleccionada y su fila y columna correspondientes.
     * Elimina el resaltado anterior y resalta la fila y columna actual en un color azul claro,
     * con la celda seleccionada resaltada en un azul más oscuro.
     *
     * @param txt El TextField que fue clickeado.
     */

    public void pressed(TextField txt){

        for (TextField jtxt: listTxtAux){
            jtxt.setStyle("-fx-background-color: white;");
        }
        listTxtAux.clear();


        for (int i = 0; i < listTxt.length; i++) {
            for (int j = 0; j < listTxt[0].length; j++) {
                if (listTxt[i][j] == txt) {


                    for (int k = 0; k < listTxt.length; k++) {
                        listTxt[k][j].setStyle("-fx-background-color: #beddeb;");
                        listTxtAux.add(listTxt[k][j]);
                    }

                    for (int k = 0; k < listTxt[0].length; k++) {
                        listTxt[i][k].setStyle("-fx-background-color: #beddeb;");
                        listTxtAux.add(listTxt[i][k]);
                    }

                    listTxt[i][j].setStyle("-fx-background-color: #87d6fa;");
                    return;
                }
            }
        }
    }

    /**
     * Proporciona una pista rellenando un número válido en una celda vacía.
     * Reinicia las pistas anteriores eliminando su color de fondo y limpiando su contenido.
     * Luego, busca una celda vacía y coloca un número válido, resaltándola con un color de fondo amarillo.
     */

    public void help() {

        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {
                if (listTxt[row][column].getStyle().contains("yellow")) {
                    listTxt[row][column].setStyle("");
                    listTxt[row][column].clear();
                }
            }
        }

        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {
                if (sudoku.getSudoku()[row][column] == 0 && listTxt[row][column].getText().isEmpty()) {
                    for (int num = 1; num <= 6; num++) {
                        if (sudoku.isNumberValid(row, column, num, listTxt)) {

                            listTxt[row][column].setText(String.valueOf(num));
                            listTxt[row][column].setStyle("-fx-background-color: yellow;");
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Establece el color de fondo del tablero de Sudoku y su tamaño.
     * Llama al método para crear los campos de texto que representan el tablero de Sudoku.
     */


    public void makeSudoku(){
        this.setStyle("-fx-background-color: #2b2b2b;");
        this.setPrefSize((txWidth + 2) * 6 + (txtMargin * 3), (txtHeight + 2) * 6 +(txtMargin * 5));
        createTxtField();
    }

    public TextField[][] getListTxt() {
        return listTxt;
    }

    public void setListTxt(TextField[][] listTxt) {
        this.listTxt = listTxt;
    }

    public int getTxWidth() {
        return txWidth;
    }

    public void setTxWidth(int txWidth) {
        this.txWidth = txWidth;
    }

    public int getTxtAltura() {
        return txtHeight;
    }

    public void setTxtAltura(int txtAltura) {
        this.txtHeight = txtAltura;
    }

    public int getTxtMargin() {
        return txtMargin;
    }

    public void setTxtMargin(int txtMargin) {
        this.txtMargin = txtMargin;
    }

    public int getTxtLetterSize() {
        return txtLetterSize;
    }

    public void setTxtLetterSize(int txtLetterSize) {
        this.txtLetterSize = txtLetterSize;
    }

    public Color getPanelBackground() {
        return panelBackground;
    }

    public void setPanelBackground(Color panelBackground) {
        this.panelBackground = panelBackground;
    }

    public Color getTxtBackground1() {
        return txtBackground1;
    }

    public void setTxtBackground1(Color txtBackground1) {
        this.txtBackground1 = txtBackground1;
    }

    public Color getTextForeground1() {
        return textForeground1;
    }

    public void setTextForeground1(Color textForeground1) {
        this.textForeground1 = textForeground1;
    }

    public Color getTxtBackground2() {
        return txtBackground2;
    }

    public void setTxtBackground2(Color txtBackground2) {
        this.txtBackground2 = txtBackground2;
    }

    public Color getTextForeground2() {
        return textForeground2;
    }

    public void setTextForeground2(Color textForeground2) {
        this.textForeground2 = textForeground2;
    }

    public Color getTxtBackground3() {
        return txtBackground3;
    }

    public void setTxtBackground3(Color txtBackground3) {
        this.txtBackground3 = txtBackground3;
    }

    public Color getTextForeground3() {
        return textForeground3;
    }

    public void setTextForeground3(Color textForeground3) {
        this.textForeground3 = textForeground3;
    }
}