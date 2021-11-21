package com.dassoop.awsec2utility;

import com.dassoop.awsec2utility.models.Instance;

import java.io.*;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preferences
{
    private static final String CONFIG_FILE = "config.txt";
    Instance[] instanceList;

    public Preferences()
    {
//        Get file path of jar
//        File jarPath = new File(MainController.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//        String jarDirpath = jarPath.getParent();
//        System.out.println(jarDirpath);

        ArrayList<Instance> instanceData = MainController.instanceList;
        Instance[] instanceArr = convertListToArray(instanceData);
        instanceList = instanceArr;
    }

    //Init config
    public static void initConfig()
    {
        Writer writer = null;
        try
        {
            Preferences preference = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
        }
        catch(IOException e)
        {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, e);
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch(IOException e)
            {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public static Preferences getPreferences()
    {
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try
        {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        }
        catch(FileNotFoundException e)
        {
            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, e);
        }
        return preferences;
    }

    public Instance[] convertListToArray(ArrayList<Instance> instanceList)
    {
        Instance[] instanceArr = new Instance[instanceList.size()];

        for(int i = 0; i < instanceArr.length; i++)
        {
            instanceArr[i] = instanceList.get(i);
        }

        return instanceArr;
    }
}
