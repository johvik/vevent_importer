package john.veventimporter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract.Events;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Geo;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class VEventUtils {
    private VEventUtils() {
    }

    /**
     * Converts the event to content values for insertion.
     *
     * @param event      Event to transform.
     * @param calendarId Id of the calendar to insert into.
     * @return The transformed content values.
     * @throws VEventException If it fails to convert the event.
     */
    public static ContentValues toContentValues(VEvent event,
                                                long calendarId) throws VEventException {
        if (event == null) {
            throw new VEventException("Event cannot be null");
        }
        ContentValues values = new ContentValues();

        // Start with mandatory values
        values.put(Events.CALENDAR_ID, calendarId);
        DtStart start = event.getStartDate();
        if (start == null) {
            throw new VEventException("DTSTART not found");
        }
        values.put(Events.DTSTART, start.getDate().getTime());
        values.put(Events.EVENT_TIMEZONE, start.getTimeZone().getID());

        // Now see if its recurring or not
        Property rrule = event.getProperty("RRULE");
        Property rdate = event.getProperty("RDATE");
        Duration duration = event.getDuration();
        boolean isRecurring = (rrule != null || rdate != null) && duration != null;
        if (isRecurring) {
            values.put(Events.DURATION, duration.getValue());
            if (rrule != null) {
                values.put(Events.RRULE, rrule.getValue());
            }
            if (rdate != null) {
                values.put(Events.RDATE, rdate.getValue());
            }
            Property exrule = event.getProperty("EXRULE");
            if (exrule != null) {
                values.put(Events.EXRULE, exrule.getValue());
            }
            Property exdate = event.getProperty("EXDATE");
            if (exdate != null) {
                values.put(Events.EXDATE, exdate.getValue());
            }
        } else {
            DtEnd end = event.getEndDate();
            if (end != null) {
                values.put(Events.DTEND, end.getDate().getTime());
                if (duration == null) {
                    values.put(Events.EVENT_END_TIMEZONE, end.getTimeZone().getID());
                }
            } else {
                throw new VEventException("Unable to determine DTEND");
            }
        }

        // Other values
        Organizer organizer = event.getOrganizer();
        if (organizer != null) {
            values.put(Events.ORGANIZER, organizer.getValue());
        }
        Summary summary = event.getSummary();
        if (summary != null) {
            values.put(Events.TITLE, summary.getValue());
        }
        Location location = event.getLocation();
        if (location != null) {
            values.put(Events.EVENT_LOCATION, location.getValue());
        } else {
            Geo geo = event.getGeographicPos();
            if (geo != null) {
                values.put(Events.EVENT_LOCATION, geo.getValue());
            }
        }
        Description description = event.getDescription();
        if (description != null) {
            values.put(Events.DESCRIPTION, description.getValue());
        }
        Uid uid = event.getUid();
        if (uid != null) {
            values.put(Events.UID_2445, uid.getValue());
        }
        return values;
    }

    /**
     * Attempts to parse the data at the uri to an array of VEvents.
     *
     * @param context Context used to resolve file and content.
     * @param uri     Uri to convert.
     * @return The list of VEvents.
     * @throws VEventException
     */
    public static ArrayList<VEvent> parseUri(Context context, Uri uri) throws VEventException {
        if (context == null) {
            throw new VEventException("Context cannot be null");
        }
        if (uri == null) {
            throw new VEventException("Uri cannot be null");
        }
        InputStream inputStream = toInputStream(context, uri);
        if (inputStream == null) {
            throw new VEventException("Failed to resolve input");
        }
        ArrayList<VEvent> res = new ArrayList<VEvent>();
        try {
            Calendar calendar = new CalendarBuilder().build(inputStream);
            for (Object o : calendar.getComponents(Component.VEVENT)) {
                try {
                    VEvent e = (VEvent) o;
                    res.add(e);
                } catch (ClassCastException e) {
                    // Let it continue
                }
            }
        } catch (IOException e) {
            throw new VEventException("Error while reading input");
        } catch (ParserException e) {
            throw new VEventException("Unable to parse input");
        }
        return res;
    }

    /**
     * Converts the uri to an InputStream.
     *
     * @param context Context used to resolve file and content.
     * @param uri     Uri to convert.
     * @return InputStream or null if something went wrong.
     */
    private static InputStream toInputStream(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if (scheme.equals("http") || scheme.equals("https")) {
            try {
                return new URL(uri.getPath()).openConnection().getInputStream();
            } catch (NullPointerException e) {
                return null;
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        } else if (scheme.equals("file") || scheme.equals("content")) {
            try {
                return context.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                return null;
            }
        }
        return null;
    }
}
