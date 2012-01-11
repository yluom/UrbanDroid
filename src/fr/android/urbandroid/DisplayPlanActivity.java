package fr.android.urbandroid;

import fr.android.urbandroid.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.content.Context;
import android.view.MotionEvent;

public class DisplayPlanActivity extends Activity implements OnTouchListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant);
        
        // code qui gere les transistions du menu
        OnClickListener menuSwitcher = new OnClickListener()
        {
          public void onClick(View actualView)
          {
              Intent intent;
        	  switch(actualView.getId())
        	  {
        	  	case R.id.btn_tar: intent = new Intent(DisplayPlanActivity.this, DisplayTarifActivity.class);
        	  						startActivity(intent); break;
         	  	case R.id.btn_iti: intent = new Intent(DisplayPlanActivity.this, DisplayItineraireActivity.class);
									startActivity(intent); break;
        	  	case R.id.btn_hor: intent = new Intent(DisplayPlanActivity.this, DisplayHorairesActivity.class);
        	  						startActivity(intent); break;
        	  	case R.id.btn_fav: intent = new Intent(DisplayPlanActivity.this, DisplayFavorisActivity.class);
        	  						startActivity(intent); break;
        	  	case R.id.btn_pla: intent = new Intent(DisplayPlanActivity.this, UrbanDroidActivity.class);
        	  						startActivity(intent); break;
        	  	case R.id.ongletGoogleMap: intent = new Intent(DisplayPlanActivity.this, DisplayPlanGoogleActivity.class);
					startActivity(intent); break;
        	  	case R.id.ongletTisseo: intent = new Intent(DisplayPlanActivity.this, DisplayPlanActivity.class);
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
        
        ImageView iv8 = (ImageView) findViewById(R.id.plantisseo);
        iv8.setOnTouchListener(this);
    }
    
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist;
    private static final String TAG = "Plan Tisseo Touch" ;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    
    private float spacing(MotionEvent event) 
    {
    	float x = event.getX(0) - event.getX(1);
    	float y = event.getY(0) - event.getY(1);
    	return FloatMath.sqrt(x * x + y * y);
	}
    
    private void midPoint(PointF point, MotionEvent event) {
    	   float x = event.getX(0) + event.getX(1);
    	   float y = event.getY(0) + event.getY(1);
    	   point.set(x / 2, y / 2);
    	}
    
	public boolean onTouch(View v, MotionEvent event) 
	{
		ImageView view = (ImageView) v;
		switch (event.getAction() & MotionEvent.ACTION_MASK) 
		{
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				Log.d(TAG, "mode=DRAG" );
				Log.d(TAG, "x=" + event.getX());
				Log.d(TAG, "y=" + event.getY());
				mode = DRAG;
			    break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				Log.d(TAG, "mode=NONE" );
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 100f) 
				{
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM" );
				}
				break;
				
		   case MotionEvent.ACTION_MOVE:
			   if (mode == DRAG) 
			   {
				   matrix.set(savedMatrix);
				   matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
			   }
			   else if (mode == ZOOM) 
			   {
		    	  float newDist = spacing(event);
		    	  Log.d(TAG, "newDist=" + newDist);
		    	  if (newDist > 100f) 
		    	  {
		    		  matrix.set(savedMatrix);
		    		  float scale = newDist / oldDist;
		    		  matrix.postScale(scale, scale, mid.x, mid.y);
		    	  }
		    	  break;
			   }	
		}
		view.setImageMatrix(matrix);
		return true;
	}
}