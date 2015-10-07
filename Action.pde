abstract class Action{
  float speed, angle;
}

abstract class Move extends Action{
  float factor;
  Move(float speed, float angle, float factor){
    super.speed = speed;
    super.angle = angle;
    this.factor = factor;
  }

  float[] getDirection(){
    return new float[]{this.speed, this.angle};
  }

  void setDirection(float[] direction){
    super.speed = direction[0];
    super.angle = direction[1];
  }

  abstract float[] action(float[] moveState);
}

abstract class Bound extends Action{
  float leftEnd, topEnd, rightEnd, bottomEnd;
  float coefficient;
  Bound(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){
    this.leftEnd = leftEnd;
    this.topEnd = topEnd;
    this.rightEnd = rightEnd;
    this.bottomEnd = bottomEnd;
    this.coefficient = coefficient;
  }
  abstract float left(float direction);
  abstract float top(float direction);
  abstract float right(float direction);
  abstract float bottom(float direction);
  float[] action(float[] moveState, float[] direction){
    direction[1] = adjustAngle(direction[1]);
    moveState[2] /= 2;
    if(90 <= direction[1] && direction[1] < 270){
      if(leftEnd != -1 && moveState[0] <= leftEnd + moveState[2]){
        direction[1] = this.left(direction[1]);
        direction[0] *= coefficient;
      }
    }
    if(180 <= direction[1] && direction[1] < 360){
      if(topEnd != -1 && moveState[1] <= topEnd + moveState[2]){
        direction[1] = this.top(direction[1]);
        direction[0] *= coefficient;
      }
    }
    if((270 <= direction[1] && direction[1] < 360) || (0 <= direction[1] && direction[1] < 90)){
      if(rightEnd != -1 && rightEnd - moveState[2] <= moveState[0]){
        direction[1] = this.right(direction[1]);
        direction[0] *= coefficient;
      }
    }
    if(0 <= direction[1] && direction[1] < 180){
      if(bottomEnd != -1 && bottomEnd - moveState[2] <= moveState[1]){
        direction[1] = this.bottom(direction[1]);
        direction[0] *= coefficient;
      }
    }
    direction[1] = adjustAngle(direction[1]);
    return direction;
  }
}
