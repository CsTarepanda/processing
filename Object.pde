abstract class Obj{
  float xPos, yPos, dia;
  Figure figure;
  Move move;
  boolean delete;
  Obj(){}
  Obj(float xPos, float yPos, float dia, Figure figure, Move move){
    this.xPos = xPos;
    this.yPos = yPos;
    this.dia = dia;
    this.figure = figure;
    this.move = move;
  }

  float[] getMoveState(){
    return new float[]{this.xPos, this.yPos, this.dia};
  }

  void setMoveState(float[] moveState){
    this.xPos = moveState[0];
    this.yPos = moveState[1];
    this.dia = moveState[2];
  }

  void delete(){
    this.delete = true;
  }

  boolean catchStop(){
    if(this.move.vector2D.speed() <= 0.1)
      return true;
    else return false;
  }
  boolean catchStop(float stopSize){
    if(this.move.vector2D.speed() <= stopSize)
      return true;
    return false;
  }

  boolean catchDiaZero(){
    if(this.dia <= 0)
      return true;
    return false;
  }

  boolean catchCollision(Obj obj){
    if(dist(this.xPos, this.yPos, obj.xPos, obj.yPos) < obj.dia/2 + this.dia/2)
      return true;
    return false;
  }

  boolean catchOutOfScreen(){
    if(this.xPos < -this.dia || this.xPos > width + this.dia ||
        this.yPos < -this.dia || this.yPos > height + this.dia)
      return true;
    return false;
  }

  abstract boolean update();
}

void objUpdate(Object arrayList){
  ArrayList<Obj> objects = (ArrayList<Obj>)arrayList;
  for(int i = 0; i < objects.size(); i++)
    if(objects.get(i).update()) objects.remove(i--);
}

class BulletObj extends Obj{
  float damage;
  BulletObj(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move);
    this.damage = 1;
  }
  BulletObj(float xPos, float yPos, float dia, Figure figure, Move move, float damage){
    super(xPos, yPos, dia, figure, move);
    this.damage = damage;
  }
  boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    for(Obj obj: enemys)
      if(this.catchCollision(obj))
        for(int i = 0; i < 5; i++){
          fireSample(super.xPos, super.yPos, 30, new color[]{color(0, 100, 255, 100)});
          fireSample(super.xPos, super.yPos, 90, new color[]{color(255, 0, 0, 100)});
          super.delete();
        }
    super.setMoveState(super.move.action(super.getMoveState()));
    if(super.catchOutOfScreen())
      super.delete();
    return super.delete;
  }
}

class EnemyObj extends Obj{
  float hp, hpBar;
  EnemyObj(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move);
    this.hp = 5;
    this.hpBar = dia / this.hp;
  }
  EnemyObj(float xPos, float yPos, float dia, Figure figure, Move move, float hp){
    super(xPos, yPos, dia, figure, move);
    this.hp = hp;
    this.hpBar = dia / this.hp;
  }
  boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    this.drawLife();
    super.setMoveState(super.move.action(super.getMoveState()));
    if(super.catchOutOfScreen())
      super.delete();
    return super.delete;
  }
  void drawLife(){
    fill(255, 170);
    rect(super.xPos - super.dia/2, super.yPos + super.dia/2, super.dia, 10);
  }
}

class ParticleObj extends Obj{
  ParticleObj(){}
  ParticleObj(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move);
  }
  boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    super.setMoveState(super.move.action(super.getMoveState()));
    if(super.catchOutOfScreen())
      super.delete();
    return super.delete;
  }
}

abstract class BarObj extends Obj{
  BarObj(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move);
  }
}
