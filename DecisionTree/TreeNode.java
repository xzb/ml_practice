package DecisionTree;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by xiezebin on 2/16/16.
 */
public class TreeNode {
    public int numTag;

    public boolean isLeaf;
    public String classType;

    public int attribute;
    public TreeNode left;
    public TreeNode right;

    TreeNode()
    {
    }

    public static TreeNode findNodeByTag(TreeNode arNode, int arTag)
    {
        if (arNode == null)
        {
            return null;
        }
        if (arNode.numTag == arTag)
        {
            return arNode;
        }

        TreeNode rvNode = findNodeByTag(arNode.left, arTag);
        if (rvNode != null)
        {
            return rvNode;
        }
        return findNodeByTag(arNode.right, arTag);
    }

    public static int getNumOfNodes(TreeNode arNode)
    {
        if (arNode == null)
        {
            return 0;
        }

        return 1 + getNumOfNodes(arNode.left) + getNumOfNodes(arNode.right);
    }


//    public static int getHeight(TreeNode arNode)
//    {
//        if (arNode == null)
//        {
//            return -1;
//        }
//        else
//        {
//            return 1 + Math.max(getHeight(arNode.left), getHeight(arNode.right));
//        }
//    }

    public static int getNumOfLeaves(TreeNode arNode)
    {
        if (arNode == null)
        {
            return 0;
        }

        if (arNode.isLeaf)
        {
            return 1;
        }

        return getNumOfLeaves(arNode.left) + getNumOfLeaves(arNode.right);
    }

    public static double getAverageDepth(TreeNode arNode)
    {
        Queue<Pair<TreeNode, Integer>> loQueue = new LinkedList<>();
        if (arNode != null)
        {
            loQueue.add(new Pair<>(arNode, 0));
        }

        Pair<TreeNode, Integer> loPair;
        TreeNode loNode;
        int loCurrentHeight;
        int loSumLeafHeight = 0;
        while (!loQueue.isEmpty())
        {
            loPair = loQueue.poll();
            loNode = loPair.getKey();
            loCurrentHeight = loPair.getValue();
            if (loNode.isLeaf)
            {
                loSumLeafHeight += loCurrentHeight;
            }
            else
            {
                if (loNode.left != null) loQueue.add(new Pair<>(loNode.left, loCurrentHeight + 1));
                if (loNode.right != null) loQueue.add(new Pair<>(loNode.right, loCurrentHeight + 1));
            }
        }

        return loSumLeafHeight * 1.0 / getNumOfLeaves(arNode);
    }
}
