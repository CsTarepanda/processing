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
    fill(255, 255, 255, 100);
    noStroke();
    if(this.hp > 0){
      rect(10, 10, this.hpBar * this.hp, 20);
      text((int)hp +" / "+ maxHp, 20, 27);
    }else{
      gameEnd = true;
      text((int)hp +" / "+ maxHp, 20, 27);
      textAlign(CENTER, CENTER);
      textSize(50);
      text("end", width/2, height/2);
    }
  }

  private void test(){
    println(5);
  }

  void damage(float damage){
    this.hp -= damage;
    if(this.hp <= 0) this.hp = 0;
  }

  void addScore(float score){
    if(!gameEnd) this.score += score;
  }
}
