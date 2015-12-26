package Physics;

import java.awt.Graphics2D;
import java.awt.Polygon;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import game.Display;
import graphics.Render;
import graphics.Screen;

public class CarBody {
	public final static float maxForwardSpeed=200000;
	
	Body body;
	BodyDef bodyDef;
	FixtureDef fixtureDef;
	PolygonShape dynamicBox;
	
	Tire t1,t2,t3,t4;
	
	float acceleration=0,turn=0;
	
	int time=0;
	
	public CarBody(float x, float y){
		bodyDef = new BodyDef();
	    bodyDef.type = BodyType.DYNAMIC;
	    bodyDef.position.set(x, y);
	    body = Display.world.createBody(bodyDef);
	    dynamicBox = new PolygonShape();
	    dynamicBox.setAsBox(10f, 20f);
	    fixtureDef = new FixtureDef();
	    fixtureDef.shape = dynamicBox;
	    fixtureDef.density=1;
	    fixtureDef.friction=0.1f;
	    body.createFixture(fixtureDef);
	    t1=addNewTire(new Vec2(-10f,15f));
	    t2=addNewTire(new Vec2(10f,15f));
	    t3=addNewTire(new Vec2(-10f,-15f));
	    t4=addNewTire(new Vec2(10f,-15f));
	}

	public void render(Render r, int color){
		double minX=Double.MAX_VALUE,minY=Double.MAX_VALUE,maxX=Double.MIN_VALUE,maxY=Double.MIN_VALUE;
		PolygonShape box=new PolygonShape();
		Vec2 shift=new Vec2(Screen.centerX-Screen.width/2, Screen.centerY-Screen.height/2);
		Vec2 pos=body.getPosition();
		float angle= body.getAngle();
		box.setAsBox(10f, 20f, pos,angle);
		for(int b=0;b<dynamicBox.m_count;b++){
			Vec2 v= box.m_vertices[b].sub(shift);
			
			if(minX>v.x)
				minX=v.x;
			if(maxX<v.x)
				maxX=v.x;
			if(minY>v.y)
				minY=v.y;
			if(maxY<v.y)
				maxY=v.y;
		}
		
		minX=Math.max(minX, 0);
		maxX=Math.min(maxX, Display.WIDTH);
		minY=Math.max(minY, 0);
		maxY=Math.min(maxY, Display.HEIGHT);
		
		for(int x=(int) minX;x<maxX;x++){
			for(int y=(int) minY;y<maxY;y++){
				if(dynamicBox.testPoint(body.getTransform(), new Vec2(x,y).add(shift)))
					r.pixels[x+y*Display.WIDTH]=color;
			}
		}
		
	}
	
	public void updateBody(){
		t1.accelerate(acceleration);
		t2.accelerate(acceleration);
		t3.accelerate(acceleration);
		t4.accelerate(acceleration);
		
		t1.turn(turn);
		t2.turn(turn);
		t3.turn(turn);
		t4.turn(turn);
		
		float lockAngle = (float) Math.toRadians(20);
	      float turnSpeedPerSec = (float) Math.toRadians(160);//from lock to lock in 0.25 sec
	      float turnPerTimeStep = turnSpeedPerSec / 60.0f;
	      float desiredAngle = 0;
	          
	      	desiredAngle=lockAngle*turn;
	      
	      float angleNow = t1.joint.getJointAngle();
	      float angleToTurn = desiredAngle - angleNow;
	      angleToTurn = MathUtils.clamp( angleToTurn, -turnPerTimeStep, turnPerTimeStep );
	      float newAngle = angleNow + angleToTurn;
	      t1.joint.setLimits( newAngle, newAngle );
	      t2.joint.setLimits( newAngle, newAngle );
	      
	      t1.update();
	      t2.update();
	      t3.update();
	      t4.update();
	      
	      
	}

	public Tire addNewTire(Vec2 v){
		
		RevoluteJoint flJoint;
		 RevoluteJointDef jointDef=new RevoluteJointDef();
		  jointDef.bodyA = body;
		  jointDef.enableLimit = true;
		  jointDef.lowerAngle = 0;//with both these at zero...
		  jointDef.upperAngle = 0;//...the joint will not move
		  jointDef.localAnchorB.set(0,0);//joint anchor in tire is always center
		  
		  Tire tire = new Tire(Display.world);
		  jointDef.bodyB = tire.body;
		  jointDef.localAnchorA.set(v);
		  flJoint = (RevoluteJoint)Display.world.createJoint(jointDef);
		  tire.setJoint(flJoint);
		  return tire;
	}

	public double getSpeed() {
		return body.getLinearVelocity().length();
	}
	
	public void giveInformation(float acceleration,float turn){
		this.acceleration=acceleration;
		this.turn=turn;
	}
	
	public Body getBody(){
		return body;
	}
	
	public boolean testPoint(Vec2 loc){
		return dynamicBox.testPoint(body.getTransform(),loc);
	}
}
