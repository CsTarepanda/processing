class StraightMove extends Move{
  StraightMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  float[] action(float[] moveState){
    moveState[0] += super.vector2D.xVector();
    moveState[1] += super.vector2D.yVector();
    super.vector2D.speed(super.vector2D.speed() * (1 - super.factor / 10));
    super.boundAction(moveState);
    return moveState;
  }
}

class CurveMove extends Move{
  CurveMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  float[] action(float[] moveState){
    moveState[0] += super.vector2D.xVector();
    moveState[1] += super.vector2D.yVector();
    super.vector2D.rotation(super.factor);
    super.boundAction(moveState);
    return moveState;
  }
}

class WaveMove extends Move{
  float rotateAngle = 0;
  float radius = 5;
  WaveMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  WaveMove(float speed, float angle, float factor, Bound bound, float radius){
    super(speed, angle, factor, bound);
    this.radius = radius;
  }
  float[] action(float[] moveState){
    adjustAngle(this.rotateAngle);
    moveState[0] += super.vector2D.xVector();
    moveState[1] += super.vector2D.yVector();
    this.rotateAngle += super.factor;
    super.vector2D.rotation(sin(radians(this.rotateAngle)) * this.radius);
    super.boundAction(moveState);
    return moveState;
  }
}

class FallMove extends Move{
  float xSpeed, ySpeed;
  FallMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  float[] action(float[] moveState){
    moveState[0] += super.vector2D.xVector();
    moveState[1] += super.vector2D.yVector();
    super.vector2D.ySpeed(super.vector2D.ySpeed() + super.factor);
    super.boundAction(moveState);
    return moveState;
  }
}

class RiseMove extends Move{
  float xSpeed, ySpeed;
  RiseMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  float[] action(float[] moveState){
    moveState[0] += super.vector2D.xVector();
    moveState[1] += super.vector2D.yVector();
    super.vector2D.ySpeed(super.vector2D.ySpeed() - super.factor);
    super.boundAction(moveState);
    return moveState;
  }
}

