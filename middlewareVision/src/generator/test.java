/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import middlewareVision.nodes.Visual.V4.V4CellStructure;
import org.opencv.core.Core;
import utils.FileUtils;

/**
 *
 * @author HumanoideFilms
 */
public class test {

    ArrayList<GArea> areas;
    ArrayList<smallNode> nodes;
    HashSet<String> allSmallNodes;

    public static void main(String[] args) {
        test t = new test();
        t.areas = new ArrayList();
        t.nodes = new ArrayList();
        t.allSmallNodes = new HashSet();
        t.walkin(new File("src/middlewareVision/nodes"));
        t.listAreas();
        t.generateNodeGraph();
        //t.generateProcessGraph();
    }

    public void walkin(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    walkin(listFile[i]);
                } else {
                    readFile(listFile[i].getPath());
                }
            }
        }
    }

    public void readFile(String path) {
        String file = FileUtils.readFile(new File(path));
        analyzeFile(file);
    }

    void analyzeFile(String file) {
        if (file.replaceAll(" ", "").contains("extendsArea")) {
            String[] ar = file.split("\n");
            for (String cad : ar) {
                if (cad.contains("public class")) {
                    String areaName = cad.replaceAll(" ", "").replace("publicclass", "").replace("extendsArea{", "");
                    areas.add(new GArea(areaName, addSmallNodes(file)));
                }
            }
        }
        if (file.replaceAll(" ", "").contains("extendsActivity")) {
            String[] ar = file.split("\n");
            for (String cad : ar) {
                if (cad.contains("public class")) {
                    String nodeName = cad.replaceAll(" ", "").replace("publicclass", "").replace("extendsActivity{", "");
                    nodes.add(new smallNode(nodeName, addNext(file)));
                }
            }
        }
    }

    ArrayList<String> addSmallNodes(String file) {
        ArrayList<String> nodes = new ArrayList();
        String lines[] = file.split("\n");
        for (String line : lines) {
            if (line.contains("addProcess(")) {
                String nodeName = line.replaceAll(" ", "").replaceAll("\t", "").replace("addProcess(", "").replace(".class);", "");
                if (!nodeName.contains("//")) {
                    nodes.add(nodeName);
                    allSmallNodes.add(nodeName);
                } else {
                    //nodes.add(nodeName.replaceAll("//", ""));
                }
            }
        }

        return nodes;
    }

    ArrayList<String> addNext(String file) {
        ArrayList<String> nodes = new ArrayList();
        String lines[] = file.split("\n");
        for (String line : lines) {
            if (line.contains("send(AreaNames.")) {
                String nodeName = line.replaceAll(" ", "").replaceAll("\t", "").replace("send(AreaNames.", "");
                nodeName = nodeName.substring(0, nodeName.indexOf(",")).replaceAll(" ", "");
                if (!nodeName.contains("//")) {
                    nodes.add(nodeName);
                }
            }
        }

        return nodes;
    }

    void listAreas() {
        for (GArea ga : areas) {
            System.out.println("area: " + ga.name);
            ga.listNodes();
        }
        System.out.println("--------");
        for (smallNode sn : nodes) {
            System.out.println("node: " + sn.name);
            sn.listNextNodes();
        }
    }

    void generateNodeGraph() {
        String c = "graph G{\n";
        String l1 = "[ label=\"@name\" shape=\"circle\" ]";
        String l2 = "[ label=\"@name\" shape=\"octagon\" ]";
        for (GArea ga : areas) {
            c = c + ga.name + " " + l1.replace("@name", ga.name) + "\n";
            for (String nodes : ga.smallNodes) {
                c = c + nodes + " " + l2.replace("@name", nodes) + "\n";
            }
        }
        c = c + "\n\n";
        for (GArea ga : areas) {
            for (String nodes : ga.smallNodes) {
                c = c + ga.name + " -- " + nodes + ";\n";
            }
            c = c + "\n";
        }
        c = c + "}";
        
        FileUtils.write("nodeDiagram", c, "txt");
        generateImg("nodeDiagram","png","circo");
        
    }
    
    public static void generateImg(String fileName, String format, String engine) {
        try {
            String cmd = "bin\\"+engine+".exe" + " -T" + format + " " +  fileName + ".txt "
                    + "-o " + fileName + "." + format;
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    void generateProcessGraph() {
        String c = "digraph G{\n";
        c = c + "rankdir=\"LR\"" + "\nnewrank=\"true\" \n";
        for (smallNode n : nodes) {
            if (allSmallNodes.contains(n.name)) {
                c = c + n.name + " [ shape=\"rectangle\" ] \n";
            }
        }
        c = c + "\n\n";
        for (smallNode n : nodes) {
            if (allSmallNodes.contains(n.name)) {
                for (String next : n.next) {
                    c = c + n.name + " -> " + next + " \n";
                }
            }
        }

        for (GArea ga : areas) {
            c = c + "\nsubgraph cluster" + ga.name + " {\n label=\"" + ga.name + "\"\nrank=\"same\"\n";
            for (String nodes : ga.smallNodes) {
                c = c + nodes + "\n";
            }
            c = c + "}\n";
        }

        c = c + "\n}";

        FileUtils.write("proccessDiagram", c, "txt");
        generateImg("proccessDiagram","png","dot");

    }

}

class GArea {

    String name;
    ArrayList<String> smallNodes;

    public GArea(String name) {
        this.name = name;
        smallNodes = new ArrayList();
    }

    public GArea(String name, ArrayList<String> nodes) {
        this.name = name;
        smallNodes = new ArrayList();
        smallNodes.addAll(nodes);
    }

    public void addSmallNode(String name) {
        smallNodes.add(name);
    }

    public void addSmallNodes(ArrayList<String> nodes) {
        smallNodes.addAll(nodes);
    }

    public void listNodes() {
        for (String node : smallNodes) {
            System.out.println("    node: " + node);
        }
    }
}

class smallNode {

    String name;
    HashSet<String> next;

    public smallNode() {
        next = new HashSet();
    }

    public smallNode(String name, ArrayList<String> list) {
        this.name = name;
        next = new HashSet();
        next.addAll(list);
    }

    public void listNextNodes() {
        for (String node : next) {
            System.out.println("    next node: " + node);
        }
    }
}


