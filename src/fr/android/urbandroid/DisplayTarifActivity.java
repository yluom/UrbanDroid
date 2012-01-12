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
import android.widget.Toast;
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
     
	 final TextView tv_nomTarif = (TextView) findViewById(R.id.tv_nomTarif);
	 final TextView tv_prix= (TextView) findViewById(R.id.tv_prix);
	 //Toast.makeText(this, s.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
	
	 
	 
	
     s.setOnItemSelectedListener(new OnItemSelectedListener() {
    			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    				Cursor item = (Cursor)(s.getSelectedItem());
    				String spinnerString = item.getString(item.getColumnIndex("libelleprofil"));
    			//	tv_nomTarif.setText(spinnerString);
    				Bdd bdd = new Bdd();
    				Cursor c2 = bdd.getCursor("TARIFS, PROFIL", new String[]{"PROFIL._id","nomtarif","prix"}, "TARIFS.idprofil=PROFIL.idprofil AND libelleprofil='"+spinnerString+"'", null, null, null, null);
    			    startManagingCursor(c2);
    			    c2.moveToFirst();
    			    int indexNoms = c2.getColumnIndex("nomtarif");
   			     	int indexTarifs = c2.getColumnIndex("prix");
    			    if(c2 != null && c2.getCount()!=0){
    			    	 String nomTarif = c2.getString(indexNoms);
    			    	 int tarifs = c2.getInt(indexTarifs);
    			    	 //Premiere ligne donc pas de récupération, pas de retour a la ligne
    			    	 tv_nomTarif.setText(nomTarif);
    			    	 tv_prix.setText(""+tarifs+"€");
    			    	  while(c2.moveToNext()){
    			    		 nomTarif = c2.getString(indexNoms);
        			    	 tarifs = c2.getInt(indexTarifs);
        			    	 //Lignes suivantes donc concaténation et retour à la ligne
        			    	 tv_nomTarif.setText(tv_nomTarif.getText()+"\n"+nomTarif);
        			    	 tv_prix.setText(tv_prix.getText()+"\n"+tarifs+"€");
    			    	  }
	    			     tv_nomTarif.setVisibility(0);
	    			     tv_prix.setVisibility(0);
	    			    
    			     }
    			     c2.close();
    			     bdd.closeDb();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

    	});
     
   }
}