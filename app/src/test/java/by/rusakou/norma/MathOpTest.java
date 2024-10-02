package by.rusakou.norma;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

/**
 * @author Русаков Евгений
 *
 */
public class MathOpTest {


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRunSizeKnife() {
        double actual = MathOp.runSizeKnife(187, 0.9815);
        double expected = 190.5247;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunAreaKnife() {
        double actual = MathOp.runAreaKnife(190.5, 139.6, 25.5);
        double expected = 260.3562;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunAreaKnifeCircle() {
        double actual = MathOp.runAreaKnifeCircle(250);
        double expected = 490.8738;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunPerimeterOneKnife() {
        double actual = MathOp.runPerimeterOneKnife(190.5, 139.6, 25.5);
        double expected = 616.421;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunPerimeterKnifeForm() {
        double actual = MathOp.runPerimeterKnifeForm(190.5, 139.6, 25.5,9);
        double expected = 5547.791;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunMass() {
        double actual = MathOp.runMass(260.3562, 700, 0.91);
        double expected = 16.5847;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunMassLimitLittle() {
        double actual = MathOp.runMassLimit(16.5847, 200);
        double expected = 0.829239;
        assertEquals(actual, expected, 0.01);
    }
    @Test
    public void testRunMassLimitBig() {
        double actual = MathOp.runMassLimit(16.5847, 700);
        double expected = 1.65847;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunCalcForm() {

        ArrayList<Double> actualList = MathOp.runCalcForm(190.5, 139.6, 25.5, 800,
                520, 15, 20, 40);

        double[] actual = new double[actualList.size()];
        for (int i = 0; i < actualList.size(); i++) {
            actual[i] = actualList.get(i);
        }

        double [] expected = new double [] {641.5, 681.5, 3, 488.8, 498.8, 3, 9, 5547.791};

        Assert.assertArrayEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunCalcFormUser() {

        ArrayList<Double> actualList = MathOp.runCalcFormUser(190.5, 139.6, 25.5, 15,
                20, 40, 3, 2);

        double[] actual = new double[actualList.size()];
        for (int i = 0; i < actualList.size(); i++) {
            actual[i] = actualList.get(i);
        }

        double [] expected = new double [] {641.5, 681.5, 3, 334.2, 344.2, 2, 6, 3698.527};

        Assert.assertArrayEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunRoundCadre() {

        ArrayList<Double> actualList = MathOp.runRoundCadre(681.5, 498.8);

        double[] actual = new double[actualList.size()];
        for (int i = 0; i < actualList.size(); i++) {
            actual[i] = actualList.get(i);
        }

        double [] expected = new double [] {685, 499};

        Assert.assertArrayEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunNorm() {
        double actual = MathOp.runNorm(685, 499, 700, 0.91, 1.04, 9);
        double expected = 25.1606;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunScrapPercent() {
        double actual = MathOp.runScrapPercent(25.1606, 16.5847);
        double expected = 34.0846;
        assertEquals(actual, expected, 0.01);
    }

    @Test
    public void testRunMaxLengthKnifeThickness() {

        double[] arrayMaxLengthKnife = new double[]{10630, 10630, 10330, 10030, 9730, 9430, 9130, 8830, 8530, 8230, 7930,
                7630, 7330, 7030, 6730, 6430, 6130, 5830, 5530, 5230, 4930};

        double actual = MathOp.runMaxLengthKnifeThickness(700, arrayMaxLengthKnife);
        double expected = 8830;
        assertEquals(actual, expected, 0.01);
    }
}
