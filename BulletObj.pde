class JointBullet extends BulletObj{
  boolean through = true;
  JointBullet(float xPos, float yPos, float dia, Figure figure, Move move){
    super(xPos, yPos, dia, figure, move, 0);
  }
  JointBullet(float xPos, float yPos, float dia, Figure figure, Move move, float damage){
    super(xPos, yPos, dia, figure, move, damage);
  }

  boolean update(){
    super.playerHit();
    super.figureUpdate();
    if(!this.through) 
      for(EnemyObj obj: enemys)
        if(this.catchCollision(obj)){
          obj.damage(this.damage);
          for(int i = 0; i < 5; i++)
            fireSample(super.xPos, super.yPos, this.dia, figure.col);
          super.delete();
        }
    super.setMoveState(super.move.action(super.getMoveState()));
    if(super.catchDiaZero())
      super.delete();
    if(super.catchOutOfScreen())
      super.delete();
    if(super.getTime() > 5) this.launch();
    return super.delete;
  }

  void throughSwitch(){
    if(this.through) this.through = false;
    else this.through = true;
  }

  void launch(){
    for(int i = 0; i < 10; i++)
      bullets.add(new BulletObj(
            super.xPos, super.yPos,
            10,
            new SimpleBullet(new color[]{color(255, 183, 80, 200)}),
            new FallMove(
              10, 36 * i, 0.3,
              new StraightBound(
                0, 0, width, height, 0.8
                )
              )
            ));
    super.delete = true;
  }
}
