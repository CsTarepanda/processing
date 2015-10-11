class EvaporationParticle extends ParticleObj{
  EvaporationParticle(float xPos, float yPos, float dia, float evapoDia, Figure figure){
    super.xPos = xPos + random(-evapoDia/2, evapoDia/2);
    super.yPos = yPos + random(-evapoDia/2, evapoDia/2);
    super.dia = dia;
    super.figure = figure;
  }

  boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    super.dia -= 1.0;
    if(super.dia < 0) super.delete();
    return delete;
  }
}
class FireParticle extends ParticleObj{
  FireParticle(float xPos, float yPos, float dia, float fireDia, Figure figure ){
    super.xPos = xPos + random(-fireDia/2, fireDia/2);
    super.yPos = yPos + random(-fireDia/2, fireDia/2);
    super.dia = dia;
    super.figure = figure;
    
    super.move = new RiseMove(random(0, 3), -random(-140, -50), 0.3,
        new StraightBound(0, 0, width, height, 0.1)
        );
  }
  boolean update(){
    super.figure.update(super.xPos, super.yPos, super.dia);
    super.setMoveState(super.move.action(super.getMoveState()));
    super.dia -= 1.0;
    if(super.dia < 0) super.delete();
    return delete;
  }
}

