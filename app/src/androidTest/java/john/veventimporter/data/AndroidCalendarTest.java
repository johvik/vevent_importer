package john.veventimporter.data;

import junit.framework.TestCase;

public class AndroidCalendarTest extends TestCase {
    private AndroidCalendar mAndroidCalendar;
    private long id = 1;
    private String name = "abc";

    @Override
    protected void setUp() throws Exception {
        mAndroidCalendar = new AndroidCalendar(id, name);
    }

    public void testGetCalendars() {
        // TODO
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
