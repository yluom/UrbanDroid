package fr.android.urbandroid;
 
import fr.android.urbandroid.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.TextView;
 
public class DisplayTarifActivity extends Activity
{
     public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.tarif);    
     OnClickListener menuSwitcher = new OnClickListener()
     {
       public void onClick(View actualView)
       {
           Intent intent;
      	  switch(actualView.getId())
      	  {
      	  	case R.id.btn_tar: intent = new Intent(DisplayTarifActivity.this, DisplayTarifActivity.class);
      	  						startActivity(intent); break;
       	  	case R.id.btn_iti: intent = new Intent(DisplayTarifActivity.this, DisplayItineraireActivity.class);
 									startActivity(intent); break;
      	  	case R.id.btn_hor: intent = new Intent(DisplayTarifActivity.this, DisplayHorairesActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_fav: intent = new Intent(DisplayTarifActivity.this, DisplayFavorisActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_pla: intent = new Intent(DisplayTarifActivity.this, DisplayPlanActivity.class);
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
     
     
     Cursor c = Bdd.fetchAllTitles("PROFIL", new String[]{"_id","libelleprofil"}, null, null, null, null, "libelleprofil ASC");
     startManagingCursor(c);

     // Stock la colone que l'on veut afficher
     String[] from = new String[]{"libelleprofil"};
     // create an array of the display item we want to bind our data to
     int[] to = new int[]{android.R.id.text1};
     // create simple cursor adapter
     SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to );
     adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
     // get reference to our spinner
     final Spinner s = (Spinner) findViewById( R.id.spi_profil );
     s.setAdapter(adapter);
     Bdd.db.close();
     
	 final TextView tv_lib1 = (TextView) findViewById(R.id.tv_lib1);
	 final TextView tv_lib2 = (TextView) findViewById(R.id.tv_lib2);
	 final TextView tv_prix1= (TextView) findViewById(R.id.tv_prix1);
	 final TextView tv_prix2 = (TextView) findViewById(R.id.tv_prix2);
      
     s.setOnItemSelectedListener(new OnItemSelectedListener() {
    			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    				String libProfil = s.getSelectedItem().toString();
    				Cursor c = Bdd.fetchAllTitles("TARIFS, PROFIL", new String[]{"_id","nomtarif","prix"}, "tarifs.idprofil=profil.idprofil AND libelleprofil="+libProfil, null, null, null, null);
    			     startManagingCursor(c);
    			     
    			     c.moveToFirst();
    			     if(c.getCount()==0)
	    			 {} else {
	   			    	
	
	    			     int indexNoms = c.getColumnIndex("nomtarifs");
	    			     int indexTarifs = c.getColumnIndex("prix");
	    			     if(c != null && c.getCount() != 0)
	    			     {
	    			    	 String nomTarif = c.getString(indexNoms);
	    			    	 int tarifs = c.getInt(indexTarifs);
	    			    	 tv_lib1.setText(nomTarif);
	    			    	 tv_prix1.setText(tarifs);
	    			    	 c.moveToNext();
	    			    	 nomTarif = c.getString(indexNoms);
	    			    	 tarifs = c.getInt(indexTarifs);
	    			    	 tv_lib2.setText(nomTarif);
	    			    	 tv_prix2.setText(tarifs);
	    			     }
	    			     tv_lib1.setVisibility(0);
	    			     tv_lib2.setVisibility(0);
	    			     tv_prix1.setVisibility(0);
	    			     tv_prix2.setVisibility(0);
	    			     c.close();
	    			     Bdd.db.close();
    			     }
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

    	});
     
   }
}