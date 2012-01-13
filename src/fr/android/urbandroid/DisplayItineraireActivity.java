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
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
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
	     
	     /*
	      * CODE DE L'ITINERAIRE - I LOVE BELLMAN FORD
	      */
	     Ligne testLigne = nouvelleLigne("MA");
	     Station depart = testLigne.getListeStation().get(1);
	     Station arrivee = testLigne.getListeStation().get(5);
	     //Toast.makeText(this, "Nom station départ = " + depart.getNom(), Toast.LENGTH_LONG).show();
	     ArrayList<Ligne> listeLigneDepart = new ArrayList<Ligne>();
	     ArrayList<Ligne> listeLigneArrivee = new ArrayList<Ligne>();
	    
	     Set<String> setLigne = depart.listeLigne().keySet();
	     Iterator<String> it = setLigne.iterator();
	     String buffer;
	     while (it.hasNext())
	     {
	    	 buffer = it.next();
	    	 listeLigneDepart.add(nouvelleLigne(buffer));
	    	 //Toast.makeText(this, "Ligne (départ) add = " + buffer, Toast.LENGTH_LONG).show();
	     }
	     
	     Set<String> setLigne2 = arrivee.listeLigne().keySet();
	     Iterator<String> it2 = setLigne2.iterator();
	     buffer = "";
	     while (it2.hasNext())
	     {
	    	 buffer = it2.next();
	    	 listeLigneArrivee.add(nouvelleLigne(buffer));
	    	 //Toast.makeText(this, "Ligne (arrivée) add = " + buffer, Toast.LENGTH_LONG).show();
	     }
	     
	     Itineraire iti = new Itineraire(depart, arrivee, listeLigneArrivee, listeLigneDepart);
	     iti.calculerItineraire();
	     Toast.makeText(this, "Résultat itinéraire = \n" + iti.toString(), Toast.LENGTH_LONG).show();
	     
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
 	    
 	    
 	    //Initialisation à null de la variable type (METRO, BUS, TRAM)
 	    String type =null;
 	    //TreeMap servant à remplir la liste des stations qui se trouve sur la ligne paramNomLigne avec en clé leur position
         TreeMap<Integer, Station> listeStation = new TreeMap<Integer, Station>();
         //Variable permetant de stocker nos station courante
 	    Station stationCourante;
         
 	    //Récupération des index de colone de la requête
 	    int colonneNomLigne = c.getColumnIndex("idligne");
         int colonneNomType = c.getColumnIndex("nomtype");
         int colonneLong = c.getColumnIndex("longitude");
         int colonneLat = c.getColumnIndex("latitude");
         int colonneNomStation = c.getColumnIndex("nomstation");
         int colonnePosition = c.getColumnIndex("position");
         int colonneIdStation = c.getColumnIndex("_id");
         
         //Vérifie que la requête retourne quelque chose
 	    if (c != null && c.getCount() != 0) {
 	    	
 	    	//Initialisation au premier passage avec les valeurs de la premiere ligne de resultats de la requête
 	    	String nomLigne = c.getString(colonneNomLigne);
 	    	type = c.getString(colonneNomType);
 	    	float lat = c.getFloat(colonneLat);
             float longi = c.getFloat(colonneLong);
             String nomStation = c.getString(colonneNomStation);
             int position = c.getInt(colonnePosition);
             int idStation = c.getInt(colonneIdStation);
             
             // Créé une station avec une longitude, latitude, nom et la MAP généré par listerTerminus
             stationCourante = new Station(longi, lat, nomStation, listerTerminus(idStation));
             
             //Ajoute à la position 1 (car ORDER BY position ) la première station créée ci dessus...
             listeStation.put(position, stationCourante);
             
             //On passe maintenant aux autres Stations de manière à remplir la TreeMap
 	        while (c.moveToNext()) {
 	        	//Récupération des valeurs des autres lignes de résultats de la requête
 		    	nomLigne = c.getString(colonneNomLigne);
 		    	type = c.getString(colonneNomType);
 		    	lat = c.getFloat(colonneLat);
 	            longi = c.getFloat(colonneLong);
 	            nomStation = c.getString(colonneNomStation);
 	            position = c.getInt(colonnePosition);
 	            idStation = c.getInt(colonneIdStation);

 	            // Créé une nouvelle station à chaque ligne de résultat
 	            stationCourante = new Station(longi, lat, nomStation, listerTerminus(idStation));
 	            
 	            //Ajoute cette nouvelle station à la TreeMap
 	            listeStation.put(position, stationCourante);
 	        }
 	}
 	//Fin du if (c != null) = Fermeture de notre Cursor c
 	//c.close();
 	// On ferme la bdd
 	bdd.closeDb();
 	return new Ligne(paramNomLigne,type,listeStation);
 }
 	
 	
 	//Fonction qui retourne la HashMap de toutes les lignes pour laquelle notre station est terminus(position=1 ou position=MAX(position) sur l'enssemble des lignes )
 	public HashMap<String, Boolean> listerTerminus(int idStation){
 		
 		//Création de la HashMap vierge
 		HashMap<String, Boolean> listeLigne = new HashMap<String, Boolean>();
 		
 		
 		//Initialisation à false de la HashMap
 		Bdd bdd = new Bdd();
 		// Requête permettant d'obtenir tous les terminus = 1		(les premiers de la ligne)		SELECT idligne FROM DESSERT WHERE idstation=1 AND position = 1
 		Cursor cInit = bdd.getCursor("DESSERT", 	//FROM
 				new String[]{"idligne", "_id"},  //SELECT
 				"idstation="+idStation, //WHERE
 				null,
 				null, 
 				null, 
 				null );
 		startManagingCursor(cInit);
 	    cInit.moveToFirst();
 	    
 	    //Recupère l'index de la colone nomligne
 	    int colonneNomLigne = cInit.getColumnIndex("idligne");
 	    
 	    //Verification que la requête retourne qqch
 	    if (cInit != null && cInit.getCount() != 0) {
 	    	
 	    	//Première ligne de résultat
 	    	String nomLigne = cInit.getString(colonneNomLigne);
 	    	listeLigne.put(nomLigne, false);
 	    	while (cInit.moveToNext()) {
 	    		 
 	    		 //Ajoute à la map listeLigne les autres résultats
 	    		nomLigne = cInit.getString(colonneNomLigne);
 	 	    	listeLigne.put(nomLigne, false);
 		     }
 		}
 	    
 	    // Fin de if (c != null) = Fermeture de c
 	    //c.close();
 	    // Fermer la bdd
 	    bdd.closeDb();
 		
 		
 		
 		
 		bdd = new Bdd();
 		// Requête permettant d'obtenir tous les terminus = 1		(les premiers de la ligne)		SELECT idligne FROM DESSERT WHERE idstation=1 AND position = 1
 		Cursor c = bdd.getCursor("DESSERT", 	//FROM
 				new String[]{"idligne", "_id"},  //SELECT
 				"idstation="+idStation+" AND position = 1", //WHERE
 				null,
 				null, 
 				null, 
 				null );
 		startManagingCursor(c);
 	    c.moveToFirst();
 	    
 	    //Recupère l'index de la colone nomligne
 	    colonneNomLigne = c.getColumnIndex("idligne");
 	    
 	    //Verification que la requête retourne qqch
 	    if (c != null && c.getCount() != 0) {
 	    	
 	    	//Première ligne de résultat
 	    	String nomLigne = c.getString(colonneNomLigne);
 	    	listeLigne.put(nomLigne, true);
 	    	while (c.moveToNext()) {
 	    		 
 	    		 //Ajoute à la map listeLigne les autres résultats
 	    		nomLigne = c.getString(colonneNomLigne);
 	 	    	listeLigne.put(nomLigne, true);
 		     }
 		}
 	    
 	    // Fin de if (c != null) = Fermeture de c
 	    //c.close();
 	    // Fermer la bdd
 	    bdd.closeDb();
 	    
 	    Bdd bdd2 = new Bdd();
 	    // Requête permettant d'obtenir tous les noms de lignes
 		Cursor cursorNomLigne = bdd2.getCursor("LIGNES", 	//FROM
 				new String[]{"idligne", "_id"},  //SELECT
 				null, //WHERE
 				null,
 				null, 
 				null, 
 				"idligne" );
 		startManagingCursor(cursorNomLigne);
 		cursorNomLigne.moveToFirst();
 		
 		 //Recupère l'index de la colone nomligne
 	    int nomLigneIndex = cursorNomLigne.getColumnIndex("idligne");
 	    
 	    //Facultatif : il y aura toujours un résultat parce qu'on aura toujours des Lignes dans la BDD...
 	    if (cursorNomLigne != null && cursorNomLigne.getCount() != 0) {
 	    	
 		    //Première ligne de résultat
 	    	String nomLigne = cursorNomLigne.getString(nomLigneIndex);
 	    	
 	    	Bdd bdd3 = new Bdd();
 	    	bdd3.closeDb();
 	    	//Toast.makeText(this, "idStation = " + idStation + "\nnomligne = " + nomLigne, Toast.LENGTH_LONG).show();
 	    	//requete d'obtention du/des terminus pour une ligne donnée (nomLigne) par rapport à notre station courante param idstation
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
 	    		//Première et unique ligne de résultat possible ( un terminus MAX possible par ligne !!! )   Explication : Le MIN(position) de nimporte quelle ligne est toujours 1, alors que le MAX(position) vari : 24, 20 ou 18 ... C'est pourquoi on parcourt le MAX(position) de CHAQUE Lignes...
 	 	    	nomLigne = cursTerm.getString(colonneNomLigne);
 	 	    	listeLigne.put(nomLigne, true);
 	    	}
 	    	// Fin du if(cursTerm != null) = Fermeture du cursTerm
 	    	//cursTerm.close();
 	    	bdd3.closeDb();
  	    	
 	    	Bdd bdd4 = new Bdd();
 	    	//Permet d'executer la requête SELECT idligne FROM DESSERT WHERE idstation=idStation AND position = (SELECT MAX(position) FROM DESSERT WHERE idligne=nomLigne) avec tout les nom de Lignes
 	    	while (cursorNomLigne.moveToNext()) {
 	    		 
 	    		 //Ajoute à la map listeLigne les autres résultats
 	    		nomLigne = cursorNomLigne.getString(nomLigneIndex);
 	    		
 	 	    	//Toast.makeText(this, "idStation = " + idStation + "\nnomligne = " + nomLigne, Toast.LENGTH_LONG).show();

 		    	//requete d'obtention du/des terminus pour une ligne donnée (nomLigne) par rapport à notre station courante param idstation
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
 		    		//Première et unique ligne de résultat possible ( un terminus MAX possible par ligne !!! )   Explication : Le MIN(position) de nimporte quelle ligne est toujours 1, alors que le MAX(position) vari : 24, 20 ou 18 ... C'est pourquoi on parcourt le MAX(position) de CHAQUE Lignes...
 		 	    	nomLigne = cursTerm.getString(colonneNomLigne);
 		 	    	listeLigne.put(nomLigne, true);
 		    	}
 		    	// Fin du if(cursTerm != null) = Fermeture du cursTerm
 		    	//cursTerm.close();
 		    	bdd4.closeDb();
 		     }
 	    }
 	    
 	    // Fin de if (cursorNomLigne != null) = Fermeture de cursorNomLigne
 	    //cursorNomLigne.close();
 	    bdd2.closeDb();
 		
 		return listeLigne;
 	}
 	



}