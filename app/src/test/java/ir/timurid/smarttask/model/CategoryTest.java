package ir.timurid.smarttask.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryTest {

    @Test
    public void testEquals() {
        Category category = new Category(1, "A", "#123");
        Category category2 = new Category(1, "A", "#123");

        assertEquals(category,category2);
    }

    @Test
    public void testObjRef() {
        Category cat1 = new Category(1, "A", "#123");
        Category cat2 = new Category(1, "A", "#123");
        Category cat3 = cat1;

        assertFalse(cat1 == cat2);
        assertTrue(cat1 == cat3);
    }
}