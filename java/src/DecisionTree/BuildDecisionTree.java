package DecisionTree;

import java.util.*;

/**
 * Created by xiezebin on 2/16/16.
 */
public class BuildDecisionTree {
    private static TreeNode obRoot;
    private static Map<Integer, String[]> obDataPool;
    private static int obNumOfAttr;
    private static boolean[] obAttrVisited;
    private static String[] obAttrName;

    private static double getEntropy(int numOfClassZero, int numOfClassOne)
    {
        if (numOfClassZero == 0 || numOfClassOne == 0)
        {
            return 0;
        }
        int totalNum = numOfClassZero + numOfClassOne;
        double propOne = numOfClassZero * 1.0 / totalNum;
        double propZero = numOfClassOne * 1.0 / totalNum;
        return - propOne * Math.log(propOne) - propZero * Math.log(propZero);
    }


    /**
     * Build decision tree by DFS
     * @param arDataSet
     * @throws Exception
     */
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
        assignTag(obRoot);

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


        // STEP 2: find the best attribute
        double loParentEntropy = getEntropy(numOfClassZero, numOfClassOne);
        int loTotal = arPoolKeySet.size();

        Integer loBestAttribute = -1;
        double loMaxInfoGain = 0;
        for(Integer attr = 0; attr < obNumOfAttr; attr++)
        {
            if (obAttrVisited[attr])        // is it possible that whole attr are visited, but class still different?
            {
                continue;
            }

            int splitLeftCount = 0;
            int splitRightCount = 0;
            int leftZeroCount = 0;
            int leftOneCount = 0;
            int rightZeroCount = 0;
            int rightOneCount = 0;

            for (Integer poolKey : arPoolKeySet)
            {
                String[] loItem = obDataPool.get(poolKey);
                boolean loClassIsZero = "0".equals(loItem[obNumOfAttr]);
                if ("0".equals(loItem[attr]))
                {
                    splitLeftCount++;
                    if (loClassIsZero)
                    {
                        leftZeroCount++;
                    }
                    else
                    {
                        leftOneCount++;
                    }
                }
                else
                {
                    splitRightCount++;
                    if (loClassIsZero)
                    {
                        rightZeroCount++;
                    }
                    else
                    {
                        rightOneCount++;
                    }
                }
            }

            double loLeftChildEntropy = getEntropy(leftZeroCount, leftOneCount);
            double loRightChildEntropy = getEntropy(rightZeroCount, rightOneCount);
            double loInfoGain = loParentEntropy - splitLeftCount * 1.0 / loTotal * loLeftChildEntropy
                    - splitRightCount * 1.0 / loTotal * loRightChildEntropy;

            if (loInfoGain > loMaxInfoGain)
            {
                loMaxInfoGain = loInfoGain;
                loBestAttribute = attr;
            }
        }

        // STEP 3: assign the best attribute to treeNode, find the split data again and pass to child
        if (loBestAttribute == -1)      // this dataset is not split-able
        {
            rvNode.isLeaf = true;
            rvNode.classType = Math.random() > 0.5 ? "1" : "0";
            return rvNode;
        }

        Set<Integer> loLeftPoolKeySet = new HashSet<>();
        Set<Integer> loRightPoolKeySet = new HashSet<>();
        for (Integer poolKey : arPoolKeySet)
        {
            String[] loItem = obDataPool.get(poolKey);
            if ("0".equals(loItem[loBestAttribute]))
            {
                loLeftPoolKeySet.add(poolKey);
            }
            else
            {
                loRightPoolKeySet.add(poolKey);
            }
        }

        rvNode.attribute = loBestAttribute;
        obAttrVisited[loBestAttribute] = true;  // forbid child to use this attribute
        rvNode.left = buildHelper(loLeftPoolKeySet);
        rvNode.right = buildHelper(loRightPoolKeySet);
        obAttrVisited[loBestAttribute] = false;
        return rvNode;
    }

    public static void assignTag(TreeNode arNode)
    {
        Queue<TreeNode> loQueue = new LinkedList<>();
        loQueue.add(arNode);
        int loTag = 1;
        TreeNode tmp;
        while (!loQueue.isEmpty())
        {
            tmp = loQueue.poll();
            tmp.numTag = loTag;
            loTag++;
            if (tmp.left != null)
            {
                loQueue.add(tmp.left);
            }
            if (tmp.right != null)
            {
                loQueue.add(tmp.right);
            }
        }
    }

    public static void printTree(TreeNode arNode, int arLevel)
    {
        if (arNode.isLeaf)
        {
            System.out.println(arNode.classType);
            return;
        }
        else
        {
            System.out.println();
        }

        StringBuilder loBuilder = new StringBuilder();
        for (int i = 0; i < arLevel; i++)
        {
            loBuilder.append("| ");
        }
        loBuilder.append(obAttrName[arNode.attribute] + " = ");

        System.out.print(loBuilder.toString() + "0 : ");
        printTree(arNode.left, arLevel + 1);

        System.out.print(loBuilder.toString() + "1 : ");
        printTree(arNode.right, arLevel + 1);
    }
}
