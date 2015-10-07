abstract class Obj{
  float xPos, yPos, dia;
  Figure figure;
  Move move;
  Bound bound;
  boolean delete;
  Obj(float xPos, float yPos, float dia, Figure figure, Move move, Bound bound){
    this.xPos = xPos;
    this.yPos = yPos;
    this.dia = dia;
    this.figure = figure;
    this.move = move;
    this.bound = bound;
  }

  float[] getDirection(){
    return move.getDirection();
  }

  void setDirection(float[] direction){
    move.setDirection(direction);
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

  abstract boolean update();
}

void objUpdate(Object arrayList){
  ArrayList<Obj> objects = (ArrayList<Obj>)arrayList;
  for(int i = 0; i < objects.size(); i++)
    if(objects.get(i).update()) objects.remove(i--);
}

abstract class BulletObj extends Obj{
  BulletObj(float xPos, float yPos, float dia, Figure figure, Move move, Bound bound){
    super(xPos, yPos, dia, figure, move, bound);
  }
}

abstract class EnemyObj extends Obj{
  EnemyObj(float xPos, float yPos, float dia, Figure figure, Move move, Bound bound){
    super(xPos, yPos, dia, figure, move, bound);
  }
}

abstract class ParticleObj extends Obj{
  ParticleObj(float xPos, float yPos, float dia, Figure figure, Move move, Bound bound){
    super(xPos, yPos, dia, figure, move, bound);
  }
}

abstract class BarObj extends Obj{
  BarObj(float xPos, float yPos, float dia, Figure figure, Move move, Bound bound){
    super(xPos, yPos, dia, figure, move, bound);
  }
}
