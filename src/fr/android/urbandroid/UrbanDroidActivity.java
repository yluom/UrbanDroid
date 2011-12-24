package fr.android.urbandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.*;


public class UrbanDroidActivity extends Activity {
	
	private static final String TAG = "UrbanDroidActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        //LEO: ici, il serait utile de faire les verifications d'usage (exemple: est ce que y'a le net , le gps...)
        
        
        
        /*
         * CODE DE MISE A JOUR
         */
		// Nos "pointeurs" de fichier
		File ptrBdd = new File("/data/data/fr.android.urbandroid/files/urbdroid.bdd");
		File ptrVersion = new File("/data/data/fr.android.urbandroid/files/bdd.version");
		
		// Nos ressources utiles
		Resources res = getResources();
		String urlBdd = res.getString(R.string.urlBdd);
		String urlVersionBdd = res.getString(R.string.urlVersionBdd);
		try
		{
			// Cas ou il n'y a pas de bdd : premiere utilisation.
			// On download la bdd et le num�ro de version
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
				int actualVersion = Integer.parseInt(this.fileContents(ptrVersion));
				int checkVersion = Integer.parseInt(this.webContents(urlVersionBdd));
				if (actualVersion < checkVersion || checkVersion == 0) //leo: Si la checkversion=0, alors on est en mode test: mise a jour a chaque lancement de l'appli!
				{
					downloadNewBdd(urlBdd, ptrBdd, urlVersionBdd, ptrVersion);
				}
				else
					Toast.makeText(this, "La base de donnée est déja a jour!", 10).show();	
			}
		}
		catch (Exception ex)
		{
			Toast.makeText(this, "1 :" + ex.toString(), 10).show();
			Log.e(TAG, Log.getStackTraceString(ex));
		}

		/*
		 * FIN DE LA MISE A JOUR
		 */
        
		// Mise � jour termin�, A COMMENTER si vous voulez voir la progressDialog infinie ..
		Intent intent = new Intent(UrbanDroidActivity.this, DisplayPlanActivity.class);
	    startActivity(intent);
    }
    
    public void downloadNewBdd(String urlBdd, File ptrBdd, String urlVersionBdd, File ptrVersion) 
    { 
    	// Affichage de la progressDialog de mise a jour
    	final ProgressDialog myProgressDialog = ProgressDialog.show(UrbanDroidActivity.this, "Veuillez patientez...", "Mise à jour de la base de donnée...", true);
    	
    	
    	// Téléchargement de la nouvelle base de donnée, ainsi que son numéro de version
        this.download(urlBdd, ptrBdd);
        this.download(urlVersionBdd, ptrVersion);
        
        /* *************************************
        TODO ici: sleeper(2secondes) afin que l'on voit la ProgressDialog, puis la .dismiss() après le sleep...
        ********************************************        */

        // leo: Ce bout de code est sencé nous faire attendre 2 secondes, puis effacer la progressdialog avec dismiss ....
        // MAIS CA MARCHE PAS ! ^_^  
        // Visuellement, on ne vois pas la progressDIalog:
        // car elle est showée avec ProgressDialog.show() et elle est détruite avec .dismiss() directement après (vu que le sleep marche pas)
        // 
        new Thread() {
			public void run() {
				try {
					Thread.sleep(2000); // ça c'est sensé nous faire patienter pendant 2 secondes, afin que l'utilisateur vois la progressDialog
				} catch (InterruptedException e) {
					e.printStackTrace();	
				}
			
			}
		}.start();

        
        // A la fin du traitement, on fait disparaitre notre message
        myProgressDialog.dismiss(); // avec ça on détruit la progressdialog
		Toast.makeText(this, "Base de donnée mise a jour! \nBDD Version = " + this.fileContents(ptrVersion), 20).show();
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
			
			// t�l�chargement du fichier
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