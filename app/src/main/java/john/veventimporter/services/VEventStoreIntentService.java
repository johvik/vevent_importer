package john.veventimporter.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;

import net.fortuna.ical4j.model.component.VEvent;

import java.util.List;

import john.veventimporter.R;
import john.veventimporter.data.VEventException;
import john.veventimporter.data.VEventUtils;

public class VEventStoreIntentService extends IntentService {
    private static final String ACTION_IMPORT = "john.veventimporter.services.action.IMPORT";

    private static final String EXTRA_CALENDARID = "john.veventimporter.services.extra.CALENDARID";
    private static final String EXTRA_URI = "john.veventimporter.services.extra.URI";
    private static final String EXTRA_REMINDER = "john.veventimporter.services.extra.REMINDER";

    private static int NOTIFICATION_ID = 0;

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
    public static void startActionImport(Context context, Long calendarId, Uri uri,
                                         Integer reminder) {
        Intent intent = new Intent(context, VEventStoreIntentService.class);
        intent.setAction(ACTION_IMPORT);
        intent.putExtra(EXTRA_CALENDARID, calendarId);
        intent.putExtra(EXTRA_URI, uri);
        intent.putExtra(EXTRA_REMINDER, reminder);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_IMPORT.equals(action)) {
                final Long calendarId = (Long) intent.getSerializableExtra(EXTRA_CALENDARID);
                final Uri uri = intent.getParcelableExtra(EXTRA_URI);
                final Integer reminder = (Integer) intent.getSerializableExtra(EXTRA_REMINDER);
                handleActionImport(calendarId, uri, reminder);
            }
        }
    }

    /**
     * Handle action Import in the provided background thread with the provided
     * parameters.
     */
    private void handleActionImport(Long calendarId, Uri uri, Integer reminder) {
        final int id = NOTIFICATION_ID++;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(), 0);
        Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.drawable
                .ic_stat_import).setContentTitle(getString(R.string.app_name)).setContentIntent
                (pendingIntent).setAutoCancel(true);

        if (calendarId == null || uri == null) {
            builder.setContentText(getString(R.string.notification_import_general_error));
            notificationManager.notify(id, builder.build());
            return;
        }

        builder.setContentText(getString(R.string.notification_import_parsing));
        builder.setProgress(0, 0, true);
        notificationManager.notify(id, builder.build());

        List<VEvent> events;
        try {
            events = VEventUtils.parseUri(this, uri);
        } catch (VEventException e) {
            e.printStackTrace();
            builder.setContentText(getString(R.string.notification_import_parse_error));
            notificationManager.notify(id, builder.build());
            return;
        }

        final int count = events.size();
        builder.setContentText(getString(R.string.notification_import_saving));
        int progress = 0;
        int failCount = 0;
        builder.setProgress(count, progress, false);
        notificationManager.notify(id, builder.build());

        Long firstEventId = null;
        for (VEvent event : events) {
            try {
                ContentValues values = VEventUtils.toContentValues(event, calendarId);
                if (reminder != null) {
                    values.put(Events.HAS_ALARM, 1);
                }
                Uri eventUri = getContentResolver().insert(Events.CONTENT_URI, values);

                long eventId = Long.parseLong(eventUri.getLastPathSegment());
                // Add reminder
                if (reminder != null) {
                    ContentValues reminderValues = new ContentValues();
                    reminderValues.put(Reminders.EVENT_ID, eventId);
                    reminderValues.put(Reminders.MINUTES, reminder);
                    reminderValues.put(Reminders.METHOD, Reminders.METHOD_ALARM);
                    getContentResolver().insert(Reminders.CONTENT_URI, reminderValues);
                }

                if (firstEventId == null) {
                    firstEventId = eventId;
                }
            } catch (VEventException e) {
                failCount++;
            }
            progress++;
            builder.setProgress(count, progress, false);
            notificationManager.notify(id, builder.build());
        }

        // Start the calendar to show the first saved event
        if (firstEventId != null) {
            Uri first = ContentUris.withAppendedId(Events.CONTENT_URI, firstEventId);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(first);
            PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(p);
        }

        final int savedCount = count - failCount;
        builder.setContentText(getResources().getQuantityString(R.plurals
                .notification_import_done, savedCount, savedCount));
        builder.setProgress(0, 0, false);
        notificationManager.notify(id, builder.build());
    }
}
