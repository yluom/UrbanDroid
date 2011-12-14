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
        
        OnClickListener menuSwitcher = new OnClickListener()
        {
          @Override
          public void onClick(View actuelView)
          {
            setContentView(R.layout.tarif);
          }
        };
        ImageView iv = (ImageView) findViewById(R.id.btn_tar);
        iv.setOnClickListener(menuSwitcher);
        
       /* iv = (ImageView) findViewById(R.id.plan);
        iv.setOnClickListener(changerTexte);*/
    }

}