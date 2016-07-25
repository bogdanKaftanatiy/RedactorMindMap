package main.builder;

import main.Diagram;

import java.util.ArrayList;

/**
 * Created by Bogdan Kaftanatiy on 27.04.2016.
 */
public abstract class AbstractBuilder {
    protected Diagram diagram;

    public Diagram getDiagram(){
        return diagram;
    }

    public void setDiagram(Diagram d){
        diagram = d;
    }

    public abstract void buildHead(String title, String center);
    public abstract void buildTop(ArrayList<String> strings);
    public abstract void buildRight(ArrayList<String> strings);
    public abstract void buildBottom(ArrayList<String> strings);
    public abstract void buildLeft(ArrayList<String> strings);
}
