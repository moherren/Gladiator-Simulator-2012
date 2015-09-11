package geometry;

import java.awt.geom.Area;

public abstract class Shape implements Renderable,Cloneable{
	
	protected Vector2D position;
	protected double boundingRadius;
	
	public abstract Area toArea();
	
	public abstract void rotate(double theta);
	
	public void setPosition(Vector2D position){
		this.position=position;
	}
	
	public Vector2D getPosition(){
		return position;
	}
	
	public boolean intersects(Shape other){
		return detectIntersection(this,other);
	}
	
	public static boolean detectIntersection(Shape s1,Shape s2){
		double dist=s1.getPosition().vectorTo(s2.getPosition()).magnitude();
		if(dist>s1.boundingRadius+s2.boundingRadius)
			return false;
		if(s1 instanceof Polygon){
			if(s2 instanceof Polygon)
				return detectPolygonPolygonIntersection((Polygon)s1,(Polygon)s2);
			else if(s2 instanceof Circle)
				return detectCirclePolygonIntersection((Circle)s2,(Polygon)s1);
		}
		if(s1 instanceof Circle){
			if(s2 instanceof Polygon)
				return detectCirclePolygonIntersection((Circle)s1,(Polygon)s2);
			else if(s2 instanceof Circle)
				return detectCircleCircleIntersection((Circle)s1,(Circle)s2);
		}
		if(s1 instanceof CompoundShape)
			return detectCompoundShapeIntersection((CompoundShape)s1,s2);
		if(s2 instanceof CompoundShape)
			return detectCompoundShapeIntersection((CompoundShape)s2,s1);
		return false;
	}
	
	public static boolean detectCircleCircleIntersection(Circle c1, Circle c2) {
		double dist=c1.getPosition().vectorTo(c2.getPosition()).magnitude();
		if(dist<=c1.getRadius()+c2.getRadius())
			return true;
		return false;
	}

	public static boolean detectCirclePolygonIntersection(Circle c, Polygon p) {
		if(p.includes(c.getPosition()))
			return true;
		for(Line l:p.toLines()){
			if(c.intersects(l)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean detectCompoundShapeIntersection(CompoundShape shape,Shape s){
		for(Shape part:shape.getShapes()){
			if(part.intersects(s))
				return true;
		}
		return false;
	}

	public static boolean detectPolygonPolygonIntersection(Polygon p1, Polygon p2) {
		for(Vector2D p:p1.toPoints()){
			if(p2.includes(p))
				return true;
		}
		for(Line l1:p1.toLines())
			for(Line l2:p2.toLines())
				if(l1.intersects(l2))
					return true;
		return false;
	}

	public abstract Shape translate(Vector2D translation);
	
	public static void main(String[] args){
		long total=0;
		for(int i=0;i<1000000;i++){
			Polygon poly=new Polygon(new double[]{-20,0,20},new double[]{-20,20,-20},10,10);
			Circle circ=new Circle(40,40,20);
			long start=System.nanoTime();
			System.out.println(poly.intersects(circ));
			total+=(System.nanoTime()-start);
			System.out.println(i);
		}
		System.out.println(total);
	}
	
	public abstract Shape clone();
	
	public double getBoundingRadius(){
		return boundingRadius;
	}
	
	public Rectangle getBoundingBox(){
		return null;
	}

	public boolean intersects(Line line) {
		return false;
	}
}
