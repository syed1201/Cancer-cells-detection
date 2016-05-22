package avalanche.example.com.avalanche;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import avalanche.example.com.avalanche.DBPackage.DatabaseHelper;
import avalanche.example.com.avalanche.ScraperPackage.Scraper;

public class Launch extends AppCompatActivity {


    ListView list;
    String[] web = {
            "High Scores", //called Stats in files
            "Update",
            //"Show Me Options",
            "Predictions",
            "Stats " //called MainActivity in files
    };
    Integer[] imageId = {
            R.drawable.high_score,
            R.drawable.update,
            R.drawable.play,
           // R.drawable.logo,
            R.drawable.logo
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        CustomList adapter = new
                CustomList(Launch.this, web, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Launch.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

                if(position==0)Stats(view);
                else if(position==1)Update(view);
                //else if(position==2)Showmeoptions(view);
                else if(position==2)Predictions(view);
                else if(position==3)Oldstats(view);

            }
        });

        // Scraping the data
        String[] DBs = this.databaseList();
        boolean dbExists = false;
        for (String DB : DBs){
            if (DB.equals("ScheduleData.db")){
                dbExists = true;
            }
        }
        if (!dbExists){
            Integer default_year = 2011;
            new Scraper(this).execute(default_year);
        }



    }

   /* public void Showmeoptions(View v)
    {
        Intent i = new Intent(this, ShowMeOptions.class);

        startActivity(i);

    }
*/
    public void Predictions(View v)
    {
        Intent i = new Intent(this, Predictions.class);

        startActivity(i);

    }

    public void Stats(View v)
    {
        Intent i = new Intent(this, Stats.class);

        startActivity(i);

    }

    public void Update(View v)
    {
        Intent i = new Intent(this, Update.class);

        startActivity(i);

    }


    public void Oldstats(View v)
    {
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);

    }



}
