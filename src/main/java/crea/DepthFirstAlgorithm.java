package crea;

import de.ailis.pherialize.MixedArray;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DepthFirstAlgorithm {
    public static JSONObject depthFirstConstruction(JSONObject node, MixedArray flatNodes) {
        int nodeId = (Integer) node.get("id");

        ArrayList<Integer> childrenList =  (ArrayList<Integer>) node.get("children");
        JSONArray children = new JSONArray();
        for (Integer child : childrenList) {
            children.add(child);
        }

        if (children.size() == 0) {
            return new JSONObject();
        }

        for (int i = 0; i < children.size(); i++) {
            int childId = (Integer) children.get(i);
            JSONObject newNode = new JSONObject();
            MixedArray newNodeInfo = flatNodes.getArray(childId);
            newNode.put("id", childId);
            newNode.put("names", Utils.convertMapToListOfStrings(newNodeInfo.getArray(0)));
            newNode.put("children", Utils.convertMapToList(newNodeInfo.getArray(2)));

            children.set(i, newNode);

            JSONObject returnedNode = depthFirstConstruction(newNode, flatNodes);
            if (!returnedNode.toString().equals("{}")) {
                children.set(i, returnedNode);
            }
        }
        node.put("children", children);

        return new JSONObject();
    }
}
