package crea;

import java.util.ArrayList;

public class Node {
    private int childrenCount;
    private String[] names;
    private int[] childrenIds;

    public Node(int childrenCount, String[] names, int[] childrenIds) {
        this.childrenCount = childrenCount;
        this.names = names;
        this.childrenIds = childrenIds;
    }

    public String[] getNames() {
        return names;
    }

    public int[] getChildrenIds() {
        return childrenIds;
    }
}
