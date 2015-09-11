package geometry;

import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Circle extends Shape{

	private double radius;
	
	public Circle(double x,double y,double radius){
		position=new Vector2D(x,y);
		this.radius=radius;
		boundingRadius=radius;
	}
	
	public Circle(double radius){
		position=new Vector2D(0,0);
		this.radius=radius;
		boundingRadius=radius;
	}
	
	public void render(Graphics g){
		g.fillOval((int)(position.getX()-radius),(int)(position.getY()-radius),(int)radius*2,(int)radius*2);
	}

	public Area toArea() {
		return new Area(new Ellipse2D.Double(position.getX()-radius,position.getY()-radius,radius*2,radius*2));
	}

	//rotating circles does nothing
	public void rotate(double theta) {}
	
	public Shape translate(Vector2D translation){
		return new Circle(position.getX()+translation.getX(),position.getY()+translation.getY(),radius);
	}
	
	public double getRadius(){
		return radius;
	}
	
	public boolean intersects(Line line){
		Vector2D toStart=position.sub(line.getPoint1());
		Vector2D toP2=line.getPoint2().sub(line.getPoint1());
		double length=toP2.magnitude();
		//normalize
		toP2.idiv(length);
		double dist=toStart.dot(toP2);
		if(dist<0)
			dist=0;
		else if(dist>length)
			dist=length;
		Vector2D closestPoint=line.getPoint1().add(toP2.mult(dist));
		Vector2D toPoint=position.sub(closestPoint);
		return toPoint.magnitudeSq()<=radius*radius;
	}

	public Shape clone() {
		return new Circle(position.x,position.y,radius);
	}
	
}
