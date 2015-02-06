package crea;

import com.tonian.director.dm.json.JSonWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class TreeBuilder {
    private ResourceReader resourceReader;

    // Properties read from configuration
    private String treeFile;
    private long maxRecursionCalls;
    private boolean outputStats;
    private boolean outputTree;
    private boolean outputIndentedTree;

    private HashMap<Integer, Node> flatNodes;
    private int totalNodeTypes;
    private boolean[] encountered;

    public TreeBuilder() {
        resourceReader = new ResourceReader();
        treeFile = resourceReader.getProperty("inputFile");
        maxRecursionCalls = Long.valueOf(resourceReader.getProperty("maxRecursionCalls"));
        outputStats = Boolean.valueOf(resourceReader.getProperty("outputStats"));
        outputTree = Boolean.valueOf(resourceReader.getProperty("outputTree"));
        outputIndentedTree = Boolean.valueOf(resourceReader.getProperty("outputIndentedTree"));

        System.out.println("Using tree file " + treeFile);
        System.out.println("Maximum recursion calls: " + maxRecursionCalls);
    }

    private void initialize() {
        StringBuilder sb = new StringBuilder("");

        try {
            Scanner scanner = new Scanner(resourceReader.getResourceFile(treeFile));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sb.append(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String serializedString = sb.toString();

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(serializedString);
            JSONArray jsonArray = (JSONArray) obj;

            Object elemZero = jsonArray.get(0);
            JSONArray elemZeroAsJsonArray;

            // convert JSONObject with index as key into JSONArray
            if (elemZero instanceof JSONObject) {
                JSONObject elemZeroAsJsonObject = (JSONObject) elemZero;
                elemZeroAsJsonArray = new JSONArray();

                for (int i = 0; i <= elemZeroAsJsonObject.size(); i++) {
                    elemZeroAsJsonArray.add(i, null);
                }

                for (Iterator it = elemZeroAsJsonObject.keySet().iterator(); it.hasNext(); ) {
                    String key = (String) it.next();
                    if (key != null && !key.equals("")) {
                        elemZeroAsJsonArray.set(Integer.valueOf(key), elemZeroAsJsonObject.get(key));
                    }
                }
            } else {
                elemZeroAsJsonArray = (JSONArray) elemZero;
            }

            totalNodeTypes = elemZeroAsJsonArray.size();
            flatNodes = new HashMap<Integer, Node>(totalNodeTypes);
            encountered = new boolean[totalNodeTypes];

            for (int i = 0; i < totalNodeTypes; i++) {
                JSONObject unprocessedNode = (JSONObject) elemZeroAsJsonArray.get(i);

                if (unprocessedNode == null) {
                    System.err.println("Node " + i + " in unprocessedNode is null.");
                    continue;
                }

                ArrayList<String> names = Utils.convertJsonArrayToListOfStrings((JSONArray) unprocessedNode.get("0"));
                ArrayList<Integer> childIds = Utils.convertJsonArrayToListOfInts((JSONArray) unprocessedNode.get("2"));

                String[] primNames = names.toArray(new String[names.size()]);
                int[] primChildIds = Utils.convertIntegers(childIds);

                Node node = new Node(primChildIds.length, primNames, primChildIds);
                flatNodes.put(i, node);
            }

            for (int i = 0; i < totalNodeTypes; i++) {
                encountered[i] = false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void buildTree() throws IOException {
        initialize();
        JSONObject outputJson = new JSONObject();
        JSONObject tree = new JSONObject();

        tree.put("id", 0);
        tree.put("names", new ArrayList<String>(Arrays.asList(flatNodes.get(0).getNames())));
        tree.put("children", flatNodes.get(0).getChildrenIds());

        try {
            DepthFirstAlgorithm algo = new DepthFirstAlgorithm(maxRecursionCalls, flatNodes, encountered);
            algo.depthFirstConstruction(tree);

            System.out.println("Done running algorithm.");

            ArrayList<Integer> encounteredNodes = new ArrayList<Integer>();
            ArrayList<Integer> notEncounteredNodes = new ArrayList<Integer>();
            for (int i = 0; i < encountered.length; i++) {
                if (encountered[i])
                    encounteredNodes.add(i);
                else
                    notEncounteredNodes.add(i);
            }

            if (outputStats) {
                outputJson.put("total unique nodes", encountered.length);
                outputJson.put("coverage", ((double) encounteredNodes.size()) / encountered.length * 100);
                outputJson.put("encountered", encounteredNodes);
                outputJson.put("notEncountered", notEncounteredNodes);
                outputJson.put("encountered count", encounteredNodes.size());
                outputJson.put("notEncountered count", notEncounteredNodes.size());
            }

            if (outputTree) {
                outputJson.put("tree", tree);
            }

        } catch (StackOverflowError e) {
            System.out.println(e);
            FileWriter file = null;
            try {
                file = new FileWriter("output.json");
                file.write(tree.toJSONString());
            } catch (IOException e2) {
                e2.printStackTrace();
            } finally {
                file.flush();
                file.close();
            }
        }


        FileWriter file = null;
        FileWriter indentedFile = null;
        try {
            file = new FileWriter("output.json");
            file.write(outputJson.toJSONString());

            indentedFile = new FileWriter("output_indented.json");
            if (outputIndentedTree) {
                Writer w = new JSonWriter();
                outputJson.writeJSONString(w);
                indentedFile.write(w.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();

            indentedFile.flush();
            indentedFile.close();
        }

        System.out.println("Finished building tree");

    }
}
