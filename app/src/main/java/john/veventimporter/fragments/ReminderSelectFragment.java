package john.veventimporter.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import john.veventimporter.MainActivity;
import john.veventimporter.R;

public class ReminderSelectFragment extends Fragment {
    private static final String SELECTED_POS = "rem_sel_pos";

    private Spinner mSpinnerReminder;
    private int mSelectedPosition;

    public ReminderSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReminderSelectFragment.
     */
    public static ReminderSelectFragment newInstance() {
        return new ReminderSelectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        mSelectedPosition = settings.getInt(SELECTED_POS, -1);
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        mSelectedPosition = mSpinnerReminder.getSelectedItemPosition();
        editor.putInt(SELECTED_POS, mSelectedPosition);
        editor.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedPosition = savedInstanceState.getInt(SELECTED_POS);
        }
        View rootView = inflater.inflate(R.layout.fragment_reminder_select, container, false);

        mSpinnerReminder = (Spinner) rootView.findViewById(R.id.spinnerReminder);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.reminders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerReminder.setAdapter(adapter);

        mSpinnerReminder.setSelection(mSelectedPosition);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mSelectedPosition = mSpinnerReminder.getSelectedItemPosition();
        outState.putInt(SELECTED_POS, mSelectedPosition);
        super.onSaveInstanceState(outState);
    }

    /**
     * Gets the selected reminder time in minutes.
     *
     * @return The time or null if none is selected.
     */
    public Integer getSelectedReminder() {
        Object o = mSpinnerReminder.getSelectedItem();
        if (o != null) {
            String reminder = o.toString();
            if (reminder.equals("None")) {
                return null;
            } else if (reminder.equals("Default")) {
                // http://developer.android.com/reference/android/provider/CalendarContract
                // .RemindersColumns.html#MINUTES
                return -1;
            } else {
                String[] split = reminder.split(" ");
                if (split.length == 2) {
                    try {
                        int value = Integer.parseInt(split[0]);
                        if (split[1].contains("minute")) {
                            return value;
                        } else if (split[1].contains("hour")) {
                            return value * 60;
                        } else if (split[1].contains("day")) {
                            return value * 60 * 24;
                        } else if (split[1].contains("week")) {
                            return value * 60 * 24 * 7;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                }
            }
        }
        return null;
    }
}
