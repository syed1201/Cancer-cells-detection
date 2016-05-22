package avalanche.example.com.avalanche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import avalanche.example.com.avalanche.ScraperPackage.Scraper;

public class Update extends AppCompatActivity {

    Spinner yearSpinner;

    String[] years;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        years = new String[12];
        for(int i = 0; i < 12; ++i) {
            years[i] = Integer.toString(i+2003);
        }

        yearSpinner = (Spinner)findViewById(R.id.yearSpinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.fullYears, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        yearSpinner.setAdapter(adapter1);
        final Button updateButton = (Button)findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String[] DBs = Update.this.databaseList();
                for (String DB : DBs) {
                    if (DB.equals("ScheduleData.db")) {
                        Update.this.deleteDatabase(DB);
                    }
                }
                Integer newYear = Integer.parseInt(yearSpinner.getSelectedItem().toString());

                //scrape one additional year for use in prediction formulas
                new Scraper(Update.this).execute(newYear - 1);

            }
        });
    }

}
