package DecisionTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiezebin on 2/16/16.
 */
public class PruneDecisionTree {
    private static TreeNode obRoot;
    private static String[][] obDataSet;
    private static double obAccuracy;

    public static int prune(TreeNode arNode, String[][] arDataSet, int arNumToPrune)
    {
        // do not prune
        if (arNumToPrune == 0)
        {
            return 0;
        }

        obRoot = arNode;
        obDataSet = arDataSet;
        obAccuracy = TestDecisionTree.test(arNode, arDataSet);

        System.out.println("Before Prune Accuracy: " + obAccuracy);

        int startIndex = 4;
        int endIndex = TreeNode.getNumOfNodes(arNode);
        List<Integer> loList = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++)
        {
            loList.add(i);
        }

        // randomly permute the tag of decision tree
        Collections.shuffle(loList);

        // prune the node one-by-one, until reach the required num
        int pruneCount = 0;
        for (Integer tag : loList)
        {
            if (pruneHelper(tag))
            {
                pruneCount++;
                if (pruneCount == arNumToPrune)
                {
                    break;
                }
            }
        }

        System.out.println("After Prune Accuracy: " + obAccuracy);
        System.out.println("Num of Node Pruned: " + pruneCount);
        return pruneCount;
    }

    public static boolean pruneHelper(int arTag)
    {
        TreeNode loNodeOfTag = TreeNode.findNodeByTag(obRoot, arTag);

        // this node is leaf in fact, cannot prune
        if (loNodeOfTag.isLeaf)
        {
            return false;
        }

        // prune the node
        loNodeOfTag.isLeaf = true;

        double loAccuracy = TestDecisionTree.test(obRoot, obDataSet);
        if (loAccuracy > obAccuracy)
        {
            obAccuracy = loAccuracy;
            return true;
        }
        else
        {
            // revert pruning
            loNodeOfTag.isLeaf = false;
            return false;
        }
    }
}
