package hu.vadasz.peter.morsecodedecoder.CodeTree;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests the Nodes of the code tree.
 */
public class NodeTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    @Test(timeout = MAX_TIME)
    public void testIsLeaf_ShouldReturnTrue() {
        Node root = new Node();
        assertEquals(OK, root.isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testIsLeaf_ShortChildShouldReturnFalse() {
        Node root = new Node();
        root.setShortTone(new Node());
        assertEquals(!OK, root.isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testIsLeaf_LongChildShouldReturnFalse() {
        Node root = new Node();
        root.setLongTone(new Node());
        assertEquals(!OK, root.isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testIsLeaf_GapChildShouldReturnFalse() {
        Node root = new Node();
        root.setGap(new Node());
        assertEquals(!OK, root.isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testIsLeaf_EachChildShouldReturnFalse() {
        Node root = new Node(new Node(), new Node());
        root.setGap(new Node());
        assertEquals(!OK, root.isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testHasShortChild_ShouldReturnFalse() {
        Node root = new Node();
        root.setShortTone(new Node());
        assertEquals(OK, root.hasShortChild());
    }

    @Test(timeout = MAX_TIME)
    public void testHasShortChild_ShouldReturnTrue() {
        Node root = new Node();
        assertEquals(!OK, root.hasShortChild());
    }

    @Test(timeout = MAX_TIME)
    public void testHasLongChild_ShouldReturnFalse() {
        Node root = new Node();
        assertEquals(!OK, root.hasLongChild());
    }

    @Test(timeout = MAX_TIME)
    public void testHasLongChild_ShouldReturnTrue() {
        Node root = new Node();
        root.setLongTone(new Node());
        assertEquals(OK, root.hasLongChild());
    }

    @Test(timeout = MAX_TIME)
    public void testHasGapChild_ShouldReturnFalse() {
        Node root = new Node();
        assertEquals(!OK, root.hasGapChild());
    }

    @Test(timeout = MAX_TIME)
    public void testHasGapChild_ShouldReturnTrue() {
        Node root = new Node();
        root.setGap(new Node());
        assertEquals(OK, root.hasGapChild());
    }
}