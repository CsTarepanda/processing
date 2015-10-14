abstract class Figure{
  float dia;
  color[] col;
  color[] myBulletCol = {color(255, 200, 100, 170), color(255, 200, 255, 170), color(255)};
  Figure(color[] col){
    this.dia = dia;
    this.col = col;
  }
  abstract void update(float xPos, float yPos, float dia);

  void toMyBullet(){
    this.col = this.myBulletCol;
  }

  void changeMyBullet(color[] col){
    this.myBulletCol = col;
  }
}

abstract class BulletFigure extends Figure{
  BulletFigure(color[] col){
    super(col);
  }
}

abstract class EnemyFigure extends Figure{
  EnemyFigure(color[] col){
    super(col);
  }
}

abstract class ParticleFigure extends Figure{
  ParticleFigure(color[] col){
    super(col);
  }
}

abstract class BarFigure extends Figure{
  BarFigure(color[] col){
    super(col);
  }
}
