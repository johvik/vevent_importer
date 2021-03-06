package john.veventimporter.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import john.veventimporter.R;
import john.veventimporter.data.VEventUtils;
import john.veventimporter.services.VEventStoreIntentService;

public class MainFragment extends Fragment {
    private static final String ARG_URI = "uri";

    private CalendarSelectFragment mCalendarSelectFragment;
    private UriSelectFragment mUriSelectFragment;
    private ReminderSelectFragment mReminderSelectFragment;

    private Toast mPreviousImportToast;
    private Button mButtonImport;

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
        Uri uri = null;
        if (mUriSelectFragment == null) {
            if (getArguments() != null) {
                uri = getArguments().getParcelable(ARG_URI);
            }
            mUriSelectFragment = UriSelectFragment.newInstance(uri);
            getFragmentManager().beginTransaction().add(R.id.fragmentUriSelect,
                    mUriSelectFragment).commit();
        } else {
            uri = mUriSelectFragment.getSelectedUri();
        }

        mCalendarSelectFragment = (CalendarSelectFragment) getFragmentManager().findFragmentById
                (R.id.fragmentCalendarSelect);
        if (mCalendarSelectFragment == null) {
            mCalendarSelectFragment = CalendarSelectFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.fragmentCalendarSelect,
                    mCalendarSelectFragment).commit();
        }

        mReminderSelectFragment = (ReminderSelectFragment) getFragmentManager().findFragmentById
                (R.id.fragmentReminderSelect);
        if (mReminderSelectFragment == null) {
            mReminderSelectFragment = ReminderSelectFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.fragmentReminderSelect,
                    mReminderSelectFragment).commit();
        }

        mButtonImport = (Button) rootView.findViewById(R.id.buttonImport);
        mButtonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importData(mCalendarSelectFragment.getSelectedCalendarId(),
                        mUriSelectFragment.getSelectedUri(),
                        mReminderSelectFragment.getSelectedReminder());
            }
        });
        updateImportButton(uri);
        return rootView;
    }

    private void updateImportButton(Uri uri) {
        if (uri == null) {
            mButtonImport.setText(R.string.button_import);
            mButtonImport.setEnabled(false);
        } else {
            mButtonImport.setText(getString(R.string.button_import_arg,
                    VEventUtils.uriToString(getActivity(), uri)));
            mButtonImport.setEnabled(true);
        }
    }

    private void importData(Long calendarId, Uri uri, Integer reminder) {
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
        setSelectedUri(null);
        VEventStoreIntentService.startActionImport(getActivity(), calendarId, uri, reminder);
        mPreviousImportToast = Toast.makeText(getActivity(), R.string.import_started,
                Toast.LENGTH_SHORT);
        mPreviousImportToast.show();

        // Quit after clicking import when its a action view
        if (isActionView()) {
            getActivity().finish();
        }
    }

    private boolean isActionView() {
        return getActivity().getIntent().getAction().contentEquals(Intent.ACTION_VIEW);
    }

    /**
     * Sets the selected uri.
     *
     * @param uri The new uri.
     */
    public void setSelectedUri(Uri uri) {
        if (mUriSelectFragment != null) {
            mUriSelectFragment.setSelectedUri(uri);
        }
        updateImportButton(uri);
    }
}
