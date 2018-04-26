package hu.vadasz.peter.morsecodedecoder.CodeTree;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a Node of the code-tree. By using this code Morse-codes with gaps at the end
 * and prefix codes can be decoded. Each Node can have three child, a short syllable child (left),
 * which represents 0, a long syllable child (right) = 1, and a middle child which represents the gap.
 * A leaf child does not have any children.
 */

@NoArgsConstructor
public class Node {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final boolean START_NODE = true;
    public static final String SHORT_EDGE = "0";
    public static final String LONG_EDGE = "1";
    public static final String GAP_EDGE = "2";

    /// CONSTANTS -- END

    @Getter
    @Setter
    private Node longTone;

    @Getter
    @Setter
    private Node shortTone;

    @Getter
    @Setter
    private Node gap;

    @Getter
    @Setter
    private boolean startNode;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Node(boolean startNode) {
        this.startNode = startNode;
    }

    public Node(Node other) {
        this.startNode = other.startNode;
        this.longTone = other.longTone;
        this.shortTone = other.shortTone;
        this.gap = other.gap;
    }

    public Node(Node shortTone, Node longTone) {
        this.shortTone = shortTone;
        this.longTone = longTone;
        startNode = !START_NODE;
    }

    public Node(Node shortTone, Node longTone, Node gap) {
        this.shortTone = shortTone;
        this.longTone = longTone;
        this.gap = gap;
        startNode = !START_NODE;
    }

    public Node(Node shortTone, Node longTone, boolean startNode) {
        this.shortTone = shortTone;
        this.longTone = longTone;
        this.startNode = startNode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This methods return true if this node has no children.
     * @return
     */

    public boolean isLeaf() {
        return longTone == null && shortTone == null && gap == null;
    }

    /**
     * This method returns true if this node has a long child.
     * @return
     */

    public boolean hasLongChild() {
        return longTone != null;
    }

    /**
     * This method return true if this node has a short child.
     * @return
     */

    public boolean hasShortChild() {
        return shortTone != null;
    }

    /**
     * This method returns true if the node ha a gap child.
     * @return
     */
    public boolean hasGapChild() { return gap != null; }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
