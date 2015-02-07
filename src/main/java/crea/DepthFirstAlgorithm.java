package crea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class DepthFirstAlgorithm {
    JSONObject tree;
    private boolean boundRecursion;
    private long maxRecursionCalls;
    private HashMap<Integer, Node> flatNodes;
    private boolean[] encountered;
    private int[] hits;
    private ArrayList<HashSet<Integer>> ancestors;

    private long iters = 0;

    public DepthFirstAlgorithm(JSONObject tree, boolean boundRecursion, long maxRecursionCalls, HashMap<Integer, Node> flatNodes, boolean[] encountered, int[] hits, ArrayList<HashSet<Integer>> ancestors) {
        this.boundRecursion = boundRecursion;
        this.maxRecursionCalls = maxRecursionCalls;
        this.flatNodes = flatNodes;
        this.encountered = encountered;
        this.hits = hits;
        this.tree = tree;
        this.ancestors = ancestors;
    }

    public void depthFirstConstruction(JSONObject node) {
        int nodeId = (Integer) node.get("id");
        encountered[nodeId] = true;
        hits[nodeId]++;
        int[] childrenList =  (int[]) node.get("children");

        // Base Case
        if (childrenList.length == 0) {
            node.put("children", new ArrayList<Integer>(0));
            return;
        }

        // Bound recursion
        if (boundRecursion) {
            iters++;
            if (iters > maxRecursionCalls)
                return;
        }

        JSONArray children = new JSONArray();
        node.put("children", children);

        HashSet<Integer> ancestorsOfParent = ancestors.get(nodeId);

        for (int i = 0; i < childrenList.length; i++) {
            int childrenNodeId = childrenList[i];
            JSONObject newNode = new JSONObject();
            children.add(newNode);

            newNode.put("id", childrenNodeId);
            newNode.put("names", new ArrayList<String>(Arrays.asList(flatNodes.get(childrenNodeId).getNames())));

            HashSet<Integer> ancestorsOfChildren = ancestors.get(childrenNodeId);
            ancestorsOfChildren.add(nodeId);
            for (int ancestorId : ancestorsOfParent) {
                ancestorsOfChildren.add(ancestorId);
            }

            int[] childrenIds = flatNodes.get(childrenNodeId).getChildrenIds();
            boolean foundCycle = false;
            for (int j = 0; j < childrenIds.length; j++) {
                if (ancestorsOfChildren.contains(childrenIds[j])) {
                    foundCycle = true;
                    break;
                }
            }

            if (!foundCycle) {
                newNode.put("children", childrenIds);
                depthFirstConstruction(newNode);
            } else {
                newNode.put("cycle", true);
            }

        }

        return;
    }
}
