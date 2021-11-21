package com.dassoop.awsec2utility;

import com.dassoop.awsec2utility.models.Instance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable
{

    @FXML
    ListView<Instance> listView = new ListView<>();
    //Instance info section
    @FXML
    Label lblName = new Label();
    @FXML
    Label lblPublicIp4v = new Label();
    @FXML
    javafx.scene.control.TextField lblPublicIpDNS = new javafx.scene.control.TextField();
    @FXML
    javafx.scene.control.TextField lblKeypair = new javafx.scene.control.TextField();
    @FXML
    javafx.scene.control.TextField lblSSH = new javafx.scene.control.TextField();
    //SCP section
    @FXML
    javafx.scene.control.TextField lblFile = new javafx.scene.control.TextField();
    @FXML
    javafx.scene.control.TextField lblSCP = new javafx.scene.control.TextField();
    //Add instance section
    @FXML
    javafx.scene.control.TextField formName = new javafx.scene.control.TextField();
    @FXML
    javafx.scene.control.TextField formIP = new javafx.scene.control.TextField();
    @FXML
    javafx.scene.control.TextField formDNS = new javafx.scene.control.TextField();
    @FXML
    javafx.scene.control.TextField formKeypair = new javafx.scene.control.TextField();
    @FXML
    Label lblStatus = new Label();

    static ArrayList<Instance> instanceList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Initiate default values from config.txt preferences
        initDefaultValues();
        //Set list view cells to custom InstanceCell class
        listView.setCellFactory(param -> new InstanceCell());

        //Select first list item (if present) on startup and load its label info
        listView.getSelectionModel().selectFirst();
        Instance selectedInstance = listView.getSelectionModel().getSelectedItem();
        if(selectedInstance != null)
        {
            setLabels(selectedInstance);
        }

        //Set on mouse clicked event for instance list view
        listView.setOnMouseClicked(mouseEvent -> {
            Instance selectedInstance1 = listView.getSelectionModel().getSelectedItem();
            if(selectedInstance1 != null)
            {
                setLabels(selectedInstance1);
                lblFile.clear();
                lblSCP.clear();
                lblStatus.setText("");
            }
        });
    }

    public void setLabels(Instance selectedInstance)
    {
        lblName.setText(selectedInstance.getName());
        lblPublicIp4v.setText(selectedInstance.getPublicIP4v());
        lblPublicIpDNS.setText(selectedInstance.getPublicIP4vDNS());
        lblKeypair.setText(selectedInstance.getKeypair());
        String ssh = "ssh -i " + lblKeypair.getText() + " ec2-user@" + lblPublicIpDNS.getText();
        lblSSH.setText(ssh);
    }

    //Choose folder to generate SCP command for
    public void chooseDirectory(ActionEvent event)
    {
        DirectoryChooser dirChooser = new DirectoryChooser();

        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();

        File dir = dirChooser.showDialog(thisStage);

        if(dir == null) return;
        lblFile.setText(dir.getAbsolutePath());

        String scp = "scp -i " + lblKeypair.getText() + " -r " + lblFile.getText() + " ec2-user@" + lblPublicIp4v.getText() + ":~/";
        lblSCP.setText(scp);
    }
    //Choose file to generate SCP command for
    public void chooseFile(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();

        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();

        File file = fileChooser.showOpenDialog(thisStage);
        if(file == null) return;
        lblFile.setText(file.getAbsolutePath());

        String scp = "scp -i " + lblKeypair.getText() + " " + lblFile.getText() + " ec2-user@" + lblPublicIp4v.getText() + ":~/";
        lblSCP.setText(scp);
    }
    //Choose keypair.pem file for instance
    public void chooseKeypair(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();

        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();

        File file = fileChooser.showOpenDialog(thisStage);
        if(file == null) return;
        formKeypair.setText(file.getAbsolutePath());
    }

    public void createInstance()
    {
        Instance newInstance = new Instance();
        String name = formName.getText();
        if(name.isEmpty())
        {
            newInstance.setName("Instance");
        }
        else
        {
            newInstance.setName(formName.getText());
        }
        newInstance.setPublicIP4v(formIP.getText());
        newInstance.setPublicIP4vDNS(formDNS.getText());
        newInstance.setKeypair(formKeypair.getText());

        formName.clear();
        formIP.clear();
        formDNS.clear();
        formKeypair.clear();

        instanceList.add(newInstance);
        listView.getItems().setAll(instanceList);
        Preferences.initConfig();
    }

    public void deleteInstance()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Remove Instance");
        alert.setHeaderText("Remove Instance?");
        alert.setContentText("This cannot be undone, are you sure?");
        alert.getDialogPane().setStyle("-fx-font-family: sans-serif");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Instance selectedInstance = listView.getSelectionModel().getSelectedItem();
            instanceList.remove(selectedInstance);
            listView.getItems().setAll(instanceList);

            lblName.setText("");
            lblPublicIp4v.setText("");
            lblPublicIpDNS.setText("");
            lblKeypair.setText("");
            lblSSH.setText("");
            lblStatus.setText("");

            lblFile.clear();
            lblSCP.clear();
            Preferences.initConfig();
        }
        else
        {
            return;
        }
    }

    //Copy Button Functions
    public void copyPubIp()
    {
        String copyText = lblPublicIp4v.getText();
        copyHelper(copyText);
        lblStatus.setText("Copied PublicIP4v Address to clipboard.");
    }
    public void copyPubIpDNS()
    {
        String copyText = lblPublicIpDNS.getText();
        copyHelper(copyText);
        lblStatus.setText("Copied PublicIP4v DNS to clipboard.");
    }
    public void copySSH()
    {
        String copyText = lblSSH.getText();
        copyHelper(copyText);
        lblStatus.setText("Copied SSH command to cliboard.");
    }
    public void copySCP()
    {
        String copyText = lblSCP.getText();
        copyHelper(copyText);
        lblStatus.setText("Copied SCP command to cliboard.");
    }

    public void copyHelper(String copyText)
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection strSel = new StringSelection(copyText);
        clipboard.setContents(strSel, null);
    }

    public ArrayList<Instance> convertArray(Instance[] list)
    {
        ArrayList<Instance> arrList = new ArrayList<Instance>();
        for(int i = 0; i < list.length; i++)
        {
            arrList.add(list[i]);
        }
        return arrList;
    }

    private void initDefaultValues()
    {
        Preferences preferences = Preferences.getPreferences();
        System.out.println(preferences.instanceList.length);

        Instance[] list = preferences.instanceList;
        ArrayList<Instance> arrList = convertArray(list);
        instanceList = arrList;
        listView.getItems().setAll(instanceList);
    }
}