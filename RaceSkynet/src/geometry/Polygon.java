package geometry;

import java.awt.Graphics;
import java.awt.geom.Area;

public class Polygon extends Shape {

	public Vector2D[] points,original;
	public double rot=0;
	
	public Polygon(double[] relativexpos,double[] relativeypos){
		points=new Vector2D[relativexpos.length];
		original=new Vector2D[relativexpos.length];
		for(int i=0;i<points.length;i++){
			points[i]=new Vector2D(relativexpos[i],relativeypos[i]);
		}
		position=new Vector2D(0,0);
		boundingRadius=0;
		for(Vector2D p:points){
			boundingRadius=Math.max(boundingRadius, p.magnitude());
		}
	}
	
	public Polygon(double[] relativexpos,double[] relativeypos,double x,double y){
		points=new Vector2D[relativexpos.length];
		for(int i=0;i<points.length;i++){
			points[i]=new Vector2D(relativexpos[i],relativeypos[i]);
		}
		position=new Vector2D(x,y);
		boundingRadius=0;
		for(Vector2D p:points){
			boundingRadius=Math.max(boundingRadius, p.magnitude());
		}
	}
	
	public Polygon(Vector2D[] relativePoints,Vector2D position){
		points=relativePoints;
		this.position=position;
		boundingRadius=0;
		for(Vector2D p:points){
			boundingRadius=Math.max(boundingRadius, p.magnitude());
		}
	}
	
	public void render(Graphics g) {
		int[] xpos=new int[points.length],ypos=new int[points.length];
		for(int i=0;i<points.length;i++){
			xpos[i]=(int)(points[i].getX()+position.getX());
			ypos[i]=(int)(points[i].getY()+position.getY());
		}
		g.fillPolygon(xpos, ypos, points.length);
	}

	public Area toArea(){
		int[] xpos=new int[points.length],ypos=new int[points.length];
		for(int i=0;i<points.length;i++){
			xpos[i]=(int)(points[i].getX()+position.getX());
			ypos[i]=(int)(points[i].getY()+position.getY());
		}
		return new Area(new java.awt.Polygon(xpos, ypos, points.length));
	}

	public void rotate(double theta) {
		rot+=theta;
		for(int i=0;i<points.length;i++){
			points[i]=points[i].rotate(theta);
		}
	}
	
	public void setRotation(double theta) {
		rot=theta;
		for(int i=0;i<points.length;i++){
			points[i]=points[i].rotate(theta);
		}
	}
	
	public Shape translate(Vector2D translation){
		return new Polygon(points,position.add(translation));
	}
	
	public Line[] toLines(){
		Line[] lines=new Line[points.length];
		for(int i=0;i<points.length-1;i++){
			lines[i]=new Line(points[i].add(position),points[i+1].add(position));
		}
		lines[points.length-1]=new Line(points[points.length-1].add(position),points[0].add(position));
		return lines;
	}
	
	public Vector2D[] toPoints(){
		Vector2D[] actualPoints=new Vector2D[points.length];
		for(int i=0;i<points.length;i++){
			actualPoints[i]=points[i].add(position);
		}
		return actualPoints;
	}
	
	public boolean includes(Vector2D point){
		if(point.vectorTo(position).magnitude()>boundingRadius)
			   return false;
		  int intersections=0;
		  for(Line l:toLines()){
		   Vector2D p1,p2;
		   if(l.getPoint1().y>l.getPoint2().y){
		    p1=l.getPoint1();
		    p2=l.getPoint2();
		   }
		   else{
		    p2=l.getPoint1();
		    p1=l.getPoint2();
		   }
		   if(point.y>=p2.y&&point.y<=p1.y){
		    if((p1.x-p2.x)*(point.y-p2.y)>=(p1.y-p2.y)*(point.x-p2.x))
		      intersections++;
		   }
		  }
		  return intersections%2==1;
		 }

	public Shape clone() {
		Vector2D[] newPoints=new Vector2D[points.length];
		for(int i=0;i<points.length;i++){
			Vector2D p=points[i];
			newPoints[i]=new Vector2D(p.x,p.y);
		}
		return new Polygon(newPoints,new Vector2D(position.x,position.y));
	}
	public Rectangle getBoundingBox(){
		double width=0,height=0;
		for(Vector2D p:points){
			width=Math.max(width, Math.abs(p.x));
			height=Math.max(height, Math.abs(p.y));
		}
		return new Rectangle(position.x,position.y,width,height);
	}
	public boolean intersects(Line l){
		for(Line line:toLines())
			if(line.intersects(l))
				return true;
		if(includes(l.getPoint1()))
			return true;
		if(includes(l.getPoint2()))
			return true;
		return false;
	}
	
	public double getRotation(){
		return rot;
	}
}
