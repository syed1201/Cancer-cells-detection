package avalanche.example.com.avalanche;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteTableLockedException;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Stats extends AppCompatActivity {

    //global vars
    int currentWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        buildHighScoreTable();

    }

    // Dynamically adds table rows, with data, to the GUI
    public void buildHighScoreTable() {

        TableLayout layout = (TableLayout) findViewById(R.id.highScoresLayout);

        Map<String, ?> highScores = getHighScores();
        Iterator it = highScores.entrySet().iterator();
        int i = 0;

        while(it.hasNext()) {

            //Retrieving data from prefs map form
            Map.Entry pair = (Map.Entry)it.next();
            String nameAndYear = (String)pair.getKey();
            List<String> keySplitter = Arrays.asList(nameAndYear.split(","));
            float userScore = (float)pair.getValue();

            ///DEBUG
            System.out.println(userScore);

            String userName = keySplitter.get(0);
            String userYear = keySplitter.get(1);
            String userScoreString = Float.toString(userScore) + "%";

            // Dynamically creating high score table rows
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);



            TextView name = new TextView(this);
            name.setText(userName);
            TextView score = new TextView(this);
            score.setText(userScoreString);
            TextView  year = new TextView(this);
            year.setText(userYear);

            TableRow.LayoutParams param = new TableRow.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f );

            name.setLayoutParams(param);
            year.setLayoutParams(param);
            score.setLayoutParams(param);

            name.setGravity(Gravity.CENTER);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            score.setGravity(Gravity.CENTER);
            score.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            year.setGravity(Gravity.CENTER);
            year.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


            row.addView(name);
            row.addView(year);
            row.addView(score);
            layout.addView(row, i);
            ++i;

        }

    }

    public Map<String, ?> getHighScores() {

        SharedPreferences prefs = this.getSharedPreferences("highScorePrefs", Context.MODE_PRIVATE);
        return prefs.getAll();
    }


}
