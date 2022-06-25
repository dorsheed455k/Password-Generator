package com.company;

import javafx.animation.FadeTransition;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class PasswordGenerator implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Spinner<Integer> mySpinner;
    @FXML
    private RadioButton rb_uppercase, rb_lowercase, rb_numbers, rb_symbols;
    @FXML
    private Button btn_copy;
    @FXML
    private Button bt_export;
    @FXML
    private TextField myTextField;

    static String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static String Small_chars = "abcdefghijklmnopqrstuvwxyz";
    static String numbers = "0123456789";
    static String symbols = "!@#$%^&*_=+-/.?<>)";
    static String values = "";

    int currentValue;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_copy = new Button();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(5,20);
        valueFactory.setValue(5);
        mySpinner.setValueFactory(valueFactory);

        currentValue = mySpinner.getValue();

        mySpinner.valueProperty().addListener(new javafx.beans.value.ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                currentValue = mySpinner.getValue();

            }
        });
        rootPane.setOpacity(0);
        makeFadeInTransition();
    }

    public void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public void createPassword(ActionEvent event) {
        if (values.length() > 0) {
            myTextField.setText(password(currentValue));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Null Request");
            alert.setContentText("Please Specify Parameters");
            alert.showAndWait();
        }
       // System.out.println("Generate");
    }

    public void exportToFile(ActionEvent event) throws IOException {
        String text = myTextField.getText();
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        File file = new File("passwords.txt");
        try {
            file.setWritable(true);
            file.setReadable(true);
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            pw.println("Password: " + text);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pw.close();
            bw.close();
            fw.close();
        }

        String parent = file.getAbsoluteFile().getParent();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setContentText("Password saved to Password.txt file successfully\nDirectory: " + parent);
        alert.showAndWait();
    }

    public void clearTextField(ActionEvent event) {
        myTextField.clear();
    }

    public void getParameters(ActionEvent event) {
        if (rb_lowercase.isSelected()) {
            values +=  Small_chars;
        } else {
             values = values.replaceAll("[a-z\\s]+", "");
        }
        if (rb_uppercase.isSelected()) {
            values += Capital_chars;
        } else {
            values = values.replaceAll("[A-Z\\s]+", "");
        }
        if (rb_numbers.isSelected()) {
            values += numbers;
        } else {
            values = values.replaceAll("[0-9]+", "");

        }
        if (rb_symbols.isSelected()) {
            values += symbols;
        } else {
            values = values.replaceAll("[!@#$%^&*_=+-\\.?//<>)]+", "");
        }
    }

    public void copyPassword() {
        if (myTextField.getText().length() > 0) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(myTextField.getText());
            clipboard.setContent(content);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setContentText("Password successfully copied to clipboard");
        alert.showAndWait();
    }

    static String password(int len) {
        Random rndm_method = new Random();

        char[] password = new char[len];

        for (int i = 0; i < len; i++)
        {
            password[i] =
                    values.charAt(rndm_method.nextInt(values.length()));

        }

        String pass = "";
        for (int i = 0; i < password.length; i++) {
            pass += String.valueOf(password[i]);
        }
        System.out.println(pass.length());
        return pass;
    }
}

