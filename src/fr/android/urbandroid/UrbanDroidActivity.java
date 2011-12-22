package fr.android.urbandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
        setContentView(R.layout.plant);
        
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
			// On download la bdd et le numéro de version
			if (!ptrBdd.exists())
			{	
				ptrBdd.createNewFile();
				ptrVersion.createNewFile();
				this.download(urlBdd, ptrBdd);
				this.download(urlVersionBdd, ptrVersion);
				Toast.makeText(this, "New BDD downloaded! :)\nBDD Version = " + this.fileContents(ptrVersion), 10).show();
			}
			
			// Cas ou on a deja la bdd : N utilisations.
			// On download la nouvelle bdd que si il y a besoin
			else
			{
				// Comparer les versions
				int actualVersion = Integer.parseInt(this.fileContents(ptrVersion));
				int checkVersion = Integer.parseInt(this.webContents(urlVersionBdd));
				if (actualVersion < checkVersion)
				{
					this.download(urlBdd, ptrBdd);
					this.download(urlVersionBdd, ptrVersion);
					Toast.makeText(this, "New BDD downloaded! :)\nBDD Version = " + this.fileContents(ptrVersion), 10).show();
				}
				else
					Toast.makeText(this, "BDD already up to date! :)\nBDD Version = " + this.fileContents(ptrVersion), 10).show();	
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
        
        // code qui gere les transistions du menu

        OnClickListener menuSwitcher = new OnClickListener()
        {
          public void onClick(View actualView)
          {
              Intent intent;
        	  switch(actualView.getId())
        	  {
        	  	case R.id.btn_tar: intent = new Intent(UrbanDroidActivity.this, DisplayTarifActivity.class);
        	  						startActivity(intent); break;
         	  	case R.id.btn_iti: intent = new Intent(UrbanDroidActivity.this, DisplayItineraireActivity.class);
									startActivity(intent); break;
        	  	case R.id.btn_hor: intent = new Intent(UrbanDroidActivity.this, DisplayHorairesActivity.class);
        	  						startActivity(intent); break;
        	  	case R.id.btn_fav: intent = new Intent(UrbanDroidActivity.this, DisplayFavorisActivity.class);
        	  						startActivity(intent); break;
        	  	case R.id.btn_pla: intent = new Intent(UrbanDroidActivity.this, UrbanDroidActivity.class);
        	  						startActivity(intent); break;
        	  	case R.id.ongletGoogleMap: intent = new Intent(UrbanDroidActivity.this, DisplayPlanGoogleActivity.class);
					startActivity(intent); break;
        	  	case R.id.ongletTisseo: intent = new Intent(UrbanDroidActivity.this, UrbanDroidActivity.class);
					startActivity(intent); break;
        	  }
          }
        };
        ImageView iv = (ImageView) findViewById(R.id.btn_tar);
        iv.setOnClickListener(menuSwitcher);
        ImageView iv2 = (ImageView) findViewById(R.id.btn_iti);
        iv2.setOnClickListener(menuSwitcher);
        ImageView iv3 = (ImageView) findViewById(R.id.btn_hor);
        iv3.setOnClickListener(menuSwitcher);
        ImageView iv4 = (ImageView) findViewById(R.id.btn_fav);
        iv4.setOnClickListener(menuSwitcher);
        ImageView iv5 = (ImageView) findViewById(R.id.btn_pla);
        iv5.setOnClickListener(menuSwitcher);
        ImageView iv6 = (ImageView) findViewById(R.id.ongletGoogleMap);
        iv6.setOnClickListener(menuSwitcher);
        ImageView iv7 = (ImageView) findViewById(R.id.ongletTisseo);
        iv7.setOnClickListener(menuSwitcher);
        
        ImageView iv8 = (ImageView) findViewById(R.id.plantisseo);
        iv8.setOnTouchListener(new Touch());
    }
    
    public void download(String link, File f)
    {
    	try
    	{
    		if (f.exists())
    			f.delete();
    		f.createNewFile();
    		Toast.makeText(this, "Link = " + link + "\nFile = " + f.getPath() + " @ " + f.getName(), 10).show();
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
			URLConnection conexion = url.openConnection();
			conexion.connect();
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