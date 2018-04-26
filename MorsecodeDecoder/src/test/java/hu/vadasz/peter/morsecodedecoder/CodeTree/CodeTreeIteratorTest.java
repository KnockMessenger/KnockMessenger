package hu.vadasz.peter.morsecodedecoder.CodeTree;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class is to test CodeTreeIterator class.
 */

public class CodeTreeIteratorTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    @Test(timeout = MAX_TIME)
    public void testHasNext_ShouldReturnFalse() {
        Node node = new Node();
        CodeTreeIterator iterator = new CodeTreeIterator(node);
        assertEquals(!OK, iterator.hasNext());
    }

    @Test(timeout = MAX_TIME)
    public void testHasNext_ShortChildShouldReturnTrue() {
        Node node = new Node();
        node.setShortTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(node);
        assertEquals(OK, iterator.hasNext());
    }

    @Test(timeout = MAX_TIME)
    public void testHasNext_LongChildShouldReturnTrue() {
        Node node = new Node();
        node.setLongTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(node);
        assertEquals(OK, iterator.hasNext());
    }

    @Test(timeout = MAX_TIME)
    public void testHasNext_GapChildShouldReturnTrue() {
        Node node = new Node();
        node.setGap(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(node);
        assertEquals(OK, iterator.hasNext());
    }

    @Test(timeout = MAX_TIME)
    public void testNextShort_ShouldIterateToShortChild() {
        Node root = new Node(new Node(), new Node());
        root.getLongTone().setShortTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextShort();
        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testNextShort_WithOneShortChild() {
        Node root = new Node();
        root.setShortTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextShort();
        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testNextShort_ShouldDoNothing() {
        Node root = new Node();
        root.setLongTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextShort();
        assertEquals(OK, !iterator.getCurrentNode().hasShortChild()
            && iterator.getCurrentNode().hasLongChild());
    }

    @Test(timeout = MAX_TIME)
    public void testNextShort_WithGapShouldIterateToShort() {
        Node root = new Node(new Node(), new Node());
        root.setGap(new Node());
        root.getGap().setGap(new Node());
        root.getLongTone().setShortTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextShort();
        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testNextShort_MoreLevels() {
        Node root = new Node(new Node(), new Node());
        root.getShortTone().setShortTone(new Node());
        root.getShortTone().setShortTone(new Node());
        root.getShortTone().getShortTone().setLongTone(new Node());

        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextShort();
        iterator.nextShort();

        assertEquals(OK, iterator.getCurrentNode().hasLongChild()
            && !iterator.getCurrentNode().hasShortChild());
    }

    @Test(timeout = MAX_TIME)
    public void testNextLong_ShouldIterateToLongChild() {
        Node root = new Node(new Node(), new Node());
        root.getShortTone().setShortTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextLong();
        assertEquals(OK, !iterator.hasNext());
    }

    @Test(timeout = MAX_TIME)
    public void testNextLong_WitOneLongChild() {
        Node root = new Node();
        root.setLongTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextLong();
        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testNextLong_ShouldDoNothing() {
        Node root = new Node();
        root.setShortTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextLong();
        assertEquals(OK, iterator.getCurrentNode().hasShortChild());
    }

    @Test(timeout = MAX_TIME)
    public void testNextLong_WithMoreChildren() {
        Node root = new Node(new Node(), new Node());
        root.setGap(new Node());
        root.getShortTone().setShortTone(new Node());
        root.getGap().setShortTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextLong();
        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testNextLong_WithMoreLevels() {
        Node root = new Node(new Node(), new Node());
        root.getShortTone().setShortTone(new Node());
        root.getLongTone().setShortTone(new Node());
        root.getLongTone().getShortTone().setGap(new Node());
        root.getLongTone().setLongTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextLong();
        iterator.nextLong();
        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testNextGap_ShouldIterateGap() {
        Node root = new Node();
        root.setGap(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextGap();
        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testNextGap_ShouldDoNothing() {
        Node root = new Node(new Node(), new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextGap();
        assertEquals(OK, iterator.getCurrentNode().hasShortChild()
            && iterator.getCurrentNode().hasLongChild()
            && !iterator.getCurrentNode().hasGapChild());
    }

    @Test(timeout = MAX_TIME)
    public void testNextGap_WithMoreChildren() {
        Node root = new Node(new Node(), new Node());
        root.getShortTone().setLongTone(new Node());
        root.getLongTone().setLongTone(new Node());
        root.setGap(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextGap();
        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testNextGap_WithMoreLevels() {
        Node root = new Node(new Node(new Node(), new Node()), new Node(new Node(), new Node()));
        root.setGap(new Node());
        root.getGap().setGap(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextGap();
        iterator.nextGap();

        assertEquals(OK, iterator.getCurrentNode().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testPath_ShouldReturnNewPath() {
        CodeTreeIterator iterator = new CodeTreeIterator(new Node());
        assertEquals(CodeTreeIterator.NEW_PATH, iterator.getPath());
    }

    @Test(timeout = MAX_TIME)
    public void testPath_ShouldReturnShortPath() {
        Node root = new Node();
        root.setShortTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextShort();
        iterator.nextShort();
        assertEquals(Node.SHORT_EDGE, iterator.getPath());
    }

    @Test(timeout = MAX_TIME)
    public void testPath_ShouldReturnLongPath() {
        Node root = new Node();
        root.setLongTone(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextLong();
        iterator.nextLong();
        assertEquals(Node.LONG_EDGE, iterator.getPath());
    }

    @Test(timeout = MAX_TIME)
    public void testPath_ShouldReturnGapPath() {
        Node root = new Node();
        root.setGap(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextGap();
        iterator.nextGap();
        assertEquals(Node.GAP_EDGE, iterator.getPath());
    }

    @Test(timeout = MAX_TIME)
    public void testPath_ShouldReturnMixedPah() {
        Node root = new Node(new Node(), new Node());
        root.getShortTone().setLongTone(new Node());
        root.getShortTone().getLongTone().setGap(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextShort();
        iterator.nextLong();
        iterator.nextGap();
        assertEquals("012", iterator.getPath());
    }

    @Test(timeout = MAX_TIME)
    public void testPath_WithWrongDirectionsShouldReturnCorrectMixedPath() {
        Node root = new Node(new Node(), new Node());
        root.getShortTone().setLongTone(new Node());
        root.getShortTone().getLongTone().setGap(new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(root);
        iterator.nextShort();
        iterator.nextShort();
        iterator.nextGap();
        iterator.nextLong();
        iterator.nextLong();
        iterator.nextGap();
        iterator.nextLong();
        iterator.nextGap();

        assertEquals("012", iterator.getPath());
    }

    @Test(timeout = MAX_TIME)
    public void testReset_PathShouldBeNewPath() {
        CodeTreeIterator iterator = new CodeTreeIterator(new Node());
        iterator.reset(new Node());
        assertEquals(CodeTreeIterator.NEW_PATH, iterator.getPath());
    }

    @Test(timeout = MAX_TIME)
    public void reset_NodeShouldBeNewNode() {
        Node left = new Node();
        left.setGap(new Node());
        Node right = new Node(new Node(), new Node());
        CodeTreeIterator iterator = new CodeTreeIterator(left);
        iterator.reset(right);
        assertEquals(OK, iterator.getCurrentNode().hasShortChild()
            && iterator.getCurrentNode().hasLongChild()
            && !iterator.getCurrentNode().hasGapChild()
            && !iterator.getCurrentNode().equals(left));
    }
}