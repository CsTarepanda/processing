class Counter{
	private int counter, minCount, maxCount;
	Counter(int minCount, int maxCount){
		this.minCount = minCount;
		this.maxCount = maxCount;
		this.counter = minCount;
	}

	private void adjustCount(){
		this.counter %= this.maxCount;
		if(this.counter < 0)
			this.counter -= this.maxCount;
	}

	public int countUp(){
		if(++this.counter > maxCount)
			this.counter = this.minCount;
		return this.counter;
	}
	public int countUp(int num){
		this.counter += num;
		this.adjustCount();
		return this.counter;
	}

	public int countDown(){
		if(--this.counter < minCount)
			this.counter = this.maxCount;
		return this.counter;
	}
	public int countDown(int num){
		this.counter -= num;
		this.adjustCount();
		return this.counter;
	}

	public void reset(){
		this.counter = this.minCount;
	}

	public void setCount(int num){
		this.counter = num;
		this.adjustCount();
	}

	public int getCount(){
		return this.counter;
	}
}
