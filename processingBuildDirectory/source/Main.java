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
public void setup(){
  size(500, 750);
  textAlign(CORNER, CORNER);
  textSize(20);
  inf = new Information(50);
}

public void mousePressed(){
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
            -1, 0, -1, height, 1.0f
            ))
        ));
}

public void keyPressed(){
  inf.damage(5);
  bullets.add(new BulletObj(
        mouseX, mouseY,
        30,
        new Ellipse(new int[]{color(255, 100, 200, 170)}),
        stopMove()
        ));
  if(key == 'r'){
    inf = new Information(500);
    bullets.get(0);
  }
}

public void draw(){
  background(0);
  objUpdate(backParticles);
  objUpdate(bullets);
  objUpdate(enemys);

  // draw area  start---------- 
  if(mousePressed){
    evaporationSample(mouseX, mouseY, 30, new int[]{color(255, 100, 0, 100)});
    evaporationSample(mouseX, mouseY, 90, new int[]{color(255, 0, 0, 100)});
  }
  // draw area  end------------

  objUpdate(frontParticles);
  objUpdate(bars);
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


abstract class Figure{
  float dia;
  int[] col;
  Figure(int[] col){
    this.dia = dia;
    this.col = col;
  }
  public abstract void update(float xPos, float yPos, float dia);
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
    fill(255, 255, 255, 100);
    noStroke();
    if(this.hp > 0){
      rect(10, 10, this.hpBar * this.hp, 20);
      text((int)hp +" / "+ maxHp, 20, 27);
    }else{
      gameEnd = true;
      text((int)hp +" / "+ maxHp, 20, 27);
      textAlign(CENTER, CENTER);
      textSize(50);
      text("end", width/2, height/2);
    }
  }

  private void test(){
    println(5);
  }

  public void damage(float damage){
    this.hp -= damage;
    if(this.hp <= 0) this.hp = 0;
  }

  public void addScore(float score){
    if(!gameEnd) this.score += score;
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

class FallMove extends Move{
  float xSpeed, ySpeed;
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
  float xSpeed, ySpeed;
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
  BulletObj(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move);
    this.damage = 1;
  }
  BulletObj(float xPos, float yPos, float dia, Figure figure, Move move, float damage){
    super(xPos, yPos, dia, figure, move);
    this.damage = damage;
  }
  public boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    for(Obj obj: enemys)
      if(this.catchCollision(obj))
        for(int i = 0; i < 5; i++){
          fireSample(super.xPos, super.yPos, 30, new int[]{color(0, 100, 255, 100)});
          fireSample(super.xPos, super.yPos, 90, new int[]{color(255, 0, 0, 100)});
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
  public boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    this.drawLife();
    super.setMoveState(super.move.action(super.getMoveState()));
    if(super.catchOutOfScreen())
      super.delete();
    return super.delete;
  }
  public void drawLife(){
    fill(255, 170);
    rect(super.xPos - super.dia/2, super.yPos + super.dia/2, super.dia, 10);
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

abstract class BarObj extends Obj{
  BarObj(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move);
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
    fill(col[0]);
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
