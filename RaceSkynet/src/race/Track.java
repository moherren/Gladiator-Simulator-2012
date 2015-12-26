package race;

import graphics.Render;
import graphics.Screen;
import graphics.Visible;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import ai.AI;
import game.Display;

public class Track implements Visible{

	public int width, height;
	public static ArrayList<Body> walls=new ArrayList<Body>();
	public static ArrayList<Area> wallAreas=new ArrayList<Area>();
	public static ArrayList<Road> roads=new ArrayList<Road>();
	public static Area wallArea=new Area();
	ArrayList<Car> cars=new ArrayList<Car>();
	ArrayList<Vec2> startPoints=new ArrayList<Vec2>();
	Render distance,roadLines,direction;
	float length=0;
	
	public Track(int width,int height){
		this.width=width;
		this.height=height;
		distance=new Render(width,height);
		roadLines=new Render(width,height);
		direction=new Render(width,height);
		Arrays.fill(distance.pixels,-1);
		Arrays.fill(roadLines.pixels,-1);
		Arrays.fill(direction.pixels,-1);
		walls.removeAll(walls);
	}
	
	public Track(int width,int height,TrackPiece[] track){
		this.width=width;
		this.height=height;
		distance=new Render(width,height);
		roadLines=new Render(width,height);
		direction=new Render(width,height);
		Arrays.fill(distance.pixels,-1);
		Arrays.fill(roadLines.pixels,-1);
		Arrays.fill(direction.pixels,-1);
		walls.removeAll(walls);
		formTrack(track);
	}
	
	public void boxOff(){
		newWall(new Vec2[]{new Vec2(0,0),new Vec2(0,Display.HEIGHT),new Vec2(10,Display.HEIGHT),new Vec2(10,0)});
		newWall(new Vec2[]{new Vec2(0,0),new Vec2(Display.WIDTH,0),new Vec2(Display.WIDTH,10),new Vec2(0,10)});
		newWall(new Vec2[]{new Vec2(0,Display.HEIGHT),new Vec2(Display.WIDTH,Display.HEIGHT),new Vec2(Display.WIDTH,Display.HEIGHT-10),new Vec2(0,Display.HEIGHT-10)});
		newWall(new Vec2[]{new Vec2(Display.WIDTH,0),new Vec2(Display.WIDTH,Display.HEIGHT),new Vec2(Display.WIDTH-10,Display.HEIGHT),new Vec2(Display.WIDTH-10,0)});
	}
	
	public static TrackPiece[] getCircleTrack(){
		TrackPiece[] output=new TrackPiece[7];
		output[0]=new TrackPiece.normal(new Vec2(200,600),200f);
		output[1]=new TrackPiece.normal(new Vec2(500,600),200f);
		output[2]=new TrackPiece.normal(new Vec2(700,100),200f);
		output[3]=new TrackPiece.normal(new Vec2(500,50),100f);
		output[4]=new TrackPiece.normal(new Vec2(230,105),200f);
		output[5]=new TrackPiece.normal(new Vec2(180,135),200f);
		output[6]=new TrackPiece.end(new Vec2(120,350),200f);
		return output;
	}
	
	public static TrackPiece[] getSimpleTrack(){
		ArrayList<TrackPiece> list=new ArrayList<TrackPiece>();
		list.add(new TrackPiece.normal(new Vec2(200,1000),200f));
		list.add(new TrackPiece.normal(new Vec2(200,1500),200f));
		list.add(new TrackPiece.normal(new Vec2(500,1900),200f));
		list.add(new TrackPiece.normal(new Vec2(600,1900),200f));
		list.add(new TrackPiece.normal(new Vec2(900,1500),200f));
		list.add(new TrackPiece.normal(new Vec2(900,1000),200f));
		list.add(new TrackPiece.normal(new Vec2(1000,900),200f));
		list.add(new TrackPiece.normal(new Vec2(1400,900),200f));
		list.add(new TrackPiece.normal(new Vec2(1600,700),200f));
		list.add(new TrackPiece.normal(new Vec2(1600,500),300f));
		list.add(new TrackPiece.normal(new Vec2(1400,300),300f));
		list.add(new TrackPiece.normal(new Vec2(1000,300),200f));
		list.add(new TrackPiece.end(new Vec2(200,500),200f));
		
		TrackPiece[] array=new TrackPiece[list.size()];
		for(int i=0;i<list.size();i++)
			array[i]=list.get(i);
		return array;
	}
	
	public void formTrack(TrackPiece[] track){
		int wallLength=0;
		for(TrackPiece t:track){
			wallLength+=t.roadSides(new Vec2(0,0), new Vec2(0,0), new Track(width,height)).length;
		}
		
		Vec2[] wall1=new Vec2[wallLength/2],wall2=new Vec2[wallLength/2],sides;
		for(int i=0;i<track.length;i++){
			int back=i-1,front=i+1;
			if(back==-1)
				back=track.length-1;
			if(front==track.length)
				front=0;
			sides=track[i].roadSides(track[front].getPosition()[0], track[back].getPosition()[track[back].getPosition().length-1],this);
			for(int a=0;a<sides.length;a++){
				int place=i*2+a%2+(a/4)*2;
				if(a%4<=1)
					wall1[place]=sides[a];
				else
					wall2[place]=sides[a];
			}
		}
		for(int a=0;a<wall1.length;a+=2){
			Vec2[] poly=new Vec2[4];
			int b=a+1,c=a+2,d=a+3;
			if(c>=wall1.length){
				c=0;
				d=1;
			}
				
			poly[0]=wall1[a];
			poly[1]=wall1[b];
			poly[2]=wall1[d];
			poly[3]=wall1[c];
			newWall(poly);
			poly[0]=wall2[a];
			poly[1]=wall2[b];
			poly[2]=wall2[d];
			poly[3]=wall2[c];
			newWall(poly);			
			poly=new Vec2[6];
			poly[0]=wall1[a];
			poly[1]=wall2[a];
			poly[2]=wall2[c];
			poly[3]=wall1[c];
			poly[4]=poly[0].add(poly[1]).mul(0.5f);
			poly[5]=poly[2].add(poly[3]).mul(0.5f);
			Road road=new Road(poly,length);
			length=road.getDistanceEnd();
			road.applyDistances(this.distance);
			road.applyDirections(direction);
			roads.add(road);
		}
	}

	public void newWall(Vec2[] Vs){
		BodyDef bodyDef=new BodyDef();
		bodyDef.type=BodyType.STATIC;
		bodyDef.position=new Vec2(0,0);
		Body body=Display.world.createBody(bodyDef);
		PolygonShape polygonShape=new PolygonShape();
		polygonShape.set(Vs, Vs.length);
		body.createFixture(polygonShape, 1);
		walls.add(body);
		
		Polygon poly=new Polygon();
		for(Vec2 v:Vs){
			poly.addPoint((int)(v.x), (int)(v.y));
		}
		wallAreas.add(new Area(poly));
		wallArea.add(new Area(poly));
	}
	
	@Override
	public void render(Render r) {
		//r.draw(distance, 0, 0);
		r.draw(roadLines, Screen.centerX-Screen.width/2, Screen.centerY-Screen.centerY/2);
		for(Car c:cars){
			c.render(r);
		}
	}
	
	public int getDistance(Vec2 v){
		return distance.pixels[(int) (v.x)+distance.width*(int)(v.y)];
	}
	
	public double getDirection(Vec2 v){
		return distance.pixels[(int) (v.x+distance.width*v.y)]/10000;
	}
	
	public ArrayList<Car> getCars(){
		return cars;
	}
	
	public void createCar(AI ai){
		if(startPoints.isEmpty())
			return;
		Vec2 v=startPoints.get(0);
		startPoints.remove(0);
		cars.add(new Car(v.x,v.y,Math.PI,ai,this,cars.size()));
	}
	
	public void restartCar(int id,AI ai){
		for(Car c:cars){
			if(c.id==id){
				c.destroy();
				Vec2 start=c.startPoint;
				cars.add(new Car(start.x,start.y,Math.PI,ai,this,c.id));
				return;
			}
		}
	}
	
	public void addStartPoint(Vec2 v){
		startPoints.add(v);
		for(int x=(int) (v.x-15);x<v.x+15;x++){
			for(int y=(int) (v.y+15);y<v.y+25;y++){
				if(x<(v.x-10)||x>(v.x+10))
					roadLines.pixels[x+y*width]=0xffffff;
				else if(y>(v.y+20))
					roadLines.pixels[x+y*width]=0xffffff;
			}
		}
	}
	
	public void updatePosistions(){
		for(Car c:cars){
			c.applyDistance(getDistance(c.getPosition()));
		}
		
		Collections.sort(cars, new Comparator<Car>() {
	        public int compare(Car  car1, Car  car2)
	        {
	            return  car1.compareTo(car2);
	        }
	    });
		
		cars.get(1).setColor(0xff0000);
		cars.get(0).setColor(0x00ff00);
	}
	
	public int getSegment(int dis,int numberOfPieces){
		return (int) Math.abs(dis/(length/numberOfPieces))%numberOfPieces;
	}
	
	public void destroy(){
		for(Car c:cars){
			c.destroy();
		}
		for(Body b:walls){
			b.getWorld().destroyBody(b);
		}
	}
}
