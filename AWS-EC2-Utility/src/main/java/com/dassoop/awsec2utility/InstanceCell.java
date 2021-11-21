package com.dassoop.awsec2utility;

import com.dassoop.awsec2utility.models.Instance;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;

public class InstanceCell extends ListCell<Instance>
{
    private String cellString = null;

    //Contructor
    public InstanceCell()
    {

    }

    @Override
    protected void updateItem(Instance instance, boolean empty)
    {
        //Run the super class update
        super.updateItem(instance, empty);
        if(empty || instance == null)
        {
            setText(null);
        }

        //get list from list view
        ObservableList<Instance> items = getListView().getItems();

        if(instance != null)
        {

            cellString = items.get(getIndex()).getName();
            setText(cellString);
        }
    }


}


