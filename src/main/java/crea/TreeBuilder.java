package crea;

import de.ailis.pherialize.Mixed;
import de.ailis.pherialize.MixedArray;
import de.ailis.pherialize.Pherialize;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class TreeBuilder {

    private String treeFile;
    private MixedArray flatNodes;

    public TreeBuilder(String treeFile) {
        this.treeFile = treeFile;
        System.out.println("Using tree file " + treeFile);
    }

    /**
     * Unserializes the tree from the tree file into a MixedArray and stores it in flatNodes
     */
    private void initialize() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(treeFile);
        File file = null;
        if (resource != null) {
            file = new File(resource.getFile());
        }
        else {
            System.err.println("Resource is not found!");
            System.exit(1);
        }
        StringBuilder sb = new StringBuilder("");

        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sb.append(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String serializedString = sb.toString();

        //System.out.println(serializedString);
        flatNodes = Pherialize.unserialize(serializedString).toArray().getArray(0);
        //System.out.println(flatNodes);
    }

    public void buildTree() throws IOException {
        // populate flatNodes
        initialize();
        JSONObject tree = new JSONObject();
        MixedArray rootNodeInfo = flatNodes.getArray(0);

        tree.put("id", 0);
        tree.put("names", Utils.convertMapToListOfStrings(rootNodeInfo.getArray(0)));
        tree.put("children", Utils.convertMapToList(rootNodeInfo.getArray(2)));

        // TODO Implement algorithm here
        DepthFirstAlgorithm.depthFirstConstruction(tree, flatNodes);

        FileWriter file = null;
        try {
            file = new FileWriter("output.json");
            file.write(tree.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }

        System.out.println("Finished building tree");

    }
}
