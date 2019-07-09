package com.example.swap.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GoodsPageTest {

    GoodsPage goodsPage;

    @Before
    public void setUp() {
        goodsPage = new GoodsPage();
        goodsPage.setTotal(3);
    }

    @Test
    public void test_getNextPage() {
        goodsPage.setCurrentPage(2);

        assertEquals(3, (int)goodsPage.getNextPage());
    }

    @Test
    public void getNextPage_WhenAtLastPage() {
        goodsPage.setCurrentPage(3);
        assertNull(goodsPage.getNextPage());
    }

    @Test
    public void getPreviousPage() {
        goodsPage.setCurrentPage(2);
        assertEquals(1, (int)goodsPage.getPreviousPage());
    }

    @Test
    public void getPreviousPage_WhenAtFirstPage_ShouldReturnNull() {
        goodsPage.setCurrentPage(1);
        assertNull(goodsPage.getPreviousPage());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setCurrentPage_ToBeGreaterThanTotalPages_ShouldThrowException() {
        goodsPage.setCurrentPage(4);
    }

    @Test
    public void getFirstGoodPosition_AtPage3AndPerPageOf15_ShouldReturn30() {
        goodsPage.setCurrentPage(2);
        goodsPage.setPerPage(30);
        assertEquals(30, goodsPage.getFirstGoodPosition());
    }
}