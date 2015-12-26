package race;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

import game.Display;
import geometry.Polygon;
import graphics.Render;

public class Road {
	PolygonShape body;
	float distanceStart=0,distanceEnd=0;
	double dir=0;
	Vec2[] points;
	
	public Road(Vec2[] points,float distanceStart){
		body=new PolygonShape();
		body.set(points, 4);
		this.distanceStart=distanceStart;
		distanceEnd=points[5].sub(points[4]).length()+distanceStart;
		dir=Math.atan2(points[5].y - points[4].y,points[5].x - points[4].x);
		this.points=points;
	}
	
	public void applyDistances(Render render){
		Transform t=new Transform();
		t.set(new Vec2(0,0), (float)-dir);
		PolygonShape body=(PolygonShape) this.body.clone();
		body.centroid(t);
		float minX=0,minY=0,maxX=Display.WIDTH,maxY=Display.HEIGHT;
		for(Vec2 v:body.getVertices()){
			minX=Math.min(minX, v.x);
			maxX=Math.max(maxX, v.x);
			minY=Math.min(minY, v.y);
			maxY=Math.max(maxY, v.y);
		}
		
		//minX=Math.max(minX, 0);
		//maxX=Math.min(maxX, 0);
		//minY=Math.max(minY, Display.WIDTH);
		//maxY=Math.min(maxY, Display.HEIGHT);
		
		for(float x=minX;x<maxX;x++){
			for(float y=minY;y<maxY;y++){
				float distance=(float) Math.abs(Math.cos(dir-Math.atan2(y - points[4].y,x - points[4].x))*points[4].sub(new Vec2(x,y)).length())+distanceStart;
				if(body.testPoint(new Transform(), new Vec2(x,y))&&render.pixels[(int) (x+render.width*y)]==-1&&distance>=0){
					render.pixels[(int) (x+render.width*y)]=(int) distance;
				}
			}
		}
	}
	
	public void applyDirections(Render render){
		Transform t=new Transform();
		t.set(new Vec2(0,0), (float)-dir);
		PolygonShape body=(PolygonShape) this.body.clone();
		body.centroid(t);
		float minX=0,minY=0,maxX=Display.WIDTH,maxY=Display.HEIGHT;
		for(Vec2 v:body.getVertices()){
			minX=Math.min(minX, v.x);
			maxX=Math.max(maxX, v.x);
			minY=Math.min(minY, v.y);
			maxY=Math.max(maxY, v.y);
		}
		
		//minX=Math.max(minX, 0);
		//maxX=Math.min(maxX, 0);
		//minY=Math.max(minY, Display.WIDTH);
		//maxY=Math.min(maxY, Display.HEIGHT);
		
		for(float x=minX;x<maxX;x++){
			for(float y=minY;y<maxY;y++){
				if(body.testPoint(new Transform(), new Vec2(x,y))&&render.pixels[(int) (x+render.width*y)]==-1){
					render.pixels[(int) (x+render.width*y)]=(int) (dir+Math.PI*2)*10000;
				}
			}
		}
	}
	
	public float getDistanceEnd(){
		return distanceEnd;
	}
}
