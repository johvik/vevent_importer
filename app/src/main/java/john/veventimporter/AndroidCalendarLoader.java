package john.veventimporter;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidCalendarLoader extends AsyncTaskLoader<List<AndroidCalendar>> {
    private List<AndroidCalendar> mData;

    public AndroidCalendarLoader(Context context) {
        super(context);
    }

    @Override
    public List<AndroidCalendar> loadInBackground() {
        ArrayList<AndroidCalendar> list = AndroidCalendar.getCalendars(getContext());
        Collections.sort(list);
        return list;
    }

    @Override
    public void deliverResult(List<AndroidCalendar> data) {
        if (isReset()) {
            return;
        }
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }
        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();
        mData = null;
    }
}
