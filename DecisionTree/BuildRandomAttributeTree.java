package DecisionTree;

import java.util.*;

/**
 * Created by xiezebin on 2/18/16.
 */
public class BuildRandomAttributeTree
{
    private static TreeNode obRoot;
    private static Map<Integer, String[]> obDataPool;
    private static int obNumOfAttr;
    private static boolean[] obAttrVisited;
    private static String[] obAttrName;

    public static TreeNode build(String[][] arDataSet) throws Exception
    {
        // initial data pool, prepare for DFS
        obDataPool = new HashMap<>();
        for (int i = 1; i < arDataSet.length; i++)
        {
            String[] lpItem = arDataSet[i];
            obDataPool.put(i, lpItem);
        }

        obNumOfAttr = arDataSet[0].length - 1;
        obAttrVisited = new boolean[obNumOfAttr];

        obAttrName = new String[obNumOfAttr];
        for (int i = 0; i < obNumOfAttr; i++)
        {
            obAttrName[i] = arDataSet[0][i];
        }

        // begin DFS
        obRoot = buildHelper(obDataPool.keySet());

        // assign Tag to DecisionTree
        BuildDecisionTree.assignTag(obRoot);

        return obRoot;
    }
    private static TreeNode buildHelper(Set<Integer> arPoolKeySet)
    {
        TreeNode rvNode = new TreeNode();

        // STEP 1: first check whether all data in this node are of same class
        int numOfClassZero = 0;
        int numOfClassOne = 0;
        for (Integer poolKey: arPoolKeySet)
        {
            String classType = obDataPool.get(poolKey)[obNumOfAttr];  //get the last column of each item
            if ("0".equals(classType))
            {
                numOfClassZero++;
            }
            else
            {
                numOfClassOne++;
            }
        }
        if (numOfClassOne == arPoolKeySet.size())
        {
            rvNode.isLeaf = true;
            rvNode.classType = "1";
            return rvNode;
        }
        else if (numOfClassZero == arPoolKeySet.size())
        {
            rvNode.isLeaf = true;
            rvNode.classType = "0";
            return rvNode;
        }
        rvNode.classType = numOfClassZero > numOfClassOne ? "0" : "1";


        // STEP 2: count the current available attributes, and pick one randomly
        List<Integer> loNotVisitedAttributes = new ArrayList<>();
        for (int i = 0; i < obNumOfAttr; i++)
        {
            if (!obAttrVisited[i])
            {
                loNotVisitedAttributes.add(i);
            }
        }
        // all attributes are visited
        if (loNotVisitedAttributes.size() == 0)
        {
            rvNode.isLeaf = true;
            return rvNode;
        }
        // pick a random attribute
        int loRandomIndex = (int) Math.round(Math.random() * (loNotVisitedAttributes.size() - 1));
        int loRandomAttribute = loNotVisitedAttributes.get(loRandomIndex);


        // STEP 3: assign the best attribute to treeNode, find the split data again and pass to child
        Set<Integer> loLeftPoolKeySet = new HashSet<>();
        Set<Integer> loRightPoolKeySet = new HashSet<>();
        for (Integer poolKey : arPoolKeySet)
        {
            String[] loItem = obDataPool.get(poolKey);
            if ("0".equals(loItem[loRandomAttribute]))
            {
                loLeftPoolKeySet.add(poolKey);
            }
            else
            {
                loRightPoolKeySet.add(poolKey);
            }
        }

        rvNode.attribute = loRandomAttribute;
        obAttrVisited[loRandomAttribute] = true;  // forbid child to use this attribute
        rvNode.left = buildHelper(loLeftPoolKeySet);
        rvNode.right = buildHelper(loRightPoolKeySet);
        obAttrVisited[loRandomAttribute] = false;
        return rvNode;
    }
}
