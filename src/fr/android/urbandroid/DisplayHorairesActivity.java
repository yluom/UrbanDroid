package fr.android.urbandroid;
 
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;
 
public class DisplayHorairesActivity extends Activity
{
     public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.horaires);
     
     OnClickListener menuSwitcher = new OnClickListener()
     {
       @Override
       public void onClick(View actualView)
       {
           Intent intent;
      	  switch(actualView.getId())
      	  {
      	  	case R.id.btn_tar: intent = new Intent(DisplayHorairesActivity.this, DisplayTarifActivity.class);
      	  						startActivity(intent); break;
       	  	case R.id.btn_iti: intent = new Intent(DisplayHorairesActivity.this, DisplayItineraireActivity.class);
 									startActivity(intent); break;
      	  	case R.id.btn_hor: intent = new Intent(DisplayHorairesActivity.this, DisplayHorairesActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_fav: intent = new Intent(DisplayHorairesActivity.this, DisplayFavorisActivity.class);
      	  						startActivity(intent); break;
      	  	case R.id.btn_pla: intent = new Intent(DisplayHorairesActivity.this, UrbanDroidActivity.class);
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
     
   }
}