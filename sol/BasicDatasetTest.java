package sol;

import org.junit.Assert;
import org.junit.Test;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

/**
 * A class to test basic decision tree functionality on a basic training dataset
 */
public class BasicDatasetTest {
    // IMPORTANT: for this filepath to work, make sure the project is open as the top-level directory in IntelliJ
    // (See the first yellow information box in the handout testing section for details)
    String trainingPath = "data/computer.csv";
    String targetAttribute = "outcome";
    TreeGenerator testGenerator;
    Row computerChoice;

    /**
     * Constructs the decision tree for testing based on the input file and the target attribute.
     */
    @Before
    public void buildTreeForTest() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator = new TreeGenerator();
        this.testGenerator.generateTree(training, this.targetAttribute);

        this.computerChoice = new Row("computer5");
        this.computerChoice.setAttributeValue("ram", "8");
        this.computerChoice.setAttributeValue("screen size", "large");
        this.computerChoice.setAttributeValue("ssd", "little");
        this.computerChoice.setAttributeValue("graphic card", "strong");
    }

    /**
     * Tests the expected classification of the "computerChoice" row is an "accept"
     */
    @Test
    public void testClassification() {
        Assert.assertEquals("accept", this.testGenerator.getDecision(this.computerChoice));
    }

    /**
     * Tests basic attributes of computerChoice
     */
    @Test
    public void basicTest() {
        Assert.assertEquals(4, this.computerChoice.getAttributes().size());
        Assert.assertEquals("8", this.computerChoice.getAttributeValue("ram"));
    }
}
