package net.jsunit.results;

import junit.framework.TestCase;

import java.io.File;

public class SkinTest extends TestCase {
    private Skin skin;

    protected void setUp() throws Exception {
        super.setUp();
        skin = new Skin(3, new File("c:\\my xsl", "my_cool_jsunit_skin.xsl"));
    }

    public void testSimple() throws Exception {
        assertEquals(3, skin.getId());
        assertEquals("my_cool_jsunit_skin", skin.getDisplayName());
        assertEquals("c:\\my xsl\\my_cool_jsunit_skin.xsl", skin.getPath());
    }

    public void testHasId() throws Exception {
        assertFalse(skin.hasId(1));
        assertTrue(skin.hasId(3));
    }

}
