package john.veventimporter.mock;

import android.database.Cursor;
import android.net.Uri;
import android.test.mock.MockContentProvider;

public class FakeContentProvider extends MockContentProvider {
    private FakeAndroidCalendarCursor mAndroidCalendarCursor;

    public FakeContentProvider(FakeAndroidCalendarCursor androidCalendarCursor) {
        mAndroidCalendarCursor = androidCalendarCursor;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return mAndroidCalendarCursor;
    }
}
