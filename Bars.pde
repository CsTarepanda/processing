class Simple extends BarFigure{
  Simple(color[] col){
    super(col);
  }
  void update(float xPos, float yPos, float dia){
    noStroke();
    fill(col[0]);
    rect(xPos - dia/2, yPos - 5, dia, 10);
  }
}
