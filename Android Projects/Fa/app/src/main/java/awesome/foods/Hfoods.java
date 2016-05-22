package awesome.foods;

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

import com.example.android.foods.R;


public class Hfoods extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hfoods);

        String[] foods ={"Biryani Nawabi","Pulao","Mirchi Ka Salan" ,"Paaya" ,"Qabooli Biryani",
                "Kheema Pulao","Khorma","Ghosht Pasinde"," Diwani Handi ","Bagar e Baigan","Murgh Do Pyaza",
                "Khatti Arbi Ka Salan"};


        final Intent biryani = new Intent(this,Biryani.class);//Biryani Nawabi
        final Intent b1 = new Intent(this,B1.class);//Pulao
        final Intent b2 = new Intent(this,B2.class);//Mirchi ka salan
        final Intent b3 = new Intent(this,B3.class);//paaya
        final Intent b4 = new Intent(this,B4.class);//qabooli
        final Intent b5 = new Intent(this,B5.class);//kheema pulao
        final Intent b6 = new Intent(this,B6.class);//khorma
        final Intent b7 = new Intent(this,B7.class);//ghosht pasinde
        final Intent b8 = new Intent(this,B8.class);//diwani handi
        final Intent b9 = new Intent(this,B9.class);//bagar e baigan
        final Intent b10 = new Intent(this,B10.class);//murgh do pyaaza
        final Intent b11 = new Intent(this,B11.class);//Khatti arbi ka salan





        ListView hl =(ListView)findViewById(R.id.hl);

        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,foods);

        hl.setAdapter(adapter);

        //code for clicking on list items
        hl.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(position==0) startActivity(biryani);
                        else if(position==1) startActivity(b1);
                        else if(position==2) startActivity(b2);
                        else if(position==3) startActivity(b3);
                        else if(position==4) startActivity(b4);
                        else if(position==5) startActivity(b5);
                        else if(position==6) startActivity(b6);
                        else if(position==7) startActivity(b7);
                        else if(position==8) startActivity(b8);
                        else if(position==9) startActivity(b9);
                        else if(position==10) startActivity(b10);
                        else if (position==11)startActivity(b11);
                    }
                }
        );














    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hfoods, menu);
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
