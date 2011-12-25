package fr.android.urbandroid;

import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
 
public class DisplayPlanGoogleActivity extends MapActivity implements LocationListener
{
	
	private static final String TAG = "DisplayPlanGoogleActivity";
	private MapView mapView;
	private MapController mc;
	//inutile:private GeoPoint location;
	private MyLocationOverlay userLocation = null;
	private LocationManager lm;
	
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
	    	 this.mapView = (MapView) this.findViewById(R.id.mapView);
	    	// this.mapView =  new MapView(this,this.getResources().getString(R.string.mapKey));
	 		this.mapView.setClickable(true);
	    	 this.mapView.setBuiltInZoomControls(true);
	    	
	     }
	     catch (Exception ex)
	     {
	    	 Log.e(TAG, Log.getStackTraceString(ex));
	    	 Toast.makeText(this, "#6 :" + ex.getStackTrace().toString(), 10).show();
	     }
	     // On toppe le controleur (pour positionner le pt central)
		 this.mc = this.mapView.getController();
		 // on instancie le LocationManager qui va permettre de suivre les changement de positions
		 lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		 lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
		 lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
		 
		 // instanciation de la localisation gps
	     this.userLocation = new MyLocationOverlay(getApplicationContext(), mapView);
	     
		
		// Ajout et l’affichage de la localisation sur la map
		this.mapView.getOverlays().add(userLocation);
		this.userLocation.enableMyLocation();

		// Zoom direct sur la position de l'utilisatube
		this.userLocation.runOnFirstFix(new Runnable() {
		    public void run() {
		    mc.animateTo(userLocation.getMyLocation());
		    mc.setZoom(17);
		    }
		});
		// On met la vue sattelite (sympatouche)
		//this.mapView.setSatellite(true);
		// je sais pas
		this.mapView.invalidate();

     }
     
     @Override
     protected boolean isRouteDisplayed()
     {
       return false;
     }
     
     
	 public void onLocationChanged(Location location) {
			if (location != null) {
				Toast.makeText(this, "Nouvelle position : " + (float)location.getLatitude() + ", " + (float)location.getLongitude(), Toast.LENGTH_SHORT).show();
				mc.animateTo(new GeoPoint(microdegrees(location.getLatitude()),microdegrees(location.getLongitude())));
				mc.setZoom(17);
			}
	 }
 	
	 
	 // Tentative de menu, a revoir (force close :/) ! (mais y'a pas mal de bon ;) )
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {        
     	menu.add(0,100,0,"Zoom +");
     	menu.add(0,101,0,"Zoom -");
     	menu.add(0,102,0,"Vue Satellite");
     	menu.add(0,103,0,"Street view");
         return true;
     }
     
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
     	switch(item.getItemId()){
     	case 100: mc.setZoom(mapView.getZoomLevel() + 1); break;
     	case 101: mc.setZoom(mapView.getZoomLevel() - 1); break;
     	case 102: mapView.setSatellite(!mapView.isSatellite()); break;
     	case 103: mapView.setStreetView(!mapView.isStreetView()); break;
     	}
     	return true;
     }
     
 	@Override
 	public boolean onPrepareOptionsMenu(Menu menu) {
 		menu.findItem(102).setIcon(mapView.isSatellite() ?android.R.drawable.checkbox_on_background:android.R.drawable.checkbox_off_background);
 		menu.findItem(104).setIcon(mapView.isStreetView()?android.R.drawable.checkbox_on_background:android.R.drawable.checkbox_off_background);
 		return true;
 	}
 	//les coordonnées sont multipliées par 1E6 car les coordonnées pour GeoPoint sont exprimées en micro-degré
 	private int microdegrees(double value){
		return (int)(value*1000000);
	}
 	
 	public void onProviderDisabled(String provider)
 	 {
 	 // TODO Auto-generated method stub
 	 }
 	public void onProviderEnabled(String provider)
 	 {
 	 
 	// TODO Auto-generated method stub
 	 }
 	 
 	 public void onStatusChanged(String provider, int status, Bundle extras)
 	 {
 	 // TODO Auto-generated method stub
 	 }
 	
}