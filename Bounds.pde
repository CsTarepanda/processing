class StraightBound extends Bound{
  StraightBound(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){
    super(leftEnd, topEnd, rightEnd, bottomEnd, coefficient);
  }

  float left(float direction){
    return 540 - direction;
  }
  float top(float direction){
    return 360 - direction;
  }
  float right(float direction){
    return 540 - direction;
  }
  float bottom(float direction){
    return 360 - direction;
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
