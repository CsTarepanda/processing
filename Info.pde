class Information{
  int maxHp;
  float hp;
  float hpBar;
  Information(float hp){
    this.hp = hp;
    this.maxHp = (int)hp;
    this.hpBar = (width - 20) / hp;
  }

  void update(){
    textAlign(CORNER, CORNER);
    textSize(20);
    if(this.hp > 0){
      fill(255, 255, 255, 100);
      rect(10, 10, this.hpBar * this.hp, 20);
      text((int)hp +" / "+ maxHp, 20, 27);
    }else{
      text((int)hp +" / "+ maxHp, 20, 27);
      textAlign(CENTER, CENTER);
      textSize(50);
      text("end", width/2, height/2);
    }
  }

  void damage(float damage){
    this.hp -= damage;
    if(this.hp <= 0) this.hp = 0;
  }
}
