package com.tohf.android.tohfa;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class List extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



        String[] duas ={"Dua e Hazin","Dua e Noor" , "Dua e Kumail" ,
        "Dua e Ganjul Arsh", "Dua e Meraj","Dua e Yastasheer" , "Dua e Tawassul",
        "Dua e Ahad","Dua e Samaat","Dua e Mashlool"  };



        ListView hl =(ListView)findViewById(R.id.hl);

        ListAdapter adapter = new CustomAdapter(this, duas);


        final Intent b1 = new Intent(this,B1.class);//Dua e Hazin
        final Intent b2 = new Intent(this,B2.class);//Dua e Noor
        final Intent b3 = new Intent(this,B3.class);//Dua e Kumail"
        final Intent b4 = new Intent(this,B4.class);//"Dua e Ganjul Arsh"
        final Intent b5 = new Intent(this,B5.class);//Dua e Meraj"
        final Intent b6 = new Intent(this,B6.class);//"Dua e Yastasheer"
        final Intent b7 = new Intent(this,B7.class);//"Dua e Tawassul"
        final Intent b8 = new Intent(this,B8.class);//"Dua e Ahad"
        final Intent b9 = new Intent(this,B9.class);//"Dua e Samaat"
        final Intent b10 = new Intent(this,B10.class);//"Dua e Mashlool"

        hl.setAdapter(adapter);



        hl.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                         if (position == 0) startActivity(b1);
                        else if (position == 1) startActivity(b2);
                        else if (position == 2) startActivity(b3);
                        else if (position == 3) startActivity(b4);
                        else if (position == 4) startActivity(b5);
                        else if (position == 5) startActivity(b6);
                        else if (position == 6) startActivity(b7);
                        else if (position == 7) startActivity(b8);
                        else if (position == 8) startActivity(b9);
                        else if (position == 9) startActivity(b10);

                    }
                }
        );


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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
