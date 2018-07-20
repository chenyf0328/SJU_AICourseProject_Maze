
public class Node implements Comparable{
	private int pacmanX;
	private int pacmanY;
	private int F;
	private int G;
	private int H;
	private Node parentNode;

	//****************************************************** 
  	//*** Purpose: Constructs a Node with pacmanX and pacmanY
  	//*** Input: int pacmanX, int pacmanY
  	//*** Output: None
  	//******************************************************
	public Node(int pacmanX, int pacmanY){
		this.pacmanX=pacmanX;
		this.pacmanY=pacmanY;
	}
	
	//****************************************************** 
  	//*** Purpose: Set F value
  	//*** Input: int F
  	//*** Output: None
  	//******************************************************
	public void setF(int F){
		this.F=F;
	}
	
	//****************************************************** 
  	//*** Purpose: Get F value
  	//*** Input: None
  	//*** Output: The valuse of F
  	//******************************************************
	public int getF(){
		return this.F;
	}
	
	//****************************************************** 
  	//*** Purpose: Set G value, multiply 2
  	//*** Input: int F
  	//*** Output: None
  	//******************************************************
	public void setG(int G){
		this.G=G;
	}
	
	//****************************************************** 
  	//*** Purpose: Get G value
  	//*** Input: None
  	//*** Output: The value of G
  	//******************************************************
	public int getG(){
		return this.G;
	}
	
	//****************************************************** 
  	//*** Purpose: Set H value, multiply 10
  	//*** Input: int H
  	//*** Output: None
  	//******************************************************
	public void setH(int H){
		this.H=H;
	}
	
	//****************************************************** 
  	//*** Purpose: Get H value
  	//*** Input: None
  	//*** Output: The value of H
  	//******************************************************
	public int getH(){
		return this.H;
	}
	
	//****************************************************** 
  	//*** Purpose: Set parent node
  	//*** Input: Node parentNode
  	//*** Output: None
  	//******************************************************
	public void setParentNode(Node parentNode){
		this.parentNode=parentNode;
	}
	
	//****************************************************** 
  	//*** Purpose: Get parent node
  	//*** Input: None
  	//*** Output: Parent Node
  	//******************************************************
	public Node getParentNode(){
		return this.parentNode;
	}
	
	//****************************************************** 
  	//*** Purpose: Set PacmanX
  	//*** Input: int pacmanX
  	//*** Output: None
  	//******************************************************
	public void setPacmanX(int pacmanX){
		this.pacmanX=pacmanX;
	}
	
	//****************************************************** 
  	//*** Purpose: Get PacmanX
  	//*** Input: None
  	//*** Output: PacmanX
  	//******************************************************
	public int getPacmanX(){
		return this.pacmanX;
	}
	
	//****************************************************** 
  	//*** Purpose: Set PacmanY
  	//*** Input: int pacmanY
  	//*** Output: None
  	//******************************************************
	public void setPacmanY(int pacmanY){
		this.pacmanY=pacmanY;
	}
	
	//****************************************************** 
  	//*** Purpose: Get PacmanY
  	//*** Input: None
  	//*** Output: PacmanY
  	//******************************************************
	public int getPacmanY(){
		return this.pacmanY;
	}
	
	//****************************************************** 
  	//*** Purpose: Override method compareTo
  	//*** Input: Object arg0
  	//*** Output: The result of comparing
  	//******************************************************
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		int numberFa = this.getF();
        int numberFb = ((Node)arg0).getF();
        if (numberFb > numberFa) {
            return -1;
        }
        else if (numberFb < numberFa) {
            return 1;
        } else {
            return 0;
        }
	}
	
	//****************************************************** 
  	//*** Purpose: Override method equals
  	//*** Input: Object arg0
  	//*** Output: The result of comparing
  	//******************************************************
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Node pacNode = null;
		if (obj instanceof Node) {
			pacNode = (Node) obj;
		}
		else {
			new Exception("it is not type of pacman");
		}

		if (this.getPacmanX() == pacNode.getPacmanX() && this.getPacmanY() == pacNode.getPacmanY()) {
			return true;
		}
		return false;
	}
}

