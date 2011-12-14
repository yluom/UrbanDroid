package fr.android.urbandroid;

import java.io.*;
import java.net.*;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class UrbanDroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try
        {
	    	super.onCreate(savedInstanceState);
	        setContentView(R.layout.plant);
	        
	        
	       /* CODE DE LA MISE A JOUR !!! 
	        * 
	        * TODO= regler la levée d'exception "unknowhostexception" de nixitup
	        * 
	          File f = new File("/data/data/fr.android.urbandroid/files/urbdroid.bdd");
	        
	        if (!f.exists())
	        {
	        	Resources res = getResources();
	        	String urlBdd = res.getString(R.string.urlBdd);
	        	
	        	 URL url = new URL(urlBdd);
	             URLConnection conexion = url.openConnection();
	             conexion.connect();
	             // this will be useful so that you can show a tipical 0-100% progress bar
	             int lenghtOfFile = conexion.getContentLength();

	             // download the file
	             InputStream input = new BufferedInputStream(url.openStream());
	        	 f.createNewFile();
	        	 BufferedOutputStream bis = new BufferedOutputStream(new FileOutputStream(f));
	             int count;
	             byte data[] = new byte[1024];
	             while ((count = input.read(data)) != -1) 
	             {
	            	 bis.write(data);
	             }

        		bis.flush();
        		bis.close();
        		input.close();
        		
	        	//afficher pas ok
	        }
	        else
	        {
	        	//afficher contenu
	        	//Toast.makeText(this, "Djir, le fichier existe!", 10).show();
	        	
	        }*/
	
	        
	        // code qui gere les transistions du menu
	
	        OnClickListener menuSwitcher = new OnClickListener()
	        {
	          @Override
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
	        	  	case R.id.ongletGoogleMap: setContentView(R.layout.plang); break;
	        	  	case R.id.ongletTisseo: setContentView(R.layout.plant); break;
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
        
        }
        catch (Exception ex)
        {
        	Toast.makeText(this, "Djir : " + ex.toString(), 10).show();
        }
    }
}