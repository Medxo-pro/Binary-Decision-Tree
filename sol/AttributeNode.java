package sol;

import java.util.ArrayList;
import java.util.List;
import src.ITreeNode;
import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
public class AttributeNode implements ITreeNode  {

    private String defaultDecision;

    private String attributeName;
    private List<ValueEdge> outgoingEdges;

    /**
     *
     * @param defaultDecision The default decision of the Node
     * @param attributeName The name of the Node's attribute
     * @param outgoingEdges A list of the Node's outgoingEdges
     */
    public AttributeNode(String defaultDecision, String attributeName, List<ValueEdge> outgoingEdges){
        this.attributeName = attributeName;
        this.defaultDecision = defaultDecision;
        this.outgoingEdges = outgoingEdges;
    }

    /**
     * @return The decision at the Node
     */
    public String getDecision(Row forDatum) {
        for (ValueEdge valueEdge : this.outgoingEdges){
            if (forDatum.getAttributeValue(this.attributeName).equals(valueEdge.getValue())){
                return valueEdge.getChild().getDecision(forDatum);
            }
        }
        return this.defaultDecision;
    }




}
