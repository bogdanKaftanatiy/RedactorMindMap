package main;

import main.primitives.*;
import main.trees.BeginTree;
import main.trees.TreeType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class Diagram extends JComponent implements Observable{
    private static Diagram instance;
    private JTextField title;
    private BeginTree right;
    private BeginTree left;
    private BeginTree top;
    private BeginTree bottom;

    private DiagramObserver parentPanel;
    private int centerX;
    private int centerY;
    private Dimension windowSize;

    private Diagram(Dimension size, DiagramObserver p){
        attach(p);
        this.windowSize = new Dimension(size);
        this.setLabelTitle("Test diagram");

        int x, y;
        centerX = (int)windowSize.getWidth()/2;
        centerY = (int)windowSize.getHeight()/2;

        x = centerX + title.getWidth()/2;
        y = centerY;
        right = new BeginTree(new MyPoint(x, y), TreeType.RIGHT, parentPanel);

        x = centerX - title.getWidth()/2;
        y = centerY;
        left = new BeginTree(new MyPoint(x, y), TreeType.LEFT, parentPanel);

        x = centerX;
        y = centerY - title.getHeight()/2;
        top = new BeginTree(new MyPoint(x, y), TreeType.TOP, parentPanel);

        x = centerX;
        y = centerY + title.getHeight()/2;
        bottom = new BeginTree(new MyPoint(x, y), TreeType.BOTTOM, parentPanel);
    }

    public void setLabelTitle(String name) {
        title = new JTextField(name);
        title.setOpaque(true);
        title.setBackground(Color.pink);
        title.setBorder(BorderFactory.createLineBorder(Color.black));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setEditable(false);
        int x, y, labelWidth, labelHeight;
        labelWidth = 100;
        labelHeight = 50;
        x = centerX - labelWidth/2;
        y = centerY - labelHeight/2;
        title.setBounds(x, y, labelWidth, labelHeight);
        title.setVisible(true);

        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton() == MouseEvent.BUTTON1 && !title.isEditable() && e.getClickCount() == 2){
                    parentPanel.saveState();
                    title.setBackground(Color.white);
                    title.setEditable(true);
                    title.getCaret().setVisible(true);
                } else if(e.getButton() == MouseEvent.BUTTON2){
                    parentPanel.saveState();
                    Color color = JColorChooser.showDialog(null, "Choose color", title.getBackground());
                    if(color != null)
                        title.setBackground(color);
                }
            }
        });
        title.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(title.isEditable()){
                    title.setBackground(Color.pink);
                    title.setEditable(false);
                    title.getCaret().setVisible(false);
                }
            }
        });

        notifyObserver(title);
    }

    public void setTitle(String name){
        title.setText(name);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int x, y, labelWidth, labelHeight;
        labelWidth = 125;
        labelHeight = 50;
        x = centerX - labelWidth/2;
        y = centerY - labelHeight/2;
        title.setBounds(x, y, labelWidth, labelHeight);
        if(parentPanel instanceof JLayeredPane)
            ((JLayeredPane)parentPanel).setPosition(title, -1);

        x = centerX + title.getWidth()/2;
        y = centerY;
        right.setBegin(new MyPoint(x, y));

        x = centerX - title.getWidth()/2;
        y = centerY;
        left.setBegin(new MyPoint(x, y));

        x = centerX;
        y = centerY - title.getHeight()/2;
        top.setBegin(new MyPoint(x, y));

        x = centerX;
        y = centerY + title.getHeight()/2;
        bottom.setBegin(new MyPoint(x, y));

        right.paintComponent(g);
        left.paintComponent(g);
        top.paintComponent(g);
        bottom.paintComponent(g);
    }

    public MyPoint getCenter(){
        return new MyPoint(centerX, centerY);
    }

    public void setCenter(int x, int y){
        centerX = x;
        centerY = y;

        x = centerX + title.getWidth()/2;
        y = centerY;
        right.setBegin(new MyPoint(x, y));

        x = centerX - title.getWidth()/2;
        y = centerY;
        left.setBegin(new MyPoint(x, y));

        x = centerX;
        y = centerY - title.getHeight()/2;
        top.setBegin(new MyPoint(x, y));

        x = centerX;
        y = centerY + title.getHeight()/2;
        bottom.setBegin(new MyPoint(x, y));
        notifyObserver();
    }

    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }

    public ArrayList<String> getStrings(){
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("<title>" + title.getText() + "</title>");
        arrayList.add("<center>" + centerX + "," + centerY + "</center>");
        arrayList.add("<top>");
        arrayList.add("<next>" + top.getNext().size() + "</next>");
        for(int i = 0; i<top.getNext().size(); i++){
            ArrayList<String> array = top.getNext().get(i).getString();
            for(int j = 0; j < array.size(); j++){
                arrayList.add("<" + array.get(j));
            }
        }
        arrayList.add("</top>");
        arrayList.add("<right>");
        arrayList.add("<next>" + right.getNext().size() + "</next>");
        for(int i = 0; i<right.getNext().size(); i++){
            ArrayList<String> array = right.getNext().get(i).getString();
            for(int j = 0; j < array.size(); j++){
                arrayList.add("<" + array.get(j));
            }
        }
        arrayList.add("</right>");
        arrayList.add("<bottom>");
        arrayList.add("<next>" + bottom.getNext().size() + "</next>");
        for(int i = 0; i<bottom.getNext().size(); i++){
            ArrayList<String> array = bottom.getNext().get(i).getString();
            for(int j = 0; j < array.size(); j++){
                arrayList.add("<" + array.get(j));
            }
        }
        arrayList.add("</bottom>");
        arrayList.add("<left>");
        arrayList.add("<next>" + left.getNext().size() + "</next>");
        for(int i = 0; i<left.getNext().size(); i++){
            ArrayList<String> array = left.getNext().get(i).getString();
            for(int j = 0; j < array.size(); j++){
                arrayList.add("<" + array.get(j));
            }
        }
        arrayList.add("</left>");

        return arrayList;
    }

    public DiagramMemento createMomento(){
        return new DiagramMemento(getStrings());
    }

    public void removeTextField(){
        if(title.isEditable()) {
            title.setBackground(Color.pink);
            title.setEditable(false);
            title.getCaret().setVisible(false);
        }

        top.removeTextField();
        right.removeTextField();
        bottom.removeTextField();
        left.removeTextField();

        notifyObserver();
    }

    public void attach(DiagramObserver obs){
        parentPanel = obs;
    }

    public void notifyObserver(){
        parentPanel.update();
    }

    public void notifyObserver(JComponent c){
        parentPanel.update(c);
    }

    public static Diagram getInstance(Dimension size, DiagramObserver p){
        if(instance == null){
            instance = new Diagram(size, p);
        }

        return instance;
    }

    public BeginTree getBottom() {
        return bottom;
    }

    public BeginTree getLeft() {
        return left;
    }

    public BeginTree getRight() {
        return right;
    }

    public BeginTree getTop() {
        return top;
    }

    public void clearDiagram(){
        for(int i = top.getNext().size() - 1; i >= 0; i--){
            top.deleteLine(top.getNext().get(i));
        }
        for(int i = right.getNext().size() - 1; i >= 0; i--){
            right.deleteLine(right.getNext().get(i));
        }
        for(int i = bottom.getNext().size() - 1; i >= 0; i--){
            bottom.deleteLine(bottom.getNext().get(i));
        }
        for(int i = left.getNext().size() - 1; i >= 0; i--){
            left.deleteLine(left.getNext().get(i));
        }
    }

    public void addIndexLine(TreeType t, ArrayList<Integer> indexs, String m, MyPoint e, Color c){
        BeginTree current = null;
        switch (t){
            case LEFT:
                current = left;
                break;
            case TOP:
                current = top;
                break;
            case RIGHT:
                current = right;
                break;
            case BOTTOM:
                current = bottom;
                break;
        }
        for(int i = 0; i < indexs.size() - 1; i++){
            current = current.getNext().get(indexs.get(i));
        }
        current.addLine(m, e, c);
    }
}
