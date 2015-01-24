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

        System.out.println(serializedString);
        flatNodes = Pherialize.unserialize(serializedString).toArray().getArray(0);
    }

    public ArrayList<String> convertMapToListOfStrings(MixedArray mixedArray) {
        ArrayList<String> list = new ArrayList<String>();
        for (Object m : mixedArray.values()) {
            list.add(m.toString());
        }
        return list;
    }

    public ArrayList<Object> convertMapToList(MixedArray mixedArray) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object m : mixedArray.values()) {
            String s = m.toString();
            Integer i = Integer.parseInt(s);
            list.add(i);
        }
        return list;
    }

    public void buildTree() throws IOException {
        // populate flatNodes
        initialize();
        JSONObject tree = new JSONObject();
        MixedArray rootNodeInfo = flatNodes.getArray(0);

        tree.put("names", convertMapToListOfStrings(rootNodeInfo.getArray(0)));
        tree.put("children", convertMapToList(rootNodeInfo.getArray(2)));

        // TODO Implement algorithm here

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
