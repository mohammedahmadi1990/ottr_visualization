package ottr;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import java.util.ArrayList;
import java.util.Iterator;

public class PlainGraph {

    String fileContent;
    String prefixes;
    ArrayList<HeadModule> headModules;
    ArrayList<BodyModule> bodyModules;
    OttrTemplate template;

    public PlainGraph(String fileContent) {
        this.fileContent = fileContent;
    }

    public void visualize(){
        Graph graph = new SingleGraph("FreePlane");
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.stylesheet", graphCSS);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.display();


        // Add Template
        Node templateNode = graph.addNode(template.templateNme);
        templateNode.setAttribute("ui.style", templateCSS);
        int i = 0;
        Node[] subNodes = new Node[template.headModule.size()];
        while (i<template.headModule.size()){
            subNodes[i] = graph.addNode(template.headModule.get(i).key + " " + template.headModule.get(i).value);
            subNodes[i].setAttribute("ui.style", subCSS);
            if(template.headModule.get(i).conditions[0]) {
                subNodes[i].setAttribute("ui.style", subCSS_blank);
            }
            if(template.headModule.get(i).conditions[1]){
                subNodes[i].setAttribute("ui.style", subCSS_list);
            }
            if(template.headModule.get(i).conditions[2]){
                subNodes[i].setAttribute("ui.style", subCSS);
            }
            graph.addEdge(template.bodyModule.get(i).relation,templateNode,subNodes[i],true);
            i++;
        }

        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }

        for (Edge edge : graph.getEdgeSet()) {
            edge.addAttribute("ui.label", edge.getId());
            edge.addAttribute("ui.style", "text-size: 12;");
        }

    }

    protected void sleep() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }

    public void parseFile() {
        String head = "";
        String body = "";
        String templateNme = "";
        prefixes = "";
        headModules = new ArrayList<>();
        bodyModules = new ArrayList<>();
        head = fileContent.split("::")[0];
        body = fileContent.split("::")[1];
        body = body.substring(body.indexOf("{")+1,body.indexOf("}")).trim();

        if(head.contains("@@")){
            head = head.split("@@")[0];
            prefixes = head.split("@@")[1];
        }

        String header = "";
        if(head.contains("(")){
            templateNme = head.substring(0,head.indexOf("("));
            header = head.substring(head.indexOf("(")+1,head.indexOf(")"));
        }else{
            templateNme = head.substring(0,head.indexOf("["));
            header = head.substring(head.indexOf("[")+1,head.indexOf("]"));
        }
        if(templateNme.contains(":")){
            templateNme = templateNme.split(":")[1];
        }

        int count = header.split(",").length;
        int i = 0;
        String tempBody = body;
        while (i<count) {
            boolean[] conditions = new boolean[3];
            String key = "";
            String value = "";
            int len = header.split(",")[i].trim().split(" ").length;
            if(len<3){
                key = header.split(",")[i].trim().split(" ")[0];
                value = header.split(",")[i].trim().split(" ")[1];
            }else {
                String sign = header.split(",")[i].trim().split(" ")[0];
                key = header.split(",")[i].trim().split(" ")[1];
                value = header.split(",")[i].trim().split(" ")[2];

                if(sign.equals("?"))
                    conditions[0] = true;  //optional
                if(sign.equals("+"))
                    conditions[1] = true;  //blank allowed
                if(sign.equals("!"))
                    conditions[2] = true;  //default value
            }
            headModules.add(new HeadModule(key,value,conditions));
            i++;
        }

        boolean flag = false;
        if(body.contains(")")) {
            flag = true;
        }
        while (flag){
            if(tempBody.indexOf(")")==tempBody.length()-1){
                flag = false;
            }
            String properties = tempBody.substring(0,tempBody.indexOf(")")+1).trim();
            tempBody = tempBody.substring(tempBody.indexOf("),")+1).trim();
            String relation = properties.substring(0,properties.indexOf("("));
            String[] parameters = properties.substring(properties.indexOf("(")+1,properties.indexOf(")")).split(",");
            bodyModules.add(new BodyModule(relation,parameters));
        }
        template = new OttrTemplate(headModules,bodyModules,templateNme,prefixes);
    }

    class OttrTemplate
    {
        ArrayList<HeadModule> headModule;
        ArrayList<BodyModule> bodyModule;
        String templateNme;
        String prefix;

        public OttrTemplate(ArrayList<HeadModule> headModule, ArrayList<BodyModule> bodyModule, String templateNme, String prefix) {
            this.headModule = headModule;
            this.bodyModule = bodyModule;
            this.templateNme = templateNme;
            this.prefix = prefix;
        }
    }


    class BodyModule{
        String relation;
        String[] parameters;

        public BodyModule(String relation, String[] parameters) {
            this.relation = relation;
            this.parameters = parameters;
        }
    }

    // inner class
    class HeadModule{
        // fields
        String key;
        String value;
        String defaultValue;
        boolean[] conditions;

        // Constructor 1
        public HeadModule(String key, String value, boolean[] conditions) {
            this.key = key;
            this.value = value;
            this.conditions = conditions;
        }

        // Constructor 2 with default value
        public HeadModule(String key, String value, boolean[] conditions, String defaultValue) {
            this.key = key;
            this.value = value;
            this.conditions = conditions;
            this.defaultValue = defaultValue;
        }

    }

    protected String graphCSS =
            "node {"
                    + "shape: rounded-box;"
                    + "stroke-mode: plain;"
                    + "stroke-color: #000000;"
                    + "shadow-mode: plain;"
                    + "shadow-color: #C8C8C8;"
                    + "shadow-width: 4;"
                    + "shadow-offset: 4, -4;"
                    + "size: 150px;"
                    + "text-alignment: above;"
                    + "padding: 0, 0px;"

                    + "text-background-mode: rounded-box;"
                    + "text-background-color: black;"
                    + "text-color: white;"
                    + "text-size: 25;"
                    + "size-mode: fit;"
                    + "}";

    protected String templateCSS =
//                    + "stroke-color: #000000;"
                     "size: 150px;" + "fill-color: rgb(252, 186, 3);";
//                    + "text-alignment: above;"
//                    + "padding: 0, 0px;"

//                    + "text-background-mode: rounded-box;"
//                    + "text-padding: 20;"
//                    + "text-background-color: black;"
//                    + "text-color: white;"
//                    + "text-size: 25;"
//                    + "size-mode: fit;";

    protected String subCSS =
                    "stroke-color: #000000;"
                    + "shape: circle;"
                    + "size: 150px;"
                    + "fill-color: rgb(140, 190, 245);"
                    + "text-alignment: center;"
                    + "padding: 0, 0px;"
                    + "text-background-mode: none;"
                    + "text-padding: 10;"
                    + "text-background-color: white;"
                    + "text-color: black;"
                    + "text-size: 14;"
                    + "size-mode: fit;";

    protected String subCSS_blank =
                    "stroke-color: #fff;"
                    + "shape: circle;"
                    + "size: 150px;"
                    + "fill-color: rgb(240, 90, 145);"
                    + "text-alignment: center;"
                    + "padding: 0, 0px;"
                    + "text-background-mode: none;"
                    + "text-padding: 10;"
                    + "text-background-color: white;"
                    + "text-color: black;"
                    + "text-size: 14;"
                    + "size-mode: fit;";

    protected String subCSS_list =
                    "stroke-color: #000000;"
                    + "shape: circle;"
                    + "size: 150px;"
                    + "fill-color: rgb(180, 20, 245);"
                    + "text-alignment: center;"
                    + "padding: 0, 0px;"
                    + "text-background-mode: none;"
                    + "text-padding: 10;"
                    + "text-background-color: white;"
                    + "text-color: black;"
                    + "text-size: 14;"
                    + "size-mode: fit;";


}

