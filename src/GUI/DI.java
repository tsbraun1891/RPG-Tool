package GUI;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import backEnd.*;

public class DI {
    public JPanel DicePanel;
    private JTextField numField;
    private JTextField sideField;
    private JTextField buffField;
    private JLabel d;
    private JLabel bufLab;
    private JLabel resLab;
    private JButton backButton;
    private JButton rollButton;
    private JScrollPane resultsScrollPane;
    private JTextArea resultTextArea;
    private JPanel equippedItemsPane;
    private JPanel equippedItemsButtonPane;
    private Dimension itemButtonsDim = new Dimension(175,48);
    private Dimension itemPaneDim = new Dimension(1,18);
    private boolean madlad = false;

    private RPGCharacter actor;

    public DI() {  // If roll is accessed from the front screen
        addRollListener();
    }

    public DI(RPGCharacter character){  // If roll is accessed from character sheet, equipped items will also appear.
        this.actor = character;
        addRollListener();
        equippedItemsPane.setLayout(new BoxLayout(equippedItemsPane, BoxLayout.PAGE_AXIS));
        equippedItemsButtonPane.setLayout(new BoxLayout(equippedItemsButtonPane, BoxLayout.PAGE_AXIS));
        String[] types = actor.inventory.getTypes();
        for(String type : types)
            if(type == "weapon")
                for(Item weapon : actor.inventory.inv.get("weapon"))
                    if(weapon.isEquipped())
                        addEquippedItemRolls(weapon);

        ArrayList<Spell> spells = actor.spellBook.getSpells();

        for(Spell spell : spells)
            if(spell.isPrepared())
                addPreparedSpellRolls(spell);

        resultTextArea.setBackground(DicePanel.getBackground().brighter());
    }

    private void addEquippedItemRolls(Item weapon){
        JLabel weaponName = new JLabel(weapon.getName());
        equippedItemsPane.add(Box.createRigidArea(itemPaneDim));
        equippedItemsPane.add(weaponName);

        JButton attackRollButton = new JButton("Attack Roll");
        JButton damageRollButton = new JButton("Damage Roll");

        JPanel itemButtons = new JPanel();
        itemButtons.setLayout(new FlowLayout());
        itemButtons.setPreferredSize(itemButtonsDim);

        itemButtons.add(attackRollButton);
        itemButtons.add(damageRollButton);

        equippedItemsButtonPane.add(itemButtons);

        damageRollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int buff;
                String bufff = buffField.getText();
                try {buff = Integer.parseInt(bufff);} catch (NumberFormatException ex) {buff = 0;}

                int roll[] = Dicey.Roll(weapon.getDamageDice());

                //int weaponBuff = roll[roll.length-1]-roll[roll.length-2];
                roll[roll.length-1] += buff;
                resultTextArea.append("\nResult: " + Dicey.rollToString(roll));
            }
        });

        attackRollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int buff;
                String bufff = buffField.getText();
                try {buff = Integer.parseInt(bufff);} catch (NumberFormatException ex) {buff = 0;}

                int roll[] = Dicey.Roll(1,20,weapon.getBonus()+buff);
                resultTextArea.append("\nResult: " + Dicey.rollToString(roll));
                if(madlad)
                    nat(20,roll);
            }
        });
    }

    private void addPreparedSpellRolls(Spell spell){
        JLabel spellName = new JLabel(spell.getName());
        equippedItemsPane.add(Box.createRigidArea(itemPaneDim));
        equippedItemsPane.add(spellName);

        JButton attackRollButton = new JButton("Attack Roll");
        JButton damageRollButton = new JButton("Damage/Heal Roll");

        JPanel itemButtons = new JPanel();
        itemButtons.setLayout(new FlowLayout());
        itemButtons.setPreferredSize(itemButtonsDim);

        itemButtons.add(attackRollButton);
        itemButtons.add(damageRollButton);

        equippedItemsButtonPane.add(itemButtons);

        damageRollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int buff;
                String bufff = buffField.getText();
                try {buff = Integer.parseInt(bufff);} catch (NumberFormatException ex) {buff = 0;}

                int roll[] = Dicey.Roll(spell.getDamageDice());

                roll[roll.length-1] += buff;
                resultTextArea.append("\nResult: " + Dicey.rollToString(roll));
            }
        });

        attackRollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int buff;
                String bufff = buffField.getText();
                try {buff = Integer.parseInt(bufff);} catch (NumberFormatException ex) {buff = 0;}

                int roll[] = Dicey.Roll(1,20,actor.spellBook.getAttackBonus()+buff);
                resultTextArea.append("\nResult: " + Dicey.rollToString(roll));
                if(madlad)
                    nat(20,roll);
            }
        });
    }

    private void addRollListener(){
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numf = numField.getText();
                String sidef = sideField.getText();
                String bufff = buffField.getText();
                if(!(numf.isEmpty()) && !(sidef.isEmpty())){  //If fields left empty, do nothing
                    int buff;
                    int num;
                    int sides;
                    try {buff = Integer.parseInt(bufff);} catch (NumberFormatException ex) {buff = 0;}
                    try {num = Integer.parseInt(numf);} catch (NumberFormatException ex) {num = 0;}
                    try {sides = Integer.parseInt(sidef);} catch (NumberFormatException ex) {sides = 0;}

                    int rolls[] = Dicey.Roll(num,sides,buff);
                    String rollString = Dicey.rollToString(rolls);
                    resultTextArea.append("\nResult: " + rollString);

                    if(madlad)
                        nat(sides,rolls);
                }
            }
        });
    }

    // Following code for hardcore gamers only
    private void nat(int sides, int[] rolls){
        if(sides == 20 && rolls[rolls.length-2] == 20){
            for(int i = 0; i < 50; i++)
                resultTextArea.append("OMG NAT 20 BOIO, YOU JUST CRITTED ALL OVER THE PLACE\n");
            freakout();
        }
        else if(sides == 20 && rolls[rolls.length-2]==1){
            for(int i = 0; i < 50; i++)
                resultTextArea.append("OMG NAT 1 BOIO, PHAT RIP, RIP THICCY 2K18\n");
            freakout();
        }
    }

    public void freakout(){

        File audioFile = new File("deja-vu.wav");
        System.out.println(audioFile.getPath());



        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.open(audioStream);

            audioClip.start();

        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }

        ArrayList<JPanel> freakPanels = new ArrayList<>();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel freakPanel;
        for(int i = 0; i < 100; i++){
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r,g,b);
            JFrame freakFrame = new JFrame("YOOOOOO");
            freakPanel = new JPanel();
            freakFrame.setMinimumSize(new Dimension(300,300));
            freakPanel.setMinimumSize(new Dimension(300,300));
            freakFrame.setLocation((int)(Math.random()*screenSize.getWidth()),(int)(Math.random()*screenSize.getHeight()));
            freakPanel.setOpaque(true);
            freakPanel.setBackground(randomColor);
            freakPanels.add(freakPanel);
            freakFrame.add(freakPanel);
            freakFrame.pack();
            freakFrame.setVisible(true);
            freakPanel.setVisible(true);
        }

    }


}
