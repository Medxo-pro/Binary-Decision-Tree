package sol;

import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset>  {
    private ITreeNode root;

    /**
     * Generates the decision tree for a given training dataset.
     *
     * @param trainingData The dataset to train on
     * @param targetAttribute The attribute to predict
     */
    @Override
    public void generateTree(Dataset trainingData, String targetAttribute) {
        Dataset copyTrainingData = new Dataset( trainingData.getAttributeList(), trainingData.getDataObjects(), trainingData.getSelectionType());
        List<ValueEdge> outgoingEdges = new ArrayList<>();
        copyTrainingData.getAttributeList().remove(targetAttribute);
        String attributeToSplitOn = copyTrainingData.getAttributeToSplitOn();
        this.root = new AttributeNode(this.getDefaultDecision(copyTrainingData, targetAttribute), attributeToSplitOn, outgoingEdges);
        this.helper(copyTrainingData, targetAttribute, outgoingEdges, attributeToSplitOn);
    }

    /**
     * Recursive method used by generateTree() to populate all fields and data structures required to form a tree.
     * This method uses a recursive procedure to evaluate subtrees at split datasets.
     *
     * @param copyTrainingData The dataset/sub-dataset to train on
     * @param targetAttribute The attribute to predict
     * @param outgoingEdges List of different values for all the edges of the current node.
     * @param attributeToSplitOn The specific attribute we split on for the current node.
     *
     *
     */
    public void helper(Dataset copyTrainingData, String targetAttribute, List<ValueEdge> outgoingEdges,
                       String attributeToSplitOn){
        if(!copyTrainingData.attributeList.isEmpty()) {
            List<Dataset> datasetsList = this.partitionData(copyTrainingData, attributeToSplitOn);
            if (!datasetsList.isEmpty()) {
                for (Dataset subDataset : datasetsList) {
                    List<ValueEdge> valueEdgesNext = new ArrayList<>();
                    subDataset.getAttributeList().remove(attributeToSplitOn);
                    if(!subDataset.getAttributeList().isEmpty()) {
                        String attributeToSplitOnNext = subDataset.getAttributeToSplitOn();
                        outgoingEdges.add
                                (new ValueEdge(subDataset.getDataObjects().get(0).getAttributeValue(attributeToSplitOn),
                                        this.childCreator(subDataset, attributeToSplitOnNext, targetAttribute,
                                                valueEdgesNext)));
                        if (!this.leafOrNode(subDataset.dataObjects, targetAttribute))
                            this.helper(subDataset, targetAttribute, valueEdgesNext, attributeToSplitOnNext);
                    }
                }
            }
        }
    }

    /**
     * This method is used to partition the training data into sub data sets. At each recursive
     * call of the method helper(), the parameter @param copyTrainingData will contain a subset of data
     * outputted by this method.
     *
     * This method will return a list of split training datasets. A subset of training data set for each
     * different kind of value on the original dataset at @param attributeName.
     *
     * @param copyData The dataset we want to split
     * @param attributeName The attribute we want to consider for splitting the data
     * @return A List of Datasets
     */
    public List<Dataset> partitionData(Dataset copyData, String attributeName){
        List<String> elements = new ArrayList<>();
        List<Dataset> datasetsList = new ArrayList<>();
        for (Row row : copyData.dataObjects) {
            String element = row.getAttributeValue(attributeName);
            if (!elements.contains(element))
                elements.add(element);
        }
        for (String element : elements) {
            Dataset dataset = new Dataset(copyData.getAttributeList(), new ArrayList<>(), copyData.getSelectionType());
            for (int j = 0; j < copyData.size(); j++) {
                if (copyData.getDataObjects().get(j).getAttributeValue(attributeName).equals(element)) {
                    dataset.getDataObjects().add(copyData.getDataObjects().get(j));
                }
            }
            dataset.getAttributeList().remove(attributeName);
            datasetsList.add(dataset);
        }
        return datasetsList;
    }


    /**
     * This method is responsible for creating a certain node. It can be though of as a method that creates a child for
     * a given value edge.It determines whether the child should be a DecisionLeaf or AttributeNode given the current
     * data available to us.
     *
     * It returns an ITreeNode that could either be an instance of the DecisionLeaf class or the AttributeNode class.
     *
     * @param subData The dataset we want to evaluate in order to determine whether to create a DecisionLeaf node or
     * AttributeNode
     * @param attributeToSplitOnNext Attribute we want to remove before calling getAttributeToSplitOn()
     * @param targetAttribute The attribute to predict
     * @param valueEdges List of ValueEdges for the specific child node we are creating.
     * @return the new child Node
     */
    public ITreeNode childCreator(Dataset subData, String attributeToSplitOnNext, String targetAttribute, List<ValueEdge> valueEdges){
        Dataset cleanDataset = new Dataset(subData.getAttributeList(), subData.getDataObjects(), subData.getSelectionType());
        if (subData.getAttributeList().isEmpty() || this.leafOrNode(cleanDataset.getDataObjects(), targetAttribute))
            return new DecisionLeaf(cleanDataset.getDataObjects().get(0).getAttributeValue(targetAttribute));
        else {
            return new AttributeNode(this.getDefaultDecision(cleanDataset,targetAttribute), attributeToSplitOnNext, valueEdges);
        }
    }


    /**
     * This method is used by the childCreator() method to determine which kind of ITreeNode should be created.
     * It returns a boolean that will later on be used to determine the output of a conditional statement.
     *
     * @param dataObjects We use this List of Rows in order to compute whether we should be creating a DecisionLeaf or
     * AttributeNode
     * @param targetAttribute The attribute to predict
     * @return
     */
    public boolean leafOrNode(List<Row> dataObjects, String targetAttribute){
        if (dataObjects.size() <= 1)
            return true;
        String initial = dataObjects.get(0).getAttributeValue(targetAttribute);
        for (int i=1; i<dataObjects.size(); i++){
            if (!initial.equals(dataObjects.get(i).getAttributeValue(targetAttribute))
                    && dataObjects.get(i).getAttributeValue(targetAttribute)!= null)
                return false;
        }
        return true;
    }


    /**
     * This method is in charge of computing what should the default decision be for
     * a given AttributeNode. In this case, it takes a training dataset as a parameter and
     * computes the most frequent outcome.
     *
     *
     * @param trainingData The sub-dataset we want to take into consideration for computing the default decision
     * @param targetAttribute The attribute to predict
     * @return the outcome (a string)
     */
    public String getDefaultDecision(Dataset trainingData, String targetAttribute){
        List<String> outcomesList = new ArrayList<>();
        List<Integer> frequencyList = new ArrayList<>();
        for (Row row : trainingData.dataObjects) {
            String decision = row.getAttributeValue(targetAttribute);
            if (outcomesList.contains(decision)) {
                int index = outcomesList.indexOf(decision);
                frequencyList.set(index, frequencyList.get(index) + 1);
            }
            else {
                outcomesList.add(decision);
                frequencyList.add(1);
            }
        }
        int max = 0;
        List<Integer> maxIndices = new ArrayList<>();
        for (int i = 0; i < frequencyList.size(); i++) {
            if (frequencyList.get(i) > max) {
                max = frequencyList.get(i);
                maxIndices = new ArrayList<>(i);
            }
            if (frequencyList.get(i) == max) {
                maxIndices.add(i);
            }
        }
        Random random = new Random();
        int upperBound = maxIndices.size();
        int randomNum = random.nextInt(upperBound);
        return outcomesList.get(maxIndices.get(randomNum));
    }



    /**
     * Computes the decision for a datum in the decision tree.
     *
     * @param datum The datum to look up a decision for.
     * @return The decision
     */
    @Override
    public String getDecision(Row datum) {
        return this.root.getDecision(datum);
    }



}
