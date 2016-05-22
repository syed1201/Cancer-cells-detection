package avalanche.example.com.avalanche;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.predictor.spi.Predictor;
import com.predictor.spi.StatsPredictor;
import com.predictor.spi.BayesPredictor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Vector;

import avalanche.example.com.avalanche.DBPackage.DatabaseHelper;
import avalanche.example.com.avalanche.ScraperPackage.Scraper;

public class Predictions extends AppCompatActivity {

    private static StatsPredictor service;
    private static BayesPredictor service2;
    Predictor p;
    Predictor bayes;
    Boolean bayesInit;

    private static HashMap<Integer,String> numMap=new HashMap<Integer, String>();
    private static HashMap<String,Integer> stringMap=new HashMap<String, Integer>();
    public static void  initMap(){
        if (numMap.size() == 0) {
            numMap.put(0, "ari");
            numMap.put(1, "atl");
            numMap.put(2, "bal");
            numMap.put(3, "buf");
            numMap.put(4, "car");
            numMap.put(5, "chi");
            numMap.put(6, "cin");
            numMap.put(7, "cle");
            numMap.put(8, "dal");
            numMap.put(9, "den");
            numMap.put(10, "det");
            numMap.put(11, "gb");
            numMap.put(12, "hou");
            numMap.put(13, "ind");
            numMap.put(14, "jax");
            numMap.put(15, "kc");
            numMap.put(16, "mia");
            numMap.put(17, "min");
            numMap.put(18, "ne");
            numMap.put(19, "no");
            numMap.put(20, "nyg");
            numMap.put(21, "nyj");
            numMap.put(22, "oak");
            numMap.put(23, "phi");
            numMap.put(24, "pit");
            numMap.put(25, "sd");
            numMap.put(26, "sf");
            numMap.put(27, "sea");
            numMap.put(28, "la");
            numMap.put(29, "tb");
            numMap.put(30, "ten");
            numMap.put(31, "wsh");
            for (HashMap.Entry<Integer, String> entry : numMap.entrySet()) {
                stringMap.put(entry.getValue(), entry.getKey());
            }
            stringMap.put("Cardinals",0);
            stringMap.put("Falcons",1);
            stringMap.put("Ravens",2);
            stringMap.put("Bills",3);
            stringMap.put("Panthers",4);
            stringMap.put("Bears",5);
            stringMap.put("Bengals",6);
            stringMap.put("Browns",7);
            stringMap.put("Cowboys",8);
            stringMap.put("Broncos",9);
            stringMap.put("Lions",10);
            stringMap.put("Packers",11);
            stringMap.put("Texans",12);
            stringMap.put("Colts",13);
            stringMap.put("Jaguars",14);
            stringMap.put("Chiefs",15);
            stringMap.put("Dolphins",16);
            stringMap.put("Vikings",17);
            stringMap.put("Patriots",18);
            stringMap.put("Saints",19);
            stringMap.put("Giants",20);
            stringMap.put("Jets",21);
            stringMap.put("Raiders",22);
            stringMap.put("Eagles",23);
            stringMap.put("Steelers",24);
            stringMap.put("Chargers",25);
            stringMap.put("49ers",26);
            stringMap.put("Seahawks",27);
            stringMap.put("Rams",28);
            stringMap.put("Buccaneers",29);
            stringMap.put("Titans",30);
            stringMap.put("Redskins",31);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initMap();
        ServiceLoader<Predictor> loader = ServiceLoader.load(Predictor.class);
        bayesInit = false;
        Iterator<Predictor> providers = loader.iterator();
        p = providers.next(); //gets the StatsPredictor object
        bayes = providers.next();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictions);

        Spinner spin1 = (Spinner)findViewById(R.id.spin1);

        // definfing the adapters for years
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spin1.setAdapter(adapter1);

    }


    public void showPredictions(View v)
    {
        Spinner spin = (Spinner)findViewById(R.id.spin1);
        EditText mEdit = (EditText)findViewById(R.id.userName);
        mEdit.setTextColor(Color.WHITE);   //---------------------------------------------------------


        String year = spin.getSelectedItem().toString();
        String userName = mEdit.getText().toString();

        Intent i = new Intent(this, PredictionTable.class);
        i.putExtra("year", year);
        i.putExtra("userName", userName);
        startActivity(i);

    }

    public Vector<String> createBeysianInput (){
        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        //Poll from the regular season data
        String table = "scheduleData";
        String columns[] = {"home", "away", "result"};
        Cursor resultSet = db.query(table, columns, null, null,null,null,null,null );

        resultSet.moveToFirst();
        Vector<String> beysianInput = new Vector<String>();
        for(int i = 0; i < resultSet.getCount()-1; ++i){
            String home = resultSet.getString(0);
            String away = resultSet.getString(1);
            String result = resultSet.getString(2);
            String type = "reg";

            /*
            Code here for assigning numbers to teams
             */
            String num1 = Integer.toString(stringMap.get(home));
            String num2 = Integer.toString(stringMap.get(away));

            String finalResult = num1+"\t"+home+"\t"+type+"\t"+num2+"\t"+away+"\t"+result;
            System.out.println(finalResult);
            beysianInput.add(finalResult);
            resultSet.moveToNext();
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
            String num1 = Integer.toString(stringMap.get(home));
            String num2 = Integer.toString(stringMap.get(away));

            String finalResult = num1+"\t"+home+"\t"+type+"\t"+num2+"\t"+away+"\t"+result;
            beysianInput.add(finalResult);

            resultSet.moveToNext();

        }

        return beysianInput;
    }


}
