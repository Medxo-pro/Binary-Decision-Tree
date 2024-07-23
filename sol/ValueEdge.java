package sol;

import src.ITreeNode;

/**
 * A class that represents the edge of an attribute node in the decision tree
 */
public class ValueEdge {
    private ITreeNode child;
    private String value;

    /**
     *
     * @param value The value of the edge
     * @param child The ITreeNode child of the edge
     */
    public ValueEdge(String value, ITreeNode child){
        this.value = value;
        this.child = child;
    }

    /**
     *
     * @return The value of the edge
     */
    public String getValue() {
        return this.value;
    }

    /**
     *
     * @return The ITreeNode child of the edge
     */
    public ITreeNode getChild() {
        return this.child;
    }
}
