package main.trees;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public enum TreeType {
    LEFT(1), TOP(2), RIGHT(3), BOTTOM(4);

    private int id;

    TreeType(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
