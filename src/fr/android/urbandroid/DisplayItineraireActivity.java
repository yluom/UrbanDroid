package fr.android.urbandroid;
 
import fr.android.urbandroid.*;
import android.app.Activity;
import android.content.Intent;
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
	
	public Cursor fetchAllTitles(){
		SQLiteDatabase db = null;
		try
		{
			String DB_PATH = "/data/data/fr.android.urbandroid/";
		    String DB_NAME = "urbdroid.db";
			String myPath = DB_PATH + DB_NAME;
		    db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}
		catch(Exception ex)
		{
			Toast.makeText(this, "BDD #1 :" + ex.toString(), Toast.LENGTH_LONG).show();
		}
		return db.query(
				  "STATIONS", new String[]{"_id","nomstation"}, null, null, null, null, "nomstation ASC" );
	}
	
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
	     Cursor c = fetchAllTitles();
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
     }
		catch(Exception ex)
		{
			Toast.makeText(this, "BDD #2 :" + ex.toString(), Toast.LENGTH_LONG).show();
		}
     
   }


}