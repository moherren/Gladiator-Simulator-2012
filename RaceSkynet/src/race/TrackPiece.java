package race;

import org.jbox2d.common.Vec2;

public class TrackPiece {
	Vec2[] position;
	float length;
	
	public static class normal extends TrackPiece{
		
		public normal(Vec2 v,float length){
			position=new Vec2[1];
			position[0]=v;
			this.length=length;
		}
		
		public Vec2[] roadSides(Vec2 front,Vec2 back,Track track){
			double dir=Math.atan2( front.y - back.y,front.x - back.x);
			Vec2[] sides=new Vec2[4];
			float sideLength=length+(10*2);
			sides[0]=new Vec2((float)(position[0].x+Math.cos(dir+Math.PI/2)*length/2),(float)(position[0].y+Math.sin(dir+Math.PI/2)*length/2));
			sides[1]=new Vec2((float)(position[0].x+Math.cos(dir+Math.PI/2)*sideLength/2),(float)(position[0].y+Math.sin(dir+Math.PI/2)*sideLength/2));
			sides[2]=new Vec2((float)(position[0].x+Math.cos(dir-Math.PI/2)*length/2),(float)(position[0].y+Math.sin(dir-Math.PI/2)*length/2));
			sides[3]=new Vec2((float)(position[0].x+Math.cos(dir-Math.PI/2)*sideLength/2),(float)(position[0].y+Math.sin(dir-Math.PI/2)*sideLength/2));
			return sides;
		}
	
	}
	
	public static class end extends TrackPiece{
		
		public end(Vec2 v,float length){
			position=new Vec2[2];
			position[0]=v.add(new Vec2(0,-70));
			position[1]=v.add(new Vec2(0,70));
			this.length=length;
		}
		
		public Vec2[] roadSides(Vec2 front,Vec2 back,Track track){
			double dir=-3*Math.PI/2;
			Vec2[] sides=new Vec2[8];
			float sideLength=length+(10*2);
			sides[0]=new Vec2((float)(position[0].x+Math.cos(dir+Math.PI/2)*length/2),(float)(position[0].y+Math.sin(dir+Math.PI/2)*length/2));
			sides[1]=new Vec2((float)(position[0].x+Math.cos(dir+Math.PI/2)*sideLength/2),(float)(position[0].y+Math.sin(dir+Math.PI/2)*sideLength/2));
			sides[2]=new Vec2((float)(position[0].x+Math.cos(dir-Math.PI/2)*length/2),(float)(position[0].y+Math.sin(dir-Math.PI/2)*length/2));
			sides[3]=new Vec2((float)(position[0].x+Math.cos(dir-Math.PI/2)*sideLength/2),(float)(position[0].y+Math.sin(dir-Math.PI/2)*sideLength/2));
			sides[4]=new Vec2((float)(position[1].x+Math.cos(dir+Math.PI/2)*length/2),(float)(position[1].y+Math.sin(dir+Math.PI/2)*length/2));
			sides[5]=new Vec2((float)(position[1].x+Math.cos(dir+Math.PI/2)*sideLength/2),(float)(position[1].y+Math.sin(dir+Math.PI/2)*sideLength/2));
			sides[6]=new Vec2((float)(position[1].x+Math.cos(dir-Math.PI/2)*length/2),(float)(position[1].y+Math.sin(dir-Math.PI/2)*length/2));
			sides[7]=new Vec2((float)(position[1].x+Math.cos(dir-Math.PI/2)*sideLength/2),(float)(position[1].y+Math.sin(dir-Math.PI/2)*sideLength/2));
			
			track.addStartPoint(position[1].add(new Vec2(-length/4,-10)));
			track.addStartPoint(position[1].add(new Vec2(length/4,-10)));
			track.addStartPoint(position[1].add(new Vec2(-length/4,-60)));
			track.addStartPoint(position[1].add(new Vec2(length/4,-60)));
			track.addStartPoint(position[1].add(new Vec2(-length/4,-110)));
			track.addStartPoint(position[1].add(new Vec2(length/4,-110)));
			
			return sides;
		}
		
	}
	
	public Vec2[] roadSides(Vec2 front,Vec2 back,Track track){
			return null;
	}

	public Vec2[] getPosition() {
		return position;
	}
	
}