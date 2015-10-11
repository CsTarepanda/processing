abstract class Action{
  /* float speed, angle; */
  Vector2D vector2D;
}

abstract class Move extends Action{
  float factor;
  Bound bound;
  Move(float speed, float angle, float factor, Bound bound){
    /* super.speed = speed; */
    /* super.angle = angle; */
    super.vector2D = new Vector2D();
    super.vector2D.setPolar(speed, angle);
    this.factor = factor;
    this.bound = bound;
    this.bound.vector2D = this.vector2D;
  }

  Vector2D getVector2D(){
    return this.vector2D;
  }

  void boundAction(float[] moveState){
    this.vector2D = this.bound.action(moveState);
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
  abstract float left(float angle);
  abstract float top(float angle);
  abstract float right(float angle);
  abstract float bottom(float angle);
  Vector2D action(float[] moveState){
    float speed = (float)super.vector2D.speed();
    float angle = (float)super.vector2D.angle();
    moveState[2] /= 2;
    if(90 <= angle && angle < 270){
      if(leftEnd != -1 && moveState[0] <= leftEnd + moveState[2]){
        vector2D.setPolar(speed * coefficient, this.left(angle));
      }
    }
    if(180 <= angle && angle < 360){
      if(topEnd != -1 && moveState[1] <= topEnd + moveState[2]){
        vector2D.setPolar(speed * coefficient, this.top(angle));
      }
    }
    if((270 <= angle && angle < 360) || (0 <= angle && angle < 90)){
      if(rightEnd != -1 && rightEnd - moveState[2] <= moveState[0]){
        vector2D.setPolar(speed * coefficient, this.right(angle));
      }
    }
    if(0 <= angle && angle < 180){
      if(bottomEnd != -1 && bottomEnd - moveState[2] <= moveState[1]){
        vector2D.setPolar(speed * coefficient, this.bottom(angle));
      }
    }
    moveState[2] *= 2;
    return vector2D;
  }
}
