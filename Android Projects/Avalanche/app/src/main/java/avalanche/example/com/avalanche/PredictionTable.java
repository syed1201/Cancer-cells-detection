package avalanche.example.com.avalanche;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.predictor.spi.BayesPredictor;
import com.predictor.spi.Predictor;
import com.predictor.spi.StatsPredictor;
import com.predictor.spi.PiRatingsPredictor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Vector;



import avalanche.example.com.avalanche.DBPackage.DatabaseHelper;

public class PredictionTable extends AppCompatActivity {

    int currentWeek;
    String[] teams;
    String year;
    String userName;
    int gameCount, userCount, powerCount, bayesCount, piCount;

    private static StatsPredictor service;
    private static BayesPredictor service2;
    private static PiRatingsPredictor service3;
    Predictor p;
    Predictor bayes;
    Predictor piRatings;
    Boolean bayesInit;
    Map<String, Double> prOutput;
    Map<String, Double> bayesOutput;
    Map<String, Double> piOutput;

    TableLayout bottomTable;
    RadioGroup radGrp;

    private static HashMap<Integer,String> numMap=new HashMap<Integer, String>();
    private static HashMap<String,Integer> stringMap=new HashMap<String, Integer>();
    public static void  initMap() {
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
            stringMap.put("Cardinals", 0);
            stringMap.put("Falcons", 1);
            stringMap.put("Ravens", 2);
            stringMap.put("Bills", 3);
            stringMap.put("Panthers", 4);
            stringMap.put("Bears", 5);
            stringMap.put("Bengals", 6);
            stringMap.put("Browns", 7);
            stringMap.put("Cowboys", 8);
            stringMap.put("Broncos", 9);
            stringMap.put("Lions", 10);
            stringMap.put("Packers", 11);
            stringMap.put("Texans", 12);
            stringMap.put("Colts", 13);
            stringMap.put("Jaguars", 14);
            stringMap.put("Chiefs", 15);
            stringMap.put("Dolphins", 16);
            stringMap.put("Vikings", 17);
            stringMap.put("Patriots", 18);
            stringMap.put("Saints", 19);
            stringMap.put("Giants", 20);
            stringMap.put("Jets", 21);
            stringMap.put("Raiders", 22);
            stringMap.put("Eagles", 23);
            stringMap.put("Steelers", 24);
            stringMap.put("Chargers", 25);
            stringMap.put("49ers", 26);
            stringMap.put("Seahawks", 27);
            stringMap.put("Rams", 28);
            stringMap.put("Buccaneers", 29);
            stringMap.put("Titans", 30);
            stringMap.put("Redskins", 31);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initMap();
        ServiceLoader<Predictor> loader = ServiceLoader.load(Predictor.class);
        Iterator<Predictor> providers = loader.iterator();
        p = providers.next(); //gets the StatsPredictor object
        bayes = providers.next();
        piRatings = providers.next();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_table);
        Button nextWeekButton = (Button)this.findViewById(R.id.nextWeekButton);

        // Next week button function
        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showResults();
            }
        });

        //Get year and userName from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            year = extras.getString("year");
            userName = extras.getString("userName");
        }


        userCount = bayesCount = powerCount = 0;
        gameCount = 0;
        teams = getTeamNames();
        currentWeek = 0;
        bottomTable = (TableLayout)findViewById(R.id.TableLayout2);

        updateWeek(null);
    }


    // Writes data to the table based on current week and year
    protected void updateWeek(View v) {

        //USED TO CLEAR HIGH SCORES
        //SharedPreferences prefs2 = this.getSharedPreferences("highScorePrefs", Context.MODE_PRIVATE);
        //prefs2.edit().clear().commit();

        if(currentWeek >= 17) {

            // Resources needed for adding data to sharedPrefs
            double userTotalScore = roundToTwo(100.0*((double) userCount) /((double) gameCount));
            float finalScore = (float)userTotalScore;
            String entryKey = userName + "," +year;

            //Saves highscores for use in highscore table
            SharedPreferences prefs = this.getSharedPreferences("highScorePrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat(entryKey, finalScore);
            editor.commit();

            this.finish();
        }

        if(currentWeek == 16) {
            finalResults();
            currentWeek++;
        }

        //Delete old radioGroup, and creates new one
        bottomTable.removeAllViews();
        radGrp = new RadioGroup(this);

        //Get rankings
        prOutput = new HashMap<String, Double>();
        bayesOutput = new HashMap<String, Double>();
        piOutput = new HashMap<String, Double>();
        if(currentWeek == 0) {
            Vector<String> vecInp = createBeysianInput();
            bayes.Init(vecInp, year);
            prOutput = p.Init(getPrePowerWeeks(), year);
            piOutput = piRatings.Init(getAllWeeks(), year);
        }
        else{
            prOutput = p.getPowerRankings(getSeasonPowerWeeks(currentWeek));
            piOutput = piRatings.getPowerRankings(getSeasonPowerWeeks(currentWeek));
        }

        // Title
        TextView title = (TextView) findViewById(R.id.textView1_1);
        String titleText = ""+ year + ", WEEK " + Integer.toString(++currentWeek);
        title.setText(titleText);

        bayesOutput = bayes.getPowerRankings(getWeek(currentWeek, Integer.parseInt(year)));

        //Add data to table
        for (int i = 0; i < 32; ++i) {

            // Set team names
            String ResID = "textView" + (i + 3) + "_1";
            int id = getResources().getIdentifier(ResID, "id", getPackageName());
            TextView text = (TextView) findViewById(id);
            String newString = teams[i];
            text.setText(newString);

            // Set power rankings
            ResID = "textView" + (i+3) + "_2";
            id = getResources().getIdentifier(ResID, "id", getPackageName());
            text = (TextView)findViewById(id);
            if (prOutput.get(teams[i]) != null)
                newString = Double.toString(roundToTwo(prOutput.get(teams[i])));
            else newString = "N/A";
            text.setText(newString);

            // Set bayes rankings
            ResID = "textView" + (i+3) + "_3";
            id = getResources().getIdentifier(ResID, "id", getPackageName());
            text = (TextView)findViewById(id);
            if (bayesOutput.get(teams[i]) == null)
                newString = "BYE";
            else
                newString = Double.toString(roundToTwo(bayesOutput.get(teams[i])));
            text.setText(newString);

            // set pi rankings
            ResID = "textView" + (i+3) + "_4";
            id = getResources().getIdentifier(ResID, "id", getPackageName());
            text = (TextView)findViewById(id);
            if (piOutput.get(teams[i]) == null)
                newString = "N/A";
            else
                newString = Double.toString(roundToTwo(piOutput.get(teams[i])));
            text.setText(newString);

        }

        writeGuesser(currentWeek);

    }

    //Writes the bottom half of the view dynamically, allowing the user to guess which teams they think will win.
    public void writeGuesser(int week) {

        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String table = "scheduleData";
        String columns[] = {"home", "away"};
        String select = "week=? AND year=?";
        String selectArgs[] = {Integer.toString(week), year};
        Cursor resultSet = db.query(table, columns, select, selectArgs, null, null, null, null);

        TableLayout tableDisplay = (TableLayout)findViewById(R.id.TableLayout2);
        resultSet.moveToFirst();

        // Dynamically create bottom table
        for(int i =0; i<resultSet.getCount() ; ++i) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            // Add radio buttons
            RadioGroup rg = new RadioGroup(this);
            rg.setOrientation(LinearLayout.HORIZONTAL);
            for(int j=0; j<2; ++j){
                RadioButton rb = new RadioButton(this);
                rb.setTextColor(Color.WHITE);

                ColorStateList colorStateList = new ColorStateList(
                        new int[][] {
                                new int[] {-android.R.attr.state_enabled},
                                new int[] {android.R.attr.state_enabled}
                        },
                        new int[] {
                                Color.WHITE,
                                Color.RED
                        }
                );
                rb.setButtonTintList(colorStateList);

                if(j==0) rb.setChecked(true); //Makes default choice the home team
                rb.setId((i*2)+j);
                rb.setText(resultSet.getString(j));
                rg.addView(rb);
            }

            // Add objects to the view
            row.addView(rg);
            tableDisplay.addView(row,i);

            resultSet.moveToNext();
        }

        db.close();

    }

    //Shows alert box with results; called when new week is pressed, then subsequently calls UpdateWeek();
    public void showResults() {

        //Gets scores
        double userScore = getUserScore(currentWeek);
        double powerScore = getPowerScore(currentWeek);
        double bayesScore = getBayesScore(currentWeek);
        double piScore = getPiScore(currentWeek);

        //creation of score display alertBox
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Score: "+roundToTwo(100.0*userScore)+"% Correct\n" +
                "Power Score: " + roundToTwo(100.0*powerScore)+"% Correct\n" +
                "Bayes Score: " + roundToTwo(100.0*bayesScore)+"% Correct\n" +
                "Pi Score: " + roundToTwo(100.0*piScore)+"% Correct");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateWeek(null); //updates to new week when OK is pressed
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    //Displayed at the end of 16 week game; shows final results for user and all prediction algorithms
    public void finalResults() {

        double userTotalScore = ((double) userCount) /((double) gameCount);
        double powerTotalScore = ((double) powerCount) /((double) gameCount);
        double bayesTotalScore = ((double) bayesCount) /((double) gameCount);
        double piTotalScore = ((double) piCount) /((double) gameCount);

        //creation of score display alertBox
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("TOTAL Score: "+roundToTwo(100.0*userTotalScore)+"% Correct\n" +
                "TOTAL Power Score: " + roundToTwo(100.0*powerTotalScore)+"% Correct\n" +
                "TOTAL Bayes Score: " + roundToTwo(100.0*bayesTotalScore)+"% Correct\n" +
                "TOTAL Pi Score: " + roundToTwo(100.0*piTotalScore)+"% Correct");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateWeek(null);
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    //---------------------------------------SPI Helper Functions-------------------------------------------
    // Used to initialize and get the first week(?) of Beysian data
    public Vector<String> createBeysianInput (){
        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        //Poll from the regular season data
        String table = "scheduleData";
        String columns[] = {"home", "away", "result", "year"};
        Cursor resultSet = db.query(table, columns, null, null,null,null,null,null );

        resultSet.moveToFirst();
        Vector<String> beysianInput = new Vector<String>();
        for(int i = 0; i < resultSet.getCount()-1; ++i){
            String home = resultSet.getString(0);
            String away = resultSet.getString(1);
            String result = resultSet.getString(2);
            String currYear = resultSet.getString(3);
            String type = "reg";
            //If the year is less than the chosen year, add it to the input.
            // " || year == "2012" "  needs to be removed once we scrape the appropriate data.
            //if ((Integer.parseInt(currYear) < Integer.parseInt(year)) || currYear.equals("2012")) {
            if ((Integer.parseInt(currYear) < Integer.parseInt(year))) {
                //Assign the appropriate team numbers to the teams
                String num1 = Integer.toString(stringMap.get(home));
                String num2 = Integer.toString(stringMap.get(away));

                String finalResult = num1 + "\t" + home + "\t" + type + "\t" + num2 + "\t" + away + "\t" + result;
                //System.out.println(finalResult);
                beysianInput.add(finalResult);
            }
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
            String currYear = resultSet.getString(3);
            String type = "pre";

            if ((Integer.parseInt(currYear) < Integer.parseInt(year))) {
                //Assign team numbers
                String num1 = Integer.toString(stringMap.get(home));
                String num2 = Integer.toString(stringMap.get(away));

                String finalResult = num1 + "\t" + home + "\t" + type + "\t" + num2 + "\t" + away + "\t" + result;
                //System.out.println(finalResult);
                beysianInput.add(finalResult);
            }
            resultSet.moveToNext();
        }
        db.close();

        return beysianInput;
    }

    // Used to get subsequent weeks of Beysian data
    public Vector<String> getWeek(int week, int year) {
        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        //Poll from the regular season data
        String table = "scheduleData";
        String columns[] = {"home", "away", "result"};
        String select = "week=? AND year=?";
        String selectArgs[] = {Integer.toString(week), Integer.toString(year)};
        Cursor resultSet = db.query(table, columns, select, selectArgs,null,null,null,null );

        resultSet.moveToFirst();

        Vector<String> returnValues = new Vector<String>();

        for(int i = 0; i < resultSet.getCount(); ++i){
            String home = resultSet.getString(0);
            String away = resultSet.getString(1);
            String teamNum1 = Integer.toString(stringMap.get(home));
            String teamNum2 = Integer.toString(stringMap.get(away));
            //String result = resultSet.getString(2);
            String result = "/";

            String output = teamNum1+"\t"+home+"\t/\t"+teamNum2+"\t"+away+"\t"+result;
            returnValues.add(output);
            resultSet.moveToNext();
        }

        db.close();

        return returnValues;
    }

    //Preseason power ranking input getter function
    public Vector<String> getPrePowerWeeks() {

        Vector<String> returnValues = new Vector<String>();
        returnValues.add("KEY:year,KEY:week,Home,HomeScore,Away,AwayScore,");
        returnValues.add(",class java.lang.Integer,class java.lang.Integer,class java.lang.String,class java.lang.Integer,class java.lang.String,class java.lang.Integer,");

        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Poll from the Preseason data
        String table = "PrescheduleData";
        String columns[] = {"year","week","home","homeScore", "away","awayScore"};
        String select = "year=?";
        String selectArgs[] = {year};
        Cursor resultSet = db.query(table, columns, select, selectArgs,null,null,null,null );

        resultSet.moveToFirst();

        for(int i = 0; i < resultSet.getCount(); ++i){
            String thisYear = resultSet.getString(0);
            String week = resultSet.getString(1);
            String home = resultSet.getString(2);
            String homeScore = resultSet.getString(3);
            String away = resultSet.getString(4);
            String awayScore = resultSet.getString(5);

            String output = ","+thisYear+","+week+","+home+","+homeScore+","+away+","+awayScore+",";
            returnValues.add(output);
            //System.out.println(output);
            resultSet.moveToNext();
        }

        db.close();

        return returnValues;
    }

    public Vector<String> getAllWeeks() {

        Vector<String> returnValues = new Vector<String>();
        returnValues.add("KEY:year,KEY:week,Home,HomeScore,Away,AwayScore,");
        returnValues.add(",class java.lang.Integer,class java.lang.Integer,class java.lang.String,class java.lang.Integer,class java.lang.String,class java.lang.Integer,");

        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Poll from the Preseason data
        String table = "scheduleData";
        String columns[] = {"year","week","home","homeScore", "away","awayScore"};
        //String select = "year=?";
        //String selectArgs[] = {year};
        //Cursor resultSet = db.query(table, columns, select, selectArgs,null,null,null,null );
        Cursor resultSet = db.query(table, columns, null,null,null,null,null,null );
        resultSet.moveToFirst();

        for(int i = 0; i < resultSet.getCount(); ++i){
            String thisYear = resultSet.getString(0);
            String week = resultSet.getString(1);
            String home = resultSet.getString(2);
            String homeScore = resultSet.getString(3);
            String away = resultSet.getString(4);
            String awayScore = resultSet.getString(5);
            //if (Integer.parseInt(thisYear) < Integer.parseInt(year) || thisYear.equals("2012")) {
            if (Integer.parseInt(thisYear) < Integer.parseInt(year)) {

                String output = "," + thisYear + "," + week + "," + home + "," + homeScore + "," + away + "," + awayScore + ",";
                returnValues.add(output);
            }
            //System.out.println(output);
            resultSet.moveToNext();
        }

        db.close();

        return returnValues;
    }

    //Normal season power ranking input getter function
    public Vector<String> getSeasonPowerWeeks (int week) {
        Vector<String> returnValues = new Vector<String>();
        returnValues.add("KEY:year,KEY:week,Home,HomeScore,Away,AwayScore,");
        returnValues.add(",class java.lang.Integer,class java.lang.Integer,class java.lang.String,class java.lang.Integer,class java.lang.String,class java.lang.Integer,");

        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Poll from the Preseason data
        String table = "scheduleData";
        String columns[] = {"year","week","home","homeScore", "away","awayScore"};
        String select = "week=? AND year=?";
        String selectArgs[] = {Integer.toString(week),year};
        Cursor resultSet = db.query(table, columns, select, selectArgs,null,null,null,null );

        resultSet.moveToFirst();

        for(int i = 0; i < resultSet.getCount(); ++i){
            String thisYear = resultSet.getString(0);
            String thisWeek = resultSet.getString(1);
            String home = resultSet.getString(2);
            String homeScore = resultSet.getString(3);
            String away = resultSet.getString(4);
            String awayScore = resultSet.getString(5);

            String output = ","+thisYear+","+thisWeek+","+home+","+homeScore+","+away+","+awayScore+",";
            returnValues.add(output);
            //System.out.println(output);
            resultSet.moveToNext();
        }

        db.close();

        return returnValues;
    }


    //--------------------------------------Other Helper Functions-------------------------------------------
    // Helper function; returns array of teamNames from strings.xml
    public String[] getTeamNames() {
        Resources res = getResources();
        String[] teams = res.getStringArray((R.array.nflTeams));
        return teams;
    }

    //Helper function; returns the user's score
    public double getUserScore(int week) {

        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String table = "scheduleData";
        String columns[] = {"result"};
        String select = "week=? AND year=?";
        String selectArgs[] = {Integer.toString(week), year};
        Cursor resultSet = db.query(table, columns, select, selectArgs,null,null,null,null );

        resultSet.moveToFirst();
        int guessCount = 0;
        int currentCorrect = 0;
        for(int i =0; i<resultSet.getCount(); ++i) {

            RadioButton tempHomeButton = (RadioButton)findViewById(i*2);
            if(tempHomeButton.isChecked() && resultSet.getString(0).equals("W"))
                currentCorrect++;
            else if(!(tempHomeButton.isChecked() || resultSet.getString(0).equals("W")))
                currentCorrect++;
            guessCount++;

            resultSet.moveToNext();
        }
        db.close();

        gameCount += guessCount;
        userCount += currentCorrect;


        Double totalScore = ((double)currentCorrect)/((double)guessCount);
        System.out.println(totalScore);
        return totalScore;
    }

    //Helper function; returns the score the power ranking algorithm predicted
    public double getPowerScore(int week) {
        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String table = "scheduleData";
        String columns[] = {"home","homeScore","away","awayScore"};
        String select = "week=? AND year=?";
        String selectArgs[] = {Integer.toString(week), year};
        Cursor resultSet = db.query(table, columns, select, selectArgs,null,null,null,null );
        resultSet.moveToFirst();


        int countNumber = 0;
        int countCorrect = 0;
        for(int i =0; i<resultSet.getCount(); ++i) {
            int homeScore = Integer.parseInt(resultSet.getString(1));
            int awayScore = Integer.parseInt(resultSet.getString(3));
            double powerScoreHome = prOutput.get(resultSet.getString(0));
            double powerScoreAway = prOutput.get(resultSet.getString(2));

            //Logic to check if Power rankings guess was correct
            if(((powerScoreHome+3) >= (powerScoreAway)) && (homeScore > awayScore) )
                countCorrect++;
            else if (((powerScoreHome+3) < powerScoreAway) && (homeScore < awayScore))
                countCorrect++;

            countNumber++;
            resultSet.moveToNext();
        }
        db.close();

        powerCount += countCorrect;

        double finalScore = ((double)countCorrect) / ((double)countNumber);
        return finalScore;
    }

    //Helper function; returns the score that Bayes Theorem algorithm predicted
    public double getBayesScore(int week) {

        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String table = "scheduleData";
        String columns[] = {"home","homeScore","away","awayScore"};
        String select = "week=? AND year=?";
        String selectArgs[] = {Integer.toString(week), year};
        Cursor resultSet = db.query(table, columns, select, selectArgs,null,null,null,null );
        resultSet.moveToFirst();


        int countNumber = 0;
        int countCorrect = 0;
        for(int i =0; i<resultSet.getCount(); ++i) {
            int homeScore = Integer.parseInt(resultSet.getString(1));
            int awayScore = Integer.parseInt(resultSet.getString(3));
            double bayesScoreHome = bayesOutput.get(resultSet.getString(0));
            double bayesScoreAway = bayesOutput.get(resultSet.getString(2));

            //Logic to check if Bayes rankings guess was correct
            if(bayesScoreHome >= .5 && (homeScore > awayScore) )
                countCorrect++;
            else if (bayesScoreAway > .5 && (homeScore < awayScore))
                countCorrect++;

            countNumber++;
            resultSet.moveToNext();
        }
        db.close();

        bayesCount += countCorrect;

        double finalScore = ((double)countCorrect) / ((double)countNumber);
        return finalScore;
    }

    //Helper function; returns the score that Pi Rating algorithm predicted
    public double getPiScore(int week) {

        DatabaseHelper dbHelper = new DatabaseHelper(this); //needs to be a context of an activity. if not called from an activity class then you need to pass the context through parameters (See scraper)
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String table = "scheduleData";
        String columns[] = {"home","homeScore","away","awayScore"};
        String select = "week=? AND year=?";
        String selectArgs[] = {Integer.toString(week), year};
        Cursor resultSet = db.query(table, columns, select, selectArgs,null,null,null,null );
        resultSet.moveToFirst();


        int countNumber = 0;
        int countCorrect = 0;
        for(int i =0; i<resultSet.getCount(); ++i) {
            int homeScore = Integer.parseInt(resultSet.getString(1));
            int awayScore = Integer.parseInt(resultSet.getString(3));
            double piScoreHome = piOutput.get(resultSet.getString(0));
            double piScoreAway = piOutput.get(resultSet.getString(2));

            //Logic to check if Bayes rankings guess was correct
            if(piScoreHome >= piScoreAway && (homeScore > awayScore) )
                countCorrect++;
            else if (piScoreHome < piScoreAway && (homeScore < awayScore))
                countCorrect++;

            countNumber++;
            resultSet.moveToNext();
        }
        db.close();

        piCount += countCorrect;

        double finalScore = ((double)countCorrect) / ((double)countNumber);
        return finalScore;
    }

    // Helper function; used to round outputs to two decimal points
    public Double roundToTwo(double input) {return Math.round(input*100.0)/100.0;}
}
