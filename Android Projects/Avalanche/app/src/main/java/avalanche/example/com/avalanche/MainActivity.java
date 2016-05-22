package avalanche.example.com.avalanche;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.predictor.spi.BayesPredictor;
import com.predictor.spi.Predictor;
import com.predictor.spi.StatsPredictor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;

import avalanche.example.com.avalanche.DBPackage.DatabaseHelper;
import avalanche.example.com.avalanche.ScraperPackage.Scraper;

public class MainActivity extends AppCompatActivity {
    Spinner spin1; //= (Spinner)findViewById(R.id.spin1);
    Spinner spin2; //= (Spinner)findViewById(R.id.spin2);

    private static StatsPredictor service;
    private static BayesPredictor service2;





    String[] strings = {"Cardinals","Falcons", "Ravens", "Bills", "Panthers", "Bears", "Bengals", "Browns", "Cowboys", "Broncos", "Lions", "Packers",
            "Texans",
            "Colts",
            "Jaguars",
            "Chiefs",
            "Dolphins",
            "Vikings",
            "Patriots",
            "Saints",
            "Giants",
            "Jets",
            "Raiders",
            "Eagles",
            "Steelers",
            "Chargers",
            "49ers",
            "Seahawks",
            "Rams",
            "Buccaneers",
            "Titans",
            "Redskins"};

    // public String[] subs = strings;
    public int arr_images[] = { R.drawable.arizonacardinals,R.drawable.atlantafalcons,R.drawable.baltimoreravens,
            R.drawable.buffalo,R.drawable.carolina,R.drawable.chicago,R.drawable.cincinati,R.drawable.cleveland,
            R.drawable.dallas,R.drawable.denver,R.drawable.detroit,R.drawable.greenbay,R.drawable.houston,
            R.drawable.indianapolis , R.drawable.jacksonville,R.drawable.kanasas,R.drawable.miami,R.drawable.minnesota,
            R.drawable.newengland,R.drawable.neworleans,R.drawable.newyork,R.drawable.newyorkjets,R.drawable.oakland,
            R.drawable.philadelphia,R.drawable.pittsburgh,R.drawable.sandiego,R.drawable.sanfrancisco,
            R.drawable.seattle,R.drawable.losangeles,R.drawable.tampabay,R.drawable.tennese,R.drawable.washington};










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServiceLoader<Predictor> loader = ServiceLoader.load(Predictor.class);



        Iterator<Predictor> providers = loader.iterator();
        Predictor p = providers.next(); //gets the StatsPredictor object
        Predictor bayes = providers.next();

        spin1 = (Spinner)findViewById(R.id.spin1); // team names
        spin2 = (Spinner)findViewById(R.id.spin2); // years

        spin1.setAdapter(new MyAdapter(this, R.layout.row,strings));




//--------------------------------------------------------------------------------------------------
        // definfing the adapters for teams
     /*   ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nflTeams, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spin1.setAdapter(adapter);
*/
//--------------------------------------------------------------------------------------------------
        // definfing the adapters for years
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.text_view);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter1.clear();

        int lowestYear = getLowestYear();

        for(int i = lowestYear; i < 2016; ++i) {
            adapter1.add(Integer.toString(i));
        }

        // Apply the adapter to the spinner
        spin2.setAdapter(adapter1);








    }


    public void createDatabase()
    {
        // getting the inputs
        //team names
        String team = spin1.getSelectedItem().toString();
        // year name
        String year = spin2.getSelectedItem().toString();


        // creating the table
        TableLayout table = (TableLayout)findViewById(R.id.table);



        //Database Creation
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Query variable creation
        String tabl = "scheduleData";
        String[] columns = {"week", "home", "homeScore", "away", "awayScore"};
        String selection = "year = ? AND (home = ? OR away = ?);";
        String[] selectionArgs = {year, team, team};

        //Query
        Cursor resultSet = db.query(tabl, columns, selection, selectionArgs,null,null,null,null );


        resultSet.moveToFirst();


        for(int i=0 ; i<resultSet.getCount()-1; i++)
        {
            // get the otput Strings
            String week = resultSet.getString(0);
            String home = resultSet.getString(1);
            String homeScore = resultSet.getString(2);
            String away = resultSet.getString(3);
            String awayScore = resultSet.getString(4);


            // creating a textview object of finalOutput
            TextView weekText = new TextView(this);
            TextView homeText = new TextView(this);
            TextView homeScoreText = new TextView(this);
            TextView awayText = new TextView(this);
            TextView awayScoreText = new TextView(this);

            weekText.setText(week);
            homeText.setText(home);
            homeScoreText.setText(homeScore);
            awayText.setText(away);
            awayScoreText.setText(awayScore);

            // Changing parameters
            TableRow.LayoutParams param = new TableRow.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f );
            weekText.setLayoutParams(param);
            homeText.setLayoutParams(param);
            homeScoreText.setLayoutParams(param);
            awayText.setLayoutParams(param);
            awayScoreText.setLayoutParams(param);

            // Formatting
            homeScoreText.setGravity(Gravity.CENTER_HORIZONTAL);
            awayScoreText.setGravity(Gravity.CENTER_HORIZONTAL);


            // creating row
            TableRow r = new TableRow(this);
            r.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            r.addView(weekText);
            r.addView(homeText);
            r.addView(homeScoreText);
            r.addView(awayText);
            r.addView(awayScoreText);


            // adding the view to table
            table.addView(r);

            resultSet.moveToNext();

        }
        resultSet.close();
        resultSet = null;


        // table.addView(r, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));




    }



    public void result(View v)
    {
        // TextView data = (TextView)findViewById(R.id.data);

        TableLayout table = (TableLayout)findViewById(R.id.table);
        table.removeAllViews();

        createDatabase();


    }

    public ArrayList<String> createBeysianInput (){
        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        //Poll from the regular season data
        String table = "scheduleData";
        String columns[] = {"home", "away", "result"};
        Cursor resultSet = db.query(table, columns, null, null,null,null,null,null );

        resultSet.moveToFirst();
        ArrayList<String> beysianInput = new ArrayList<String>();
        for(int i = 0; i < resultSet.getCount()-1; ++i){
            String home = resultSet.getString(0);
            String away = resultSet.getString(1);
            String result = resultSet.getString(2);
            String type = "reg";

            /*
            Code here for assigning numbers to teams
             */
            String num1 = "0";
            String num2 = "0";

            String finalResult = num1+"\t"+home+"\t"+type+"\t"+num2+"\t"+away+"\t"+result;
            beysianInput.add(finalResult);
        }

        //Poll from the pre season data
        table = "PrescheduleData";
        resultSet = db.query(table, columns, null, null,null,null,null,null );

        resultSet.moveToFirst();
        for(int i = 0; i < resultSet.getCount()-1; ++i){
            String home = resultSet.getString(0);
            String away = resultSet.getString(1);
            String result = resultSet.getString(2);
            String type = "pre";

            /*
            Code here for assigning numbers to teams
             */
            String num1 = "0";
            String num2 = "0";

            String finalResult = num1+"\t"+home+"\t"+type+"\t"+num2+"\t"+away+"\t"+result;
            beysianInput.add(finalResult);
        }

        return beysianInput;
    }





    // Adapter class for  custom spinner control
    public class MyAdapter extends ArrayAdapter<String>{

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.company);
            label.setText(strings[position]);
            label.setTextColor(Color.WHITE);

            // TextView sub=(TextView)row.findViewById(R.id.sub);
            //sub.setText(subs[position]);

            ImageView icon=(ImageView)row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);
            return row;
        }
    }

    public int getLowestYear(){
        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Cursor resultSet = db.rawQuery("SELECT MIN(year) FROM scheduleData", null);
        resultSet.moveToFirst();
        return resultSet.getInt(0);
    }




}