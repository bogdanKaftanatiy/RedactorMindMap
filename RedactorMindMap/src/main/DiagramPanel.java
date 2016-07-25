package main;

import main.builder.Director;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class DiagramPanel extends JLayeredPane implements DiagramObserver {
    private Diagram diagram;
    private Stack<DiagramMemento> undo;
    private Stack<DiagramMemento> redo;
    private JLabel showHistory;
    private ArrayList<DiagramMemento> history;
    private JScrollPane pane;
    private JPanel historyPane;
    private int currentCheckpoint;
    private int changeCount;
    private int beginX;
    private int beginY;
    private int startX;
    private int startY;

    public DiagramPanel(Dimension size) {
        beginX = (int)size.getWidth()/2;
        beginY = (int)size.getHeight()/2;
        currentCheckpoint = 0;
        changeCount = 5;
        this.setSize((int)size.getWidth(), (int)size.getHeight());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setLayout(null);
        diagram = Diagram.getInstance(size, this);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseClicked(MouseEvent e){
                super.mouseClicked(e);
                diagram.removeTextField();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int dX, dY;
                dX = e.getX() - startX;
                dY = e.getY() - startY;
                beginX += dX;
                beginY += dY;
                setDiagramCenter(beginX, beginY);
                startX = e.getX();
                startY = e.getY();
                update();
            }
        });

        Action doUndo = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        };
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"),
                "doUndo");
        getActionMap().put("doUndo",
                doUndo);
        Action doRedo = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        };
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"),
                "doRedo");
        getActionMap().put("doRedo",
                doRedo);


        JLabel center = new JLabel(new ImageIcon("resources\\icon\\alignment.png"));
        center.setBounds(10, 10, 25, 25);
        center.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                beginX = getWidth()/2;
                beginY = getHeight()/2;
                setDiagramCenter(beginX, beginY);
                update();
            }
        });
        center.setVisible(true);
        add(center);

        undo = new Stack<>();
        redo = new Stack<>();
        history = new ArrayList<>();
        showHistory = new JLabel("Show history");
        showHistory.setBounds((int)(size.getWidth() - 85), 10, 75, 25);
        showHistory.setVisible(true);
        add(showHistory);
        historyPane = new JPanel();
        historyPane.setLayout(new BoxLayout(historyPane, BoxLayout.Y_AXIS));
        pane = new JScrollPane(historyPane);
        pane.setBounds((int)(size.getWidth() - 150), 1, 150, (int)size.getHeight());
        historyPane.setVisible(true);
        pane.setVisible(false);
        add(pane);
        showHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!pane.isVisible()) {
                    showHistory.setText("Hide history");
                    showHistory.setBounds((getWidth() - 100 - historyPane.getWidth()), 10, 75, 25);
                    pane.setVisible(true);
                } else {
                    showHistory.setText("Show history");
                    showHistory.setBounds((getWidth() - 85), 10, 75, 25);
                    pane.setVisible(false);
                }
            }
        });
    }

    public void addState(){
        System.out.println("add state");
        undo.push(diagram.createMomento());
        if(!redo.isEmpty())
            redo.clear();
    }

    public void update(){
        revalidate();
        repaint();
    }

    @Override
    public void update(JComponent c) {
        if(this.getIndexOf(c) == -1) {
            this.add(c);
            setComponentZOrder(c, this.getIndexOf(c));
        } else
            this.remove(c);
        update();
    }

    @Override
    public void saveState() {
        changeCount++;
        addState();
        if(changeCount >= 5) {
            if(currentCheckpoint < history.size() - 1){
                for(int i = history.size() - 1; i >=currentCheckpoint; i--){
                    history.remove(i);
                    historyPane.remove(i);
                }
            }
            history.add(diagram.createMomento());
            JLabel lab = new JLabel("<html><br>Checkpoint №" + (history.size()) + "<br>");
            lab.setName("" + (history.size() - 1));
            lab.setVisible(true);
            historyPane.add(lab);
            lab.setHorizontalAlignment(JLabel.CENTER);
            JLayeredPane t = this;
            lab.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ArrayList<String> array = history.get(Integer.parseInt(lab.getName())).getMemento();
                    currentCheckpoint = Integer.parseInt(lab.getName());
                    changeCount = 5;

                    Director dir = new Director();
                    try {
                        dir.buildDiagram(diagram, array);
                    } catch (Exception ex){
                        JOptionPane.showMessageDialog(t, "Input file incorrect");
                        diagram.clearDiagram();
                    }
                    beginX = diagram.getCenter().getX();
                    beginY = diagram.getCenter().getY();

                    update();
                }
            });
            changeCount = 0;
            currentCheckpoint++;
        }
    }

    public void undo(){
        if(!undo.empty()) {
            System.out.println("Undo");

            redo.push(diagram.createMomento());
            ArrayList<String> array = undo.pop().getMemento();

            Director dir = new Director();
            dir.buildDiagram(diagram, array);
            beginX = diagram.getCenter().getX();
            beginY = diagram.getCenter().getY();

            update();
        }
    }

    public void redo(){
        if(!redo.empty()){
            System.out.println("Redo");

            undo.push(diagram.createMomento());
            ArrayList<String> array = redo.pop().getMemento();

            Director dir = new Director();
            dir.buildDiagram(diagram, array);
            beginX = diagram.getCenter().getX();
            beginY = diagram.getCenter().getY();

            update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        diagram.setWindowSize(new Dimension(this.getWidth(), this.getHeight()));
        diagram.paintComponent(g);
    }

    public void setDiagramCenter(int x, int y){
        diagram.setCenter(x, y);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        this.setSize((int)preferredSize.getWidth(), (int)preferredSize.getHeight());
        pane.setBounds((int)(preferredSize.getWidth() - 150), 1, 150, (int)preferredSize.getHeight());
        if(pane.isVisible()) {
            showHistory.setBounds((int) (preferredSize.getWidth() - 100 - historyPane.getWidth()), 10, 75, 25);
        } else {
            showHistory.setBounds((int)(preferredSize.getWidth() - 85), 10, 75, 25);
        }
    }

    public void writeFile(String filename){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false)))
        {
            String endLine = "\n";
            ArrayList<String> arr = diagram.getStrings();
            for(String str : arr)
                bw.write(str + endLine);

            for(DiagramMemento memento : history){
                arr.clear();
                arr = memento.getMemento();
                for(String str : arr)
                    bw.write(str + endLine);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void readFile(String filename){
        undo.clear();
        redo.clear();
        history.clear();
        historyPane.removeAll();

        ArrayList<String> array = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String s;
            while((s=br.readLine())!=null){
                array.add(s);
                if(s.equals("</left>")){
                    Director dir = new Director();
                    try {
                        dir.buildDiagram(diagram, array);
                    } catch (Exception ex){
                        JOptionPane.showMessageDialog(this, "Input file incorrect", "Read Error", JOptionPane.ERROR_MESSAGE);
                        diagram.clearDiagram();
                        return;
                    }
                    array.clear();
                    break;
                }
            }

            while((s=br.readLine())!=null){
                array.add(s);
                if(s.equals("</left>")){
                    history.add(new DiagramMemento(array));
                    array.clear();
                }
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        for(int i = 0; i<history.size(); i++) {
            JLabel lab = new JLabel("<html><br>Checkpoint №" + (i+1) + "<br>");
            lab.setName("" + i);
            lab.setVisible(true);
            historyPane.add(lab);
            lab.setHorizontalAlignment(JLabel.CENTER);
            JLayeredPane temp = this;
            lab.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ArrayList<String> array = history.get(Integer.parseInt(lab.getName())).getMemento();
                    currentCheckpoint = Integer.parseInt(lab.getName());
                    changeCount = 5;

                    Director dir = new Director();
                    try {
                        dir.buildDiagram(diagram, array);
                    } catch (Exception ex){
                        JOptionPane.showMessageDialog(temp, "Input file incorrect");
                        diagram.clearDiagram();
                    }
                    beginY = diagram.getCenter().getY();
                    beginX = diagram.getCenter().getX();
                    update();
                }
            });
        }
        currentCheckpoint = history.size() - 1;
        changeCount = 5;
    }
}
