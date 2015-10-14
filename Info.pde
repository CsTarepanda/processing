class Information{
  private int maxHp;
  private float hp;
  private float hpBar;
  private int score = 0;
  private boolean gameEnd = false;
  Information(float hp){
    this.hp = hp;
    this.maxHp = (int)hp;
    this.hpBar = (width - 20) / hp;
  }

  void update(){
    textAlign(CORNER, CORNER);
    textSize(20);
    noStroke();
    fill(255, 255, 255, 100);
    if(this.hp > 0){
      drawHp();
      drawScore();
    }else{
      drawEnd();
    }
  }

  private void drawScore(){
    text(this.score, 20, 57);
  }

  private void drawHp(){
    rect(10, 10, this.hpBar * this.hp, 20);
    text((int)hp +" / "+ this.maxHp, 20, 27);
  }

  private void drawEnd(){
    this.gameEnd = true;
    text((int)this.hp +" / "+ this.maxHp, 20, 27);
    textAlign(CENTER, CENTER);
    textSize(50);
    text("end", width/2, height/2);
  }

  void damage(float damage){
    this.hp -= damage;
    if(this.hp <= 0) this.hp = 0;
  }

  void addScore(float score){
    if(!this.gameEnd) this.score += score;
  }
}
