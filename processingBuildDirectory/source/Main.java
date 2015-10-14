import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {


ArrayList<BulletObj> bullets = new ArrayList<BulletObj>();
ArrayList<EnemyObj> enemys = new ArrayList<EnemyObj>();
ArrayList<ParticleObj> backParticles = new ArrayList<ParticleObj>();
ArrayList<ParticleObj> frontParticles = new ArrayList<ParticleObj>();
ArrayList<BarObj> bars = new ArrayList<BarObj>();
Information inf;
int[] myBulletCol = {color(255, 200, 100, 170), color(255, 200, 255, 170), color(255)};

Counter rotateCount = new Counter(0, 360);
public void setup(){
  size(500, 750);
  textAlign(CORNER, CORNER);
  textSize(20);
  inf = new Information(50);
  bars.add(new BarObj(width/2, height - 20, 70, 
        new Simple(new int[]{color(0, 170, 255, 170)})));
  bars.add(new BarObj(width/2, height - 40, 70, 
        new Simple(new int[]{color(0, 170, 255, 170)})));
}

public void mousePressed(){
  bullets.add(new BulletObj(
        mouseX, mouseY,
        30,
        new Ellipse(new int[]{color(255, 100, 200, 170)}),
        new FallMove(3, 90, 0.3f,
          new StraightBound(0, -1, width, -1, 1.0f)
          )
        ));
}

boolean a, d, h, l;
public void keyPressed(){
  /* inf.damage(5); */
  if(!a && key == 'a') a = true;
  else if(!d && key == 'd') d = true;
  else if(!h && key == 'h') h = true;
  else if(!l && key == 'l') l = true;
  else{
    if(key == 'r'){
      inf = new Information(500);
    }else{
      frontParticles.add(new ParticleObj(
            mouseX, mouseY,
            50, 
            new Ellipse(new int[]{color(255, 170)}),
            new FallMove(
              5, random(360), 0.1f,
              new StraightBound(
                -1, 0, -1, height, 1.0f
                ))
            ));
      enemys.add(new EnemyObj(
            mouseX, mouseY,
            70,
            new Ellipse(new int[]{color(255, 0, 0, 170)}),
            new FallMove(
              5, random(360), 0.1f,
              new StraightBound(
                0, 0, width, height, 0.8f
                ))
            ));
    }
  }
}

public void keyReleased(){
  switch(key){
    case 'a':
      a = false;
      break;
    case 'd':
      d = false;
      break;
    case 'h':
      h = false;
      break;
    case 'l':
      l = false;
      break;
    default:
      break;
  }
}

public void draw(){
  background(0);
  objUpdate(backParticles);
  objUpdate(bullets);
  objUpdate(enemys);

  // draw area  start---------- 
  if(a) bars.get(0).leftDisplace(3);
  if(d) bars.get(0).rightDisplace(3);
  if(h) bars.get(1).leftDisplace(3);
  if(l) bars.get(1).rightDisplace(3);

  /* bullets.add(new JointBullet( */
  /*       mouseX, mouseY, */
  /*       10, */
  /*       new SimpleBullet(new color[]{color(100, 255, 255, 200)}), */
  /*       new FallMove( */
  /*         10, rotateCount.countUp(80), 0.3, */
  /*         new StraightBound( */
  /*           0, 0, width, height, 0.8 */
  /*           ) */
  /*         ) */
  /*       )); */

  /* for(Obj obj: bullets) */
  /*   if(obj.getTime() > 3) obj.delete(); */
  /* if(enemys.size() < 5) */
  /*   enemys.add(new EnemyObj( */
  /*         width/2, height/2, */
  /*         70, */
  /*         new Ellipse(new color[]{color(255, 0, 200, 170)}), */
  /*         new FallMove( */
  /*           5, random(360), 0.1, */
  /*           new StraightBound( */
  /*             0, 0, width, height, 0.8 */
  /*             )), */
  /*         10 */
  /*         )); */
  if(mousePressed){
    bullets.add(new BulletObj(
          mouseX, mouseY, 30, 
          new SimpleBullet(new int[]{color(255, 170)}),
          new CustomMove(
            new EvaporateMove(1),
            new StraightMove(5, 90, 0.0f,
              new StraightBound(0, 0, width, height, 1.0f)),
            new FallMove(5, random(30, 50), 0.3f,
              new StraightBound(0, 0, width, height, 1.0f))
            )
          ));
    evaporationSample(mouseX, mouseY, 30, new int[]{color(255, 100, 0, 100)});
    evaporationSample(mouseX, mouseY, 90, new int[]{color(255, 0, 0, 100)});
  }
  // draw area  end------------

  objUpdate(frontParticles);
  objUpdate(bars);
  /* inf.addScore(5); */
  inf.update();
}

public float adjustAngle(float angle){
    angle %= 360;
    if(angle < 0)
      angle = 360 + angle;
    return angle;
}

public Move stopMove(){
  return new StraightMove(
      0, 0, 0.0f,
      new StraightBound(
        -1, -1, -1, -1, 0.0f
        )
      );
}
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

  public Vector2D getVector2D(){
    return this.vector2D;
  }

  public void boundAction(float[] moveState){
    this.vector2D = this.bound.action(moveState);
  }

  public abstract float[] action(float[] moveState);
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
  public abstract float left(float angle);
  public abstract float top(float angle);
  public abstract float right(float angle);
  public abstract float bottom(float angle);
  public Vector2D action(float[] moveState){
    float speed = (float)super.vector2D.speed();
    float angle = (float)super.vector2D.angle();
    moveState[2] /= 2;
    if(90 <= angle && angle < 270){
      if(leftEnd != -1 && moveState[0] <= leftEnd + moveState[2] - super.vector2D.xSpeed()){
        moveState[0] = moveState[2];
        vector2D.setPolar(speed * coefficient, this.left(angle));
      }
    }
    if(180 <= angle && angle < 360){
      if(topEnd != -1 && moveState[1] <= topEnd + moveState[2] - super.vector2D.ySpeed()){
        moveState[1] = moveState[2];
        vector2D.setPolar(speed * coefficient, this.top(angle));
      }
    }
    if((270 <= angle && angle < 360) || (0 <= angle && angle < 90)){
      if(rightEnd != -1 && rightEnd - moveState[2] - super.vector2D.xSpeed() <= moveState[0]){
        moveState[0] = width - moveState[2];
        vector2D.setPolar(speed * coefficient, this.right(angle));
      }
    }
    if(0 <= angle && angle < 180){
      if(bottomEnd != -1 && bottomEnd - moveState[2] - super.vector2D.ySpeed() <= moveState[1]){
        moveState[1] = height - moveState[2];
        vector2D.setPolar(speed * coefficient, this.bottom(angle));
      }
    }
    moveState[2] *= 2;
    return vector2D;
  }
}

class Simple extends BarFigure{
  Simple(int[] col){
    super(col);
  }
  public void update(float xPos, float yPos, float dia){
    noStroke();
    fill(col[0]);
    rect(xPos - dia/2, yPos - 5, dia, 10);
  }
}
class StraightBound extends Bound{
  StraightBound(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){
    super(leftEnd, topEnd, rightEnd, bottomEnd, coefficient);
  }

  public float left(float angle){
    return 540 - angle;
  }
  public float top(float angle){
    return 360 - angle;
  }
  public float right(float angle){
    return 540 - angle;
  }
  public float bottom(float angle){
    return 360 - angle;
  }
}

class RandomBound extends Bound{
  RandomBound(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){
    super(leftEnd, topEnd, rightEnd, bottomEnd, coefficient);
  }

  public float left(float direction){
    return random(280, 440);
  }
  public float top(float direction){
    return random(10, 170);
  }
  public float right(float direction){
    return random(100, 260);
  }
  public float bottom(float direction){
    return random(190, 350);
  }
}

/* class CalculationBound extends Bound{ */
/*   Calculation(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){ */
/*     super(leftEnd, topEnd, rightEnd, bottomEnd, coefficient); */
/*   } */
/* } */
class JointBullet extends BulletObj{
  boolean through = true;
  JointBullet(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move, 0);
  }
  JointBullet(float xPos, float yPos, float dia, Figure figure, Move move, float damage){
    super(xPos, yPos, dia, figure, move, damage);
  }

  public boolean update(){
    super.playerHit();
    super.figureUpdate();
    if(!this.through) 
      for(EnemyObj obj: enemys)
        if(this.catchCollision(obj)){
          obj.damage(this.damage);
          for(int i = 0; i < 5; i++)
            fireSample(super.xPos, super.yPos, this.dia, figure.col);
          super.delete();
        }
    super.setMoveState(super.move.action(super.getMoveState()));
    if(super.catchDiaZero())
      super.delete();
    if(super.catchOutOfScreen())
      super.delete();
    if(super.getTime() > 5) this.launch();
    return super.delete;
  }

  public void throughSwitch(){
    if(this.through) this.through = false;
    else this.through = true;
  }

  public void launch(){
    for(int i = 0; i < 10; i++)
      bullets.add(new BulletObj(
            super.xPos, super.yPos,
            10,
            new SimpleBullet(new int[]{color(255, 183, 80, 200)}),
            new FallMove(
              10, 36 * i, 0.3f,
              new StraightBound(
                0, 0, width, height, 0.8f
                )
              )
            ));
    super.delete = true;
  }
}
class SimpleBullet extends Figure{
  SimpleBullet(int[] col){
    super(col);
  }

  public void update(float xPos, float yPos, float dia){
    fill(col[0]);
    ellipse(xPos, yPos, dia, dia * 2.0f);
  }
}

abstract class Figure{
  float dia;
  int[] col;
  int[] myBulletCol = {color(255, 200, 100, 170), color(255, 200, 255, 170), color(255)};
  Figure(int[] col){
    this.dia = dia;
    this.col = col;
  }
  public abstract void update(float xPos, float yPos, float dia);

  public void toMyBullet(){
    this.col = this.myBulletCol;
  }

  public void changeMyBullet(int[] col){
    this.myBulletCol = col;
  }
}

abstract class BulletFigure extends Figure{
  BulletFigure(int[] col){
    super(col);
  }
}

abstract class EnemyFigure extends Figure{
  EnemyFigure(int[] col){
    super(col);
  }
}

abstract class ParticleFigure extends Figure{
  ParticleFigure(int[] col){
    super(col);
  }
}

abstract class BarFigure extends Figure{
  BarFigure(int[] col){
    super(col);
  }
}
class Information{
  private int maxHp;
  private float hp;
  private float hpBar;
  private int score = 0;
  private boolean gameEnd = false;
  Information(float hp){
    this.hp = hp;
    this.maxHp = (int)hp;
    this.hpBar = (width - 20) / hp;
  }

  public void update(){
    textAlign(CORNER, CORNER);
    textSize(20);
    noStroke();
    fill(255, 255, 255, 100);
    if(this.hp > 0){
      drawHp();
      drawScore();
    }else{
      drawEnd();
    }
  }

  private void drawScore(){
    text(this.score, 20, 57);
  }

  private void drawHp(){
    rect(10, 10, this.hpBar * this.hp, 20);
    text((int)hp +" / "+ this.maxHp, 20, 27);
  }

  private void drawEnd(){
    this.gameEnd = true;
    text((int)this.hp +" / "+ this.maxHp, 20, 27);
    textAlign(CENTER, CENTER);
    textSize(50);
    text("end", width/2, height/2);
  }

  public void damage(float damage){
    this.hp -= damage;
    if(this.hp <= 0) this.hp = 0;
  }

  public void addScore(float score){
    if(!this.gameEnd) this.score += score;
  }
}
class StraightMove extends Move{
  StraightMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  public float[] action(float[] moveState){
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
  public float[] action(float[] moveState){
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
  public float[] action(float[] moveState){
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
  public float[] action(float[] moveState){
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
  public float[] action(float[] moveState){
    if(millis() - this.callTime > limitTime) moveState[2] = 0;
    return moveState;
  }
}

class FallMove extends Move{
  FallMove(float speed, float angle, float factor, Bound bound){
    super(speed, angle, factor, bound);
  }
  public float[] action(float[] moveState){
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
  public float[] action(float[] moveState){
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
  public float[] action(float[] moveState){
    for(Move m: moves){
      super.vector2D = m.vector2D;
      moveState = m.action(moveState);
    }
    return moveState;
  }
}
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

  public float[] getMoveState(){
    return new float[]{this.xPos, this.yPos, this.dia};
  }

  public void setMoveState(float[] moveState){
    this.xPos = moveState[0];
    this.yPos = moveState[1];
    this.dia = moveState[2];
  }

  public void delete(){
    this.delete = true;
  }

  public float getTime(){
    return (millis() - this.callTime) / 1000.0f;
  }

  public void evaporate(float speed){
    this.dia -= speed;
  }

  public boolean catchDelete(){
    return (catchStop() || catchDiaZero() || catchOutOfScreen());
  }

  public boolean catchStop(){
    if(this.move.vector2D.speed() <= 0.1f)
      return true;
    else return false;
  }
  public boolean catchStop(float stopSize){
    if(this.move.vector2D.speed() <= stopSize)
      return true;
    return false;
  }

  public boolean catchDiaZero(){
    if(this.dia <= 0)
      return true;
    return false;
  }

  public boolean catchCollision(Obj obj){
    if(obj == this) return false;
    if(dist(this.xPos, this.yPos, obj.xPos, obj.yPos) < obj.dia/2 + this.dia/2)
      return true;
    return false;
  }

  public boolean catchOutOfScreen(){
    if(this.xPos < -this.dia || this.xPos > width + this.dia ||
        this.yPos < -this.dia || this.yPos > height + this.dia)
      return true;
    return false;
  }

  public abstract boolean update();
}

public void objUpdate(Object arrayList){
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
  public boolean update(){
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

  public void figureUpdate(){
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
  public boolean update(){
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
  public void drawLife(){
    fill(255, 170);
    rect(super.xPos - super.dia/2, super.yPos + super.dia/2, this.hpBar * this.hp, 10);
  }
  public void damage(float damage){
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
  public boolean update(){
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
  public boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    return super.delete;
  }

  public void leftDisplace(float speed){
    super.xPos -= speed;
    if(super.xPos < dia/2) super.xPos = dia/2;
  }

  public void rightDisplace(float speed){
    super.xPos += speed;
    if(width - super.dia/2 < super.xPos) super.xPos = width - super.dia/2;
  }

  public boolean catchHit(Obj obj){
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
class EvaporationParticle extends ParticleObj{
  EvaporationParticle(float xPos, float yPos, float dia, float evapoDia, Figure figure){
    super.xPos = xPos + random(-evapoDia/2, evapoDia/2);
    super.yPos = yPos + random(-evapoDia/2, evapoDia/2);
    super.dia = dia;
    super.figure = figure;
  }

  public boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    super.dia -= 1.0f;
    if(super.dia < 0) super.delete();
    return delete;
  }
}
class FireParticle extends ParticleObj{
  FireParticle(float xPos, float yPos, float dia, float fireDia, Figure figure ){
    super.xPos = xPos + random(-fireDia/2, fireDia/2);
    super.yPos = yPos + random(-fireDia/2, fireDia/2);
    super.dia = dia;
    super.figure = figure;
    
    super.move = new RiseMove(random(0, 3), -random(-140, -50), 0.3f,
        new StraightBound(0, 0, width, height, 0.1f)
        );
  }
  public boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    super.setMoveState(super.move.action(super.getMoveState()));
    super.dia -= 1.0f;
    if(super.dia < 0) super.delete();
    return delete;
  }
}

class Ellipse extends ParticleFigure{
  Ellipse(int[] col){
    super(col);
  }
  public void update(float xPos, float yPos, float dia){
    noStroke();
    fill(super.col[0]);
    ellipse(xPos, yPos, dia, dia);
  }
}
public void fireSample(float xPos, float yPos, float dia, int[] col){
  for(int i = 0; i < 1; i++){
    backParticles.add(new FireParticle(
          xPos, yPos,
          random(10, 40), dia,
          new Ellipse(col)
          ));
    frontParticles.add(new FireParticle(
          xPos, yPos,
          random(10, 50), dia,
          new Ellipse(col)
          ));
  }
}

public void evaporationSample(float xPos, float yPos, float dia, int[] col){
  for(int i = 0; i < 1; i++){
    backParticles.add(new EvaporationParticle(
          xPos, yPos,
          random(10, 40), dia,
          new Ellipse(col)
          ));
    frontParticles.add(new EvaporationParticle(
          xPos, yPos,
          random(10, 50), dia,
          new Ellipse(col)
          ));
  }
}
abstract class Skill{
  String name;
}

class EnemySkill extends Skill{
}

class MySkill extends Skill{
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
