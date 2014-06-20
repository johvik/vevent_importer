package john.veventimporter.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import net.fortuna.ical4j.model.component.VEvent;

import java.util.List;

import john.veventimporter.data.VEventException;
import john.veventimporter.data.VEventUtils;

public class VEventStoreIntentService extends IntentService {
    private static final String ACTION_IMPORT = "john.veventimporter.services.action.IMPORT";

    private static final String EXTRA_CALENDARID = "john.veventimporter.services.extra.CALENDARID";
    private static final String EXTRA_URI = "john.veventimporter.services.extra.URI";

    public VEventStoreIntentService() {
        super("VEventStoreIntentService");
        // Restart if it is killed while running
        setIntentRedelivery(true);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionImport(Context context, Long calendarId, Uri uri) {
        Intent intent = new Intent(context, VEventStoreIntentService.class);
        intent.setAction(ACTION_IMPORT);
        intent.putExtra(EXTRA_CALENDARID, calendarId);
        intent.putExtra(EXTRA_URI, uri);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_IMPORT.equals(action)) {
                final Long calendarId = (Long) intent.getSerializableExtra(EXTRA_CALENDARID);
                final Uri uri = intent.getParcelableExtra(EXTRA_URI);
                handleActionImport(calendarId, uri);
            }
        }
    }

    /**
     * Handle action Import in the provided background thread with the provided
     * parameters.
     */
    private void handleActionImport(Long calendarId, Uri uri) {
        if (calendarId == null || uri == null) {
            return; // TODO Notify
        }

        List<VEvent> events;
        try {
            events = VEventUtils.parseUri(this, uri);
        } catch (VEventException e) {
            e.printStackTrace();
            return; // TODO Notify
        }

        for (VEvent event : events) {
            try {
                ContentValues values = VEventUtils.toContentValues(event, calendarId);
                //getContentResolver().insert(uri, values);
            } catch (VEventException e) {
                e.printStackTrace();
                return; // TODO Notify
            }
        }
        Log.d("VEventStoreIntentService", "handleActionImport " + calendarId + " " + uri);
    }
}
