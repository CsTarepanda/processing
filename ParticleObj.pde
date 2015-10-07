class FireParticle extends ParticleObj{
  FireParticle(float xPos, float yPos, float dia, Figure figure, Move move, Bound bound){
    super(xPos, yPos, dia, figure, move, bound);
  }
  boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    super.setMoveState(super.move.action(super.getMoveState()));
    super.setDirection(super.bound.action(super.getMoveState(), super.getDirection()));
    super.dia -= 0.2;
    if(super.dia < 0) super.delete();
    return delete;
  }
}
