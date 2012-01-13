package fr.android.urbandroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OpenPdf extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openpdf);

        Button button = (Button) findViewById(R.id.btn_pdf);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File file = new File("/sdcard/example.pdf"); //http://www.tisseo.fr/sites/default/files/Tisseo_hiv03web.pdf

                if (file.exists()) {
                    Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(intent);
                    } 
                    catch (ActivityNotFoundException e) {
                        Toast.makeText(OpenPdf.this, 
                            "No Application Available to View PDF", 
                            Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    
    /////////////////////////
    public void verifBdd()
    {
        /*
         * CODE DE MISE A JOUR
         */
    	try
    	{
			// Nos "pointeurs" de fichier
			File ptrBdd = new File("/data/data/fr.android.urbandroid/urbdroid.db");
			
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
    	try {
	    	// Téléchargement de la nouvelle base de donnée, ainsi que son numéro de version
	        this.download(urlBdd, ptrBdd);
	        this.download(urlVersionBdd, ptrVersion);
        	chmod(ptrBdd, 0666);
        } catch (Exception ex) {
        	Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
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
    		Toast.makeText(this, "2 :" + ex.toString(), Toast.LENGTH_LONG).show();
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
    		Toast.makeText(this, "3 :" + ex.toString(), Toast.LENGTH_LONG).show();
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
    		Toast.makeText(this, "4 :" + ex.toString(), Toast.LENGTH_LONG).show();
    	}
    	return null;
    }
    
    // Utilisé pour modifier le chmod du fichier .bdd
    public int chmod(File path, int mode) throws Exception {
    	  Class fileUtils = Class.forName("android.os.FileUtils");
    	  Method setPermissions =
    	      fileUtils.getMethod("setPermissions", String.class, int.class, int.class, int.class);
    	  return (Integer) setPermissions.invoke(null, path.getAbsolutePath(), mode, -1, -1);
    	}
    //////////
    
    
    
}