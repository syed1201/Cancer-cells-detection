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


public class Duas extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duas);



        String[] duas ={
                "To quench any kind of thirst"
                , "Dua for increase in sustenance" ,
        "Dua for increase in sustenance and repayment of debts",
                "Dua for safeguard against all tribulations",
        "Dua of 4 billion good deeds"
                ,"Dua for spread of Noor & Ayat to get up at a particular time",
        "Dua for cure from illness",
                "Dua for safeguard against disease" ,
                "Dua for safety of Imaan",
                "Dua of 7 benefits",
        "Dua of Ism e Azam",
                "Dua for protection",
                "Dua of 5 major benefits",
                "Dua for obtaining wealth",
        "7 HAYQALS" ,
                "Dua to safeguard against calamities and oppressor",
        "Magnificent Dua",
                "Dua for increase in memory",
                "Dua before starting a journey" ,
                "Dua of treasure of heaven"};



        ListView hl =(ListView)findViewById(R.id.hl);

        ListAdapter adapter = new CustomAdapter1(this, duas);


        final Intent a1 = new Intent(this,A1.class);//"To quench any kind of thirst"
        final Intent a2 = new Intent(this,A2.class);//"Dua for increase in sustenance"
        final Intent a3 = new Intent(this,A3.class);//"Dua for increase in sustenance and repayment of debts"
        final Intent a4 = new Intent(this,A4.class);//" "Dua for safeguard against all tribulations"
        final Intent a5 = new Intent(this,A5.class);//  "Dua of 4 billion good deeds"
        final Intent a6 = new Intent(this,A6.class);//"Dua for spread of Noor & Ayat to get up at a particular time"
        final Intent a7 = new Intent(this,A7.class);//  "Dua for cure from illness"
        final Intent a8 = new Intent(this,A8.class);//"Dua for safeguard against disease"
        final Intent a9 = new Intent(this,A9.class);//"Dua for safety of Imaan"
        final Intent a10 = new Intent(this,A10.class);//Dua of 7 benefits",
        final Intent a11 = new Intent(this,A11.class);//Dua of Ism e Azam"
        final Intent a12 = new Intent(this,A12.class);//Dua for protection"
        final Intent a13 = new Intent(this,A13.class);//"Dua of 5 major benefits"
        final Intent a14 = new Intent(this,A14.class);//"Dua for obtaining wealth",
        final Intent a15 = new Intent(this,A15.class);// 7 HAYQALS
        final Intent a16 = new Intent(this,A16.class);// "Dua to safeguard against calamities and oppressor",
        final Intent a17 = new Intent(this,A17.class);// "Magnificent Dua"
        final Intent a18 = new Intent(this,A18.class);//"Dua for increase in memory"
        final Intent a19 = new Intent(this,A19.class);//"Dua before starting a journey"
        final Intent a20 = new Intent(this,A20.class);//"Dua of treasure of heaven"




        hl.setAdapter(adapter);



        hl.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        if (position == 0) startActivity(a1);
                        else if (position == 1) startActivity(a2);
                        else if (position == 2) startActivity(a3);
                        else if (position == 3) startActivity(a4);
                        else if (position == 4) startActivity(a5);
                        else if (position == 5) startActivity(a6);
                        else if (position == 6) startActivity(a7);
                        else if (position == 7) startActivity(a8);
                        else if (position == 8) startActivity(a9);
                        else if (position == 9) startActivity(a10);
                        else if (position == 10) startActivity(a11);
                        else if (position == 11) startActivity(a12);
                        else if (position == 12) startActivity(a13);
                        else if (position == 13) startActivity(a14);
                        else if (position == 14) startActivity(a15);
                        else if (position == 15) startActivity(a16);
                        else if (position == 16) startActivity(a17);
                        else if (position == 17) startActivity(a18);
                        else if (position == 18) startActivity(a19);
                        else if (position == 19) startActivity(a20);

                    }
                }
        );

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_duas, menu);
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
