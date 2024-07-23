package sol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.DecisionTreeTester;
import src.Row;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {

    //Large Dataset
    String trainingPathLargeDataset = "data/computer.csv";
    String testingPathLargeDataset = "data/computer-testing.csv";
    String targetAttributeLargeDataset = "outcome";
    TreeGenerator testGeneratorLargeDataset;
    Dataset trainingLargeDataset;


    //Small Dataset
    String trainingPathSmallDataset = "data/days-temperature.csv";
    String targetAttributeSmallDataset = "outcome (Jacket?)";
    TreeGenerator testGeneratorSmallDataset;
    Dataset trainingSmallDataset;

    //Mushroom Dataset
    String trainingPathMushroomDataset = "data/mushrooms/training.csv";
    String testingPathMushroomDataset = "data/mushrooms/testing.csv";

    //Villains Dataset
    String trainingPathVillainsDataset = "data/villains/training.csv";
    String testingPathVillainsDataset = "data/villains/testing.csv";





    @Before
    public void buildTreeForTest() {
        List<Row> dataObjectsLargeDataset = DecisionTreeCSVParser.parse(this.trainingPathLargeDataset);
        List<String> attributeListLargeDataset = new ArrayList<>(dataObjectsLargeDataset.get(0).getAttributes());
        this.trainingLargeDataset = new Dataset(attributeListLargeDataset, dataObjectsLargeDataset, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGeneratorLargeDataset = new TreeGenerator();
        this.testGeneratorLargeDataset.generateTree(trainingLargeDataset, this.targetAttributeLargeDataset);

        List<Row> dataObjectsSmallDataset = DecisionTreeCSVParser.parse(this.trainingPathSmallDataset);
        List<String> attributeListSmallDataset = new ArrayList<>(dataObjectsSmallDataset.get(0).getAttributes());
        this.trainingSmallDataset = new Dataset(attributeListSmallDataset, dataObjectsSmallDataset, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGeneratorSmallDataset = new TreeGenerator();
        this.testGeneratorSmallDataset.generateTree(trainingSmallDataset, this.targetAttributeSmallDataset);
    }

    /**
     * Tests the average accuracy of the Mushrooms dataset.
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public void testAccuracyMushroomDataset() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        DecisionTreeTester dtt = new DecisionTreeTester(TreeGenerator.class, Dataset.class);
        Dataset MushroomDataSetTraining = DecisionTreeTester.makeDataset(this.trainingPathMushroomDataset, Dataset.class);
        Dataset MushroomDataSetTesting = DecisionTreeTester.makeDataset(this.testingPathMushroomDataset, Dataset.class);
        double accuracy = dtt.getDecisionTreeAccuracy(MushroomDataSetTraining, MushroomDataSetTesting, "isPoisonous");
        Assert.assertTrue(accuracy > 0.95);
        double accuracy2 = dtt.getDecisionTreeAccuracy(MushroomDataSetTesting, "isPoisonous");
        Assert.assertTrue(accuracy2 > 0.7);
    }

    /**
     * Tests the training and testing accuracy of the Villains dataset.
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public void testAccuracyVillainsDataset() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        DecisionTreeTester dtt = new DecisionTreeTester(TreeGenerator.class, Dataset.class);
        Dataset VillainsDataSetTraining = DecisionTreeTester.makeDataset(this.trainingPathVillainsDataset, Dataset.class);
        Dataset VillainsDataSetTesting = DecisionTreeTester.makeDataset(this.testingPathVillainsDataset, Dataset.class);
        double accuracy = dtt.getDecisionTreeAccuracy(VillainsDataSetTraining, VillainsDataSetTesting, "isVillain");
        Assert.assertTrue(accuracy > 0.95);
        double accuracy2 = dtt.getDecisionTreeAccuracy(VillainsDataSetTesting, "isVillain");
        Assert.assertTrue(accuracy2 > 0.7);
    }


    /**
     * Tests the training and testing accuracy of the large dataset.
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public void testAccuracyLargeDataset() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        DecisionTreeTester dtt = new DecisionTreeTester(TreeGenerator.class, Dataset.class);
        Dataset LargeDataSetTraining = DecisionTreeTester.makeDataset(this.trainingPathLargeDataset, Dataset.class);
        Dataset LargeDataSetTesting = DecisionTreeTester.makeDataset(this.testingPathLargeDataset, Dataset.class);
        double accuracy = dtt.getDecisionTreeAccuracy(LargeDataSetTraining, LargeDataSetTesting, this.targetAttributeLargeDataset);
        Assert.assertTrue(accuracy > 0.95);
        double accuracy2 = dtt.getDecisionTreeAccuracy(LargeDataSetTesting, this.targetAttributeLargeDataset);
        Assert.assertTrue(accuracy2 > 0.7);
    }


    /**
     * Tests basic getters from the Dataset class.
     */
    @Test
    public void testIDataset() {
        Row computer = new Row("computer5");
        computer.setAttributeValue("ram", "8");
        computer.setAttributeValue("screen size", "large");
        computer.setAttributeValue("ssd", "little");
        computer.setAttributeValue("graphic card", "strong");
        assertEquals("[screen size, ssd, outcome, ram, graphic card]", this.trainingLargeDataset.getAttributeList().toString());
        assertEquals(computer.getAttributeValue("ram"),
                this.trainingLargeDataset.getDataObjects().get(4).getAttributeValue("ram"));
    }

    /**
     * Tests small dataset
     */
    @Test
    public void testSmallDataset() {
        Row day1 = new Row("day1");
        day1.setAttributeValue("temperature", "Cold");
        Assert.assertEquals("yes", this.testGeneratorSmallDataset.getDecision(day1));
        Row day2 = new Row("day2");
        day2.setAttributeValue("temperature", "medium");
        Assert.assertEquals("yes", this.testGeneratorSmallDataset.getDecision(day1));
    }


    /**
     * Tests the dataset partition method from the TreeGenerator class.
     */
    @Test
    public void testPartitionData() {
        // Partition for weak (graphic card)
        List<Row> testRows = new ArrayList<>();
        List<String> testAttributes = new ArrayList<>(this.trainingLargeDataset.attributeList);
        Row computer2 = new Row("computer2");
        computer2.setAttributeValue("ram", "4");
        computer2.setAttributeValue("screen size", "small");
        computer2.setAttributeValue("ssd", "little");
        computer2.setAttributeValue("graphic card", "weak");
        Row computer4 = new Row("computer4");
        computer4.setAttributeValue("ram", "16");
        computer4.setAttributeValue("screen size", "small");
        computer4.setAttributeValue("ssd", "lot");
        computer4.setAttributeValue("graphic card", "weak");
        testRows.add(computer2);
        testRows.add(computer4);
        Dataset testDataset = new Dataset(testAttributes,testRows,AttributeSelection.ASCENDING_ALPHABETICAL);
        assertEquals(testDataset.getDataObjects().size(),
                this.testGeneratorLargeDataset.partitionData(this.trainingLargeDataset, "graphic card").get(1).getDataObjects().size());



        // Partition for strong (graphic card)
        List<Row> testRows2 = new ArrayList<>();
        List<String> testAttributes2 = new ArrayList<>(this.trainingLargeDataset.attributeList);
        Row computer1 = new Row("computer1");
        computer1.setAttributeValue("ram", "4");
        computer1.setAttributeValue("screen size", "large");
        computer1.setAttributeValue("ssd", "lot");
        computer1.setAttributeValue("graphic card", "strong");
        Row computer3 = new Row("computer3");
        computer3.setAttributeValue("ram", "16");
        computer3.setAttributeValue("screen size", "large");
        computer3.setAttributeValue("ssd", "little");
        computer3.setAttributeValue("graphic card", "strong");
        Row computer5 = new Row("computer5");
        computer5.setAttributeValue("ram", "8");
        computer5.setAttributeValue("screen size", "large");
        computer5.setAttributeValue("ssd", "little");
        computer5.setAttributeValue("graphic card", "strong");
        Row computer6 = new Row("computer6");
        computer6.setAttributeValue("ram", "8");
        computer6.setAttributeValue("screen size", "small");
        computer6.setAttributeValue("ssd", "lot");
        computer6.setAttributeValue("graphic card", "strong");
        Row computer7 = new Row("computer7");
        computer7.setAttributeValue("ram", "8");
        computer7.setAttributeValue("screen size", "small");
        computer7.setAttributeValue("ssd", "little");
        computer7.setAttributeValue("graphic card", "strong");
        testRows2.add(computer1);
        testRows2.add(computer3);
        testRows2.add(computer5);
        testRows2.add(computer6);
        testRows2.add(computer7);
        Dataset testDataset2 = new Dataset(testAttributes2,testRows2,AttributeSelection.ASCENDING_ALPHABETICAL);
        assertEquals(testDataset2.getDataObjects().size(),
                this.testGeneratorLargeDataset.partitionData(this.trainingLargeDataset, "graphic card").get(0).getDataObjects().size());

        // Partition for Strong (graphic card) then for 8 (ram)
        List<Row> testRows3 = new ArrayList<>();
        List<String> testAttributes3 = new ArrayList<>(this.trainingLargeDataset.attributeList);
        testRows3.add(computer5);
        testRows3.add(computer6);
        testRows3.add(computer7);
        Dataset testDataset3 = new Dataset(testAttributes3,testRows3,AttributeSelection.ASCENDING_ALPHABETICAL);
        assertEquals(testDataset3.getDataObjects().size(),
                this.testGeneratorLargeDataset.partitionData(testDataset2, "ram").get(2).getDataObjects().size());

    }

    /**
     * Tests the getDefaultDecision() algorithm from the TreeGenerator class.
     * We input computers that have unexpected fields such as medium, 32, and giant to see what will the actual
     * outcome be. We compare the actual outcome by the expected outcome we determined looking at out dataset.
     */
    @Test
    public void defaultDecision() {
        Row computer2 = new Row("computer2");
        computer2.setAttributeValue("ram", "4");
        computer2.setAttributeValue("screen size", "small");
        computer2.setAttributeValue("ssd", "little");
        computer2.setAttributeValue("graphic card", "medium");
        Assert.assertEquals("reject", this.testGeneratorLargeDataset.getDecision(computer2));

        Row computer3 = new Row("computer3");
        computer3.setAttributeValue("ram", "32");
        computer3.setAttributeValue("screen size", "small");
        computer3.setAttributeValue("ssd", "little");
        computer3.setAttributeValue("graphic card", "strong");
        Assert.assertEquals("accept", this.testGeneratorLargeDataset.getDecision(computer3));

        Row computer4 = new Row("computer4");
        computer4.setAttributeValue("ram", "8");
        computer4.setAttributeValue("screen size", "giant");
        computer4.setAttributeValue("ssd", "little");
        computer4.setAttributeValue("graphic card", "strong");
        Assert.assertEquals("accept", this.testGeneratorLargeDataset.getDecision(computer3));
    }

}
