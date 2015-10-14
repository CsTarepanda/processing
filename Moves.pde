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

class EvaporateMove extends Move{
  EvaporateMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  EvaporateMove(float speed){
    super(speed, 0, 0, new StraightBound(-1, -1, -1, -1, 0));
  }
  float[] action(float[] moveState){
    moveState[2] -= super.vector2D.speed();
    return moveState;
  }
}

class LimitMove extends Move{
  int callTime = millis();
  float limitTime;
  LimitMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  LimitMove(float limitTime){
    super(0, 0, 0, new StraightBound(-1, -1, -1, -1, 0));
    this.limitTime = limitTime * 1000;
  }
  float[] action(float[] moveState){
    if(millis() - this.callTime > limitTime) moveState[2] = 0;
    return moveState;
  }
}

class FallMove extends Move{
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

class CustomMove extends Move{
  Move[] moves;
  CustomMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  CustomMove(Move... moves){
    super(0, 0, 0, new StraightBound(-1, -1, -1, -1, 0));
    for(Move m: moves)
      m.vector2D.speed(m.vector2D.speed() / moves.length);
    this.moves = moves;
  }
  float[] action(float[] moveState){
    for(Move m: moves){
      super.vector2D = m.vector2D;
      moveState = m.action(moveState);
    }
    return moveState;
  }
}
