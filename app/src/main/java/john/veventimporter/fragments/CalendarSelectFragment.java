package john.veventimporter.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.List;

import john.veventimporter.R;
import john.veventimporter.data.AndroidCalendar;
import john.veventimporter.data.AndroidCalendarListAdapter;
import john.veventimporter.data.AndroidCalendarLoader;

public class CalendarSelectFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<List<AndroidCalendar>> {
    private static final String SELECTED_POS = "sel_pos";

    private Spinner mSpinnerCalendars;
    private AndroidCalendarListAdapter mAdapter;
    private int mSelectedPosition = -1;

    public CalendarSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarSelectFragment.
     */
    public static CalendarSelectFragment newInstance() {
        return new CalendarSelectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedPosition = savedInstanceState.getInt(SELECTED_POS);
        }
        mAdapter = new AndroidCalendarListAdapter(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_calendar_select, container, false);
        mSpinnerCalendars = (Spinner) rootView.findViewById(R.id.spinnerCalendars);
        mSpinnerCalendars.setEmptyView(rootView.findViewById(R.id.textViewEmptySpinner));
        mSpinnerCalendars.setAdapter(mAdapter);

        // Load calendars
        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_POS, mSpinnerCalendars.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<AndroidCalendar>> onCreateLoader(int i, Bundle bundle) {
        return new AndroidCalendarLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<AndroidCalendar>> listLoader,
                               List<AndroidCalendar> androidCalendars) {
        mAdapter.setData(androidCalendars);
        mSpinnerCalendars.setSelection(mSelectedPosition);
    }

    @Override
    public void onLoaderReset(Loader<List<AndroidCalendar>> listLoader) {
        mAdapter.setData(null);
    }

    /**
     * Gets the selected calendar id.
     *
     * @return The selected calendar id or null.
     */
    public Long getSelectedCalendarId() {
        AndroidCalendar calendar = (AndroidCalendar) mSpinnerCalendars.getSelectedItem();
        Long calendarId;
        if (calendar == null) {
            calendarId = null;
        } else {
            calendarId = calendar.getId();
        }
        return calendarId;
    }
}
