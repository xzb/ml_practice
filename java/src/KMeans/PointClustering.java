package KMeans;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by xiezebin on 4/26/16.
 */
public class PointClustering {
    private final static String DATA_FILE = "data/KMeans/test_data.txt";

    private static double[][] obPoints;
    private static double[][] obCentroids;
    private static Map<Integer, List<Integer>> obCenBuckets;    //point offset -1

    private static void initData(String arFile, int k)
    {
        obCentroids = new double[k][2];
        obCenBuckets = new HashMap<>();

        // initial centroids
        Random rand = new Random();
        for (int centId = 0; centId < k; centId++)
        {
            obCentroids[centId][0] = rand.nextDouble();
            obCentroids[centId][1] = rand.nextDouble();
            obCenBuckets.put(centId, new LinkedList<Integer>());
        }

        // read points from file
        String line;
        List<String> loContent = new LinkedList<>();
        try{
            FileReader fr = new FileReader(arFile);
            BufferedReader bfr= new BufferedReader(fr);
            bfr.readLine();             //remove header

            while((line = bfr.readLine()) != null) {
                loContent.add(line);
            }
            bfr.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        obPoints = new double[loContent.size()][2];
        int pointId = 0;
        for (String cont : loContent)
        {
            String[] parts = cont.split("\\t");
            obPoints[pointId][0] = Double.valueOf(parts[1]);
            obPoints[pointId][1] = Double.valueOf(parts[2]);
            pointId++;
        }
    }

    private static double distance(double[] po1, double[] po2)
    {
        double delX = po1[0] - po2[1];
        double delY = po1[0] - po2[1];
        return delX * delX + delY * delY;
    }

    /**
     * running time O(K)
     * @param pointId
     */
    private static void classPoint(int pointId)
    {
        double[] loPoint = obPoints[pointId];
        double minDist = Double.MAX_VALUE;
        int minCentId = 0;

        for (int centId = 0; centId < obCentroids.length; centId++)
        {
            double[] loCentroid = obCentroids[centId];
            double curDist = distance(loPoint, loCentroid);
            if (curDist < minDist)
            {
                minDist = curDist;
                minCentId = centId;
            }
        }

        // save pointId to cluster bucket
        obCenBuckets.get(minCentId).add(pointId);
    }

    private static void clearBuckets()
    {
        for (int centId = 0; centId < obCenBuckets.size(); centId++)
        {
            obCenBuckets.get(centId).clear();
        }
    }

    /**
     * running time: O(N*K)
     */
    private static void classAllPoints()
    {
        for (int pointId = 0; pointId < obPoints.length; pointId++)
        {
            classPoint(pointId);
        }
    }

    /**
     * running time: O(N*K)
     */
    private static void updateCentroid()
    {
        for (int centId = 0; centId < obCentroids.length; centId++)
        {
            List<Integer> pointLs = obCenBuckets.get(centId);
            if (pointLs.size() == 0)
            {
                continue;
            }

            double sumX = 0;
            double sumY = 0;
            for (int pointId : pointLs)
            {
                double[] point = obPoints[pointId];
                sumX += point[0];
                sumY += point[1];
            }
            obCentroids[centId][0] = sumX / pointLs.size();
            obCentroids[centId][1] = sumY / pointLs.size();
        }
    }

    private static double evaluate()
    {
        return 0;
    }

    public static void main(String[] args)
    {
//        if (args.length)

        int k = 10;

        initData(DATA_FILE, k);

//        for (int i = 0; i < 5; i++)
//        {
            clearBuckets();
            classAllPoints();
            updateCentroid();
            evaluate();
//        }
    }
}
