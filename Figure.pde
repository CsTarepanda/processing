abstract class Figure{
  float dia;
  color[] col;
  Figure(color[] col){
    this.dia = dia;
    this.col = col;
  }
  abstract void update(float xPos, float yPos, float dia);
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
