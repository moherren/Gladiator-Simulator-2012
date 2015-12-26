package com.mime.evolve.input;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mime.evolve.Controller;
import com.mime.evolve.Display;
import com.mime.evolve.Game;

public class UserDefiner extends JPanel implements ActionListener,ChangeListener{

	JLabel Title=new JLabel("Gladiator Simulator 2012");
	JLabel author=new JLabel("by Matthew \"The Hammer\" O'Herren");
	JLabel intro=new JLabel("Number of Players:",JLabel.LEADING);
	JSpinner userAmount,inputIntensity;
	JLabel[] names;
	JTextField[] inputNames;
	JTextField inputEvolution;
	JButton done;
	GambleHandler handle;
	int gap=60;
	GroupLayout layout=new GroupLayout(this);
	int amount=2;
	JPanel options,filler;
	
	public UserDefiner(GambleHandler handle){
		this.handle=handle;		
		System.out.println("users to be defined");
		
		Title.setFont(Title.getFont().deriveFont(32.0f));
		Title.setFont(Title.getFont().deriveFont(Font.BOLD));
		Title.setHorizontalTextPosition(JLabel.CENTER);
		
		filler=new JPanel();
		options=new JPanel();
		options.setLayout(new GridLayout(0,2));
		TitledBorder border = BorderFactory.createTitledBorder("OPTIONS");
	    border.setTitleJustification(TitledBorder.CENTER);
		options.setBorder(border);
		options.setMinimumSize(new Dimension(250,60));
		options.setMaximumSize(new Dimension(250,60));
		
		setVisible(true);
		setLayout(layout);
		
		inputEvolution=new JTextField(Game.evolutionAmount+"");
		inputIntensity=new JSpinner();
		inputIntensity.setModel(new SpinnerNumberModel(Controller.intensity,2,8,1));
		//inputEvolution.setPreferredSize(new Dimension(60,20));
		options.add(new JLabel("Generation Amount: "));
		options.add(inputEvolution);
		options.add(new JLabel("Generation Intensity: "));
		options.add(inputIntensity);
		
		
		userAmount=new JSpinner();
		userAmount.setModel(new SpinnerNumberModel(amount,1,8,1));
		userAmount.addChangeListener(this);
		userAmount.setPreferredSize(new Dimension(200,20));
		userAmount.setFont(intro.getFont().deriveFont(14.0f));
		
		intro.setPreferredSize(new Dimension(100,20));
		intro.setFont(intro.getFont().deriveFont(14.0f));
		
		names=new JLabel[8];
		inputNames=new JTextField[8];
		for(int i=0;i<8;i++){
			names[i]=new JLabel("Player "+(i+1)+":");
			names[i].setFont(names[i].getFont().deriveFont(14.0f));
			inputNames[i]=new JTextField();
			inputNames[i].setPreferredSize(new Dimension(200,20));
			inputNames[i].setVisible(true);
			inputNames[i].setFont(inputNames[i].getFont().deriveFont(14.0f));
			if(i>=amount){
				inputNames[i].setVisible(false);
				names[i].setVisible(false);
			}
		}
		done=new JButton("Continue");
		done.addActionListener(this);
		
		
		layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup().addComponent(Title).addComponent(author)
				.addGroup(
				layout.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup()
						.addComponent(options)
						.addComponent(filler)
				)
				.addComponent(intro)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(userAmount)
						.addGroup(layout.createSequentialGroup()
								.addGroup(
									layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(names[0])
									.addComponent(names[1])
									.addComponent(names[2])
									.addComponent(names[3])
									.addComponent(names[4])
									.addComponent(names[5])
									.addComponent(names[6])
									.addComponent(names[7])
								)
								.addGroup(
									layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(inputNames[0])
									.addComponent(inputNames[1])
									.addComponent(inputNames[2])
									.addComponent(inputNames[3])
									.addComponent(inputNames[4])
									.addComponent(inputNames[5])
									.addComponent(inputNames[6])
									.addComponent(inputNames[7])
								)
								)
						)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(done)
								)
						)
				);
				
		layout.setVerticalGroup(
				layout.createSequentialGroup().addComponent(Title).addComponent(author)
				.addGroup(
				layout.createParallelGroup()
				.addGroup(
						layout.createSequentialGroup()
						.addComponent(options)
						.addComponent(filler))
				.addGroup(layout.createSequentialGroup()
					.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(intro)
							.addComponent(userAmount)
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(names[0])
							.addComponent(inputNames[0])
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(names[1])
							.addComponent(inputNames[1])
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(names[2])
							.addComponent(inputNames[2])
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(names[3])
							.addComponent(inputNames[3])
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(names[4])
							.addComponent(inputNames[4])
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(names[5])
							.addComponent(inputNames[5])
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(names[6])
							.addComponent(inputNames[6])
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(names[7])
							.addComponent(inputNames[7])
							)
							.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(done)
							)
						)
					)
				);
		
		Display.changeComponent(this);
		updateWindow();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(optionsMakeSense()){
			Game.evolutionAmount=Integer.parseInt(inputEvolution.getText());
			Controller.intensity=(int) inputIntensity.getValue();
			System.out.println("all done");
			setVisible(false);
			User[] users=new User[(Integer)userAmount.getValue()];
			for(int i=0;i<(Integer)userAmount.getValue();i++){
				if(inputNames[i].getText().equals(""))
					inputNames[i].setText("Player "+(i+1));
				users[i]=new User(inputNames[i].getText());
			}
			System.out.println("hi");
			handle.finishMakingUsers(users);
			Display.returnCanvas();
		}
	}

	private boolean optionsMakeSense() {
		try{
			Integer.parseInt(inputEvolution.getText());
		}catch(NumberFormatException e){
			return false;
		}
		if(Game.evolutionAmount<Game.minEvolution)
			return false;
		return true;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		amount=(Integer)userAmount.getValue();
		updateWindow();
	}

	public void updateWindow(){
		boolean v=true;
		for(int i=0;i<8;i++){
			if(i>=amount)
				v=false;
			names[i].setVisible(v);
			inputNames[i].setVisible(v);
		}
		
		revalidate();
		Display.frame.repaint();
	}
}
