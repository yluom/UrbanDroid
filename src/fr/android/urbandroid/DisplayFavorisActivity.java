package fr.android.urbandroid;
 
import fr.android.urbandroid.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class DisplayFavorisActivity extends Activity
{
	 private static final String TAG = "UrbanDroidActivity";
	 private static String depart;
	 private static String arrivee; 
	 
     public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.favoris);
 	
     OnClickListener menuSwitcher = new OnClickListener()
     {
       public void onClick(View actualView)
       {
           Intent intent;
      	  switch(actualView.getId())
      	  {
      	  	case R.id.btn_tar: intent = new Intent(DisplayFavorisActivity.this, DisplayTarifActivity.class);
      	  						startActivity(intent); break;
       	  	case R.id.btn_iti: intent = new Intent(DisplayFavorisActivity.this, DisplayItineraireActivity.class);
 									startActivity(intent); break;
      	  	case R.id.btn_hor: intent = new Intent(DisplayFavorisActivity.this, DisplayHorairesActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_fav: intent = new Intent(DisplayFavorisActivity.this, DisplayFavorisActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_pla: intent = new Intent(DisplayFavorisActivity.this, DisplayPlanActivity.class);
      	  						startActivity(intent); break;
      	    case R.id.btn_calcul:
      	    					Log.e(TAG, "CLIC sur btn_calcul");
					   	  		intent = new Intent(DisplayFavorisActivity.this, DisplayResItineraireActivity.class);
					   	  		Bundle bundle = new Bundle();
					   	  	    
					   	  		bundle.putString("StationDepart", depart);
					   	  		bundle.putString("StationArrivee", arrivee);
					   	  		intent.putExtras(bundle);
								startActivity(intent);
					   	  	    Log.e(TAG, "startActivity Started");
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
     Button btn_calcul = (Button) findViewById(R.id.btn_calcul);
     btn_calcul.setOnClickListener(menuSwitcher);
     
     Cursor c = Bdd.fetchAllTitles("FAVORIS, STATIONS as sta1, STATIONS AS sta2", new String[]{"sta1._id","sta1.nomstation as depart", "sta2.nomstation as arrivee", "nomfavoris"}, "FAVORIS.idstationfav=sta1._id AND FAVORIS.idstation = sta2._id", null, null, null, null);
     startManagingCursor(c);
     // Stock la colone que l'on veut afficher
     String[] from = new String[]{"nomfavoris"};
     // create an array of the display item we want to bind our data to
     int[] to = new int[]{android.R.id.text1};
     // create simple cursor adapter
     SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to );
     adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
     // get reference to our spinner
     final Spinner s = (Spinner) findViewById( R.id.spiFavoris );
     s.setAdapter(adapter);
     Bdd.db.close();
     
     s.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Cursor item = (Cursor)(s.getSelectedItem());
				depart = item.getString(item.getColumnIndex("depart"));
				arrivee = item.getString(item.getColumnIndex("arrivee"));
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	});

     
   }
}