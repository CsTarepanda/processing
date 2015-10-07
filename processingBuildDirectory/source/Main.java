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
  frontParticles.add(new FireParticle(
        mouseX, mouseY,
        50,
        new Ellipse(new int[]{color(255, 170), color(0)}),
        new WaveMove(5, random(360), 3.0f, 10),
        new StraightBound(0, 0, width, height, 0.6f)
        ));
}

public void keyPressed(){
  inf.damage(5);
}

public void draw(){
  background(0);
  objUpdate(backParticles);
  objUpdate(bullets);
  objUpdate(enemys);
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

  public float[] getDirection(){
    return new float[]{this.speed, this.angle};
  }

  public void setDirection(float[] direction){
    super.speed = direction[0];
    super.angle = direction[1];
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
  public abstract float left(float direction);
  public abstract float top(float direction);
  public abstract float right(float direction);
  public abstract float bottom(float direction);
  public float[] action(float[] moveState, float[] direction){
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
class StraightBound extends Bound{
  StraightBound(float leftEnd, float topEnd, float rightEnd, float bottomEnd, float coefficient){
    super(leftEnd, topEnd, rightEnd, bottomEnd, coefficient);
  }

  public float left(float direction){
    return 540 - direction;
  }
  public float top(float direction){
    return 360 - direction;
  }
  public float right(float direction){
    return 540 - direction;
  }
  public float bottom(float direction){
    return 360 - direction;
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
  int maxHp;
  float hp;
  float hpBar;
  Information(float hp){
    this.hp = hp;
    this.maxHp = (int)hp;
    this.hpBar = (width - 20) / hp;
  }

  public void update(){
    textAlign(CORNER, CORNER);
    textSize(20);
    if(this.hp > 0){
      fill(255, 255, 255, 100);
      rect(10, 10, this.hpBar * this.hp, 20);
      text((int)hp +" / "+ maxHp, 20, 27);
    }else{
      text((int)hp +" / "+ maxHp, 20, 27);
      textAlign(CENTER, CENTER);
      textSize(50);
      text("end", width/2, height/2);
    }
  }

  public void damage(float damage){
    this.hp -= damage;
    if(this.hp <= 0) this.hp = 0;
  }
}
class StraightMove extends Move{
  StraightMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  public float[] action(float[] moveState){
    moveState[0] += super.speed * cos(radians(super.angle));
    moveState[1] += super.speed * sin(radians(super.angle));
    super.speed *= 1 - super.factor / 10;
    return moveState;
  }
}

class CurveMove extends Move{
  CurveMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  public float[] action(float[] moveState){
    moveState[0] += super.speed * cos(radians(super.angle));
    moveState[1] += super.speed * sin(radians(super.angle));
    super.angle += super.factor;
    return moveState;
  }
}

class WaveMove extends Move{
  float rotateAngle = 0;
  float radius = 5;
  WaveMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  WaveMove(float speed, float angle, float factor, float radius){
    super(speed, angle, factor);
    this.radius = radius;
  }
  public float[] action(float[] moveState){
    adjustAngle(this.rotateAngle);
    moveState[0] += super.speed * cos(radians(super.angle));
    moveState[1] += super.speed * sin(radians(super.angle));
    this.rotateAngle += super.factor;
    super.angle += sin(radians(this.rotateAngle)) * this.radius;
    return moveState;
  }
}

class FallMove extends Move{
  float xSpeed, ySpeed;
  FallMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  public float[] action(float[] moveState){
    this.xSpeed = super.speed * cos(radians(super.angle));
    this.ySpeed = super.speed * sin(radians(super.angle));
    moveState[0] += this.xSpeed;
    moveState[1] += this.ySpeed;
    this.ySpeed += super.factor;
    super.speed = sqrt(sq(this.xSpeed) + sq(this.ySpeed));
    super.angle = degrees(atan2(this.ySpeed, this.xSpeed));
    return moveState;
  }
}

class RiseMove extends Move{
  float xSpeed, ySpeed;
  RiseMove(float speed, float angle, float factor){
    super(speed, angle, factor);
  }
  public float[] action(float[] moveState){
    this.xSpeed = super.speed * cos(radians(super.angle));
    this.ySpeed = super.speed * sin(radians(super.angle));
    moveState[0] += this.xSpeed;
    moveState[1] += this.ySpeed;
    this.ySpeed -= super.factor;
    super.speed = sqrt(sq(this.xSpeed) + sq(this.ySpeed));
    super.angle = degrees(atan2(this.ySpeed, this.xSpeed));
    return moveState;
  }
}

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

  public float[] getDirection(){
    return move.getDirection();
  }

  public void setDirection(float[] direction){
    move.setDirection(direction);
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

  public abstract boolean update();
}

public void objUpdate(Object arrayList){
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
class FireParticle extends ParticleObj{
  FireParticle(float xPos, float yPos, float dia, Figure figure, Move move, Bound bound){
    super(xPos, yPos, dia, figure, move, bound);
  }
  public boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    super.setMoveState(super.move.action(super.getMoveState()));
    super.setDirection(super.bound.action(super.getMoveState(), super.getDirection()));
    super.dia -= 0.2f;
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
