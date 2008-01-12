package nl.gridshore.samples.raffle.business;

import junit.framework.TestCase;
import nl.gridshore.samples.raffle.business.impl.BasicRandomizer;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Dec 13, 2007
 * Time: 1:29:29 PM
 * TestCase for the basic randomizer
 */
public class BasicRandomizerTest extends TestCase {
    private BasicRandomizer randomizer;

    public BasicRandomizerTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        randomizer = new BasicRandomizer();
    }

    public void testWithOnlyOneItem() {
        int randomNumber = randomizer.createRandomNumber(1);
        assertEquals(1, randomNumber);
    }

    public void testWithInvalidMaxValue() {
        try {
            randomizer.createRandomNumber(0);
            fail("an exception should have been thrown");
        } catch (Exception e) {
            // as expected
        }
    }
}