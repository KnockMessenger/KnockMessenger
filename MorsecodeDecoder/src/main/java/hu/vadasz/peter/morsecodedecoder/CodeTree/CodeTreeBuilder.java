package hu.vadasz.peter.morsecodedecoder.CodeTree;

import java.util.List;

/**
 * This class is responsible for building code trees from list of codes. The code tree is built based
 * on three items (0=left, 1=right, 2=middle child).
 */

public class CodeTreeBuilder {

    /**
     * Builds a code tree from the list of the codes.
     * @param codes the codes must be prefix!
     * @return
     */

    public static Node build(List<String> codes) {
        Node startNode = new Node(Node.START_NODE);
        for (String code : codes) {
            Node currentNode = startNode;
            for (int i = 0; i < code.length(); ++i) {
                if (Node.SHORT_EDGE.equals(Character.toString(code.charAt(i)))) {
                    if (currentNode.hasShortChild()) {
                        currentNode = currentNode.getShortTone();
                    } else {
                        currentNode.setShortTone(new Node());
                        currentNode = currentNode.getShortTone();
                    }
                } else if (Node.LONG_EDGE.equals(Character.toString(code.charAt(i)))) {
                    if (currentNode.hasLongChild()) {
                        currentNode = currentNode.getLongTone();
                    } else {
                        currentNode.setLongTone(new Node());
                        currentNode = currentNode.getLongTone();
                    }
                } else {
                    currentNode.setGap(new Node());
                }
            }
        }

        return startNode;
    }
}
