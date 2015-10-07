class StraightMove extends Move{
  StraightMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  float[] action(float[] moveState){
    moveState[0] += super.speed * cos(radians(super.angle));
    moveState[1] += super.speed * sin(radians(super.angle));
    super.speed *= 1 - super.factor / 10;
    return moveState;
  }
}

class CurveMove extends Move{
  CurveMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  float[] action(float[] moveState){
    moveState[0] += super.speed * cos(radians(super.angle));
    moveState[1] += super.speed * sin(radians(super.angle));
    super.angle += super.factor;
    return moveState;
  }
}

class WaveMove extends Move{
  float rotateAngle = 0;
  float radius = 5;
  WaveMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  WaveMove(float speed, float angle, float factor, float radius){
    super(speed, angle, factor);
    this.radius = radius;
  }
  float[] action(float[] moveState){
    adjustAngle(this.rotateAngle);
    moveState[0] += super.speed * cos(radians(super.angle));
    moveState[1] += super.speed * sin(radians(super.angle));
    this.rotateAngle += super.factor;
    super.angle += sin(radians(this.rotateAngle)) * this.radius;
    return moveState;
  }
}

class FallMove extends Move{
  float xSpeed, ySpeed;
  FallMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  float[] action(float[] moveState){
    this.xSpeed = super.speed * cos(radians(super.angle));
    this.ySpeed = super.speed * sin(radians(super.angle));
    moveState[0] += this.xSpeed;
    moveState[1] += this.ySpeed;
    this.ySpeed += super.factor;
    super.speed = sqrt(sq(this.xSpeed) + sq(this.ySpeed));
    super.angle = degrees(atan2(this.ySpeed, this.xSpeed));
    return moveState;
  }
}

class RiseMove extends Move{
  float xSpeed, ySpeed;
  RiseMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  float[] action(float[] moveState){
    this.xSpeed = super.speed * cos(radians(super.angle));
    this.ySpeed = super.speed * sin(radians(super.angle));
    moveState[0] += this.xSpeed;
    moveState[1] += this.ySpeed;
    this.ySpeed -= super.factor;
    super.speed = sqrt(sq(this.xSpeed) + sq(this.ySpeed));
    super.angle = degrees(atan2(this.ySpeed, this.xSpeed));
    return moveState;
  }
}

