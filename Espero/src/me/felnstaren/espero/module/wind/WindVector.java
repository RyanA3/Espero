package me.felnstaren.espero.module.wind;

public class WindVector {
	
	public float radian;
	public float speed;
	
	public float x;
	public float y;
	
	public WindVector(float radian, float speed) {
		this.radian = radian;
		this.speed = speed;
	}
	
	private void cartesify() {
		y = (float) (Math.sin(radian) * speed);
		x = (float) (Math.cos(radian) * speed);
	}

	public void rotate(float radian) {
		this.radian += radian;
		if(radian > 6.2832f) radian = 0;
		else if(radian < 0) radian = 6.2831f;
		cartesify();
	}
	
	public void accelerate(float speed) {
		this.speed += speed;
		cartesify();
	}
	
	public void setRotation(float radian) {
		this.radian = radian;
		cartesify();
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
