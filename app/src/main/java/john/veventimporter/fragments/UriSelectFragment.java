package john.veventimporter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import john.veventimporter.R;
import john.veventimporter.data.VEventUtils;

public class UriSelectFragment extends Fragment {
    private static final int FILE_SELECT_CODE = 0;
    private static final String ARG_URI = "uri";
    private static final String STATE_URI = "state_uri";

    private TextView mTextViewUri;
    private Uri mUri;

    public UriSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uri The uri.
     * @return A new instance of fragment VEventFragment.
     */
    public static UriSelectFragment newInstance(Uri uri) {
        UriSelectFragment fragment = new UriSelectFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (getArguments() != null) {
                mUri = getArguments().getParcelable(ARG_URI);
            }
        } else {
            mUri = savedInstanceState.getParcelable(STATE_URI);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_URI, mUri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_uri_select, container, false);

        Button buttonBrowse = (Button) rootView.findViewById(R.id.buttonBrowse);
        buttonBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        mTextViewUri = (TextView) rootView.findViewById(R.id.textViewUri);
        updateUriText();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    mUri = data.getData();
                    updateUriText();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/calendar");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent,
                    getString(R.string.calendar_chooser_title)), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), R.string.no_file_manager_info, Toast.LENGTH_LONG).show();
        }
    }

    private void updateUriText() {
        if (mUri != null) {
            mTextViewUri.setText(VEventUtils.uriToString(getActivity(), mUri));
        } else {
            mTextViewUri.setText(getString(R.string.no_file_selected));
        }
    }

    /**
     * Gets the selected uri.
     *
     * @return The uri or null.
     */
    public Uri getSelectedUri() {
        return mUri;
    }
}
