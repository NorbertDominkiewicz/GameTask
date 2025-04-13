package app.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class TitlePanel extends JPanel {
    public TitlePanel() {
        setOpaque(true);
        setBackground(Color.BLACK);
        ImageIcon logo = new ImageIcon("resources/gamePanel/logo.png");
        JLabel settingsLabel = new JLabel(logo);
        add(settingsLabel);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}

class ChampionPreview extends JPanel {
    int currentChampion = 1;
    BufferedImage image;
    ArrayList<String> champions;
    String source;
    public ChampionPreview() {
        champions = new ArrayList<>();
        champions.add("mozart");
        champions.add("debussy");
        champions.add("chopin");
        source = "/units/" + champions.get(currentChampion) + ".png";
        setPreferredSize(new Dimension(100, 200));
        setOpaque(false);
        try{
            image = ImageIO.read(getClass().getResource(source));
        } catch (Exception e){
            e.printStackTrace();
        }
        setBackground(Color.DARK_GRAY);
    }
    public void changeChampion(int index) {
        int newIndex = currentChampion + index;
        System.out.println(newIndex);
        if (newIndex >= 0 && newIndex < champions.size()) {
            currentChampion = newIndex;
            source = "/units/" + champions.get(currentChampion) + ".png";
            System.out.println(source);
            try {
                image = ImageIO.read(getClass().getResource(source));
            } catch (Exception e) {
                e.printStackTrace();
            }
            repaint();
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0,100,200, null);
    }
}

class ChampionPickerPanel extends JPanel {
    ChampionPreview preview = new ChampionPreview();
    public ChampionPickerPanel() {
        setBackground(Color.DARK_GRAY);
        setOpaque(true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        ImageIcon leftButtonIMG = new ImageIcon("resources/gamePanel/arrowLeft.png");
        ImageIcon rightButtonIMG = new ImageIcon("resources/gamePanel/arrowRight.png");
        JButton leftButton = new JButton(leftButtonIMG);
        leftButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        leftButton.setBackground(Color.DARK_GRAY);
        leftButton.setFocusPainted(false);
        leftButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, true));
        leftButton.addActionListener(e ->{
            int shL = -1;
            preview.changeChampion(shL);
        });
        JButton rightButton = new JButton(rightButtonIMG);
        rightButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightButton.setBackground(Color.DARK_GRAY);
        rightButton.setFocusPainted(false);
        rightButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, true));
        rightButton.addActionListener(e ->{
            int shR = 1;
            preview.changeChampion(shR);
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 0, 20);
        add(leftButton, gbc);
        gbc.gridx = 1;
        add(preview, gbc);
        gbc.gridx = 2;
        add(rightButton, gbc);
    }
}

class OptionBox extends JComboBox<String> {
    public OptionBox(String[] options) {
        super(options);
        setOpaque(false);
        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(100, 50));
        setFont(new Font("Courier", Font.BOLD, 20));
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        setSelectedIndex(0);
        addActionListener(e ->{
            String option = (String) getSelectedItem();
            System.out.println(option);
        });
    }
    public String getOption() {
        return (String) getSelectedItem();
    }
}

class StartButton extends JButton {
    public StartButton(SettingsPanel settingsPanel) {
        setBackground(Color.RED);
        setForeground(Color.WHITE);
        setText("Start");
        setPreferredSize(new Dimension(100, 50));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, true));
        setFocusPainted(false);
        addActionListener(e ->{
            settingsPanel.close();
            new Game(settingsPanel.championPickerPanel.preview.champions.get(settingsPanel.championPickerPanel.preview.currentChampion),Integer.parseInt(settingsPanel.mapSelectorPanel.optionBox.getOption()));
        });
    }
}

class MapSelectorPanel extends JPanel {
    String [] options = {"50", "100", "200", "250", "500", "1000"};
    OptionBox optionBox = new OptionBox(options);
    StartButton startButton;
    public MapSelectorPanel(SettingsPanel settingsPanel) {
        startButton = new StartButton(settingsPanel);
        setOpaque(true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        setBackground(Color.DARK_GRAY);
        add(optionBox,gbc);
        gbc.gridy = 1;
        add(startButton,gbc);
    }
}

class SettingsPanel extends JPanel {
    ChampionPickerPanel championPickerPanel = new ChampionPickerPanel();
    MapSelectorPanel mapSelectorPanel;
    JFrame frameToClose;
    public SettingsPanel(JFrame frame) {
        frameToClose = frame;
        mapSelectorPanel = new MapSelectorPanel(this);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.BLACK);
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        add(championPickerPanel);
        add(mapSelectorPanel);
    }
    public void close(){
        frameToClose.dispose();
    }
}

public class Launcher extends JFrame {
    TitlePanel tp;
    SettingsPanel sp;
    public Launcher() {
        tp = new TitlePanel();
        sp = new SettingsPanel(this);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setTitle("Launcher");
        getContentPane().setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        add(tp, c);
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(sp, c);
        setVisible(true);
    }
}