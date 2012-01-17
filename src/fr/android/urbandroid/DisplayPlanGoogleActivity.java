package fr.android.urbandroid;

import java.util.*;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.*; 
public class DisplayPlanGoogleActivity extends MapActivity implements LocationListener
{
	
	private static final String TAG = "DisplayPlanGoogleActivity";
	private MapView mapView;
	private MapController mc;
	private MyLocationOverlay userLocation = null;
	private LocationManager lm;
	// Utilisés pour afficher les lignes de metro.
	private List<Overlay> mapOverlays;
	private Projection projection; 
	
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
	     
	     // Avant tout, on draw la ligne entre les deux points
	     mapOverlays = this.mapView.getOverlays();        
	    this.projection = mapView.getProjection();
	     mapOverlays.add(new MyOverlay());  
	     
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
	    mc.setZoom(13);
	    
	    // On se place sur la ville de toulouse
	    mc.animateTo(new GeoPoint(microdegrees((double)43.60),microdegrees((double)1.44)));
	    
		// Zoom direct sur la position de l'utilisatube
		this.userLocation.runOnFirstFix(new Runnable() {
		    public void run() {
		    mc.animateTo(userLocation.getMyLocation());
		    }
		});
		// On met la vue sattelite (sympatouche)
		this.mapView.setSatellite(true);

		// je sais pas
		this.mapView.invalidate();
		
		

     } // fin du onCreate
     
     @Override
     protected boolean isRouteDisplayed()
     {
       return false;
     }
     
     
	 public void onLocationChanged(Location location) {
			if (location != null) {
				Toast.makeText(this, "Nouvelle position : " + (float)location.getLatitude() + ", " + (float)location.getLongitude(), Toast.LENGTH_SHORT).show();
				mc.animateTo(new GeoPoint(microdegrees(location.getLatitude()),microdegrees(location.getLongitude())));
			}
	 }
 	
	 
/*	 // Tentative de menu, a revoir (force close :/) ! (mais y'a pas mal de bon ;) )
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {  
    	 super.onCreateOptionsMenu(menu);
   	 	MenuInflater mi = getMenuInflater();
    	 mi.inflate(R.menu.liste_menu, menu);
         return true;
     }
     
     @Override
     public boolean onMenuItemSelected(int featureId, MenuItem item) {
     	switch(item.getItemId()){
     	case R.id.menu_carte: this.mapView.setStreetView(true); break;
     	case R.id.menu_sat: this.mapView.setSatellite(true); break;
     	}
     	return super.onMenuItemSelected(featureId, item);
     }*/
     
 	//les coordonnées sont multipliées par 1E6 car les coordonnées pour GeoPoint sont exprimées en micro-degré
 	private int microdegrees(double value){
		return (int)(value*1000000);
	}
 	
 	class MyOverlay extends Overlay{

 	    public MyOverlay(){

 	    }   
 	    
 	    
 	    public void draw(Canvas canvas, MapView mapv, boolean shadow){
 	        super.draw(canvas, mapv, shadow);
 	        // cette condition limite le zoom mininum
 	       if (mapv.getZoomLevel() > 10)
 	       {
 	    	    // TODO: POUR chaque ligne, afficherLIgne();
 	    	   	afficherLigne("MA", Color.RED, mapv, canvas); // affiche ligne A du metro
 	 	        afficherLigne("MB", Color.YELLOW, mapv, canvas); // ligne B
 	 	        afficherLigne("B2", Color.GREEN, mapv, canvas); // ligne de bus
 	 	        afficherLigne("T1", Color.BLUE, mapv, canvas); // ligne de tram
 	       }
 	       if (mapv.getZoomLevel() > 13)
 	       {
 	        afficherPoints(mapv, canvas, 'M', R.drawable.iconem); // on affiche pour le metro
 	        afficherPoints(mapv, canvas, 'T', R.drawable.iconet); // on affiche pour les trams
 	       }
 	       if(mapv.getZoomLevel() > 14)
 	       {
 	    	  afficherPoints(mapv, canvas, 'B', R.drawable.iconeb); // on affiche pour les bus  	       }
 	       }
 	    }
 	    // transport = M -> metro, = B -> Bus .. etc 
 	    public void afficherPoints(MapView mapv, Canvas canvas, char transport, int image) {
 	    	
 	       Cursor c = Bdd.fetchAllTitles("STATIONS, DESSERT", new String[]{"DESSERT._id","longitude","latitude"}, "STATIONS._id = DESSERT.idstation AND idligne LIKE '"+transport+"%'", null, null, null, "position");
 	       startManagingCursor(c);
 	       c.moveToFirst();
 	       int colonneLong = c.getColumnIndex("longitude");
           int colonneLat = c.getColumnIndex("latitude");
           GeoPoint p;
           if (c != null) {
                       float lat = c.getFloat(colonneLat);
                       float longi = c.getFloat(colonneLong);
                       p = new GeoPoint(microdegrees(longi),microdegrees(lat));
                     //---translate the GeoPoint to screen pixels---
                       Point screenPts = new Point();
                       mapView.getProjection().toPixels(p, screenPts);
            
                       // Ajout de l'icone de bus/metro/tram
                       Bitmap bmp = BitmapFactory.decodeResource(getResources(), image);       
                       canvas.drawBitmap(bmp, screenPts.x-15, screenPts.y-15, null);  
                  
                   while (c.moveToNext()) {
                       /* Retrieve the values of the Entry
                        * the Cursor is pointing to. */
                       lat = c.getFloat(colonneLat);
                       longi = c.getFloat(colonneLong);
                       /* Add current Entry to results. */
                       p = new GeoPoint(microdegrees(longi),microdegrees(lat));
                     //---translate the GeoPoint to screen pixels---
                       screenPts = new Point();
                       mapView.getProjection().toPixels(p, screenPts);
            
                       //---add the marker---
                       bmp = BitmapFactory.decodeResource(getResources(), image);
                       switch(transport) {
	                       case 'M': canvas.drawBitmap(bmp, screenPts.x-15, screenPts.y-15, null);  break;
	                       case 'T': canvas.drawBitmap(bmp, screenPts.x, screenPts.y, null);  break;
	                       case 'B': canvas.drawBitmap(bmp, screenPts.x+5, screenPts.y-15, null);  break;
                       }
                        
                   }
           }
           c.close();
           Bdd.db.close();
           
 	    }
        public void afficherSegment(GeoPoint gp1, GeoPoint gp2, MapView mapv, Canvas canvas, int c)
        {
	        	Paint mPaint = new Paint();
	 	        mPaint.setDither(true);
	 	        mPaint.setColor(c);
	 	        mPaint.setStyle(Paint.Style.STROKE);
	 	        mPaint.setStrokeJoin(Paint.Join.ROUND);
	 	        mPaint.setStrokeCap(Paint.Cap.ROUND);
	 	        mPaint.setStrokeWidth(5);
	 	        
	 	        // les deux géopoints que l'on veut relier  
	 	        Point p1 = new Point();
	 	        Point p2 = new Point();
	
	 	        Path path = new Path();
	
	 	        Projection projection = mapv.getProjection();
	 	        try {
	 	        projection.toPixels(gp1, p1);
	 	        projection.toPixels(gp2, p2);
	 	        } catch(Exception ex)
	 	        {
	 	        	
	 	        	Log.e(TAG, Log.getStackTraceString(ex));
	 	        }
	 	        path.moveTo(p2.x, p2.y);
	 	        path.lineTo(p1.x,p1.y);
	
	 	        canvas.drawPath(path, mPaint);
        }
        public void afficherLigne(String ligne, int color, MapView mapv, Canvas canvas)
        {
   	       Cursor c = Bdd.fetchAllTitles("STATIONS, DESSERT", new String[]{"DESSERT._id","longitude","latitude"}, "STATIONS._id = DESSERT.idstation AND idligne='" + ligne + "'", null, null, null, "position");
 	       startManagingCursor(c);
 	       c.moveToFirst();
 	       int colonneLong = c.getColumnIndex("longitude");
           int colonneLat = c.getColumnIndex("latitude");
           GeoPoint gP1=null, gP2;
           if (c != null) {
                       float lat = c.getFloat(colonneLat);
                       float longi = c.getFloat(colonneLong);
                	   gP1 = new GeoPoint(microdegrees(longi),microdegrees(lat));

                  
                   while (c.moveToNext()) {
                       /* Retrieve the values of the Entry
                        * the Cursor is pointing to. */
                       float lat1 = c.getFloat(colonneLat);
                       float long1 = c.getFloat(colonneLong);
                       /* Add current Entry to results. */
                       gP2 = new GeoPoint(microdegrees(long1),microdegrees(lat1));
                       afficherSegment(gP1, gP2, mapv, canvas, color);
                       gP1 = gP2;
                   }
           }
           c.close();
           Bdd.db.close();
        }
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