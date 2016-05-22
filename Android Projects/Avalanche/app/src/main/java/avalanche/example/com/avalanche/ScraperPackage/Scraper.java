package avalanche.example.com.avalanche.ScraperPackage;

import java.util.ArrayList;

import android.app.AlertDialog;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.AsyncTask;

        import avalanche.example.com.avalanche.DBPackage.*;

        import java.io.IOException;
        import java.util.HashMap;
        import java.util.ArrayList;
        import java.util.Iterator;

        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Element;
        import org.jsoup.nodes.Document;
        import org.jsoup.select.Elements;

        import android.database.sqlite.*;

/**
 * Created by Mitchell on 4/7/2016.
 */
public class Scraper extends AsyncTask<Integer, Integer, Context>{
    AlertDialog.Builder scrapeBuilder;
    AlertDialog scrapeAlert;
    Context activityContext;

    public Scraper(Context c) {
        activityContext = c;
    }

    @Override
    protected void  onPreExecute() {
        super.onPreExecute();
        scrapeBuilder = new AlertDialog.Builder(activityContext);
        scrapeBuilder.setMessage("Scraping, please wait.");
        scrapeBuilder.setCancelable(false);

        scrapeAlert = scrapeBuilder.create();

        scrapeAlert.show();
    }

    protected Context doInBackground(Integer... i){
        /*
        Logic needed for implementation of Update feature

        String[] DBs = c[0].databaseList();
        if(DBs.length > 0){
            for(String DB : DBs){
                c[0].deleteDatabase(DB);
            }
        */
        Scrape(i[0]);
        return activityContext;
    }
    @Override
    protected void onPostExecute(Context i) {
        scrapeBuilder.setMessage("Done!");
        scrapeBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        scrapeAlert.hide();
        scrapeAlert = scrapeBuilder.create();

        scrapeAlert.show();
    }

    public void Scrape(int inputYear) {
        DatabaseHelper dbHelper = new DatabaseHelper(activityContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();





        int year = 0;
        int week = 0;

        try {

            //Parser pe = new Parser();

            //create this hashmap here so that it can track stats across weeks. It is reset each year
            HashMap<String,weeklyStats> teamStats = generateTeamHash();

            for (year = inputYear; year < 2016; ++year) {
                //scrape 17 weeks each year
                for (week = 1; week < 18; ++week) {
                    //System.out.println(year + ", " + week);
                    Document ESPN_page = Jsoup.connect("http://espn.go.com/nfl/schedule/_/seasontype/2/year/" + year + "/week/" + week).get();
                    Elements teams;

                    //holds teams on bye week
                    ArrayList<String> byeWeekTeams = new ArrayList<String>();

                    //get table holding bye week info
                    Element byeWeekTable = ESPN_page.select("table").last();
                    //only do if byeweek
                    if (byeWeekTable.select("tr").get(0).text().equals("BYE")) {
                        //second row has team info
                        Element byeWeek = byeWeekTable.select("tr").get(1);
                        teams = byeWeek.getElementsByClass("team-name");
                        for (Element team : teams) {
                            byeWeekTeams.add(team.select("span").text());
                        }
                    }

                    //get all team names on page
                    teams = ESPN_page.getElementsByClass("team-name");
                    boolean postponedFlag = false; //flag to keep track of postponed bills game in 2014
                    ArrayList<Matchup> matchups = new ArrayList<Matchup>();
                    for (int i = 0; i < teams.size() - byeWeekTeams.size(); i = i + 2) {
                        //taking a teams full name (ex "Houston Texans") and stripping out the location (ex "Texans") so that it can be used later as a key
                        String[] temp = teams.get(i).select("abbr").attr("title").split("\\s");
                        String awayTeam = temp[temp.length - 1]; //last item is just team name

                        temp = teams.get(i + 1).select("abbr").attr("title").split("\\s");
                        String homeTeam = temp[temp.length - 1];//last item is just team name

                        if (year == 2014 && week == 12 && postponedFlag == false && homeTeam.equals("Bills")) {
                            postponedFlag = true; //bills game postponed, but listed twice on ESPN, preventing postponed match with no score from being added
                        } else {
                            matchups.add(new Matchup(awayTeam, homeTeam));
                        }
                    }

                    //iterator to go through each matchup. order is maintained so adding scores will be right
                    Iterator<Matchup> matches = matchups.iterator();

                    //each page can have a table count from 1 to 4, depending on if the games were all played on sunday with no byes or
                    //monday, sunday, and thursday, as well as a bye week.
                    //this portion steps through each table and assigns the scores, regardless of how many teams are in each table
                    Elements tables = ESPN_page.select("table");
                    for (int i = 0; i < tables.size(); ++i) {
                        Elements rows = tables.get(i).select("tr");

                        //break out of the loop if the table is of the bye week teams
                        //this will always be the last table, so its safe to break
                        if (rows.get(0).text().equals("BYE"))
                            break;

                        //go through each table grabbing scores and matching them to the correct teams
                        for (int j = 1; j < rows.size(); ++j) {
                            String[] s = rows.get(j).select("td").get(2).text().split("[\\p{Punct}\\s]+");
                            if (s[0].equals("Postponed")) //if postponed then no score listed, so its skipped
                            {
                            } else if (s[0].equals(rows.get(j).select("td").get(0).select("a").select("abbr").text())) { //uses espn formatting to match score to teams
                                Matchup m = matches.next();
                                m.awayScore = Integer.parseInt(s[1]);
                                m.homeScore = Integer.parseInt(s[3]);
                            } else {
                                Matchup m = matches.next();
                                m.homeScore = Integer.parseInt(s[1]);
                                m.awayScore = Integer.parseInt(s[3]);
                            }

                        }
                    }


                    //create a query for each game of the week and put it into the DB

                    for (int i = 0; i < matchups.size(); ++i) {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_YEAR, year);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_WEEK, week);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_HOME, matchups.get(i).home);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_HOMESCORE, matchups.get(i).homeScore);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_AWAY, matchups.get(i).away);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_AWAYSCORE, matchups.get(i).awayScore);
                        if(matchups.get(i).homeScore > matchups.get(i).awayScore)
                            values.put(DatabaseContract.DataEntry.COLUMN_NAME_RESULT, "W");
                        else
                            values.put(DatabaseContract.DataEntry.COLUMN_NAME_RESULT, "L");
                        db.insert(DatabaseContract.DataEntry.TABLE_NAME, null, values);
                    }
                }

                //reset team stats each year
                teamStats = generateTeamHash();
            }

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage() + "Year: " + year + "Week: " + week);
        }


        /**********************************
         * Grab preseason data and put into new file
         */

        try {

            //create this hashmap here so that it can track stats across weeks. It is reset each year
            HashMap<String,weeklyStats> teamStats = generateTeamHash();

            for (year = inputYear; year < 2016; ++year) {
                //scrape 17 weeks each year
                for (week = 2; week < 6; ++week) {
                    //System.out.println(year + ", " + week);
                    Document ESPN_page = Jsoup.connect("http://espn.go.com/nfl/schedule/_/seasontype/1/year/" + year + "/week/" + week).get();
                    Elements teams;

                    //holds teams on bye week
                    ArrayList<String> byeWeekTeams = new ArrayList<String>();
                    /*
                    //get table holding bye week info
                    Element byeWeekTable = ESPN_page.select("table").last();
                    //only do if byeweek

                    if (byeWeekTable.select("tr").get(0).text().equals("BYE")) {
                        //second row has team info
                        Element byeWeek = byeWeekTable.select("tr").get(1);
                        teams = byeWeek.getElementsByClass("team-name");
                        for (Element team : teams){
                            byeWeekTeams.add(team.select("span").text());
                        }
                    }
*/
                    //get all team names on page
                    teams = ESPN_page.getElementsByClass("team-name");
                    boolean postponedFlag = false; //flag to keep track of postponed bills game in 2014
                    ArrayList<Matchup> matchups = new ArrayList<Matchup>();
                    for (int i = 0; i < teams.size() - byeWeekTeams.size(); i = i + 2){
                        //taking a teams full name (ex "Houston Texans") and stripping out the location (ex "Texans") so that it can be used later as a key
                        String[] temp = teams.get(i).select("abbr").attr("title").split("\\s");
                        String awayTeam = temp[temp.length-1]; //last item is just team name

                        temp = teams.get(i+1).select("abbr").attr("title").split("\\s");
                        String homeTeam = temp[temp.length-1];//last item is just team name

                        if (year == 2014 && week == 12 && postponedFlag == false && homeTeam.equals("Bills")) {
                            postponedFlag = true; //bills game postponed, but listed twice on ESPN, preventing postponed match with no score from being added
                        } else {
                            matchups.add(new Matchup(awayTeam, homeTeam));
                        }
                    }

                    //iterator to go through each matchup. order is maintained so adding scores will be right
                    Iterator<Matchup> matches = matchups.iterator();

                    //each page can have a table count from 1 to 4, depending on if the games were all played on sunday with no byes or
                    //monday, sunday, and thursday, as well as a bye week.
                    //this portion steps through each table and assigns the scores, regardless of how many teams are in each table
                    Elements tables = ESPN_page.select("table");
                    for (int i = 0; i < tables.size(); ++i){
                        Elements rows = tables.get(i).select("tr");

                        //break out of the loop if the table is of the bye week teams
                        //this will always be the last table, so its safe to break
                        if (rows.get(0).text().equals("BYE"))
                            break;

                        //go through each table grabbing scores and matching them to the correct teams
                        for (int j = 1; j < rows.size(); ++j){
                            String[] s = rows.get(j).select("td").get(2).text().split("[\\p{Punct}\\s]+");
                            if (s[0].equals("Postponed")) //if postponed then no score listed, so its skipped
                            {}
                            else if (s[0].equals(rows.get(j).select("td").get(0).select("a").select("abbr").text())){ //uses espn formatting to match score to teams
                                Matchup m = matches.next();
                                m.awayScore = Integer.parseInt(s[1]);
                                m.homeScore = Integer.parseInt(s[3]);
                            }
                            else {
                                Matchup m = matches.next();
                                m.homeScore = Integer.parseInt(s[1]);
                                m.awayScore = Integer.parseInt(s[3]);
                            }

                        }
                    }

                    for (int i = 0; i < matchups.size(); ++i) {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_YEAR, year);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_WEEK, week);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_HOME, matchups.get(i).home);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_HOMESCORE, matchups.get(i).homeScore);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_AWAY, matchups.get(i).away);
                        values.put(DatabaseContract.DataEntry.COLUMN_NAME_AWAYSCORE, matchups.get(i).awayScore);
                        if(matchups.get(i).homeScore > matchups.get(i).awayScore)
                            values.put(DatabaseContract.DataEntry.COLUMN_NAME_RESULT, "W");
                        else
                            values.put(DatabaseContract.DataEntry.COLUMN_NAME_RESULT, "L");
                        db.insert("Pre"+DatabaseContract.DataEntry.TABLE_NAME, null, values);
                    }
                }
                teamStats = generateTeamHash();
            }
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage() + "Year: " + year + "Week: " + week);
        }


    }


    //data structure to hold matchup data
    //pretty straight forward class
    public static class Matchup {
        public String home;
        public String away;
        public int homeScore;
        public int awayScore;

        public Matchup(String a, String h) {
            home = h;
            away = a;
            homeScore = 0;
            awayScore = 0;
        }
        public Matchup() {
            home = "";
            away = "";
            homeScore = 0;
            awayScore = 0;
        }
    }

    //class to keep track of stats across weeks
    //yards and attempts are kept for both offense and defense
    //retrieving PYA and DPYA returns strings so I didnt have
    //to cast them since I only use those function calls when adding to the DB
    public static class weeklyStats {
        public double yards;
        public double att;
        public double dyards;
        public double datt;
        public int TO;

        public weeklyStats () {
            yards = 0;
            dyards = 0;
            att = 0;
            datt = 0;
            TO = 0;
        }

        public String getPYA() {
            return Double.toString(yards/att);
        }

        public String getDPYA() {
            return Double.toString(dyards/datt);
        }
    }

    //generates a new hashmap and fills it with each team, the name being the key
    //this can be used to fill an empty hashmap tracking stats or to
    //clear an old one and put in empty data
    public static HashMap<String, weeklyStats> generateTeamHash(){
        HashMap<String, weeklyStats> hm = new HashMap<String, weeklyStats>();
        hm.put("Ravens", new weeklyStats());
        hm.put("Bengals", new weeklyStats());
        hm.put("Browns", new weeklyStats());
        hm.put("Steelers", new weeklyStats());
        hm.put("Bills", new weeklyStats());
        hm.put("Dolphins", new weeklyStats());
        hm.put("Patriots", new weeklyStats());
        hm.put("Jets", new weeklyStats());
        hm.put("Texans", new weeklyStats());
        hm.put("Colts", new weeklyStats());
        hm.put("Jaguars", new weeklyStats());
        hm.put("Titans", new weeklyStats());
        hm.put("Broncos", new weeklyStats());
        hm.put("Chiefs", new weeklyStats());
        hm.put("Raiders", new weeklyStats());
        hm.put("Chargers", new weeklyStats());
        hm.put("Cowboys", new weeklyStats());
        hm.put("Giants", new weeklyStats());
        hm.put("Eagles", new weeklyStats());
        hm.put("Redskins", new weeklyStats());
        hm.put("Bears", new weeklyStats());
        hm.put("Lions", new weeklyStats());
        hm.put("Packers", new weeklyStats());
        hm.put("Vikings", new weeklyStats());
        hm.put("Falcons", new weeklyStats());
        hm.put("Panthers", new weeklyStats());
        hm.put("Saints", new weeklyStats());
        hm.put("Buccaneers", new weeklyStats());
        hm.put("Cardinals", new weeklyStats());
        hm.put("Rams", new weeklyStats());
        hm.put("49ers", new weeklyStats());
        hm.put("Seahawks", new weeklyStats());
        return hm;
    }

    //can be used to find the opponent for the week
    //week is specified by the arraylist passed in
    public static String findOpponent(ArrayList<Matchup> matches, String team){
        for (Matchup m : matches){
            if (m.away.equals(team))
                return m.home;
            else if (m.home.equals(team))
                return m.away;
        }
        return "";
    }
}
