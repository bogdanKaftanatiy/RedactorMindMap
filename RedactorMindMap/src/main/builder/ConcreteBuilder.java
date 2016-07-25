package main.builder;

import main.primitives.MyPoint;
import main.trees.BeginTree;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Bogdan Kaftanatiy on 27.04.2016.
 */
public class ConcreteBuilder extends AbstractBuilder {
    @Override
    public void buildHead(String title, String center) {
        diagram.setTitle(title);
        String[] xy = center.split(",");
        diagram.setCenter(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    }

    @Override
    public void buildTop(ArrayList<String> strings) {
        int count = Integer.parseInt(strings.get(0).substring(6, strings.get(0).indexOf("</next>")));
        for(int i = 1; i < strings.size(); i++){
            initTree(diagram.getTop(), strings.get(i));
        }
    }

    @Override
    public void buildRight(ArrayList<String> strings) {
        for(int i = 1; i < strings.size(); i++){
            initTree(diagram.getRight(), strings.get(i));
        }
    }

    @Override
    public void buildBottom(ArrayList<String> strings) {
        for(int i = 1; i < strings.size(); i++){
            initTree(diagram.getBottom(), strings.get(i));
        }
    }

    @Override
    public void buildLeft(ArrayList<String> strings) {
        for(int i = 1; i < strings.size(); i++){
            initTree(diagram.getLeft(), strings.get(i));
        }
    }

    private void initTree(BeginTree bt, String tree){
        String[] ind = tree.substring(1, tree.indexOf(">")).split(",");
        ArrayList<Integer> indexs = new ArrayList<>();
        for(int i = 0; i < ind.length; i++){
            indexs.add(Integer.parseInt(ind[i]));
        }

        String message = tree.substring(tree.indexOf("<message>")+9, tree.indexOf("</message>"));

        String[] rgb = tree.substring(tree.indexOf("<color>")+7, tree.indexOf("</color>")).split(",");
        Color color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

        String[] xy = tree.substring(tree.indexOf("<end>")+5, tree.indexOf("</end>")).split(",");
        MyPoint end = new MyPoint(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));

        int next = Integer.parseInt(tree.substring(tree.indexOf("<next>")+6, tree.indexOf("</next>")));

        diagram.addIndexLine(bt.getType(), indexs, message, end, color);
    }
}
