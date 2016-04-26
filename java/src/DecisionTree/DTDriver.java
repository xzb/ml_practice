package DecisionTree;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by xiezebin on 2/11/16.
 */
public class DTDriver
{
    private final static String TRAINING_SET_1 = "/data/data_sets1/training_set.csv";
    private final static String VALIDATION_SET_1 = "/data/data_sets1/validation_set.csv";
    private final static String TEST_SET_1 = "/data/data_sets1/test_set.csv";
    private final static String TRAINING_SET_2 = "/data/data_sets2/training_set.csv";
    private final static String VALIDATION_SET_2 = "/data/data_sets2/validation_set.csv";
    private final static String TEST_SET_2 = "/data/data_sets2/test_set.csv";

    private static String[][] getCSVcontent(String arPath) throws Exception
    {
        Path currentRelativePath = Paths.get("");
        String loAbsolutePath = currentRelativePath.toAbsolutePath().toString() + arPath;

        try (CSVReader reader = new CSVReader(new BufferedReader(new FileReader(loAbsolutePath)));)
        {
            List<String[]> lines = reader.readAll();
            return lines.toArray(new String[lines.size()][]);
        }
    }

    public static void main(String[] args) throws Exception
    {
//        if (args.length < 5) {
//            System.err.println("<num to prune> <training_file> <validate_file> <test_file> <1/0 print>");
//            System.exit(1);
//        }
//
//        int numToPrune = Integer.valueOf(args[0]);
//        String loTrainingFile = args[1];
//        String loValidateFile = args[2];
//        String loTestFile = args[3];
//        boolean shouldPrint = "1".equals(args[4]);
//        boolean showBonusResult = args.length == 6 && "1".equals(args[5]);


        int numToPrune = 0;
        String loTrainingFile = TRAINING_SET_1;
        String loValidateFile = VALIDATION_SET_1;
        String loTestFile = TEST_SET_1;
        boolean shouldPrint = false;
        boolean showBonusResult = true;


        // build tree
        TreeNode loTreeNode = BuildDecisionTree.build(getCSVcontent(loTrainingFile));

        // prune tree
        int numPruned = PruneDecisionTree.prune(loTreeNode, getCSVcontent(loValidateFile), numToPrune);

        // test tree
        double accuracy = TestDecisionTree.test(loTreeNode, getCSVcontent(loTestFile));
        System.out.println("Test Accuracy: " + accuracy);

        if (shouldPrint)
        {
            BuildDecisionTree.printTree(loTreeNode, 0);
        }


        if (showBonusResult)
        {
            double loAveDepthOfID3Tree = TreeNode.getAverageDepth(loTreeNode);
            int loNodesNumOfID3Tree = TreeNode.getNumOfNodes(loTreeNode);

            loTreeNode = BuildRandomAttributeTree.build(getCSVcontent(loTrainingFile));

            numPruned = PruneDecisionTree.prune(loTreeNode, getCSVcontent(loValidateFile), numToPrune);

            accuracy = TestDecisionTree.test(loTreeNode, getCSVcontent(loTestFile));
            System.out.println("Test Accuracy: " + accuracy);

            double loAveDepthOfRandomTree = TreeNode.getAverageDepth(loTreeNode);
            int loNodesNumOfRandomTree = TreeNode.getNumOfNodes(loTreeNode);

            System.out.println("Average Depth of ID3 tree: " + loAveDepthOfID3Tree);
            System.out.println("Num of Nodes of ID3 tree: " + loNodesNumOfID3Tree);
            System.out.println("Average Depth of random attribute tree: " + loAveDepthOfRandomTree);
            System.out.println("Num of Nodes of random attribute tree: " + loNodesNumOfRandomTree);
        }
    }
}
