class StraightBound extends Bound{
  StraightBound(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){
    super(leftEnd, topEnd, rightEnd, bottomEnd, coefficient);
  }

  float left(float angle){
    return 540 - angle;
  }
  float top(float angle){
    return 360 - angle;
  }
  float right(float angle){
    return 540 - angle;
  }
  float bottom(float angle){
    return 360 - angle;
  }
}

class RandomBound extends Bound{
  RandomBound(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){
    super(leftEnd, topEnd, rightEnd, bottomEnd, coefficient);
  }

  float left(float direction){
    return random(280, 440);
  }
  float top(float direction){
    return random(10, 170);
  }
  float right(float direction){
    return random(100, 260);
  }
  float bottom(float direction){
    return random(190, 350);
  }
}

/* class CalculationBound extends Bound{ */
/*   Calculation(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){ */
/*     super(leftEnd, topEnd, rightEnd, bottomEnd, coefficient); */
/*   } */
/* } */
