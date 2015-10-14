import java.util.*;
ArrayList<BulletObj> bullets = new ArrayList<BulletObj>();
ArrayList<EnemyObj> enemys = new ArrayList<EnemyObj>();
ArrayList<ParticleObj> backParticles = new ArrayList<ParticleObj>();
ArrayList<ParticleObj> frontParticles = new ArrayList<ParticleObj>();
ArrayList<BarObj> bars = new ArrayList<BarObj>();
Information inf;

Counter rotateCount = new Counter(0, 360);
void setup(){
  size(500, 750);
  textAlign(CORNER, CORNER);
  textSize(20);
  inf = new Information(50);
  bars.add(new BarObj(width/2, height - 20, 70, 
        new Simple(new color[]{color(0, 170, 255, 170)})));
  bars.add(new BarObj(width/2, height - 40, 70, 
        new Simple(new color[]{color(0, 170, 255, 170)})));
}

void mousePressed(){
  bullets.add(new BulletObj(
        mouseX, mouseY,
        30,
        new Ellipse(new color[]{color(255, 100, 200, 170)}),
        new FallMove(3, 90, 0.3,
          new StraightBound(0, -1, width, -1, 1.0)
          )
        ));
}

boolean a, d, h, l;
void keyPressed(){
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
                0, 0, width, height, 0.8
                ))
            ));
    }
  }
}

void keyReleased(){
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

void draw(){
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
          new SimpleBullet(new color[]{color(255, 170)}),
          new CustomMove(
            new EvaporateMove(1),
            new StraightMove(5, 90, 0.0,
              new StraightBound(0, 0, width, height, 1.0)),
            new FallMove(5, random(30, 50), 0.3,
              new StraightBound(0, 0, width, height, 1.0))
            )
          ));
    evaporationSample(mouseX, mouseY, 30, new color[]{color(255, 100, 0, 100)});
    evaporationSample(mouseX, mouseY, 90, new color[]{color(255, 0, 0, 100)});
  }
  // draw area  end------------

  objUpdate(frontParticles);
  objUpdate(bars);
  /* inf.addScore(5); */
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
