package john.veventimporter.mock;

import android.content.ContentResolver;
import android.provider.CalendarContract;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

public class FakeContext extends MockContext {
    private FakeAndroidCalendarCursor mAndroidCalendarCursor;

    public FakeContext(FakeAndroidCalendarCursor androidCalendarCursor) {
        mAndroidCalendarCursor = androidCalendarCursor;
    }

    @Override
    public ContentResolver getContentResolver() {
        MockContentResolver contentResolver = new MockContentResolver();
        FakeContentProvider provider = new FakeContentProvider(mAndroidCalendarCursor);
        contentResolver.addProvider(CalendarContract.AUTHORITY, provider);
        return contentResolver;
    }
}
