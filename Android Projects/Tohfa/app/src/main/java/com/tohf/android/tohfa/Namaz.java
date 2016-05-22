package com.tohf.android.tohfa;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


public class Namaz extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namaz);


        String[] namaz ={"Nameaz e Gofaila" , "Namaz e Wahshat" , "Namaz e Hadiya e Waledain",
        "Namaz e Shab"};



        ListView hl =(ListView)findViewById(R.id.hl);

        ListAdapter adapter = new CustomAdapter2(this,namaz);


        final Intent c1 = new Intent(this,C1.class);//Nameaz e Gofaila"
        final Intent c2 = new Intent(this,C2.class);//"Namaz e Wahshat"
        final Intent c3 = new Intent(this,C3.class);//"Namaz e Hadiya e Waledain",
        final Intent c4 = new Intent(this,C4.class);//" "Namaz e Shab"



        hl.setAdapter(adapter);

        hl.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        if (position == 0) startActivity(c1);
                        else if (position == 1) startActivity(c2);
                        else if (position == 2) startActivity(c3);
                        else if (position == 3) startActivity(c4);


                    }
                }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_namaz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
