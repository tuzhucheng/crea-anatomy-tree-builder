package crea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DepthFirstAlgorithm {
    private long maxRecursionCalls = 100;
    private HashMap<Integer, Node> flatNodes;
    private boolean[] encountered;

    private int iters = 0;

    public DepthFirstAlgorithm(long maxRecursionCalls, HashMap<Integer, Node> flatNodes, boolean[] encountered) {
        this.maxRecursionCalls = maxRecursionCalls;
        this.flatNodes = flatNodes;
        this.encountered = encountered;
    }

    public JSONObject depthFirstConstruction(JSONObject node) {
        iters++;
        if (iters > maxRecursionCalls)
            return new JSONObject();

        int nodeId = (Integer) node.get("id");
        encountered[nodeId] = true;
        System.out.println(nodeId);

        int[] childrenList =  (int[]) node.get("children");

        if (childrenList.length == 0) {
            return new JSONObject();
        }

        JSONArray children = new JSONArray();

        for (int i = 0; i < childrenList.length; i++) {
            JSONObject newNode = new JSONObject();
            children.add(newNode);

            newNode.put("id", childrenList[i]);
            newNode.put("names", new ArrayList<String>(Arrays.asList(flatNodes.get(nodeId).getNames())));
            int[] childrenIds = flatNodes.get(nodeId).getChildrenIds();

            if (childrenIds.length != 0) {
                newNode.put("children", childrenIds);

                JSONObject returnedNode = depthFirstConstruction(newNode);
                if (!returnedNode.toString().equals("{}")) {
                    children.set(i, returnedNode);
                }
            }
        }
        node.put("children", children);

        return new JSONObject();
    }
}
