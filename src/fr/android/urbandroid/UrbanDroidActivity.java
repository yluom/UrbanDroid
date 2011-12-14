package fr.android.urbandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class UrbanDroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //maj();
        //remplirTout(bdd.sql);
        setContentView(R.layout.plan);
        
        // code qui gere les transistions du menu
        OnClickListener menuSwitcher = new OnClickListener()
        {
          @Override
          public void onClick(View actualView)
          {
        	  switch(actualView.getId())
        	  {
        	  	case R.id.btn_tar: setContentView(R.layout.tarif); break;
        	  	case R.id.btn_iti: setContentView(R.layout.itineraire); break;
        	  	case R.id.btn_hor: setContentView(R.layout.horaires); break;
        	  	case R.id.btn_fav: setContentView(R.layout.favoris); break;
        	  	case R.id.btn_pla: setContentView(R.layout.plan); break;
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
        
       /* iv = (ImageView) findViewById(R.id.plan);
        iv.setOnClickListener(changerTexte);*/
    }

}