package com.mime.evolve.tests;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SoundTest extends JFrame{
	int volume;
	float volFloat;
	JLabel volText;
	public SoundTest() {
		
		volText = new JLabel("Gain: " + Integer.toString(volume));
		
		JPanel sliderPanel=new JPanel();
		Container cp = this.getContentPane();
		
		cp.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		
		
		
		int volMin = -6;
		int volMax = 6;
		
		JSlider volSlider = new JSlider(volMin,volMax);
		volSlider.setPaintTicks(true);
		volSlider.setMajorTickSpacing(1);
		volSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				volume = ((JSlider)e.getSource()).getValue();
				//volText = new JLabel(Integer.toString(volume));
				volText.setText("Gain: " + Integer.toString(volume));
				volFloat = (float)volume;
			}
		});
		sliderPanel.add(volText);
		sliderPanel.add(volSlider);
		cp.add(sliderPanel);
		
		
		
		JButton btnSound1 = new JButton("GRUNT_ONE");

		btnSound1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundHandler.play(SoundHandler.GRUNT_ONE,volFloat);
			}
		});
		cp.add(btnSound1);

		JButton btnSound2 = new JButton("GRUNT_TWO");

		btnSound2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundHandler.play(SoundHandler.GRUNT_TWO,volFloat);
			}
		});
		cp.add(btnSound2);

		JButton btnSound3 = new JButton("VOICE_ONE");

		btnSound3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundHandler.play(SoundHandler.VOICE_ONE,volFloat);
			}
		});
		cp.add(btnSound3);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Test SoundEffct");
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new SoundTest();
	}
}
