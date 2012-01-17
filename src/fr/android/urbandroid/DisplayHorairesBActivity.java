package fr.android.urbandroid;
 
import java.text.DateFormat;
import java.util.Date;

import fr.android.urbandroid.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class DisplayHorairesBActivity extends Activity
{
	private static final String TAG = "UrbanDroidActivity";
     public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.horairesb);
     
     OnClickListener menuSwitcher = new OnClickListener()
     {
       public void onClick(View actualView)
       {
           Intent intent;
      	  switch(actualView.getId())
      	  {
      	  	case R.id.btn_tar: intent = new Intent(DisplayHorairesBActivity.this, DisplayTarifActivity.class);
      	  						startActivity(intent); break;
       	  	case R.id.btn_iti: intent = new Intent(DisplayHorairesBActivity.this, DisplayItineraireActivity.class);
 									startActivity(intent); break;
      	  	case R.id.btn_hor: intent = new Intent(DisplayHorairesBActivity.this, DisplayHorairesActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_fav: intent = new Intent(DisplayHorairesBActivity.this, DisplayFavorisActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_pla: intent = new Intent(DisplayHorairesBActivity.this, DisplayPlanActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.ongletMetro: intent = new Intent(DisplayHorairesBActivity.this, DisplayHorairesActivity.class);
								startActivity(intent); break;
      	  	case R.id.ongletBus: intent = new Intent(DisplayHorairesBActivity.this, DisplayHorairesBActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.ongletTram: intent = new Intent(DisplayHorairesBActivity.this, DisplayHorairesTActivity.class);
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
     ImageView iv6 = (ImageView) findViewById(R.id.ongletBus);
     iv6.setOnClickListener(menuSwitcher);
     ImageView iv7 = (ImageView) findViewById(R.id.ongletTram);
     iv7.setOnClickListener(menuSwitcher);
     ImageView iv8 = (ImageView) findViewById(R.id.ongletMetro);
     iv8.setOnClickListener(menuSwitcher);
     
     
     Cursor c = Bdd.fetchAllTitles("LIGNES", new String[]{"_id","nomligne"}, "idligne LIKE 'B%'", null, null, null, "nomligne ASC");
     startManagingCursor(c);
     // Stock la colone que l'on veut afficher
     String[] from = new String[]{"nomligne"};
     // create an array of the display item we want to bind our data to
     int[] to = new int[]{android.R.id.text1};
     // create simple cursor adapter
     SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to );
     adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
     // get reference to our spinner
     final Spinner spiLigne = (Spinner) findViewById( R.id.spinnerLigne );
     final Spinner spiStation = (Spinner) findViewById( R.id.spinnerStation );
     spiLigne.setAdapter(adapter); 
     Bdd.db.close();
     

     spiLigne.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
			     try {
					    Cursor item = (Cursor)(spiLigne.getSelectedItem());
						String spinnerString = item.getString(item.getColumnIndex("nomligne"));
						Bdd bdd = new Bdd();
						Cursor c2 = bdd.getCursor("STATIONS, DESSERT, LIGNES, HORAIRES", new String[]{"distinct STATIONS._id","nomstation"}, "DESSERT.idstation=STATIONS._id AND DESSERT.idligne=LIGNES.idligne AND HORAIRES.idstation=STATIONS._id AND nomligne ='"+spinnerString+"'", null, null, null, "position asc");
					    startManagingCursor(c2);
					     // Stock la colonne que l'on veut afficher
					     String[] from = new String[]{"nomstation"};
					     // create an array of the display item we want to bind our data to
					     int[] to = new int[]{android.R.id.text1};
					     // create simple cursor adapter
					     SimpleCursorAdapter adapter = new SimpleCursorAdapter(parent.getContext(), android.R.layout.simple_spinner_item, c2, from, to );
					     adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
					     // get reference to our spinner
					     //final Spinner spiStation = (Spinner) findViewById( R.id.spinnerStation );
					     spiStation.setAdapter(adapter);
					     bdd.closeDb();
			     } catch (Exception ex)
			 	 {
			 		Log.e(TAG, Log.getStackTraceString(ex));
			 	 }
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}

	});
     
     final TextView tv_horaires = (TextView) findViewById(R.id.tv_horaires);
     
     spiStation.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
			     try {
					    Cursor item = (Cursor)(spiStation.getSelectedItem());
					    //Log.e(TAG, item.getString(item.getColumnIndex("nomstation")));
						String spinnerString = item.getString(item.getColumnIndex("nomstation"));
						
						Bdd bdd = new Bdd();
						// top5 des prochains horaires pour une station donnée avec l'heure actuelle
						Cursor c2 = bdd.getCursor("HORAIRES,STATIONS", new String[]{"HORAIRES._id", "heurepassage"}, "STATIONS._id=HORAIRES.idstation AND heurepassage > time(strftime('%H:%M:%S'), 'localtime') AND nomStation ='"+spinnerString+"' LIMIT 5 ", null, null, null, null);
						startManagingCursor(c2);
						//c2.moveToFirst();
						c2.moveToPosition(0);
						int indexHoraire = c2.getColumnIndex("heurepassage");
		   			    int indexTmpsRestant = c2.getColumnIndex("prix");
	    			    if(c2 != null && c2.getCount()!=0){
	    			    	 Time nextHoraire = new Time();
	    			    	 Time now = new Time();
	    			    	 now.setToNow();
	    			    	 nextHoraire.set(now.second, Integer.parseInt(c2.getString(indexHoraire).substring(4, 5)), Integer.parseInt(c2.getString(indexHoraire).substring(0, 1)), now.monthDay, now.month, now.year);
	    			    	 Log.d(TAG, "nextHoraire=" + nextHoraire.format("%H:%M"));
	    			    	 // On va avoir besoin de l'heure courante
	    			    	/* Time tempsRestant = new Time();
	    			    	 tempsRestant.set(nextHoraire.toMillis(true)-System.currentTimeMillis());
	    			    	 tv_horaires.setText("Dans "+tempsRestant+ " à "+nextHoraire.toString());
	    			    	 while(c2.moveToNext()){
	    			    		 tempsRestant.set(nextHoraire.toMillis(true)-System.currentTimeMillis());
		    			    	 tv_horaires.setText(tv_horaires.getText()+"\n"+"Dans "+tempsRestant+ " à "+nextHoraire.toString());	    			    		 
	    			    	 }*/
		    			     tv_horaires.setVisibility(0);
		    			    
	    			     }
						
						
						
					     bdd.closeDb();
			     } catch (Exception ex)
			 	 {
			 		Log.e(TAG, Log.getStackTraceString(ex));
			 	 }
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}

	});
     
   }
}