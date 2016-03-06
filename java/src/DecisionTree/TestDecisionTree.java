package DecisionTree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiezebin on 2/16/16.
 */
public class TestDecisionTree
{
    private static Map<Integer, String[]> obDataPool;
    private static int obNumOfAttr;

    public static double test(TreeNode arNode, String[][] arDataSet)
    {
        obDataPool = new HashMap<>();
        for (int i = 1; i < arDataSet.length; i++)
        {
            String[] lpItem = arDataSet[i];
            obDataPool.put(i, lpItem);
        }
        obNumOfAttr = arDataSet[0].length - 1;

        int[] trueFalse = testHelper(arNode, obDataPool.keySet());

        return trueFalse[0] * 1.0 / (trueFalse[0] + trueFalse[1]);
    }

    private static int[] testHelper(TreeNode arNode, Set<Integer> arPoolKeySet)
    {
        if (arNode.isLeaf)
        {
            int[] trueFalse = new int[2];
            for (Integer poolKey : arPoolKeySet)
            {
                String[] loItem = obDataPool.get(poolKey);
                if (arNode.classType.equals(loItem[obNumOfAttr]))
                {
                    trueFalse[0]++;
                }
                else
                {
                    trueFalse[1]++;
                }
            }
            return trueFalse;
        }

        Set<Integer> loLeftPoolKeySet = new HashSet<>();
        Set<Integer> loRightPoolKeySet = new HashSet<>();
        int loNodeAttr = arNode.attribute;
        for (Integer poolKey : arPoolKeySet)
        {
            String[] loItem = obDataPool.get(poolKey);
            if ("0".equals(loItem[loNodeAttr]))
            {
                loLeftPoolKeySet.add(poolKey);
            }
            else
            {
                loRightPoolKeySet.add(poolKey);
            }
        }
        int[] leftRes = testHelper(arNode.left, loLeftPoolKeySet);
        int[] rightRes = testHelper(arNode.right, loRightPoolKeySet);

        return new int[]{ leftRes[0] + rightRes[0], leftRes[1] + rightRes[1] };
    }
}
