package fr.unice.polytech.qgl.qda.Island;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Biome Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class BiomeTest {
    private Biome biome;

    @Before
    public void before() throws Exception {
        biome = Biome.OCEAN;
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: main(String[] args)
     */
    @Test
    public void testMain() throws Exception {
        //Test goes here...
    }

    /**
     * Method: getBiomeTypeAssociated()
     */
    @Test
    public void testGetBiomeTypeAssociated() throws Exception {
        assertEquals(Biome.BiomeType.COMMON, biome.getBiomeTypeAssociated());
    }

    /**
     * Method: getDescription()
     */
    @Test
    public void testGetDescription() throws Exception {
        assertEquals("plain ocean, wide open area full of unknown;", biome.getDescription());
    }

    /**
     * Method: isRare()
     */
    @Test
    public void testIsRare() throws Exception {
        assertEquals(false, biome.isRare());
    }


} 
