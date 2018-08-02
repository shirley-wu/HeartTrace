package com.example.dell.db;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by wu-pc on 2018/8/2.
 */

public class PictureTest {

    @Test
    public void testParse() {
        Picture picture = new Picture("img_1340156180471.jpg");
        assertEquals(1340156180471L, picture.getId());
    }

}
