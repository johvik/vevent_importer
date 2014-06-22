package john.veventimporter.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AndroidCalendar implements Comparable<AndroidCalendar> {
    private static final String[] CALENDAR_PROJECTION = new String[]{Calendars._ID,
            Calendars.CALENDAR_DISPLAY_NAME};

    private final long id;
    private final String name;

    public AndroidCalendar(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Retrieves the available calendars on the device.
     *
     * @param context Context used to resolve the content.
     * @return The list of AndroidCalendars.
     */
    public static List<AndroidCalendar> getCalendars(Context context) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        }
        Uri uri = Calendars.CONTENT_URI;
        Cursor cur = context.getContentResolver().query(uri, CALENDAR_PROJECTION, null, null, null);
        ArrayList<AndroidCalendar> res = new ArrayList<AndroidCalendar>();
        while (cur.moveToNext()) {
            long id = cur.getLong(0);
            String name = cur.getString(1);
            res.add(new AndroidCalendar(id, name));
        }
        return res;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " - " + id;
    }

    @Override
    public int compareTo(@NotNull AndroidCalendar other) {
        int cmp = name.compareTo(other.name);
        if (cmp == 0) {
            cmp = Long.valueOf(id).compareTo(other.id);
        }
        return cmp;
    }
}
