package john.veventimporter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;

import java.util.ArrayList;

public class AndroidCalendar {
    private static final String[] CALENDAR_PROJECTION = new String[]{Calendars._ID,
            Calendars.CALENDAR_DISPLAY_NAME};
    private long id;
    private String name;

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
    public static ArrayList<AndroidCalendar> getCalendars(Context context) {
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

    @Override
    public String toString() {
        return name + " - " + id;
    }
}
