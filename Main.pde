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
  frontParticles.add(new FireParticle(
        mouseX, mouseY,
        50,
        new Ellipse(new color[]{color(255, 170), color(0)}),
        new WaveMove(5, random(360), 3.0, 10),
        new StraightBound(0, 0, width, height, 0.6)
        ));
}

void keyPressed(){
  inf.damage(5);
}

void draw(){
  background(0);
  objUpdate(backParticles);
  objUpdate(bullets);
  objUpdate(enemys);
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
