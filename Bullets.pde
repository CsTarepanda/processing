class SimpleBullet extends Figure{
  SimpleBullet(color[] col){
    super(col);
  }

  void update(float xPos, float yPos, float dia){
    fill(col[0]);
    ellipse(xPos, yPos, dia, dia * 2.0);
  }
}
