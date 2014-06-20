package john.veventimporter.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import john.veventimporter.R;
import john.veventimporter.services.VEventStoreIntentService;

public class MainFragment extends Fragment {
    private static final String ARG_URI = "uri";

    private CalendarSelectFragment mCalendarSelectFragment;
    private UriSelectFragment mUriSelectFragment;
    private Toast mPreviousImportToast;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uri The uri.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(Uri uri) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mUriSelectFragment = (UriSelectFragment) getFragmentManager().findFragmentById(R.id
                .fragmentUriSelect);
        if (mUriSelectFragment == null) {
            Uri uri = null;
            if (getArguments() != null) {
                uri = getArguments().getParcelable(ARG_URI);
            }
            mUriSelectFragment = UriSelectFragment.newInstance(uri);
            getFragmentManager().beginTransaction().add(R.id.fragmentUriSelect,
                    mUriSelectFragment).commit();
        }

        mCalendarSelectFragment = (CalendarSelectFragment) getFragmentManager().findFragmentById
                (R.id.fragmentCalendarSelect);
        if (mCalendarSelectFragment == null) {
            mCalendarSelectFragment = CalendarSelectFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.fragmentCalendarSelect,
                    mCalendarSelectFragment).commit();
        }
        Button buttonImport = (Button) rootView.findViewById(R.id.buttonImport);
        buttonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importData(mCalendarSelectFragment.getSelectedCalendarId(),
                        mUriSelectFragment.getSelectedUri());
            }
        });
        return rootView;
    }

    private void importData(Long calendarId, Uri uri) {
        if (mPreviousImportToast != null) {
            mPreviousImportToast.cancel();
        }
        if (calendarId == null) {
            mPreviousImportToast = Toast.makeText(getActivity(), R.string.no_calendar_selected,
                    Toast.LENGTH_SHORT);
            mPreviousImportToast.show();
            return;
        }
        if (uri == null) {
            mPreviousImportToast = Toast.makeText(getActivity(), R.string.no_uri_selected,
                    Toast.LENGTH_SHORT);
            mPreviousImportToast.show();
            return;
        }
        VEventStoreIntentService.startActionImport(getActivity(), calendarId, uri);
        mPreviousImportToast = Toast.makeText(getActivity(), R.string.import_started,
                Toast.LENGTH_SHORT);
        mPreviousImportToast.show();
    }
}
