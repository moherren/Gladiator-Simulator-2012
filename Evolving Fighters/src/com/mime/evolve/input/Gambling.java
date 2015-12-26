package com.mime.evolve.input;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.mime.evolve.Controller;
import com.mime.evolve.Display;
import com.mime.evolve.Game;

public class Gambling extends JPanel implements ActionListener{
	JComboBox<String> dropDown;
	JButton button=new JButton(),transferButton1=new JButton("<"),transferButton2=new JButton(">");
	JLabel descriptor1=new JLabel(""),weapon1=new JLabel(""),fitness1=new JLabel(""),money=new JLabel(""),betDisplay1=new JLabel("0"),username=new JLabel("");
	JLabel descriptor2=new JLabel(""),weapon2=new JLabel(""),fitness2=new JLabel(""),betDisplay2=new JLabel("0"),vs=new JLabel("vs.");
	
	GroupLayout layout=new GroupLayout(this);
	
	JPanel panel1=new JPanel(),panel2=new JPanel();
	int betAmount=0,maxBet=0;
	String[] names,descriptors,weapons,fitnesses;
	Player[] players;
	String name="";
	User user;
	GambleHandler handle;

	public Gambling(Player[] players,User user,GambleHandler handle){
		
		setVisible(true);
		this.setMaximumSize(new Dimension(Display.WIDTH,Display.HEIGHT));
		this.handle=handle;
		this.players=players;
		this.user=user;
		names=new String[players.length/2];
		Arrays.fill(names, 0, names.length, "");
		descriptors=new String[players.length];
		weapons=new String[players.length];
		fitnesses=new String[players.length];
		for(int i=0;i<players.length;i++){
			names[i/2]=names[i/2]+" "+players[i].species.name+" ";
			names[i/2]=names[i/2].replaceAll("  ", " vs. ");
			descriptors[i]=players[i].species.descriptor;
			weapons[i]=players[i].species.projectile.name;
			fitnesses[i]=String.valueOf(players[i].fitness);
		}
		
		dropDown=new JComboBox<String>(names);
		dropDown.setMaximumSize(new Dimension(100,40));
		dropDown.setVisible(true);
		dropDown.addActionListener(this);
		dropDown.setFont(dropDown.getFont().deriveFont(12.0f));

		descriptor1.setPreferredSize(new Dimension(200,18));
		descriptor1.setFont(descriptor1.getFont().deriveFont(14.0f));
		weapon1.setPreferredSize(new Dimension(200,18));
		weapon1.setFont(weapon1.getFont().deriveFont(14.0f));
		fitness1.setPreferredSize(new Dimension(200,18));
		fitness1.setFont(fitness1.getFont().deriveFont(14.0f));
	
		descriptor2.setPreferredSize(descriptor1.getPreferredSize());
		descriptor2.setFont(descriptor2.getFont().deriveFont(14.0f));
		weapon2.setPreferredSize(weapon1.getPreferredSize());
		weapon2.setFont(weapon2.getFont().deriveFont(14.0f));
		fitness2.setPreferredSize(fitness1.getPreferredSize());
		fitness2.setFont(fitness2.getFont().deriveFont(14.0f));
		
		panel1.setPreferredSize(new Dimension(300,75));
		panel1.setLayout(new GridLayout(0,1));
		panel1.add(descriptor1);
		panel1.add(weapon1);
		panel1.add(fitness1);
		
		panel2.setPreferredSize(new Dimension(300,75));
		panel2.setLayout(new GridLayout(0,1));
		panel2.add(descriptor2);
		panel2.add(weapon2);
		panel2.add(fitness2);
		
		transferButton1.setActionCommand("<");
		transferButton2.setActionCommand(">");
		transferButton1.addActionListener(new betButtonHandler());
		transferButton2.addActionListener(new betButtonHandler());
		
		betDisplay1.setHorizontalTextPosition(JLabel.CENTER);
		betDisplay1.setPreferredSize(new Dimension(20,20));
		
		money.setText(String.valueOf(user.money));
		money.setPreferredSize(new Dimension(60,18));
		button.setText("Wagering Complete");
		button.addActionListener(new buttonHandler());
		updateInfo();
		
		username.setText(user.name);
		
		layout.setAutoCreateGaps(true);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(username)
					.addComponent(dropDown)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(panel1).addComponent(vs).addComponent(panel2))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(betDisplay1).addComponent(transferButton1).addComponent(money).addComponent(transferButton2)
							.addComponent(betDisplay2)).addComponent(button)
					
					);
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(panel1))
					.addGroup(layout.createSequentialGroup()
							.addComponent(betDisplay1)
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(username)
									.addComponent(dropDown).addComponent(vs)
									.addGroup(layout.createSequentialGroup()
											.addComponent(transferButton1).addComponent(money).addComponent(transferButton2)
									)
								.addComponent(button)
							)
							.addComponent(betDisplay2))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
							.addComponent(panel2))
			);
		
		/*layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(username)
					.addComponent(dropDown)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(name1).addComponent(vs).addComponent(name2))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(descriptor1).addComponent(descriptor2))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(weapon1)
						.addComponent(betDisplay1).addComponent(transferButton1).addComponent(money).addComponent(transferButton2).addComponent(betDisplay2)
						.addComponent(weapon2))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(fitness1).addComponent(fitness2))
					.addComponent(button)
					);
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(name1).addComponent(descriptor1).addComponent(weapon1).addComponent(fitness1))
					.addGroup(layout.createSequentialGroup()
							.addComponent(betDisplay1)
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(username)
									.addComponent(dropDown).addComponent(vs)
									.addGroup(layout.createSequentialGroup()
											.addComponent(transferButton1).addComponent(money).addComponent(transferButton2)
									)
									.addComponent(button)
							)
							.addComponent(betDisplay2))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
							.addComponent(name2).addComponent(descriptor2).addComponent(weapon2).addComponent(fitness2))
			);*/
		setLayout(layout);
		for(Component c:this.getComponents()){
			c.setFont(c.getFont().deriveFont(14.0f));
		}
		Display.changeComponent(this);
	}
	
	/*public static void main(String[] args){
			JFrame window=new JFrame();
			JPanel pan=new Gambling(new Player[]{
					new Player(0,0,11,Math.PI,Game.newSpecies(0x000000)),
					new Player(0,0,11,Math.PI,Game.newSpecies(0x000000))},new User("Hammer"),new GambleHandler(new Controller()));
			window.add(pan);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}*/
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		updateInfo();
	}
	public void updateInfo(){
		
		betAmount=user.getBet(players[dropDown.getSelectedIndex()*2],players[dropDown.getSelectedIndex()*2+1]);
		
		TitledBorder border1 = BorderFactory.createTitledBorder(players[dropDown.getSelectedIndex()*2].species.name);
	    border1.setTitleJustification(TitledBorder.CENTER);
	    panel1.setBorder(border1);
	    
	    TitledBorder border2 = BorderFactory.createTitledBorder(players[dropDown.getSelectedIndex()*2+1].species.name);
	    border2.setTitleJustification(TitledBorder.CENTER);
		panel2.setBorder(border2);
		
		descriptor1.setText("    Desciption: "+descriptors[dropDown.getSelectedIndex()*2]+"\t ");
		weapon1.setText("    Weapon: "+weapons[dropDown.getSelectedIndex()*2]+"\t ");
		fitness1.setText("    Scout Score: "+fitnesses[dropDown.getSelectedIndex()*2]+"\t ");
		if(betAmount>=0){
			betDisplay2.setText(""+betAmount);
			betDisplay1.setText("0");
		}
		else{
			betDisplay2.setText("0");
			betDisplay1.setText(""+(-betAmount));
		}
			
		descriptor2.setText("    Desciption: "+descriptors[dropDown.getSelectedIndex()*2+1]+"\t ");
		weapon2.setText("    Weapon: "+weapons[dropDown.getSelectedIndex()*2+1]+"\t ");
		fitness2.setText("    Scout Score: "+fitnesses[dropDown.getSelectedIndex()*2+1]+"\t ");
		
		maxBet=user.money+Math.abs(betAmount);
		money.setText(String.valueOf(user.money));
	}
	private class betButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(Math.abs(betAmount)+user.money>0){
			int change=0;
			if(arg0.getActionCommand().equals("<")&&(user.money>0||user.getBet(players[dropDown.getSelectedIndex()*2+1])>0)){
				betAmount--;
			}
			else if(arg0.getActionCommand().equals(">")&&(user.money>0||user.getBet(players[dropDown.getSelectedIndex()*2])>0)){
				betAmount++;
			}
			
			if(betAmount>0)
				change=user.getBet(players[dropDown.getSelectedIndex()*2+1]).intValue()-Math.abs(betAmount);
			else if(betAmount<0)
				change=user.getBet(players[dropDown.getSelectedIndex()*2]).intValue()-Math.abs(betAmount);
			else if(Math.abs(betAmount)==0)
				change=1;
				
			user.money+=change;
			
			if(betAmount>=0)
				user.placeBet(players[dropDown.getSelectedIndex()*2+1], betAmount);
			if(betAmount<=0)
				user.placeBet(players[dropDown.getSelectedIndex()*2], -betAmount);
			money.setText(String.valueOf(user.money));
			
			updateInfo();
			}
		}
		
	}
	private class buttonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			handle.finishPlacingBet(user);
			setVisible(false);
		}
		
	}
}