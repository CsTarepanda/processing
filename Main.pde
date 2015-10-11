import java.util.*;
ArrayList<BulletObj> bullets = new ArrayList<BulletObj>();
ArrayList<EnemyObj> enemys = new ArrayList<EnemyObj>();
ArrayList<ParticleObj> backParticles = new ArrayList<ParticleObj>();
ArrayList<ParticleObj> frontParticles = new ArrayList<ParticleObj>();
ArrayList<BarObj> bars = new ArrayList<BarObj>();
Information inf;
void setup(){
  size(500, 750);
  textAlign(CORNER, CORNER);
  textSize(20);
  inf = new Information(50);
}

void mousePressed(){
  frontParticles.add(new ParticleObj(
        mouseX, mouseY,
        50, 
        new Ellipse(new color[]{color(255, 170)}),
        new FallMove(
          5, random(360), 0.1,
          new StraightBound(
            -1, 0, -1, height, 1.0
            ))
        ));
  enemys.add(new EnemyObj(
        mouseX, mouseY,
        70,
        new Ellipse(new color[]{color(255, 0, 0, 170)}),
        new FallMove(
          5, random(360), 0.1,
          new StraightBound(
            -1, 0, -1, height, 1.0
            ))
        ));
}

void keyPressed(){
  inf.damage(5);
  bullets.add(new BulletObj(
        mouseX, mouseY,
        30,
        new Ellipse(new color[]{color(255, 100, 200, 170)}),
        stopMove()
        ));
  if(key == 'r'){
    inf = new Information(500);
  }
}

void draw(){
  background(0);
  objUpdate(backParticles);
  objUpdate(bullets);
  objUpdate(enemys);

  // draw area  start---------- 
  if(mousePressed){
    evaporationSample(mouseX, mouseY, 30, new color[]{color(255, 100, 0, 100)});
    evaporationSample(mouseX, mouseY, 90, new color[]{color(255, 0, 0, 100)});
  }
  // draw area  end------------

  objUpdate(frontParticles);
  objUpdate(bars);
  inf.update();
}

float adjustAngle(float angle){
    angle %= 360;
    if(angle < 0)
      angle = 360 + angle;
    return angle;
}

Move stopMove(){
  return new StraightMove(
      0, 0, 0.0,
      new StraightBound(
        -1, -1, -1, -1, 0.0
        )
      );
}
