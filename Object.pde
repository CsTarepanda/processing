abstract class Obj{
  float xPos, yPos, dia;
  float callTime = millis();
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

  float getTime(){
    return (millis() - this.callTime) / 1000.0;
  }

  void evaporate(float speed){
    this.dia -= speed;
  }

  boolean catchDelete(){
    return (catchStop() || catchDiaZero() || catchOutOfScreen());
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
    if(obj == this) return false;
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
  boolean myBullet = false;
  BulletObj(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move);
    this.damage = 5;
  }
  BulletObj(){}
  BulletObj(float xPos, float yPos, float dia, Figure figure, Move move, float damage){
    super(xPos, yPos, dia, figure, move);
    this.damage = damage;
  }
  boolean update(){
    if(this.playerHit()){
      this.myBullet = true;
    }
    this.figureUpdate();
    if(this.myBullet){
      super.figure.toMyBullet();
      for(EnemyObj obj: enemys)
        if(this.catchCollision(obj)){
          obj.damage(this.damage);
          for(int i = 0; i < 5; i++)
            fireSample(super.xPos, super.yPos, this.dia, figure.col);
          super.delete();
        }
    }
    super.setMoveState(super.move.action(super.getMoveState()));
    if(super.catchDelete())
      super.delete();
    return super.delete;
  }

  private boolean playerHit(){
    for(BarObj player: bars)
      if(player.catchHit(this)){
        this.adjustBound(player);
        return true;
      }
    return false;
  }

  private void normalBound(){
    super.move.vector2D.yReflect();
  }

  private void adjustBound(Obj player){
    super.move.vector2D.angle(degrees(atan2(super.yPos - player.yPos, super.xPos - player.xPos)));
  }

  private void randomBound(){
    if(super.move.vector2D.ySpeed() < 0)
      super.move.vector2D.angle(random(210, 330));
    else
      super.move.vector2D.angle(random(30, 150));
  }

  void figureUpdate(){
    pushMatrix();
    translate(super.xPos, super.yPos);
    rotate(atan2(super.move.vector2D.ySpeed(), super.move.vector2D.xSpeed()) + radians(90));
    super.figure.update(0, 0, super.dia);
    popMatrix();
  }
}

class EnemyObj extends Obj{
  float hp, hpBar, maxHp;
  EnemyObj(){}
  EnemyObj(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move);
    this.hp = 5;
    this.hpBar = dia / this.hp;
    this.maxHp = hp;
  }
  EnemyObj(float xPos, float yPos, float dia, Figure figure, Move move, float hp){
    super(xPos, yPos, dia, figure, move);
    this.hp = hp;
    this.hpBar = dia / this.hp;
    this.maxHp = hp;
  }
  boolean update(){
    /* for(Obj obj: enemys){ */
    /*   if(catchCollision(obj)) super.move.vector2D.rotation(180); */
    /* } */
    super.figure.update(super.xPos, super.yPos, super.dia);
    this.drawLife();
    super.setMoveState(super.move.action(super.getMoveState()));
    if(super.catchOutOfScreen())
      super.delete();
    return super.delete;
  }
  void drawLife(){
    fill(255, 170);
    rect(super.xPos - super.dia/2, super.yPos + super.dia/2, this.hpBar * this.hp, 10);
  }
  void damage(float damage){
    this.hp -= damage;
    if(this.hp <= 0){
      this.hp = 0;
      inf.addScore(this.maxHp);
      for(int i = 0; i < 5; i++)
        fireSample(super.xPos, super.yPos, this.dia, figure.col);
      super.delete();
    }
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

class BarObj extends Obj{
  BarObj(){}
  BarObj(float xPos, float yPos, float dia, BarFigure figure){
    super(xPos, yPos, dia, figure, stopMove());
  }
  BarObj(float xPos, float yPos, float dia, BarFigure figure, Move move){
    super(xPos, yPos, dia, figure, move);
  }
  boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    return super.delete;
  }

  void leftDisplace(float speed){
    super.xPos -= speed;
    if(super.xPos < dia/2) super.xPos = dia/2;
  }

  void rightDisplace(float speed){
    super.xPos += speed;
    if(width - super.dia/2 < super.xPos) super.xPos = width - super.dia/2;
  }

  boolean catchHit(Obj obj){
    if(obj.yPos + obj.dia/2 + obj.move.vector2D.ySpeed() > super.yPos - 5 && 
        obj.yPos - obj.dia/2 + obj.move.vector2D.ySpeed() < super.yPos + 5 && 
        obj.xPos > super.xPos - super.dia/2 && 
        obj.xPos < super.xPos + super.dia/2){
      if(0 <= obj.move.vector2D.ySpeed()) obj.yPos = super.yPos - 10;
      else obj.yPos = super.yPos + 10;
      return true;
    }
    return false;
  }
}
