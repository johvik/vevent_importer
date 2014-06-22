package john.veventimporter.data;

import junit.framework.TestCase;

import java.util.List;

import john.veventimporter.mock.FakeAndroidCalendarCursor;
import john.veventimporter.mock.FakeContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

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
        assertThat(calendars, equalTo(androidCalendarCursor.getData()));

        androidCalendarCursor.addData(mAndroidCalendar);

        calendars = AndroidCalendar.getCalendars(context);
        assertEquals(1, calendars.size());
        assertThat(calendars, equalTo(androidCalendarCursor.getData()));
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
        assertEquals("Same object", 0, mAndroidCalendar.compareTo(mAndroidCalendar));

        assertEquals("Same arguments", 0, mAndroidCalendar.compareTo(new AndroidCalendar(id,
                name)));

        assertThat("Smaller id", 0, lessThan(mAndroidCalendar.compareTo(new AndroidCalendar(id -
                1, name))));

        assertThat("Bigger id", 0, greaterThan(mAndroidCalendar.compareTo(new AndroidCalendar(id
                + 1, name))));

        assertThat("Smaller name", 0, lessThan(mAndroidCalendar.compareTo(new AndroidCalendar(id,
                "a" + name))));

        assertThat("Bigger name", 0, greaterThan(mAndroidCalendar.compareTo(new AndroidCalendar
                (id, "b" + name))));
    }

    public void testEquals() {
        assertEquals("Same object", true, mAndroidCalendar.equals(mAndroidCalendar));

        assertEquals("Same arguments", true, mAndroidCalendar.equals(new AndroidCalendar(id,
                name)));

        assertEquals("Another type", false, mAndroidCalendar.equals(new Object()));

        assertEquals("Other arguments", false, mAndroidCalendar.equals(new AndroidCalendar(id -
                1, name)));
    }

    public void testHashCode() {
        assertEquals("Same object", mAndroidCalendar.hashCode(), mAndroidCalendar.hashCode());

        assertEquals("Same arguments", mAndroidCalendar.hashCode(), new AndroidCalendar(id,
                name).hashCode());

        assertNotEquals("Smaller id", mAndroidCalendar.hashCode(), new AndroidCalendar(id - 1,
                name).hashCode());

        assertNotEquals("Bigger id", mAndroidCalendar.hashCode(), new AndroidCalendar(id + 1,
                name).hashCode());

        assertNotEquals("Smaller name", mAndroidCalendar.hashCode(), new AndroidCalendar(id,
                "a" + name).hashCode());

        assertNotEquals("Bigger name", mAndroidCalendar.hashCode(), new AndroidCalendar(id,
                "b" + name).hashCode());
    }
}
