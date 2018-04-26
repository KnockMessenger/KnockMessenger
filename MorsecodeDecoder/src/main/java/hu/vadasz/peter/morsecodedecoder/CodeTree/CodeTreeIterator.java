package hu.vadasz.peter.morsecodedecoder.CodeTree;

import lombok.Getter;

/**
 * This class is responsible for iterating a code tree. The iterator iterates over the nodes of the tree.
 */

public class CodeTreeIterator {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final String NEW_PATH = "";

    /// CONSTANTS -- END

    @Getter
    private Node currentNode;

    @Getter
    private String path;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public CodeTreeIterator(Node startNode) {
        reset(startNode);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This methods decides whether the current node has children or not.
     * @return
     */

    public boolean hasNext() {
        return !currentNode.isLeaf();
    }

    /**
     * This methods sets the current node to the current node's short child.
     */

    public void nextShort() {
        if (currentNode.hasShortChild()) {
            currentNode = currentNode.getShortTone();
            path += Node.SHORT_EDGE;
        }
    }

    /**
     * This methods sets the current node to the current node's long child.
     */

    public void nextLong() {
        if (currentNode.hasLongChild()) {
            currentNode = currentNode.getLongTone();
            path += Node.LONG_EDGE;
        }
    }

    /**
     * This methods sets the current node to the current node's gap child.
     */

    public void nextGap() {
        if (currentNode.hasGapChild()) {
            currentNode = currentNode.getGap();
            path += Node.GAP_EDGE;
        }
    }

    /**
     * This method sets the iterator to a specified node and clears the path.
     * @param startNode
     */

    public void reset(Node startNode) {
        currentNode = new Node(startNode);
        path = NEW_PATH;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
