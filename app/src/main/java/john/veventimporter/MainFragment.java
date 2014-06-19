package john.veventimporter;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

public class MainFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<List<AndroidCalendar>> {
    private OnFragmentInteractionListener mListener;
    private AndroidCalendarListAdapter mAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdapter = new AndroidCalendarListAdapter(getActivity());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final Spinner spinnerCalendars = (Spinner) rootView.findViewById(R.id.spinnerCalendars);
        spinnerCalendars.setEmptyView(rootView.findViewById(R.id.textViewEmptySpinner));
        spinnerCalendars.setAdapter(mAdapter);
        Button buttonImport = (Button) rootView.findViewById(R.id.buttonImport);
        buttonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    Object o = spinnerCalendars.getSelectedItem();
                    Long calendarId;
                    if (o == null) {
                        calendarId = null;
                    } else {
                        calendarId = ((AndroidCalendar) o).getId();
                    }
                    mListener.onImportButtonClick(calendarId);
                }
            }
        });

        // Load calendars
        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<List<AndroidCalendar>> onCreateLoader(int i, Bundle bundle) {
        return new AndroidCalendarLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<AndroidCalendar>> listLoader,
                               List<AndroidCalendar> androidCalendars) {
        mAdapter.setData(androidCalendars);
    }

    @Override
    public void onLoaderReset(Loader<List<AndroidCalendar>> listLoader) {
        mAdapter.setData(null);
    }

    public interface OnFragmentInteractionListener {
        public void onImportButtonClick(Long calendarId);
    }
}
