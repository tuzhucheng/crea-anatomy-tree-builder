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

    private HashMap<Integer, Node> flatNodes;
    private int totalNodeTypes;
    private boolean[] encountered;

    public TreeBuilder() {
        resourceReader = new ResourceReader();
        treeFile = resourceReader.getProperty("inputFile");
        maxRecursionCalls = Long.valueOf(resourceReader.getProperty("maxRecursionCalls"));

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
            JSONObject unprocessedFlatNodes = (JSONObject) jsonArray.get(0);
            totalNodeTypes = unprocessedFlatNodes.size();
            flatNodes = new HashMap<Integer, Node>(totalNodeTypes);
            encountered = new boolean[totalNodeTypes];

            for (Iterator it = unprocessedFlatNodes.keySet().iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                if (key != null && !key.equals("")) {
                    JSONObject unprocessedNode = (JSONObject) unprocessedFlatNodes.get(key);

                    int nodeId = Integer.valueOf(key);
                    ArrayList<String> names = Utils.convertJsonArrayToListOfStrings((JSONArray) unprocessedNode.get("0"));
                    ArrayList<Integer> childIds = Utils.convertJsonArrayToListOfInts((JSONArray) unprocessedNode.get("2"));

                    String[] primNames = names.toArray(new String[names.size()]);
                    int[] primChildIds = Utils.convertIntegers(childIds);

                    Node node = new Node(primChildIds.length, primNames, primChildIds);
                    flatNodes.put(nodeId, node);
                }
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
            tree.put("encountered", encounteredNodes);
            tree.put("notEncountered", notEncounteredNodes);
            tree.put("encountered count", encounteredNodes.size());
            tree.put("notEncountered count", notEncounteredNodes.size());
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
            file.write(tree.toJSONString());

            indentedFile = new FileWriter("output_indented.json");
            Writer w = new JSonWriter();
            tree.writeJSONString(w);
            indentedFile.write(w.toString());

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
