package john.veventimporter.data;

import junit.framework.TestCase;

import java.util.List;

import john.veventimporter.mock.FakeAndroidCalendarCursor;
import john.veventimporter.mock.FakeContext;

public class AndroidCalendarTest extends TestCase {
    private AndroidCalendar mAndroidCalendar;
    private long id = 1;
    private String name = "abc";

    @Override
    protected void setUp() throws Exception {
        mAndroidCalendar = new AndroidCalendar(id, name);
    }

    public void testGetCalendars() {
        FakeAndroidCalendarCursor androidCalendarCursor = new FakeAndroidCalendarCursor();
        FakeContext context = new FakeContext(androidCalendarCursor);

        List<AndroidCalendar> calendars = AndroidCalendar.getCalendars(context);
        assertEquals(0, calendars.size());

        androidCalendarCursor.addData(mAndroidCalendar);

        calendars = AndroidCalendar.getCalendars(context);
        assertEquals(1, calendars.size());
        assertEquals(0, mAndroidCalendar.compareTo(calendars.get(0)));
    }

    public void testGetId() {
        assertEquals(id, mAndroidCalendar.getId());
    }

    public void testGetName() {
        assertEquals(name, mAndroidCalendar.getName());
    }

    public void testToString() {
        assertEquals(name + " - " + id, mAndroidCalendar.toString());
    }

    public void testCompareTo() {
        AndroidCalendar other = new AndroidCalendar(id, name);
        assertEquals(0, mAndroidCalendar.compareTo(other));

        other = new AndroidCalendar(id - 1, name);
        assertEquals(true, mAndroidCalendar.compareTo(other) > 0);

        other = new AndroidCalendar(id + 1, name);
        assertEquals(true, mAndroidCalendar.compareTo(other) < 0);

        other = new AndroidCalendar(id, "a" + name);
        assertEquals(true, mAndroidCalendar.compareTo(other) > 0);

        other = new AndroidCalendar(id, "b" + name);
        assertEquals(true, mAndroidCalendar.compareTo(other) < 0);
    }
}
