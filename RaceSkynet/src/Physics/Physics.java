package Physics;

import geometry.Line;
import geometry.Polygon;
import geometry.Vector2D;

public class Physics {
	public static Vector2D pointOfCollision(Polygon p1,Polygon p2){
		if(!p1.intersects(p2))
			return null;
		for(Line l:p1.toLines()){
			for(Vector2D p:p2.points){
				if(l.intersects(p))
					return p;
			}
		}
			return pointOfCollision(p2,p1);
	}
	
	public static Vector2D wallCollisionDirection(Polygon wall,Vector2D POC){
		Vector2D center=wall.toLines()[0].centerPoint();
		Line direction=new Line(POC,wall.getPosition());
		for(Line line:wall.toLines()){
			//Line l=new Line(line.getPoint1().add(wall.getPosition()),line.getPoint2().add(wall.getPosition()));
			if(direction.intersects(line)){
			center=line.centerPoint();	
			break;
			}
		}
		
		Vector2D wc=wall.getPosition();
		return new Vector2D(center.getX()-wc.getX(),center.getY()-wc.getY());
	}
	
	public static double directionToVector(Vector2D target){
		return Math.atan2(target.getY(), target.getX());
	}
}
