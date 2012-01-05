package fr.android.urbandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.*;

public class UrbanDroidActivity extends Activity {
	
	private static final String TAG = "UrbanDroidActivity";
	TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		tv = (TextView) findViewById(R.id.loadingText);
    }
    
    @Override
    public void onStart() 
	{
    	super.onStart();
    	new Thread(new Runnable() 
    	{
    		//@Override
    		public void run() 
    		{
    			try 
    			{
    				verifBdd();
    				Thread.sleep(5000);
    				Intent intent = new Intent(UrbanDroidActivity.this, DisplayPlanActivity.class);
    			    startActivity(intent);
    			} 
    			catch (Exception ex) 
    			{
    				Log.e(TAG, Log.getStackTraceString(ex));
    			}

    		}

    	}).start();
    }
    
    public void verifBdd()
    {
    	//TODO : vérification (internet, GPS, mémoire ...)
		
        /*
         * CODE DE MISE A JOUR
         */
    	try
    	{
			// Nos "pointeurs" de fichier
			File ptrBdd = new File("/data/data/fr.android.urbandroid/files/urbdroid.bdd");
			File ptrVersion = new File("/data/data/fr.android.urbandroid/files/bdd.version");
			
			// Nos ressources utiles
			Resources res = getResources();
			String urlBdd = res.getString(R.string.urlBdd);
			String urlVersionBdd = res.getString(R.string.urlVersionBdd);
			// Cas ou il n'y a pas de bdd : premiere utilisation.
			// On download la bdd et le numero de version
			if (!ptrBdd.exists())
			{	
				ptrBdd.createNewFile();
				ptrVersion.createNewFile();
				downloadNewBdd(urlBdd, ptrBdd, urlVersionBdd, ptrVersion);
			}
			
			// Cas ou on a deja la bdd : N utilisations.
			// On download la nouvelle bdd que si il y a besoin
			else
			{
				// Comparer les versions
				tv.setText("Vérification de mise à jour...");
				int actualVersion = Integer.parseInt(this.fileContents(ptrVersion));
				int checkVersion = Integer.parseInt(this.webContents(urlVersionBdd));
				if (actualVersion < checkVersion || checkVersion == 0) //leo: Si la checkversion=0, alors on est en mode test: mise a jour a chaque lancement de l'appli!
				{
					downloadNewBdd(urlBdd, ptrBdd, urlVersionBdd, ptrVersion);
				}
				else
					tv.setText("La base de donnée est déjà à jour.");
			}
    	}
    	catch (Exception ex)
    	{
    		
    	}
    }
    
    public void downloadNewBdd(String urlBdd, File ptrBdd, String urlVersionBdd, File ptrVersion) 
    { 
    	// Téléchargement de la nouvelle base de donnée, ainsi que son numéro de version
        this.download(urlBdd, ptrBdd);
        this.download(urlVersionBdd, ptrVersion);
        tv.setText("Base de donnée mise a jour! \nBDD Version = " + this.fileContents(ptrVersion));
    }
   
    public void download(String link, File f)
    {
    	try
    	{
    		if (f.exists())
    			f.delete();
    		f.createNewFile();
	    	URL url = new URL(link);
			URLConnection conexion = url.openConnection();
			conexion.connect();
			
			// téléchargement du fichier
			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream os = new FileOutputStream(f.getPath());
			int count;
			byte data[] = new byte[1024];
			while ((count = input.read(data)) != -1) 
			{
				os.write(data, 0, count);
			}
			
			os.flush();
			os.close();
			input.close();
		
    	}
    	catch (Exception ex)
    	{
    		Toast.makeText(this, "2 :" + ex.toString(), 10).show();
    		Log.e(TAG, Log.getStackTraceString(ex));
    	}
    }
    public String fileContents(File f)
    {
    	try
    	{
            FileInputStream fStream = new FileInputStream(f);
            String res = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(fStream));
            while (in.ready())
                res += in.readLine();
            in.close();
			return res.toString();
    	}
    	catch (Exception ex)
    	{
    		Toast.makeText(this, "3 :" + ex.toString(), 10).show();
    		Log.e(TAG, Log.getStackTraceString(ex));
    	}
		return null;
    }
    public String webContents(String link)
    {
    	try
    	{
	    	URL url = new URL(link);
			URLConnection connexion = url.openConnection();
			connexion.connect();
			InputStream input = new BufferedInputStream(url.openStream());
            String res = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            while (in.ready())
                res += in.readLine();
            in.close();
            input.close();
			return res.toString();
    	}
    	catch (Exception ex)
    	{
    		Log.e(TAG, Log.getStackTraceString(ex));
    		Toast.makeText(this, "4 :" + ex.toString(), 10).show();
    	}
    	return null;
    }

}