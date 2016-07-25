package main;

import java.util.ArrayList;

/**
 * Created by Bogdan Kaftanatiy on 26.04.2016.
 */
public class DiagramMemento {
    private ArrayList<String> array;

    public DiagramMemento(ArrayList<String> a){
        array = new ArrayList<>();
        array.addAll(a);
    }

    public ArrayList<String> getMemento(){
        return array;
    }
}
