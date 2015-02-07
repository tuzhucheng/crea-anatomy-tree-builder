package crea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DepthFirstAlgorithm {
    JSONObject tree;
    private long maxRecursionCalls = 100;
    private HashMap<Integer, Node> flatNodes;
    private boolean[] encountered;
    private int[] hits;

    private int iters = 0;

    public DepthFirstAlgorithm(JSONObject tree, long maxRecursionCalls, HashMap<Integer, Node> flatNodes, boolean[] encountered, int[] hits) {
        this.maxRecursionCalls = maxRecursionCalls;
        this.flatNodes = flatNodes;
        this.encountered = encountered;
        this.hits = hits;
        this.tree = tree;
    }

    public void depthFirstConstruction(JSONObject node) {
        int nodeId = (Integer) node.get("id");
        encountered[nodeId] = true;
        hits[nodeId]++;
        // System.out.println(nodeId);
        int[] childrenList =  (int[]) node.get("children");

        // Base Case
        if (childrenList.length == 0) {
            node.put("children", new ArrayList<Integer>(0));
            return;
        }

        // Bound recursion
        iters++;
        if (iters > maxRecursionCalls)
            return;

        JSONArray children = new JSONArray();
        node.put("children", children);

        for (int i = 0; i < childrenList.length; i++) {
            JSONObject newNode = new JSONObject();
            children.add(newNode);

            newNode.put("id", childrenList[i]);
            newNode.put("names", new ArrayList<String>(Arrays.asList(flatNodes.get(childrenList[i]).getNames())));
            int[] childrenIds = flatNodes.get(childrenList[i]).getChildrenIds();
            newNode.put("children", childrenIds);

            depthFirstConstruction(newNode);
        }

        return;
    }
}
