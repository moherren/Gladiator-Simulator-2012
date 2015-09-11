package geometry;

public class Line {

	private Vector2D p1,p2;
	
	public Line(Vector2D p1,Vector2D p2) {
		this.p1=p1;
		this.p2=p2;
	}
	
	public Line(double x1,double y1,double x2,double y2) {
		this.p1=new Vector2D(x1,y1);
		this.p2=new Vector2D(x2,y2);
	}

	public Vector2D[] getEndPoints(){
		return new Vector2D[]{p1,p2};
	}
	
	public Vector2D getPoint1(){
		return p1;
	}
	
	public Vector2D getPoint2(){
		return p2;
	}
	
	public boolean intersects(Line other){
		Vector2D q1=other.getPoint1(),q2=other.getPoint2();
		//orientations
		double a=Math.signum((p2.getY()-p1.getY())*(q1.getX()-p2.getX())-(q1.getY()-p2.getY())*(p2.getX()-p1.getX()));
		double b=Math.signum((p2.getY()-p1.getY())*(q2.getX()-p2.getX())-(q2.getY()-p2.getY())*(p2.getX()-p1.getX()));
		
		double m=Math.signum((q2.getY()-q1.getY())*(p1.getX()-q2.getX())-(p1.getY()-q2.getY())*(q2.getX()-q1.getX()));
		double n=Math.signum((q2.getY()-q1.getY())*(p2.getX()-q2.getX())-(p2.getY()-q2.getY())*(q2.getX()-q1.getX()));
		if(a!=b&&m!=n)
			return true;
		else if(a==0&&b==0){
			double x1=Math.min(p1.getX(), p2.getX()),x2=Math.max(p1.getX(), p2.getX()),
					x3=Math.min(q1.getX(), q2.getX()),x4=Math.max(q1.getX(), q2.getX());
			double y1=Math.min(p1.getY(), p2.getY()),y2=Math.max(p1.getY(), p2.getY()),
					y3=Math.min(q1.getY(), q2.getY()),y4=Math.max(q1.getY(), q2.getY());
			if(((x3>x1&&x3<x2)||(x4>x1&&x4<x2)||(x1>x3&&x1<x4)||(x2>x3&&x2<x4))&&
					((y3>y1&&y3<y2)||(y4>y1&&y4<y2)||(y1>y3&&y1<y4)||(y2>y3&&y2<y4)))
				return true;
		}
		return false;
	}
	public boolean intersects(Vector2D v){
		if(p1.distanceTo(p2)==p1.distanceTo(v)+p2.distanceTo(v))
			return true;
		else
			return false;
	}
	public Vector2D closestPoint(Polygon p){
		double minDistance=Double.MAX_VALUE,distance=0;
		Vector2D minPoint=new Vector2D(0,0);
		/*for(Vector2D v:p.toPoints()){
			distance=p1.distanceTo(v)+p2.directionTo(v);
			if(distance<minDistance){
				minPoint=v;
				minDistance=distance;
			}
		}*/
		for(Line line:p.toLines()){
			Vector2D v1=line.closestPointToSegment(p1),v2=line.closestPointToSegment(p2);
			if(p1.distanceTo(v1)+p2.distanceTo(v1)<minDistance){
				minPoint=v1;
				minDistance=p1.distanceTo(v1)+p2.distanceTo(v1);
			}
			if(p1.distanceTo(v2)+p2.distanceTo(v2)<minDistance){
				minPoint=v2;
				minDistance=p1.distanceTo(v2)+p2.distanceTo(v2);
			}
		}
		return minPoint;
	}
	public Vector2D intersectionPoint(Line other){
		double x1=p1.getX(), y1=p1.getY(), x2=p2.getX(), y2=p2.getY(), x3=other.getPoint1().getX(), y3=other.getPoint1().getY(), x4=other.getPoint2().getX(), y4=other.getPoint2().getY();
		double d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		if (d == 0) return null;
		double xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
		double yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
		return new Vector2D(xi,yi);
		}
	
	public Vector2D closestPointToSegment( Vector2D v)
	  {
		double sx1=this.getPoint1().getX(), sy1=this.getPoint1().getY(), sx2=this.getPoint2().getX(), sy2=this.getPoint2().getY(), px=v.getX(), py=v.getY();
	    double xDelta = sx2 - sx1;
	    double yDelta = sy2 - sy1;

	    double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

	    final Vector2D closestPoint;
	    if (u < 0)
	    {
	      closestPoint = new Vector2D(sx1, sy1);
	    }
	    else if (u > 1)
	    {
	      closestPoint = new Vector2D(sx2, sy2);
	    }
	    else
	    {
	      closestPoint = new Vector2D((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
	    }
	    return closestPoint;
	  }
	
	public Vector2D centerPoint(){
		return new Vector2D((p1.x+p2.x)/2,(p1.y+p2.y)/2);
	}
	
}
