package sol;

import src.ITreeNode;
import src.Row;

import java.util.List;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode  {

    String decision;

    /**
     *
     * @param decision the decision at the Leaf
     */
    public DecisionLeaf(String decision) {
        this.decision = decision;
    }

    /**
     * Necessary getter
     * @return the decision at the Leaf
     */
    public String getDecision(Row forDatum) {
        return this.decision;
    }

}
