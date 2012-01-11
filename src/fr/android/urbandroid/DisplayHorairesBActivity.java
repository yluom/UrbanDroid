package fr.android.urbandroid;
 
import fr.android.urbandroid.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.TextView;
 
public class DisplayHorairesBActivity extends Activity
{
	
	
	public Cursor fetchAllTitles(){
		String DB_PATH = "/data/data/fr.android.urbandroid/";
	    String DB_NAME = "urbdroid.db";
		String myPath = DB_PATH + DB_NAME;
		SQLiteDatabase db;
	     db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		return db.query(
		  "LIGNES", new String[]{"_id","nomligne"}, "idligne LIKE 'B%'", null, null, null, "nomligne ASC" );
	}
	
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
     
     
     Cursor c = fetchAllTitles();
     startManagingCursor(c);
      
     // Stock la colone que l'on veut afficher
     String[] from = new String[]{"nomligne"};
     // create an array of the display item we want to bind our data to
     int[] to = new int[]{android.R.id.text1};
     // create simple cursor adapter
     SimpleCursorAdapter adapter =
       new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to );
     adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
     // get reference to our spinner
     Spinner s = (Spinner) findViewById( R.id.spinnerH );
     s.setAdapter(adapter);
     
   }
}