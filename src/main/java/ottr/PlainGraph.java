package ottr;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.File;
import java.util.Iterator;

public class PlainGraph {

    String fileContent;

    public PlainGraph(String fileContent) {
        this.fileContent = fileContent;
    }

    public void visualize(){
        Graph graph = new SingleGraph("FreePlane");
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.display();


        graph.addEdge("1", "Pizza", "Burger");
        graph.addEdge("2", "Burger", "Kabab");
        graph.addEdge("3", "Kabab", "Pizza");
        graph.addEdge("4", "Coffee", "Kabab");

        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }

        explore(graph.getNode("Kabab"));


    }

    public void explore(Node source) {
        Iterator<? extends Node> k = source.getBreadthFirstIterator();

        /* Breadth first showup? */

        while (k.hasNext()) {
            Node next = k.next();
            next.setAttribute("ui.class", "marked");
            sleep();
        }
    }

    protected void sleep() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }

    protected String styleSheet =
            "node {"
                    + "shape: rounded-box;"
                    + "stroke-mode: plain;"
                    + "stroke-color: #000000;"
                    + "shadow-mode: plain;"
                    + "shadow-color: #C8C8C8;"
                    + "shadow-width: 4;"
                    + "shadow-offset: 4, -4;"
                    + "size: 120px;"
                    + "fill-color: rgb(240, 194, 70);"
                    + "text-alignment: above;"
                    + "padding: 0, 0px;"

                    + "text-background-mode: rounded-box;"
                    + "text-padding: 5;"
                    + "text-background-color: black;"
                    + "text-color: white;"
                    + "text-size: 25;"
                    + "size-mode: fit;"
                    + "}"
                    +"node.marked {"
                    +"   fill-color: rgb(140, 190, 245);"
                    +"}";

    public void parseFile() {


    }
}

