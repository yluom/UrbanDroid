package fr.android.urbandroid;

import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Bundle;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
 
public class DisplayPlanGoogleActivity extends MapActivity
{
	
	private static final String TAG = "DisplayPlanGoogleActivity";

     public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.plang);
     
     OnClickListener menuSwitcher = new OnClickListener()
     {
       public void onClick(View actualView)
       {
           Intent intent;
      	  switch(actualView.getId())
      	  {
      	  	case R.id.btn_tar: intent = new Intent(DisplayPlanGoogleActivity.this, DisplayTarifActivity.class);
      	  						startActivity(intent); break;
       	  	case R.id.btn_iti: intent = new Intent(DisplayPlanGoogleActivity.this, DisplayItineraireActivity.class);
 									startActivity(intent); break;
      	  	case R.id.btn_hor: intent = new Intent(DisplayPlanGoogleActivity.this, DisplayHorairesActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_fav: intent = new Intent(DisplayPlanGoogleActivity.this, DisplayFavorisActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_pla: intent = new Intent(DisplayPlanGoogleActivity.this, DisplayPlanActivity.class);
      	  						startActivity(intent); break;
    	  	case R.id.ongletGoogleMap: intent = new Intent(DisplayPlanGoogleActivity.this, DisplayPlanGoogleActivity.class);
				startActivity(intent); break;
    	  	case R.id.ongletTisseo: intent = new Intent(DisplayPlanGoogleActivity.this, DisplayPlanActivity.class);
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
     ImageView iv6 = (ImageView) findViewById(R.id.ongletGoogleMap);
     iv6.setOnClickListener(menuSwitcher);
     ImageView iv7 = (ImageView) findViewById(R.id.ongletTisseo);
     iv7.setOnClickListener(menuSwitcher);
     
     try
     {
    	 MapView mapView = (MapView) this.findViewById(R.id.mapView);
    	 mapView.setBuiltInZoomControls(true);
     }
     catch (Exception ex)
     {
    	 Log.e(TAG, Log.getStackTraceString(ex));
    	 Toast.makeText(this, "#6 :" + ex.getStackTrace().toString(), 10).show();
     }
     }
     
     @Override
     protected boolean isRouteDisplayed()
     {
       return false;
     }
}