package john.veventimporter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import john.veventimporter.R;
import john.veventimporter.data.VEventUtils;

public class UriSelectFragment extends Fragment {
    private static final String ARG_URI = "uri";
    private static final String STATE_URI = "state_uri";

    private OnFragmentInteractionListener mListener;
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
                if (mListener != null) {
                    mListener.onBrowseClick();
                }
            }
        });

        mTextViewUri = (TextView) rootView.findViewById(R.id.textViewUri);
        updateUriText();
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

    /**
     * Sets the selected uri.
     *
     * @param uri The new uri.
     */
    public void setSelectedUri(Uri uri) {
        mUri = uri;
        updateUriText();
    }

    public interface OnFragmentInteractionListener {
        public void onBrowseClick();
    }
}
