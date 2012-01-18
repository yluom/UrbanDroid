package fr.android.urbandroid;
 
import fr.android.urbandroid.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class DisplayHorairesTActivity extends Activity
{
	
 	public static int idTerminus;
	public final static String nomLigne = "Arenes - Aeroconstellation";
	public final static String idLigne = "T1";
	public final String TAG = "DisplayHoraireTActivity";
	
     public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.horairest);
     final TextView t_tv_sens = (TextView)findViewById(R.id.t_tv_sens);

     
     OnClickListener menuSwitcher = new OnClickListener()
     {
       public void onClick(View actualView)
       {
           Intent intent;
      	  switch(actualView.getId())
      	  {
      	  	case R.id.btn_tar: intent = new Intent(DisplayHorairesTActivity.this, DisplayTarifActivity.class);
      	  						startActivity(intent); break;
       	  	case R.id.btn_iti: intent = new Intent(DisplayHorairesTActivity.this, DisplayItineraireActivity.class);
 									startActivity(intent); break;
      	  	case R.id.btn_hor: intent = new Intent(DisplayHorairesTActivity.this, DisplayHorairesActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_fav: intent = new Intent(DisplayHorairesTActivity.this, DisplayFavorisActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_pla: intent = new Intent(DisplayHorairesTActivity.this, DisplayPlanActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.ongletMetro: intent = new Intent(DisplayHorairesTActivity.this, DisplayHorairesActivity.class);
								startActivity(intent); break;
      	  	case R.id.ongletBus: intent = new Intent(DisplayHorairesTActivity.this, DisplayHorairesBActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.ongletTram: intent = new Intent(DisplayHorairesTActivity.this, DisplayHorairesTActivity.class);
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
     
     final Spinner t_spiStation = (Spinner) findViewById( R.id.t_spinnerStation );
     
     	// Remplissage du t_spinnerStation
		Bdd bdd = new Bdd();
		Cursor c2 = bdd.getCursor("STATIONS, DESSERT, LIGNES, HORAIRES", new String[]{"distinct STATIONS._id","nomstation"}, "DESSERT.idstation=STATIONS._id AND DESSERT.idligne=LIGNES.idligne AND HORAIRES.idstation=STATIONS._id AND nomligne ='"+nomLigne+"'", null, null, null, "nomstation asc");
		startManagingCursor(c2);
	    if(c2.getCount() == 0)
	     {
	    	 Log.e(TAG, "count(c2) = 0 !!!");
	     }
		 // Stock la colonne que l'on veut afficher
		 String[] from = new String[]{"nomstation"};
		 // create an array of the display item we want to bind our data to
		 int[] to = new int[]{android.R.id.text1};
		 // create simple cursor adapter
		 SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c2, from, to );
		 adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
		 // get reference to our spinner
		 t_spiStation.setAdapter(adapter);
		 bdd.closeDb();
		 stopManagingCursor(c2);
					     
     t_spiStation.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
				
				//Recupération de l'id du premier terminus (ex : 2 ) et du nom de cette station (ex : Paul Sabatier)
			     Bdd bdd = new Bdd();
			     Cursor c3 = bdd.getCursor("STATIONS, DESSERT, LIGNES", new String[]{"STATIONS._id","nomstation"}, "DESSERT.idstation=STATIONS._id AND LIGNES.idligne =DESSERT.idligne AND position = 1 AND nomligne ='"+nomLigne+"'", null, null, null, null);
			     startManagingCursor(c3);
			     c3.moveToFirst();
			 
			     int indexTerminus = c3.getColumnIndex("_id");
			     int indexNomStation = c3.getColumnIndex("nomstation");
			     String nomStation = c3.getString(indexNomStation);
			     idTerminus = c3.getInt(indexTerminus);
			     Log.d(TAG, "idTermi=" + idTerminus);
			     t_tv_sens.setText(Html.fromHtml("Direction: <b>"+nomStation+"</b>"));
			     
			     afficherHoraires(t_spiStation, idTerminus);
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}

	});
     
     final ToggleButton t_btn_switch=(ToggleButton)findViewById(R.id.t_btn_switch);
     
     t_btn_switch.setOnClickListener(new OnClickListener()
     {
	    
		    public void onClick(View v) {
		    
		    try {
			    //	ToggleButton btn_swich=(Button)v;
			    	String nomStation = null;
			    	int idTerm = 0;
			    	Bdd bdd = new Bdd();
			    	Log.d(TAG, "idLigne BIG REQUETE : =" + idLigne);
			    	Cursor c5 = bdd.getCursor("STATIONS, DESSERT", new String[]{"distinct STATIONS._id","nomstation"}, "DESSERT.idstation = STATIONS._id AND idligne = '"+idLigne+"' AND position = (SELECT MAX(position) FROM DESSERT WHERE idligne = '"+idLigne+"') OR nomstation = (SELECT nomstation FROM DESSERT, STATIONS WHERE DESSERT.idstation = STATIONS._id AND idligne = '"+idLigne+"' AND position = 1)", null, null, null, "position asc");
			    	startManagingCursor(c5);
				    c5.moveToFirst();
			    	
				    int indexIdTerm= c5.getColumnIndex("_id");
				    Log.d(TAG, "indexNomStation=" + indexIdTerm);
				    int indexNomStation= c5.getColumnIndex("nomstation");
				    Log.d(TAG, "indexNomStation=" + indexNomStation);
		
			    	if(t_btn_switch.isChecked())
			    	{
			    		c5.moveToPosition(0);
			    		idTerm = c5.getInt(indexIdTerm);
			    		Log.d(TAG, "idLigne Cheked =" + idTerm);
			    		Log.d(TAG, "idLigne Cheked =" + idLigne);
					    nomStation = c5.getString(indexNomStation);
					    Log.d(TAG, "nomStation Cheked =" + nomStation);
			    		Log.d(TAG, "Clic sur SWITCH UNChecked");
			    		
			    	 
			    	} else {
			    		
			    		c5.moveToPosition(1);
			    		idTerm = c5.getInt(indexIdTerm);
			    		Log.d(TAG, "idLigne Cheked =" + idTerm);
					    nomStation = c5.getString(indexNomStation);
					    Log.d(TAG, "nomStation Cheked =" + nomStation);
			    		Log.d(TAG, "Clic sur SWITCH Checked");
			    		
			    	}
			    	
			    	t_tv_sens.setText(Html.fromHtml("Direction: <b>"+nomStation+"</b>"));
			    	 Log.d(TAG, "AVANT afficherHoraires(t_spiStation, idTerm);");
			    	
			    	afficherHoraires(t_spiStation, idTerm);
			    	 Log.d(TAG, "APRES afficherHoraires(t_spiStation, idTerm);");
		     } catch (Exception ex) {
		    	 Log.e(TAG, Log.getStackTraceString(ex));
		     }
		    }

     }
     );
}

    public void afficherHoraires(Spinner t_spiStation, int idTerm){
    	 try {
    		 Log.e(TAG, "Entree dans afficherHoraires");
    		 TextView t_tv_hor = (TextView) findViewById(R.id.t_tv_hor);
			    Cursor item = (Cursor)(t_spiStation.getSelectedItem());
			    //Log.e(TAG, item.getString(item.getColumnIndex("nomstation")));
				String spinnerString = item.getString(item.getColumnIndex("nomstation"));
				
				Bdd bdd = new Bdd();
				Log.e(TAG, "idterm="+idTerm);
				Log.e(TAG, "nomstation="+spinnerString);
				// top5 des prochains horaires pour une station donnée avec l'heure actuelle
				Cursor c2 = bdd.getCursor("HORAIRES,STATIONS", new String[]{"HORAIRES._id", "heurepassage"}, "STATIONS._id=HORAIRES.idstation AND heurepassage > time(strftime('%H:%M:%S'), 'localtime') AND direction="+ idTerm +" AND nomStation ='"+spinnerString+"' LIMIT 5 ", null, null, null, null);
				startManagingCursor(c2);
				c2.moveToFirst();
				int indexHoraire = c2.getColumnIndex("heurepassage");
			    if(c2 != null && c2.getCount()!=0){
			    	 Time nextHoraire = new Time();
			    	 // On va avoir besoin de l'heure courante
			    	 Time now = new Time();
			    	 now.setToNow();
			    	 nextHoraire.set(now.second, Integer.parseInt(c2.getString(indexHoraire).substring(3, 5)), Integer.parseInt(c2.getString(indexHoraire).substring(0, 2)), now.monthDay, now.month, now.year);
			    	 Log.d(TAG, "getString=" + c2.getString(indexHoraire));
			    	 Log.d(TAG, "nextHoraire=" + nextHoraire.format("%H:%M"));
			    	 
			    	 Time tempsRestant = new Time();
			    	 tempsRestant.set(nextHoraire.toMillis(false)-System.currentTimeMillis()-3600000);
			    	 Log.d(TAG, "TEMPSRESTANT=" + tempsRestant.format("%H:%M"));
			    	 t_tv_hor.setText("Dans "+tempsRestant.format("%H:%M")+ " à "+nextHoraire.format("%H:%M"));
			    	 while(c2.moveToNext()){
			    			Log.e(TAG, "Boucle While)");
			    		 nextHoraire.set(now.second, Integer.parseInt(c2.getString(indexHoraire).substring(3, 5)), Integer.parseInt(c2.getString(indexHoraire).substring(0, 2)), now.monthDay, now.month, now.year);
			    		 tempsRestant.set(nextHoraire.toMillis(false)-System.currentTimeMillis()-3600000);
 			    	 t_tv_hor.setText(t_tv_hor.getText()+"\n"+"Dans "+tempsRestant.format("%H:%M")+ " à "+nextHoraire.format("%H:%M"));	    			    		 
			    	 }
 			     t_tv_hor.setVisibility(0);
 			    
			     }
				
			    stopManagingCursor(c2);
			    c2.close();
			     bdd.closeDb();
	     } catch (Exception ex)
	 	 {
	 		Log.e(TAG, Log.getStackTraceString(ex));
	 	 }
	     Log.e(TAG, "SORTIE de afficherHoraires");
     }
}