public class Vector2D{
	private double xSpeed, ySpeed, speed, angle;
	private void adjustAngle(){
		this.angle %= 360;
		if(this.angle < 0) this.angle += 360;
	}
	private void calcCartesian(){
		this.xSpeed = this.xVector();
		this.ySpeed = this.yVector();
	}

	private void calcPolar(){
		this.speed = Math.sqrt(this.xSpeed * this.xSpeed + this.ySpeed * this.ySpeed);
		this.angle = Math.toDegrees(Math.atan2(this.ySpeed, this.xSpeed));
		this.adjustAngle();
	}

	public void setRandom(){
		java.util.Random rdm = new java.util.Random();
		this.speed = rdm.nextInt(50);
		this.angle = rdm.nextInt(360);
	}

	public void setCartesian(double xSpeed, double ySpeed){
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.calcPolar();
	}
	public void setCartesian(double xOrigin, double yOrigin, double xSpeed, double ySpeed){
		this.xSpeed = xSpeed - xOrigin;
		this.ySpeed = ySpeed - yOrigin;
		this.calcPolar();
	}
	public float[] getCartesian(){
		return new float[]{(float)this.xSpeed, (float)this.ySpeed};
	}

	public void setPolar(double speed, double angle){
		angle %= 360;
		if(angle < 0) angle = 360 + angle;
		this.angle = angle;
		this.speed = speed;
		this.calcCartesian();
	}
	public float[] getPolar(){
		return new float[]{(float)this.speed, (float)this.angle};
	}

	public float xSpeed(double xSp){
		this.xSpeed = xSp;
		this.calcPolar();
		return (float)this.xSpeed;
	}
	public float xSpeed(){
		return (float)this.xSpeed;
	}
	public float ySpeed(double ySp){
		this.ySpeed = ySp;
		this.calcPolar();
		return (float)this.ySpeed;
	}
	public float ySpeed(){
		return (float)this.ySpeed;
	}
	public float speed(double sp){
		this.speed = sp;
		this.calcCartesian();
		return (float)this.speed;
	}
	public float speed(){
		return (float)this.speed;
	}
	public float angle(double ang){
		this.angle = ang;
		this.calcCartesian();
		return (float)this.angle;
	}
	public float angle(){
		return (float)this.angle;
	}
	public double xVector(){
		return this.speed * Math.cos(Math.toRadians(this.angle));
	}
	public double yVector(){
		return this.speed * Math.sin(Math.toRadians(this.angle));
	}

	public void add(Vector2D... vector2D){
		for(Vector2D v: vector2D){
			this.xSpeed += v.getCartesian()[0];
			this.ySpeed += v.getCartesian()[1];
		}
		this.calcPolar();
	}

	public void sub(Vector2D... vector2D){
		for(Vector2D v: vector2D){
			this.xSpeed -= v.getCartesian()[0];
			this.ySpeed -= v.getCartesian()[1];
		}
		this.calcPolar();
	}

	public void rotation(double angle){
		this.angle += angle;
		this.adjustAngle();
		this.calcCartesian();
	}

	public Vector2D copy(){
		Vector2D newVector = new Vector2D();
		newVector.setPolar(this.speed, this.angle);
		return newVector;
	}

	public Vector2D cross(){
		Vector2D newVector = new Vector2D();
		newVector.setPolar(this.speed, this.angle + 90);
		return newVector;
	}
}
