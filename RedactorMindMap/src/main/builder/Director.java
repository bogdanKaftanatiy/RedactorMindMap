package main.builder;

import main.Diagram;

import java.util.ArrayList;

/**
 * Created by Bogdan Kaftanatiy on 27.04.2016.
 */
public class Director {
    private AbstractBuilder builder;

    public Director(){
        builder = new ConcreteBuilder();
    }

    public void setBuilder(AbstractBuilder b){
        builder = b;
    }

    public Diagram getDiagram(){
        return builder.getDiagram();
    }

    public void buildDiagram(Diagram d, ArrayList<String> arrayList){
        d.clearDiagram();

        builder.setDiagram(d);
        String title = arrayList.get(0).substring(7, arrayList.get(0).indexOf("</title>"));
        String center = arrayList.get(1).substring(8, arrayList.get(1).indexOf("</center>"));
        builder.buildHead(title, center);

        int endTree = 0;

        ArrayList<String> topArray = new ArrayList<>();
        for(int i = 3; i < arrayList.size(); i++){
            if(arrayList.get(i).equals("</top>")){
                endTree = i;
                break;
            }
            topArray.add(arrayList.get(i));
        }
        builder.buildTop(topArray);

        ArrayList<String> rightArray = new ArrayList<>();
        for(int i = endTree + 2; i < arrayList.size(); i++){
            if(arrayList.get(i).equals("</right>")){
                endTree = i;
                break;
            }
            rightArray.add(arrayList.get(i));
        }
        builder.buildRight(rightArray);

        ArrayList<String> bottomArray = new ArrayList<>();
        for(int i = endTree + 2; i < arrayList.size(); i++){
            if(arrayList.get(i).equals("</bottom>")){
                endTree = i;
                break;
            }
            bottomArray.add(arrayList.get(i));
        }
        builder.buildBottom(bottomArray);

        ArrayList<String> leftArray = new ArrayList<>();
        for(int i = endTree + 2; i < arrayList.size(); i++){
            if(arrayList.get(i).equals("</left>")){
                endTree = i;
                break;
            }
            leftArray.add(arrayList.get(i));
        }
        builder.buildLeft(leftArray);
    }
}
