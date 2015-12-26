package game;

import graphics.Screen;
import input.InputListener;

import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import race.Car;
import race.Track;
import Physics.body;

public class Display extends Canvas implements ActionListener{

	public final static int WIDTH = 1200;
	public final static int HEIGHT = 800;
	private int[] pixels;
	private BufferedImage img;
	public static JPanel frame;
	private boolean Running=false;
	Screen screen;
	public static long genSeed;
	long frameNum=System.nanoTime();
	private InputListener input;
	public static Display display;
	private static Game game;
	public static World world;
	public static Timer timer;

	public static void main(String[] args) {
		if(args.length==0){
			genSeed=new Random().nextLong();
		}else{
			genSeed=Long.parseLong(args[0]);
		}
		
		Display game = new Display();
		JFrame window=new JFrame();
		window.pack();
		window.add(game);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);
		window.setSize(WIDTH, HEIGHT);
		window.setLocationRelativeTo(null);
		System.out.println("Running...");
		game.start();
	}
	
	public void step() {
			//if(game.species[0].getElite().fitness>1000)
				render();
			game.tick(input.key);
			for(Car c:Game.getTrack().getCars()){
				c.update();
			}
			
			world.step(1/40f,8,3);
	}
	
	private void start(){
		if (Running)
			return;
		Running=true;
		timer=new Timer(1, this);
		timer.start();
	}
	
	private void stop(){
		if(!Running)
			return;
		Running=false;
		timer.stop();
	}

	public Display(){
		world=new World(new Vec2(0,0));
		
		display=this;
		img=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		pixels= ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		screen=new Screen(WIDTH,HEIGHT);
		
		input=new InputListener();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		
		game=new Game(WIDTH,HEIGHT);
	}
	
	public void render(){
		Game.getTrack().render(screen);
		frameNum=System.nanoTime();
		BufferStrategy bs=this.getBufferStrategy();
		if(bs==null){
			createBufferStrategy(2);
			render();
			return;
		}
		screen.render();
		for(int i=0;i<WIDTH*HEIGHT;i++){
			pixels[i]=screen.pixels[i];
		}
		Graphics g=bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		
		Graphics2D g2=(Graphics2D) g;
		g2.drawString(Game.species[0]+"", 0, 10);
		g2.drawString(Game.species[1]+"", 0, 25);
		g2.drawString(new Vec2(input.MouseX,input.MouseY)+" "+Game.getTrack().getSegment(Game.getTrack().getDistance(new Vec2(input.MouseX,input.MouseY)), 3), 0, 40);
		g2.translate(-(Screen.centerX-Screen.width/2), -(Screen.centerY-Screen.height/2));
		g2.fill(Track.wallArea);
		g2.translate(Screen.centerX-Screen.width/2, Screen.centerY-Screen.height/2);
		g.dispose();
		bs.show();	
	}
	
	public static Game getGame() {
		return game;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		step();
	}
}
