package GUI;

import backEnd.*;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainFrame {
    private static JFrame frame = new JFrame("RPG Tool");
    private JPanel mainMenu;
    private JButton newCharacterButton;
    private JButton loadCharacterButton;
    private JButton diceyButton;
    private JPanel welcomePanel;
    private JLabel welcomeLabel;
    private JPanel buttons;
    private JButton deleteButton;
    private JButton settingsButton;
    private int tester = 0;

    /* Active inventory */
    static Inventory inventory;
    /* Active notes */
    static String background, notes, featsntraits;
    /* Active spellbook */
    static Spellbook spellBook;

    static String sep = "@";  // Separator used in JSON character files to separate different pieces of data.
    static Dimension mainDim = new Dimension(725, 800);

    /* List of the component types so that fonts and backgrounds can be easily changed */
    static String[] componentFonts = {"Button.font","Label.font","Panel.font","CheckBox.font","CheckBoxMenuItem.font","ScrollPane.font","TabbedPane.font","TextField.font","TextArea.font"};
    static String[] componentBackgrounds = {"Panel.background","Frame.background","ScrollPane.background","TabbedPane.background","TextArea.background","Label.background","CheckBox.background","CheckBoxMenuItem.font"};

    static boolean debug = false;

    public MainFrame() {
        /* When load character is pressed, creates an object to interact with computers file system, and user
           Will select character folder (characters are currently stored in folders, not just one file)
         */
        loadCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("Characters");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.showOpenDialog(mainMenu);
                if (fc.getName(fc.getSelectedFile()) != null) {
                    JSONObject charJSON = RPGCharacter.loadCharJSON(fc.getName(fc.getSelectedFile()));
                    background = charJSON.getString("background");
                    notes = charJSON.getString("notes");
                    featsntraits = charJSON.getString("featuresntraits");
                    CharForm charFrame = new CharForm(charJSON,frame);
                    JPanel pan = charFrame.charPan;
                    frame.setContentPane(pan);
                    frame.validate();
                    frame.repaint();
                    //charFrame.updateFormData(charJSON);

                    inventory = new Inventory(charJSON);
                    spellBook = new Spellbook(charJSON);
                    if (debug){
                        System.out.println(inventory);
                    }
                }
            }
        });

        // Creates new frame for dice rolling
        diceyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int diceyWidth = 500;
                JFrame diceFrame = new JFrame("Dicey");
                diceFrame.setContentPane(new DI().DicePanel);
                diceFrame.setPreferredSize(new Dimension(diceyWidth, 300));
                diceFrame.pack();
                diceFrame.setLocation(frame.getLocation().x-diceyWidth,frame.getLocation().y);
                diceFrame.setVisible(true);

            }
        });
        newCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                background = "";
                notes = "";
                featsntraits = "";
                frame.setContentPane(new CharForm(frame).charPan);
                frame.validate();
                frame.repaint();

                inventory = new Inventory();
                spellBook = new Spellbook();

                if (debug) {
                    System.out.println(inventory);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("Characters");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.showOpenDialog(mainMenu);
                String fldrName = fc.getName(fc.getSelectedFile());
                File dir = new File("Characters" + System.getProperty("file.separator") + fldrName);

                if (dir.isDirectory() != false) {
                    File[] listFiles = dir.listFiles();
                    for (File file : listFiles) {
                        file.delete();
                    }
                    dir.delete();
                }
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tester == 0){
                    for(String s : componentBackgrounds)
                        UIManager.put(s, Color.red);
                    tester = 1;
                    //System.out.println(UIManager.getColor("Panel.background"));
                    mainMenu.revalidate();
                    mainMenu.repaint();
                }
                else{
                    for(String s : componentBackgrounds)
                        UIManager.put(s, Color.lightGray);
                    tester = 0;
                    //System.out.println(UIManager.getColor("Panel.background"));
                    mainMenu.revalidate();
                    mainMenu.repaint();
                }

            }
        });

    }

    public static void main(String[] args) {
        for(String s : componentFonts)
            UIManager.put(s, new Font("STLiti", Font.BOLD, 16));
        for(String s : componentBackgrounds)
            UIManager.put(s, new Color(174,162,135));
        UIManager.put("Button.background",Color.lightGray);

        MainFrame mainFrame = new MainFrame();
        JPanel mainMenu = mainFrame.getMainMenu();
        frame.setContentPane(mainMenu);
        frame.setPreferredSize(mainDim);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int)((screenSize.getWidth()-frame.getWidth())/2);
        int centerXadjust = 50; // Inv and Spell frames are slightly larger, so we should shift the main screen left a bit
        int centerY = 25;
        frame.setLocation(centerX-centerXadjust,centerY);
        frame.setVisible(true);

        mainFrame.setNotOpaque();
        // So if we set all upper panels to opaque(false), background color on main panel will come through

        /* add debugging statements to here */
        if (debug) {
        }

        /* Uncomment if you think you're cool enough */
        //mainFrame.letsRide();
    }

    public static void init() {
        MainFrame mainFrame = new MainFrame();
        JPanel mainMenu = mainFrame.getMainMenu();
        frame.setContentPane(mainMenu);
        frame.validate();
        frame.repaint();
    }

    public String str2path(String path) {
        String rhet = "";
        String sep = System.getProperty("file.separator");
        for (int i = 0; i < path.length(); i++)
            if (path.charAt(i) == '/')
                rhet = rhet + sep;
            else
                rhet = rhet + path.charAt(i);

        return rhet;
    }

    private void setNotOpaque(){
        welcomePanel.setOpaque(false);
        buttons.setOpaque(false);
    }

    public void letsRide() {
        /* tfw you use an infinitely incrementing for loop as opposed to a while(true) */
        for (int i = 1; i > 0; i++) {
            welcomePanel.setBackground(Color.BLACK);
            welcomePanel.setBackground(Color.BLUE);
            welcomePanel.setBackground(Color.red);
            welcomePanel.setBackground(Color.green);
            welcomePanel.setBackground(Color.gray);
            welcomePanel.setBackground(Color.yellow);
            welcomePanel.setBackground(Color.cyan);
            welcomePanel.setBackground(Color.orange);
            welcomePanel.setBackground(Color.pink);
            buttons.setBackground(Color.BLACK);
            buttons.setBackground(Color.BLUE);
            buttons.setBackground(Color.red);
            buttons.setBackground(Color.green);
            buttons.setBackground(Color.gray);
            buttons.setBackground(Color.yellow);
            buttons.setBackground(Color.cyan);
            buttons.setBackground(Color.orange);
            buttons.setBackground(Color.pink);
        }
    }

    public JPanel getMainMenu() {
        return mainMenu;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
