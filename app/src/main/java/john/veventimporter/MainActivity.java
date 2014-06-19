package john.veventimporter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.fortuna.ical4j.model.component.VEvent;

import java.util.ArrayList;

public class MainActivity extends Activity implements MainFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container,
                    MainFragment.newInstance()).commit();
        }

        Intent intent = getIntent();
        if (intent.getAction().contentEquals(Intent.ACTION_VIEW)) {
            try {
                ArrayList<VEvent> events = VEventUtils.parseUri(this, intent.getData());
                for (VEvent e : events) {
                    Log.d("MainActivity", "" + e);
                }
                if (events.size() > 0) {
                    ContentValues values = VEventUtils.toContentValues(events.get(0), -1);
                    Log.d("MainActivity", "" + values);
                }
            } catch (VEventException e) {
                e.printStackTrace();
            }
        }

        ArrayList<AndroidCalendar> calendars = AndroidCalendar.getCalendars(this);
        for (AndroidCalendar c : calendars) {
            Log.d("MainActivity", "" + c);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImportButtonClick(Long calendarId) {
        Log.d("MainActivity", "onImportButtonClick " + calendarId);
    }
}
