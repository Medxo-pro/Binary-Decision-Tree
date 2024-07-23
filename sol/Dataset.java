package sol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



import src.AttributeSelection;
import src.IDataset;
import src.Row;

import javax.swing.tree.MutableTreeNode;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset  {


    List<Row> dataObjects;
    List<String> attributeList;
    AttributeSelection selectionType;



    /**
     * Constructor for a Dataset object
     * @param attributeList A list of attributes
     * @param dataObjects A list of rows
     * @param attributeSelection An enum for which way to select attributes
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection attributeSelection) {
        this.selectionType = attributeSelection;
        this.dataObjects = new ArrayList<>(dataObjects);
        this.attributeList = new ArrayList<>(attributeList);


    }

    /**
     * Gets list of attributes in the dataset
     *
     * @return the attributeList
     */
    @Override
    public List<String> getAttributeList() {
        return this.attributeList;
    }

    /**
     * Gets list of data objects (row) in the dataset
     *
     * @return the dataObjects
     */
    @Override
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }

    /**
     * Returns the attribute selection type (alphabetical, reverse alphabetical, random) for this Dataset
     *
     * @return The attribute selection type
     */
    @Override
    public AttributeSelection getSelectionType() {
        return this.selectionType;
    }

    /**
     * finds the size of the dataset (number of rows)
     *
     * @return The number of rows in the dataset
     */
    @Override
    public int size() {
        return this.dataObjects.size();
    }


    /**
     * @return The next attribute to split on depending on the @param selectionType of the given Dataset.
     */
    public String getAttributeToSplitOn() {
        switch (this.selectionType) {
            case ASCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(0);
            }
            case DESCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
            }
            case RANDOM -> {
                Random random = new Random();
                int upperBound = this.attributeList.size();
                int randomNum = random.nextInt(upperBound);
                String unit = this.attributeList.stream().sorted().toList().get(randomNum);
                return unit;
            }
        }
        throw new RuntimeException("Non-Exhaustive Switch Case");
    }
}
