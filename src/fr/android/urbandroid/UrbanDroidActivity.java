package fr.android.urbandroid;

import android.app.Activity;
import android.content.Intent;
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
              Intent intent;
        	  switch(actualView.getId())
        	  {
        	  	case R.id.btn_tar: intent = new Intent(UrbanDroidActivity.this, DisplayTarifActivity.class);
        	  						startActivity(intent); break;
         	  	case R.id.btn_iti: intent = new Intent(UrbanDroidActivity.this, DisplayItineraireActivity.class);
									startActivity(intent); break;
        	  	case R.id.btn_hor: intent = new Intent(UrbanDroidActivity.this, DisplayHorairesActivity.class);
        	  						startActivity(intent); break;
        	  	case R.id.btn_fav: intent = new Intent(UrbanDroidActivity.this, DisplayFavorisActivity.class);
        	  						startActivity(intent); break;
        	  	case R.id.btn_pla: intent = new Intent(UrbanDroidActivity.this, UrbanDroidActivity.class);
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
        
       /* iv = (ImageView) findViewById(R.id.plan);
        iv.setOnClickListener(changerTexte);*/
    }

}