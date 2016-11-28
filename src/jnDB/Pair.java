package jnDB;

public class Pair<F,S> {
	public F first;
	public S second;
	
	public Pair(F fst, S snd){
		first = fst;
		second = snd;
	}

	@Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Pair)){ return false; }
        Pair<F,S> pair = (Pair<F,S>)obj;
        System.out.println("qqqqqq");
		return (this.first == pair.first || (this.first!=null && this.first.equals(pair.first)))
					&& (this.second == pair.second || (this.second!= null && this.second.equals(pair.second)));
    }
	
	@Override
	public int hashCode(){
		if(first == null&&second==null)return 0;
		if(first == null)return second.hashCode();
		if(second == null)return first.hashCode();
		return first.hashCode() ^ second.hashCode();
	}
}
