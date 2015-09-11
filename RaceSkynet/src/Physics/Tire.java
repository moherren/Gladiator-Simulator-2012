package Physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;

public class Tire {
	public Body body;
	float acceleration=0,turn=0;
	PolygonShape polygonShape;
	RevoluteJoint joint;
	public Tire(World world){
		BodyDef bodyDef=new BodyDef();
		bodyDef.type=BodyType.DYNAMIC;
		bodyDef.position=new Vec2(50,50);
		body=world.createBody(bodyDef);
		polygonShape=new PolygonShape();
		polygonShape.setAsBox(1f, 1.5f);
		body.createFixture(polygonShape, 1);
		body.applyForce(new Vec2(0,100) ,body.getWorldCenter());
	}
	public void update(){
	updateFriction();
	updateMovement();
	}
	public Vec2 getLateralVelocity(){
		Vec2 currentRightNormal=body.getWorldVector(new Vec2(1,0));
		return currentRightNormal.mul(Vec2.dot(currentRightNormal,body.getLinearVelocity()));
	}
	public Vec2 getForwardVelocity(){
		Vec2 currentRightNormal=body.getWorldVector(new Vec2(0,1));
		return currentRightNormal.mul(Vec2.dot(currentRightNormal,body.getLinearVelocity()));
	}
	public void updateFriction(){
		Vec2 impulse=getLateralVelocity().mul(-body.getMass());
		float maxLateralImpulse=5.5f;
		if ( impulse.length() > maxLateralImpulse )
            impulse.mul(maxLateralImpulse / impulse.length());
		body.applyLinearImpulse(impulse, body.getWorldCenter());
		body.applyAngularImpulse(0.1f*body.getInertia()*-body.getAngularVelocity());
		Vec2 currentForwardNormal = getForwardVelocity();
		float currentForwardSpeed = currentForwardNormal.normalize();
		float dragForceMagnitude = -2 * currentForwardSpeed;
		body.applyForce( currentForwardNormal.mul(dragForceMagnitude), body.getWorldCenter() );
	}
	public void updateMovement(){
		float maxForwardSpeed=200000;
		float maxBackwardSpeed=-5000;
		float maxDriveForce=5000;
		float desiredSpeed = 0;
		float desiredTorque=0;
			if(acceleration>0)
				desiredSpeed=maxForwardSpeed*acceleration;
			else
				desiredSpeed=maxBackwardSpeed*acceleration;
			
			desiredTorque=15*turn;
	        body.applyTorque(desiredTorque);
	      	      
	      //find current speed in forward direction
	      Vec2 currentForwardNormal = body.getWorldVector(new Vec2(0,1) );
	      float currentSpeed = Vec2.dot( getForwardVelocity(), currentForwardNormal );
	      
	      //apply necessary force
	      float force = 0;
	      if ( desiredSpeed > currentSpeed )
	          force = maxDriveForce;
	      else if ( desiredSpeed < currentSpeed )
	          force = -maxDriveForce;
	      else
	          return;
	      body.applyForce( currentForwardNormal.mul(force), body.getWorldCenter());
	}
	public void accelerate(float acceleration){
		this.acceleration=acceleration;
	}
	public void turn(float turn){
		this.turn=turn;
	}
	public void setJoint(RevoluteJoint r){
		joint=r;
	}
}
