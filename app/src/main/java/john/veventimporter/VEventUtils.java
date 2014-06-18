package john.veventimporter;

import android.content.Context;
import android.net.Uri;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

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

