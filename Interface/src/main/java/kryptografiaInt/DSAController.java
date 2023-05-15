package kryptografiaInt;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kryptografia.DSA;

import java.io.File;
import java.math.BigInteger;

import static kryptografia.OperationsWithFiles.*;

public class DSAController {
    public TextField textFieldPrivateKey, textFieldPublicKey, textFieldG, textFieldQ, textFieldP, textFieldS, textFieldR;
    public TextArea textAreaText, textAreaSignature;
    private Stage stage;
    private byte[] fileBytes;
    //private File wczytanyPlik, zapisanyPlik;
    private BigInteger[] signature;
    private boolean verification;
    private final DSA DSA = new DSA();
    public BigInteger[] values;         //q, p, h, g, a, b
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void onGenerateKeysButtonClick() {
        values = DSA.generateKeys();
        textFieldQ.setText(String.valueOf(values[0]));
        textFieldP.setText(String.valueOf(values[1]));
        textFieldG.setText(String.valueOf(values[3]));
        textFieldPrivateKey.setText(String.valueOf(values[4]));
        textFieldPublicKey.setText(String.valueOf(values[5]));
    }

    @FXML
    protected void onLoadTextFileButtonClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
        try {
            File file = fileChooser.showOpenDialog(stage);
            String sciezka = file.getPath();
            fileBytes = readFileBytesDSA(sciezka);
            textAreaText.setText(new String(fileBytes));
         }
        catch (Exception exception) {
            throw new Exception("File could not be read");
        }
    }
    @FXML
    protected void onLoadSignatureFileButtonClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
        try {
            File file = fileChooser.showOpenDialog(stage);
            String sciezka = file.getPath();
            signature = readFileBigIntegerDSA(sciezka);
            textAreaSignature.setText(signature[0] + " " + signature[1]);
            textFieldR.setText(String.valueOf(signature[0]));
            textFieldS.setText(String.valueOf(signature[1]));
        }
        catch (Exception exception) {
            throw new Exception("File could not be read");
        }
    }
    @FXML
    protected void onSaveTextFileButtonClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
        try {
            File file = fileChooser.showSaveDialog(stage);
            String sciezka = file.getPath();
            File zapisanyPlik = new File(sciezka);
            writeFileDSA(sciezka, textAreaText.getText().getBytes());
        } catch (Exception exception) {
            throw new Exception("File could not be write");
        }
    }

    @FXML
    protected void onSaveSignatureFileButtonClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
        try {
            File file = fileChooser.showSaveDialog(stage);
            String sciezka = file.getPath();
            writeFileDSA(sciezka, textAreaSignature.getText().getBytes());
        } catch (Exception exception) {
            throw new Exception("File could not be write");
        }
    }


    @FXML
    protected void onSignButtonClick() {
        signature = DSA.sign(textAreaText.getText().getBytes());
        textAreaSignature.setText(signature[0] + " " + signature[1]);
        textFieldR.setText(String.valueOf(signature[0]));
        textFieldS.setText(String.valueOf(signature[1]));
    }

    @FXML
    protected void onVerifyButtonClick(){
        String tab[]=(textAreaSignature.getText().split(" "));
        verification = DSA.verify(textAreaText.getText().getBytes(), tab[0], tab[1]);
        if (verification) {
            new Alert(Alert.AlertType.CONFIRMATION, "The signature is correct").show();
        }
        else new Alert(Alert.AlertType.ERROR, "The signature isn't correct").show();
    }


    public void onChangePrivateKey(KeyEvent keyEvent) {
        DSA.setA(new BigInteger(textFieldPrivateKey.getText()));
    }

    public void onChangePublicKey(KeyEvent keyEvent) {
        DSA.setB(new BigInteger(textFieldPublicKey.getText()));
    }

    public void onChangeQ(KeyEvent keyEvent) {
        DSA.setQ(new BigInteger(textFieldQ.getText()));
    }

    public void onChangeP(KeyEvent keyEvent) {
        DSA.setP(new BigInteger(textFieldP.getText()));
    }

    public void onChangeG(KeyEvent keyEvent) {
        DSA.setG(new BigInteger(textFieldG.getText()));
    }
}