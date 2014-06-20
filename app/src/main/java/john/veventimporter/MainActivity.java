package john.veventimporter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import john.veventimporter.fragments.MainFragment;
import john.veventimporter.fragments.UriSelectFragment;

public class MainActivity extends Activity implements UriSelectFragment
        .OnFragmentInteractionListener {
    private static final int FILE_SELECT_CODE = 0;

    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.container);
        if (mMainFragment == null) {
            Uri uri = null;
            if (savedInstanceState == null) {
                Intent intent = getIntent();
                if (intent.getAction().contentEquals(Intent.ACTION_VIEW)) {
                    uri = intent.getData();
                }
            }
            mMainFragment = MainFragment.newInstance(uri);
            getFragmentManager().beginTransaction().add(R.id.container, mMainFragment).commit();
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (mMainFragment != null) {
                        mMainFragment.setSelectedUri(data.getData());
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBrowseClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/calendar");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent,
                    getString(R.string.calendar_chooser_title)), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, R.string.no_file_manager_info, Toast.LENGTH_LONG).show();
        }
    }
}
