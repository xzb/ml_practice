package DecisionTree;

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
}
