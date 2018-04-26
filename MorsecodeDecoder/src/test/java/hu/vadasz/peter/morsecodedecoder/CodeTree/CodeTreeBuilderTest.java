package hu.vadasz.peter.morsecodedecoder.CodeTree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class is to test CodeTreeBuilder util class.
 */

public class CodeTreeBuilderTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    @Test(timeout = MAX_TIME)
    public void testBuild_ShouldReturnRootNodeWithOnlyShortChild() {
        List<String> codes = new ArrayList<>();
        codes.add("0");
        Node root = CodeTreeBuilder.build(codes);
        assertEquals(OK, root.hasShortChild()
                && !root.hasGapChild()
                && !root.hasLongChild()
                && root.getShortTone().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testBuild_ShouldReturnRootWithOnlyLongChild() {
        List<String> codes = new ArrayList<>();
        codes.add("1");
        Node root = CodeTreeBuilder.build(codes);
        assertEquals(OK, root.hasLongChild()
                && !root.hasGapChild()
                && !root.hasShortChild()
                && root.getLongTone().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testBuild_ShouldReturnWithOnlyGapChild() {
        List<String> codes = new ArrayList<>();
        codes.add("2");
        Node root = CodeTreeBuilder.build(codes);
        assertEquals(OK, root.hasGapChild()
                && !root.hasShortChild()
                && !root.hasShortChild()
                && root.getGap().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testBuild_ShouldReturnWithShortAndLongChild() {
        List<String> codes = new ArrayList<>();
        codes.add("0");
        codes.add("1");
        Node root = CodeTreeBuilder.build(codes);
        assertEquals(OK, root.hasLongChild()
                && root.hasShortChild()
                && !root.hasGapChild()
                && root.getLongTone().isLeaf()
                && root.getShortTone().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testBuild_ShouldReturnWithShortChildWhichHasLongChild() {
        List<String> codes = new ArrayList<>();
        codes.add("01");
        Node root = CodeTreeBuilder.build(codes);
        assertEquals(OK, root.hasShortChild()
                && root.getShortTone().hasLongChild()
                && !root.getShortTone().hasShortChild()
                && !root.getShortTone().hasGapChild()
                && root.getShortTone().getLongTone().isLeaf()
                && !root.hasGapChild() && !root.hasLongChild());
    }

    @Test(timeout = MAX_TIME)
    public void testBuild_ShouldReturnWithLongChildWhichHasShortChildAndGap() {
        List<String> codes = new ArrayList<>();
        codes.add("10");
        codes.add("12");
        Node root = CodeTreeBuilder.build(codes);
        assertEquals(OK, !root.hasShortChild()
                && root.hasLongChild()
                && root.getLongTone().hasShortChild()
                && root.getLongTone().hasGapChild()
                && !root.getLongTone().hasLongChild()
                && root.getLongTone().getShortTone().isLeaf()
                && root.getLongTone().getGap().isLeaf());
    }

    @Test(timeout = MAX_TIME)
    public void testBuild_ShouldReturnWithShortChildLongChildGapChild() {
        List<String> codes = new ArrayList<>();
        codes.add("0");
        codes.add("1");
        codes.add("2");

        Node root = CodeTreeBuilder.build(codes);
        assertEquals(OK, root.hasShortChild()
            && root.hasLongChild()
            && root.hasShortChild()
            && root.hasGapChild());
    }

    @Test(timeout = MAX_TIME)
    public void testBuild_MoreLevels() {
        List<String> codes = new ArrayList<>();
        codes.add("001");
        Node root = CodeTreeBuilder.build(codes);

        assertEquals(OK, root.hasShortChild()
            && !root.hasGapChild()
            && !root.hasLongChild()
            && root.getShortTone().hasShortChild()
            && !root.getShortTone().hasLongChild()
            && !root.getShortTone().hasGapChild()
            && root.getShortTone().getShortTone().hasLongChild()
            && root.getShortTone().getShortTone().getLongTone().isLeaf()
            && !root.getShortTone().getShortTone().hasShortChild()
            && !root.getShortTone().getShortTone().hasGapChild());
    }
}