class Ellipse extends ParticleFigure{
  Ellipse(color[] col){
    super(col);
  }
  void update(float xPos, float yPos, float dia){
    noStroke();
    fill(col[0]);
    ellipse(xPos, yPos, dia, dia);
  }
}
