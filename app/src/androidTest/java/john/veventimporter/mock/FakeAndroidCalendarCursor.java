package john.veventimporter.mock;

import android.test.mock.MockCursor;

import java.util.ArrayList;
import java.util.List;

import john.veventimporter.data.AndroidCalendar;

public class FakeAndroidCalendarCursor extends MockCursor {
    private List<AndroidCalendar> mData;
    private int position;

    public FakeAndroidCalendarCursor() {
        setData(new ArrayList<AndroidCalendar>());
    }

    public void addData(AndroidCalendar calendar) {
        mData.add(calendar);
        position = -1;
    }

    public List<AndroidCalendar> getData() {
        return mData;
    }

    public void setData(List<AndroidCalendar> data) {
        mData = data;
        position = -1;
    }

    @Override
    public boolean moveToNext() {
        position++;
        return position < mData.size();
    }

    @Override
    public long getLong(int columnIndex) {
        return mData.get(position).getId();
    }

    @Override
    public String getString(int columnIndex) {
        return mData.get(position).getName();
    }

    @Override
    public void close() {
        mData = null;
        position = -1;
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
