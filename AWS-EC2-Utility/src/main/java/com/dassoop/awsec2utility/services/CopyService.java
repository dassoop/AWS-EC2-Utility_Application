package com.dassoop.awsec2utility.services;

import com.dassoop.awsec2utility.Main;
import com.dassoop.awsec2utility.MainController;
import com.dassoop.awsec2utility.models.Instance;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class CopyService
{
    MainController controller;

    @FXML
    ListView<Instance> listView = new ListView<>();

    public CopyService() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        controller = loader.getController();
    }


    //Main function to copy from individual labels and set response text in status line.
    public void copyCommand(String command)
    {
        Instance selectedInstance = listView.getSelectionModel().getSelectedItem();
        String copyText = "";
        String labelStatus = "";
        switch (command.toLowerCase())
        {
            case "pubip":
                copyText = controller.lblPublicIp4v.getText();
                copyHelper(copyText);
                labelStatus = " PublicIP4 to clipboard.";
                break;
            case "pubipdns":
                copyText = controller.lblPublicIpDNS.getText();
                copyHelper(copyText);
                labelStatus = " PublicIP4v DNS to clipboard.";
                break;
            case "ssh":
                copyText = controller.lblSSH.getText();
                copyHelper(copyText);
                labelStatus = " SSH command to cliboard.";
                break;
            case "scp":
                copyText = controller.lblSCP.getText();
                copyHelper(copyText);
                labelStatus = " SCP command to cliboard.";
                break;
        }
        controller.lblStatus.setText("Copied " + selectedInstance.getName() + labelStatus);
    }

    //Copy to clipboard
    public void copyHelper(String copyText)
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection strSel = new StringSelection(copyText);
        clipboard.setContents(strSel, null);
    }
}
