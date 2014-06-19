package john.veventimporter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidCalendarLoader extends MyAsyncTaskLoader<List<AndroidCalendar>> {
    public AndroidCalendarLoader(Context context) {
        super(context);
    }

    @Override
    public List<AndroidCalendar> loadInBackground() {
        ArrayList<AndroidCalendar> list = AndroidCalendar.getCalendars(getContext());
        Collections.sort(list);
        return list;
    }
}
