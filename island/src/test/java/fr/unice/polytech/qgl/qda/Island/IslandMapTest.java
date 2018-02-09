package fr.unice.polytech.qgl.qda.Island;

import eu.ace_design.island.stdlib.Resources;
import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * IslandMap Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class IslandMapTest {
    IslandMap islandMap;

    @Before
    public void before() throws Exception {
        islandMap = new IslandMap(Direction.E);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getDirectionActuelle()
     */
    @Test
    public void testGetDirectionActuelle() throws Exception {
        assertEquals(Direction.E, islandMap.getDirectionActuelle());
    }

    /**
     * Method: getCreeks()
     */
    @Test
    public void testGetCreeks() throws Exception {
        assertEquals(0, islandMap.getCreeks().size());

        islandMap.scan(new ArrayList<>(), new ArrayList<String>() {{
            add("test1");
            add("test2");
        }});
        assertEquals("[test2, test1]", islandMap.getCreeks().toString());
    }

    /**
     * Method: actualizePosition(int x, int y)
     */
    @Test
    public void testActualizePosition() throws Exception {
        islandMap.actualizePosition(100, 100);
        assertEquals(islandMap.getPositionActuelle().getX(), new Tile(100, 100).getX());

        assertEquals(islandMap.getPositionActuelle().getY(), new Tile(100, 100).getY());
    }

    /**
     * Method: changePhase()
     */
    @Test
    public void testChangePhase() throws Exception {
        assertEquals(AvailableActions.Phase.PHASE2, islandMap.changePhase());
    }

    /**
     * Method: createTile(Point point)
     */
    @Test
    public void testCreateTile() throws Exception {
        assertEquals(islandMap.get(new Point(100, 100)), null);
        islandMap.createTile(new Point(100, 100));
        assertTrue(islandMap.get(new Point(100, 100)) != null);
    }

    /**
     * Method: fly()
     */
    @Test
    public void testFly() throws Exception {
        islandMap.fly();
        islandMap.fly();
        assertEquals(islandMap.getPositionActuelle().getX(), 6);
        assertEquals(islandMap.getPositionActuelle().getY(), 0);
        islandMap.fly();
        islandMap.fly();
        assertEquals(islandMap.getPositionActuelle().getX(), 12);
        assertEquals(islandMap.getPositionActuelle().getY(), 0);
        islandMap.heading(Direction.W);
        islandMap.fly();
        assertEquals(islandMap.getPositionActuelle().getX(), 9);
        assertEquals(islandMap.getPositionActuelle().getY(), 0);
    }

    /**
     * Method: heading(Direction direction)
     */
    @Test
    public void testHeading() throws Exception {
        islandMap.fly();
        islandMap.heading(Direction.S);
        islandMap.fly();
        assertEquals(islandMap.getPositionActuelle().getX(), 6);
        assertEquals(islandMap.getPositionActuelle().getY(), -6);
        islandMap.fly();
        islandMap.heading(Direction.E);
        islandMap.fly();
        assertEquals(islandMap.getPositionActuelle().getX(), 12);
        assertEquals(islandMap.getPositionActuelle().getY(), -12);
        islandMap.heading(Direction.W);
        islandMap.fly();
        assertEquals(islandMap.getPositionActuelle().getX(), 9);
        assertEquals(islandMap.getPositionActuelle().getY(), -12);
    }

    @Test
    public void testEchoParseUpdateOutOfBond() {
        // Creating map instance, and setting position
        IslandMap theIsland = new IslandMap(Direction.E);
        theIsland.actualizePosition(1, 1);

        // Creating mocked JSON echo action result
        JSONObject echoResult = new JSONObject();
        echoResult.put("cost", 1);
        JSONObject extras = new JSONObject();
        extras.put("range", 50);
        extras.put("found", "OUT_OF_RANGE");
        echoResult.put("extras", extras);

        // Calling echo parsing method
        theIsland.echo(theIsland.getDirectionActuelle(), (int) extras.get("range"), Biome.valueOf((String) extras.get("found")));

        // Asserting results
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(1, 1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(50, 1)).getOnlyBiome());
        assertEquals(Biome.OUT_OF_RANGE, theIsland.createTile(new Point(51, 1)).getOnlyBiome());
    }

    @Test
    public void testEchoParseUpdateDistanceToGround() {
        // Creating map instance, and setting position
        IslandMap theIsland = new IslandMap(Direction.S);
        theIsland.actualizePosition(50, 0);

        // Creating mocked JSON echo action result
        JSONObject echoResult = new JSONObject();
        echoResult.put("cost", 1);
        JSONObject extras = new JSONObject();
        extras.put("range", 80);
        extras.put("found", "GROUND");
        echoResult.put("extras", extras);

        // Calling echo parsing method
        theIsland.echo(theIsland.getDirectionActuelle(), (int) extras.get("range"), Biome.valueOf((String) extras.get("found")));

        // Asserting results
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(50, 0)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(50, -79)).getOnlyBiome());
        assertEquals(Biome.GROUND, theIsland.createTile(new Point(50, -80)).getOnlyBiome());
    }

    @Test
    public void testScanParseUpdateIslandModelWhenAboveOcean() {
        // Creating map instance, and setting position
        IslandMap theIsland = new IslandMap(Direction.W);
        theIsland.actualizePosition(80, 0);

        // Creating mocked JSON echo action result
        JSONObject scanResult = new JSONObject();
        scanResult.put("cost", 1);
        JSONObject extras = new JSONObject();
        JSONArray biomes = new JSONArray();
        biomes.put("OCEAN");
        extras.put("biomes", biomes);
        extras.put("creeks", new JSONArray());
        scanResult.put("extras", extras);

        // Creating objects to comply with method parameters
        ArrayList<Biome> biomeFound = new ArrayList();
        biomeFound.add(Biome.OCEAN);
        ArrayList creekFound = new ArrayList();

        // Calling scan parsing method
        theIsland.scan(biomeFound, creekFound);

        // Asserting results
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(79, 1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(80, 1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(81, 1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(79, 0)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(80, 0)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(81, 0)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(79, -1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(80, -1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(81, -1)).getOnlyBiome());
    }

    @Test
    public void testScanParseUpdateIslandModelWhenAboveOceanWithCreek() {
        // Creating map instance, and setting position
        IslandMap theIsland = new IslandMap(Direction.W);
        theIsland.actualizePosition(0, 0);

        // Creating mocked JSON echo action result
        JSONObject scanResult = new JSONObject();
        scanResult.put("cost", 1);
        JSONObject extras = new JSONObject();
        JSONArray biomes = new JSONArray();
        biomes.put("OCEAN");
        extras.put("biomes", biomes);
        JSONArray creeks = new JSONArray();
        creeks.put("creekID");
        extras.put("creeks", creeks);
        scanResult.put("extras", extras);

        // Creating objects to comply with method parameters
        ArrayList<Biome> biomeFound = new ArrayList();
        biomeFound.add(Biome.OCEAN);
        ArrayList creekFound = new ArrayList();
        creekFound.add("creekID");

        // Calling scan parsing method
        theIsland.scan(biomeFound, creekFound);

        // Asserting results
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(-1, 1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(0, 1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(1, 1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(-1, 0)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(0, 0)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(1, 0)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(-1, -1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(0, -1)).getOnlyBiome());
        assertEquals(Biome.OCEAN, theIsland.createTile(new Point(1, -1)).getOnlyBiome());

        assertEquals("creekID", theIsland.getCreeks().get(0));
    }

    /**
     * Method: getPositionActuelle()
     */
    @Test
    public void testGetPositionActuelle() throws Exception {
        islandMap.actualizePosition(10, 10);
        assertTrue(new Point(islandMap.getPositionActuelle().getX(), islandMap.getPositionActuelle().getY()).equals(new Point(10, 10)));
    }

    /**
     * Method: land(String creekId, int peoples)
     */
    @Test
    public void testLand() throws Exception {
        islandMap.fly();
        islandMap.fly();
        islandMap.fly();
        islandMap.fly();
        islandMap.fly();
        islandMap.fly();
        islandMap.fly();
        islandMap.fly();
        islandMap.scan(new ArrayList<>(), new ArrayList<String>() {{
            add("creekId");
        }});
        Point tmp = new Point(islandMap.getPositionActuelle().getX(), islandMap.getPositionActuelle().getY());
        islandMap.fly();
        islandMap.fly();
        islandMap.land("creekId", 1);
        assertTrue(new Point(islandMap.getPositionActuelle().getX(), islandMap.getPositionActuelle().getY()).equals(tmp));
    }

    /**
     * Method: move_to(Direction direction)
     */
    @Test
    public void testMove_to() throws Exception {
        islandMap.move_to(Direction.E);

        assertTrue(new Point(islandMap.getPositionActuelle().getX(), islandMap.getPositionActuelle().getY()).equals(new Point(1, 0)));

        islandMap.move_to(Direction.N);
        //System.out.println(islandMap.getPositionActuelle().toString());

        assertTrue(new Point(islandMap.getPositionActuelle().getX(), islandMap.getPositionActuelle().getY()).equals(new Point(1, 1)));
    }

    /**
     * Method: scout(Direction direction, int altitude, ArrayList<Ressource> ressources)
     */
    @Test
    public void testScout() throws Exception {

        islandMap.scout(Direction.E, 2, new ArrayList<Ressource>() {{
            add(Ressource.FISH);
        }});

        assertFalse(islandMap.getPositionActuelle().getAssociatedRessources().contains(Ressource.FISH));
    }

    /**
     * Method: glimpse(Direction direction, int range, ArrayList<HashMap<Biome, Double>> myReport)
     */
    @Test
    public void testGlimpse() throws Exception {
        islandMap.glimpse(Direction.E, 1, new ArrayList<HashMap<Biome, Double>>());
    }

    /**
     * Method: explore(ArrayList<Ressource> ressources)
     */
    @Test
    public void testExplore() throws Exception {
        islandMap.explore(new ArrayList<>());
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
        assertEquals("IslandMap{creeks={}, phase=PHASE1, positionActuelle=Tile{altitude=0, relatedBiomes={}, associatedRessources=[], x=0, y=0, unreachable=false, visited=false, scouted=false, glimpsed=false}, directionActuelle=E}", islandMap.toString());
    }


    /**
     * Method: root()
     */
    @Test
    public void testRoot() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: moveRootToFront(HashMap.Node<K, V>[] var0, HashMap.TreeNode<K, V> var1)
     */
    @Test
    public void testMoveRootToFront() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: find(int var1, Object var2, Class<?> var3)
     */
    @Test
    public void testFind() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: getTreeNode(int var1, Object var2)
     */
    @Test
    public void testGetTreeNode() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: tieBreakOrder(Object var0, Object var1)
     */
    @Test
    public void testTieBreakOrder() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: treeify(HashMap.Node<K, V>[] var1)
     */
    @Test
    public void testTreeify() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: untreeify(HashMap<K, V> var1)
     */
    @Test
    public void testUntreeify() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: putTreeVal(HashMap<K, V> var1, HashMap.Node<K, V>[] var2, int var3, K var4, V var5)
     */
    @Test
    public void testPutTreeVal() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: removeTreeNode(HashMap<K, V> var1, HashMap.Node<K, V>[] var2, boolean var3)
     */
    @Test
    public void testRemoveTreeNode() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: split(HashMap<K, V> var1, HashMap.Node<K, V>[] var2, int var3, int var4)
     */
    @Test
    public void testSplit() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: rotateLeft(HashMap.TreeNode<K, V> var0, HashMap.TreeNode<K, V> var1)
     */
    @Test
    public void testRotateLeft() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: rotateRight(HashMap.TreeNode<K, V> var0, HashMap.TreeNode<K, V> var1)
     */
    @Test
    public void testRotateRight() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: balanceInsertion(HashMap.TreeNode<K, V> var0, HashMap.TreeNode<K, V> var1)
     */
    @Test
    public void testBalanceInsertion() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: balanceDeletion(HashMap.TreeNode<K, V> var0, HashMap.TreeNode<K, V> var1)
     */
    @Test
    public void testBalanceDeletion() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: checkInvariants(HashMap.TreeNode<K, V> var0)
     */
    @Test
    public void testCheckInvariants() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: trySplit()
     */
    @Test
    public void testTrySplit() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: forEachRemaining(Consumer<? super Entry<K, V>> var1)
     */
    @Test
    public void testForEachRemainingAction() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: tryAdvance(Consumer<? super Entry<K, V>> var1)
     */
    @Test
    public void testTryAdvanceAction() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: characteristics()
     */
    @Test
    public void testCharacteristics() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: getFence()
     */
    @Test
    public void testGetFence() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: estimateSize()
     */
    @Test
    public void testEstimateSize() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: next()
     */
    @Test
    public void testNext() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: hasNext()
     */
    @Test
    public void testHasNext() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: nextNode()
     */
    @Test
    public void testNextNode() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: remove()
     */
    @Test
    public void testRemove() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: size()
     */
    @Test
    public void testSize() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: clear()
     */
    @Test
    public void testClear() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: iterator()
     */
    @Test
    public void testIterator() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: contains(Object var1)
     */
    @Test
    public void testContainsO() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: remove(Object var1)
     */
    @Test
    public void testRemoveO() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: spliterator()
     */
    @Test
    public void testSpliterator() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: forEach(Consumer<? super Entry<K, V>> var1)
     */
    @Test
    public void testForEachAction() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: remove(Object var1)
     */
    @Test
    public void testRemoveKey() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: getKey()
     */
    @Test
    public void testGetKey() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: getValue()
     */
    @Test
    public void testGetValue() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: hashCode()
     */
    @Test
    public void testHashCode() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: setValue(V var1)
     */
    @Test
    public void testSetValueNewValue() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: equals(Object var1)
     */
    @Test
    public void testEqualsO() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: setValue(V var1)
     */
    @Test
    public void testSetValueValue() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: comparingByKey()
     */
    @Test
    public void testComparingByKey() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: comparingByValue()
     */
    @Test
    public void testComparingByValue() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: comparingByKey(Comparator<? super K> var0)
     */
    @Test
    public void testComparingByKeyCmp() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: comparingByValue(Comparator<? super V> var0)
     */
    @Test
    public void testComparingByValueCmp() throws Exception {
        //TODO: Test goes here...
    }


} 
