package fr.android.urbandroid;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import fr.android.urbandroid.*;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.TextView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
 
public class DisplayItineraireActivity extends Activity
{
	
	private static final String TAG = "DisplayItineraireActivity";
	
     public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.itineraire);
     
     OnClickListener menuSwitcher = new OnClickListener()
     {
       public void onClick(View actualView)
       {
           Intent intent;
     	  switch(actualView.getId())
     	  {
     	  	case R.id.btn_tar: intent = new Intent(DisplayItineraireActivity.this, DisplayTarifActivity.class);
     	  						startActivity(intent); break;
      	  	case R.id.btn_iti: intent = new Intent(DisplayItineraireActivity.this, DisplayItineraireActivity.class);
									startActivity(intent); break;
     	  	case R.id.btn_hor: intent = new Intent(DisplayItineraireActivity.this, DisplayHorairesActivity.class);
     	  						startActivity(intent); break;
     	  	case R.id.btn_fav: intent = new Intent(DisplayItineraireActivity.this, DisplayFavorisActivity.class);
     	  						startActivity(intent); break;
     	  	case R.id.btn_pla: intent = new Intent(DisplayItineraireActivity.this, DisplayPlanActivity.class);
     	  						startActivity(intent); break;
     	  	case R.id.btnValider:
     	  		intent = new Intent(DisplayItineraireActivity.this, DisplayResItineraireActivity.class);
     	  		
     	  		Spinner spinnerDepart = (Spinner) findViewById(R.id.spinner1);
     	       	Spinner spinnerArrivee = (Spinner) findViewById(R.id.spinner2);
     	  		Bundle bundle = new Bundle();
     	  		
     	  		Cursor cursorDepart = (Cursor) spinnerDepart.getSelectedItem();
     	  		bundle.putString("StationDepart", cursorDepart.getString(cursorDepart.getColumnIndex("nomstation")));
     	  		
     	  		Cursor cursorArrivee = (Cursor) spinnerArrivee.getSelectedItem();	  		
     	  		bundle.putString("StationArrivee", cursorArrivee.getString(cursorArrivee.getColumnIndex("nomstation")));

     	  		
     	  		Log.e(TAG, "stationArrivee = " + bundle.getString("StationArrivee"));
     	        Log.e(TAG, "stationDepart = " + bundle.getString("StationDepart"));
     	        
     	  		intent.putExtras(bundle);
				startActivity(intent);
				break;
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
     Button b = (Button) findViewById(R.id.btnValider);
     b.setOnClickListener(menuSwitcher);
     TimePicker tp = (TimePicker)findViewById(R.id.timePicker);
     tp.setIs24HourView(true);
     
     try
     {
    	 
	     Cursor c = Bdd.fetchAllTitles("STATIONS", new String[]{"_id","nomstation"}, null, null, null, null, "nomstation ASC" );
	     startManagingCursor(c);
	     // Stock la colone que l'on veut afficher
	     String[] from = new String[]{"nomstation"};
	     // create an array of the display item we want to bind our data to
	     int[] to = new int[]{android.R.id.text1};
	     // create simple cursor adapter
	     SimpleCursorAdapter adapter =
	       new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to );
	     adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
	     // get reference to our spinner
	     Spinner s = (Spinner) findViewById( R.id.spinner1 );
	     s.setAdapter(adapter);
	     
	     Spinner s2 = (Spinner) findViewById( R.id.spinner2 );
	     s2.setAdapter(adapter);
	     Bdd.db.close();
	     
     }
		catch(Exception ex)
		{
			Log.e(TAG, Log.getStackTraceString(ex));
			Toast.makeText(this, "BDD #2 :" + ex.toString(), Toast.LENGTH_LONG).show();
		}
     
   }

     @Override
     protected void onDestroy() {
         super.onDestroy();
         if (Bdd.db != null)
             Bdd.db.close();
     }
     
     public Ligne nouvelleLigne(String paramNomLigne){
 		Bdd bdd = new Bdd();
 		Cursor c = bdd.getCursor(	"LIGNES, TYPES, STATIONS, DESSERT", 	//FROM
 				new String[]{"LIGNES.idligne", "nomtype", "position", "longitude", "latitude", "nomstation", "STATIONS._id"},  //SELECT
 				"LIGNES.idtype = TYPES.idtype AND STATIONS._id = DESSERT.idstation AND DESSERT.idligne = LIGNES.idligne AND LIGNES.idligne = '"+paramNomLigne+"'", //WHERE
 				null,
 				null, 
 				null,
 				"position" ); //ORDER BY
 		startManagingCursor(c);
 	    c.moveToFirst();
 	    
 	    
 	    //Initialisation � null de la variable type (METRO, BUS, TRAM)
 	    String type =null;
 	    //TreeMap servant � remplir la liste des stations qui se trouve sur la ligne paramNomLigne avec en cl� leur position
         TreeMap<Integer, Station> listeStation = new TreeMap<Integer, Station>();
         //Variable permetant de stocker nos station courante
 	    Station stationCourante;
         
 	    //R�cup�ration des index de colone de la requ�te
 	    int colonneNomLigne = c.getColumnIndex("idligne");
         int colonneNomType = c.getColumnIndex("nomtype");
         int colonneLong = c.getColumnIndex("longitude");
         int colonneLat = c.getColumnIndex("latitude");
         int colonneNomStation = c.getColumnIndex("nomstation");
         int colonnePosition = c.getColumnIndex("position");
         int colonneIdStation = c.getColumnIndex("_id");
         
         //V�rifie que la requ�te retourne quelque chose
 	    if (c != null && c.getCount() != 0) {
 	    	
 	    	//Initialisation au premier passage avec les valeurs de la premiere ligne de resultats de la requ�te
 	    	String nomLigne = c.getString(colonneNomLigne);
 	    	type = c.getString(colonneNomType);
 	    	float lat = c.getFloat(colonneLat);
             float longi = c.getFloat(colonneLong);
             String nomStation = c.getString(colonneNomStation);
             int position = c.getInt(colonnePosition);
             int idStation = c.getInt(colonneIdStation);
             
             // Cr�� une station avec une longitude, latitude, nom et la MAP g�n�r� par listerTerminus
             stationCourante = new Station(longi, lat, nomStation, listerTerminus(idStation));
             
             //Ajoute � la position 1 (car ORDER BY position ) la premi�re station cr��e ci dessus...
             listeStation.put(position, stationCourante);
             
             //On passe maintenant aux autres Stations de mani�re � remplir la TreeMap
 	        while (c.moveToNext()) {
 	        	//R�cup�ration des valeurs des autres lignes de r�sultats de la requ�te
 		    	nomLigne = c.getString(colonneNomLigne);
 		    	type = c.getString(colonneNomType);
 		    	lat = c.getFloat(colonneLat);
 	            longi = c.getFloat(colonneLong);
 	            nomStation = c.getString(colonneNomStation);
 	            position = c.getInt(colonnePosition);
 	            idStation = c.getInt(colonneIdStation);

 	            // Cr�� une nouvelle station � chaque ligne de r�sultat
 	            stationCourante = new Station(longi, lat, nomStation, listerTerminus(idStation));
 	            
 	            //Ajoute cette nouvelle station � la TreeMap
 	            listeStation.put(position, stationCourante);
 	        }
 	}
 	//Fin du if (c != null) = Fermeture de notre Cursor c
 	//c.close();
 	// On ferme la bdd
 	bdd.closeDb();
 	stopManagingCursor(c);
 	c.close();
 	
 	return new Ligne(paramNomLigne,type,listeStation);
 }
 	
 	
 	//Fonction qui retourne la HashMap de toutes les lignes pour laquelle notre station est terminus(position=1 ou position=MAX(position) sur l'enssemble des lignes )
 	public HashMap<String, Boolean> listerTerminus(int idStation){
 		
 		//Cr�ation de la HashMap vierge
 		HashMap<String, Boolean> listeLigne = new HashMap<String, Boolean>();
 		
 		
 		//Initialisation � false de la HashMap
 		Bdd bdd = new Bdd();
 		// Requ�te permettant d'obtenir tous les terminus = 1		(les premiers de la ligne)		SELECT idligne FROM DESSERT WHERE idstation=1 AND position = 1
 		Cursor cInit = bdd.getCursor("DESSERT", 	//FROM
 				new String[]{"idligne", "_id"},  //SELECT
 				"idstation="+idStation, //WHERE
 				null,
 				null, 
 				null, 
 				null );
 		startManagingCursor(cInit);
 	    cInit.moveToFirst();
 	    
 	    //Recup�re l'index de la colone nomligne
 	    int colonneNomLigne = cInit.getColumnIndex("idligne");
 	    
 	    //Verification que la requ�te retourne qqch
 	    if (cInit != null && cInit.getCount() != 0) {
 	    	
 	    	//Premi�re ligne de r�sultat
 	    	String nomLigne = cInit.getString(colonneNomLigne);
 	    	listeLigne.put(nomLigne, false);
 	    	while (cInit.moveToNext()) {
 	    		 
 	    		 //Ajoute � la map listeLigne les autres r�sultats
 	    		nomLigne = cInit.getString(colonneNomLigne);
 	 	    	listeLigne.put(nomLigne, false);
 		     }
 		}
 	    
 	    // Fin de if (c != null) = Fermeture de c
 	    //c.close();
 	    // Fermer la bdd
 	    bdd.closeDb();
 	 	stopManagingCursor(cInit);
 	 	cInit.close();
 		
 		bdd = new Bdd();
 		// Requ�te permettant d'obtenir tous les terminus = 1		(les premiers de la ligne)		SELECT idligne FROM DESSERT WHERE idstation=1 AND position = 1
 		Cursor c = bdd.getCursor("DESSERT", 	//FROM
 				new String[]{"idligne", "_id"},  //SELECT
 				"idstation="+idStation+" AND position = 1", //WHERE
 				null,
 				null, 
 				null, 
 				null );
 		startManagingCursor(c);
 	    c.moveToFirst();
 	    
 	    //Recup�re l'index de la colone nomligne
 	    colonneNomLigne = c.getColumnIndex("idligne");
 	    
 	    //Verification que la requ�te retourne qqch
 	    if (c != null && c.getCount() != 0) {
 	    	
 	    	//Premi�re ligne de r�sultat
 	    	String nomLigne = c.getString(colonneNomLigne);
 	    	listeLigne.put(nomLigne, true);
 	    	while (c.moveToNext()) {
 	    		 
 	    		 //Ajoute � la map listeLigne les autres r�sultats
 	    		nomLigne = c.getString(colonneNomLigne);
 	 	    	listeLigne.put(nomLigne, true);
 		     }
 		}
 	    
 	    // Fin de if (c != null) = Fermeture de c
 	    //c.close();
 	    // Fermer la bdd
 	    bdd.closeDb();
 	 	stopManagingCursor(c);
 	 	c.close();
 	 	
 	    Bdd bdd2 = new Bdd();
 	    // Requ�te permettant d'obtenir tous les noms de lignes
 		Cursor cursorNomLigne = bdd2.getCursor("LIGNES", 	//FROM
 				new String[]{"idligne", "_id"},  //SELECT
 				null, //WHERE
 				null,
 				null, 
 				null, 
 				"idligne" );
 		startManagingCursor(cursorNomLigne);
 		cursorNomLigne.moveToFirst();
 		
 		 //Recup�re l'index de la colone nomligne
 	    int nomLigneIndex = cursorNomLigne.getColumnIndex("idligne");
 	    
 	    //Facultatif : il y aura toujours un r�sultat parce qu'on aura toujours des Lignes dans la BDD...
 	    if (cursorNomLigne != null && cursorNomLigne.getCount() != 0) {
 	    	
 		    //Premi�re ligne de r�sultat
 	    	String nomLigne = cursorNomLigne.getString(nomLigneIndex);
 	    	
 	    	Bdd bdd3 = new Bdd();
 	    	bdd3.closeDb();
 	    	//Toast.makeText(this, "idStation = " + idStation + "\nnomligne = " + nomLigne, Toast.LENGTH_LONG).show();
 	    	//requete d'obtention du/des terminus pour une ligne donn�e (nomLigne) par rapport � notre station courante param idstation
 	    	Cursor cursTerm = bdd3.getCursor("DESSERT", 	//FROM
 					new String[]{ "_id","idligne"},  //SELECT
 					"idstation="+idStation+" AND position = (SELECT MAX(position) FROM DESSERT WHERE idligne='"+nomLigne+"')", //WHERE
 					null,
 					null, 
 					null, 
 					null );
 	    	startManagingCursor(cursTerm);
 	    	colonneNomLigne = cursTerm.getColumnIndex("idligne");
 	    	cursTerm.moveToFirst();
 	    	//Toast.makeText(this, "valeur = " + colonneNomLigne, Toast.LENGTH_LONG).show();
 	    	if(cursTerm != null && cursTerm.getCount() != 0){
 	    		//Premi�re et unique ligne de r�sultat possible ( un terminus MAX possible par ligne !!! )   Explication : Le MIN(position) de nimporte quelle ligne est toujours 1, alors que le MAX(position) vari : 24, 20 ou 18 ... C'est pourquoi on parcourt le MAX(position) de CHAQUE Lignes...
 	 	    	nomLigne = cursTerm.getString(colonneNomLigne);
 	 	    	listeLigne.put(nomLigne, true);
 	    	}
 	    	// Fin du if(cursTerm != null) = Fermeture du cursTerm
 	    	//cursTerm.close();
 	    	bdd3.closeDb();
 	    	stopManagingCursor(cursTerm);
 	    	cursTerm.close();
 	    	
 	    	Bdd bdd4 = new Bdd();
 	    	//Permet d'executer la requ�te SELECT idligne FROM DESSERT WHERE idstation=idStation AND position = (SELECT MAX(position) FROM DESSERT WHERE idligne=nomLigne) avec tout les nom de Lignes
 	    	while (cursorNomLigne.moveToNext()) {
 	    		 
 	    		 //Ajoute � la map listeLigne les autres r�sultats
 	    		nomLigne = cursorNomLigne.getString(nomLigneIndex);
 	    		
 	 	    	//Toast.makeText(this, "idStation = " + idStation + "\nnomligne = " + nomLigne, Toast.LENGTH_LONG).show();

 		    	//requete d'obtention du/des terminus pour une ligne donn�e (nomLigne) par rapport � notre station courante param idstation
 		    	cursTerm = bdd4.getCursor("DESSERT", 	//FROM
 						new String[]{"idligne", "_id"},  //SELECT
 						"idstation="+idStation+" AND position = (SELECT MAX(position) FROM DESSERT WHERE idligne='"+nomLigne+"')", //WHERE
 						null,
 						null, 
 						null, 
 						null );
 		    	startManagingCursor(cursTerm);
 		    	cursTerm.moveToFirst();
 		    	colonneNomLigne = cursTerm.getColumnIndex("idligne");
 		    	
 		    	if(cursTerm != null && cursTerm.getCount() != 0){
 		    		//Premi�re et unique ligne de r�sultat possible ( un terminus MAX possible par ligne !!! )   Explication : Le MIN(position) de nimporte quelle ligne est toujours 1, alors que le MAX(position) vari : 24, 20 ou 18 ... C'est pourquoi on parcourt le MAX(position) de CHAQUE Lignes...
 		 	    	nomLigne = cursTerm.getString(colonneNomLigne);
 		 	    	listeLigne.put(nomLigne, true);
 		    	}
 		    	// Fin du if(cursTerm != null) = Fermeture du cursTerm
 		    	//cursTerm.close();
 		    	bdd4.closeDb();
 	 	    	stopManagingCursor(cursTerm);
 	 	    	cursTerm.close();
 	 	    	
 		     }
 	    }
 	    
 	    // Fin de if (cursorNomLigne != null) = Fermeture de cursorNomLigne
 	    //cursorNomLigne.close();
 	    bdd2.closeDb();
	    stopManagingCursor(cursorNomLigne);
	    cursorNomLigne.close();
	    	
 		return listeLigne;
 	}

}