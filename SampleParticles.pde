void fireSample(float xPos, float yPos, float dia, color[] col){
  for(int i = 0; i < 1; i++){
    backParticles.add(new FireParticle(
          xPos, yPos,
          random(10, 40), dia,
          new Ellipse(col)
          ));
    frontParticles.add(new FireParticle(
          xPos, yPos,
          random(10, 50), dia,
          new Ellipse(col)
          ));
  }
}

void evaporationSample(float xPos, float yPos, float dia, color[] col){
  for(int i = 0; i < 1; i++){
    backParticles.add(new EvaporationParticle(
          xPos, yPos,
          random(10, 40), dia,
          new Ellipse(col)
          ));
    frontParticles.add(new EvaporationParticle(
          xPos, yPos,
          random(10, 50), dia,
          new Ellipse(col)
          ));
  }
}
