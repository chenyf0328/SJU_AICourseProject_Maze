/* *********************************************************************
 * ********* Author¡¯s name(s): Yifan Chen
 * Course Title: Artificial Intelligence
 * Semester: Fall 2017
 * Assignment Number 2
 * Submission Date: 10/16/2017
 * Purpose: This program implement the Pacman Game,the goal is to find the ghost with the minimum cost.
 * Input: java Maze
 * Output: The optimum path of Pacman to find the ghost
 * Help: I worked alone.
 * ************************************************************************
 * ****** */

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import javax.swing.*;

//***********************************************************************
public class Maze extends JFrame
{
   //*** can keep track of visited cell positions here
   static boolean [][] visited;
   
   private PriorityQueue<Node> openList=new PriorityQueue<Node>();
   private ArrayList<Node> closedList=new ArrayList<Node>();
   
   private static final int PIRATE_COST=100;
   private static final int STRIPE_COST=90;
   private static final int NORMAL_COST=1;

   //*** the maze itself
   //***    0 means Power Pellet
   //***    1 means wall
   //***    2 means Stripes
   //***    3 means Pirate
   static int [][] mazePlan =
      {
         {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
         {1,0,1,0,0,3,0,0,0,0,0,0,0,0,0,0,1,0,1},
         {1,0,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,1},
         {1,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,1},
         {1,0,0,0,0,0,0,0,0,1,2,0,0,0,0,0,3,0,1},
         {1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,1},
         {1,1,1,0,0,0,0,1,0,0,0,1,0,0,0,0,1,1,1},
         {1,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,1},
         {1,2,1,1,1,0,0,0,0,3,0,0,0,0,0,0,0,0,1},
         {1,0,0,0,1,0,0,1,1,1,1,1,0,0,0,0,0,0,1},
         {1,0,0,0,1,0,0,1,0,0,0,1,0,0,0,0,0,0,1},
         {1,0,0,0,1,0,0,1,0,0,0,1,0,3,1,1,1,0,1},
         {1,0,0,0,1,0,0,1,1,1,1,1,0,0,1,0,0,3,1},
         {1,1,1,0,1,0,0,0,0,0,0,0,0,0,1,0,1,1,1},
         {1,0,0,3,1,0,0,1,1,1,1,1,2,0,1,0,0,0,1},
         {1,0,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,0,1},
         {1,0,1,1,1,3,0,1,0,0,0,1,0,0,1,0,1,0,1},
         {1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,1},
         {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1},
         {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
      };

   //*** set up the maze wall positions and set all visited states to false
   static MazePanel mp = new MazePanel(mazePlan, visited);

   //*** set up and display main characters' initial maze positions
   static int  ghostX = 15, ghostY = 8;              //** Ghost
   static int  pacmanX = 1, pacmanY = 1;             //*** Pacman

   //*** each maze cell is 37 pixels long and wide
   static int panelWidth = 37;

   //*** a simple random number generator for random search
   static int randomInt(int n) {return (int)(n * Math.random());}

   //******************************************************
   //*** main constructor
   //******************************************************
   public Maze()
   {
      //*** display the ghost
      mp.setupChar(ghostX, ghostY, "ghost.gif");

      //*** display Pacman
      mp.setupChar(pacmanX, pacmanY, "pacman.gif");

      //*** set up the display panel
      getContentPane().setLayout(new GridLayout());
      setSize(mazePlan[0].length*panelWidth, mazePlan[0].length*panelWidth);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      getContentPane().add(mp);
   }

   //****************************************************** 
   //*** Purpose: Get the openList	
   //*** Input: None
   //*** Output: PriorityQueue<Node>
   //******************************************************
   public PriorityQueue<Node> getOpenList(){
	   return openList;
   }
   
   //******************************************************
   //*** a delay routine
   //******************************************************
   public void wait(int milliseconds)
   {
      try
         {Thread.sleep(milliseconds);}
      catch (Exception e)
         {}
   }

   //******************************************************
   //*** move Pacman to position (i, j)
   //******************************************************
   public void movePacman(int i, int j)
   {
      mp.setupChar(i, j, "pacman.gif");
   }


   //******************************************************
   //*** remove Pacman from position (i, j)
   //******************************************************
   public void removePacman(int i, int j)
   {
     	mp.removeChar(i, j);
   }

   //******************************************************
   //*** is position (i,j) a power-pellet cell?
   //******************************************************
   public boolean openSpace(int i, int j)
   {
      return (mazePlan[i][j] == 1);
   }
   
   //****************************************************** 
   //*** Purpose: Calculate Pacman's cost
   //*** Input: int newX, int newY
   //*** Output: int
   //******************************************************
   public int calculateCost(int newX, int newY){
	   if (this.mazePlan[newX][newY]==0)
		   return NORMAL_COST;
	   else if (this.mazePlan[newX][newY]==2)
		   return STRIPE_COST;
	   else
		   return PIRATE_COST;
   }
   
   //****************************************************** 
   //*** Purpose: Use AStar method to find the optimum solution
   //*** Input: Node initialNode
   //*** Output: None
   //******************************************************
   public void AStarAlgo(Node initialNode) throws InterruptedException{
	   //To judge if the goal is found
	   boolean judgeFinished=false;
	   //To judge if the new node is in closedList
	   boolean haveOccupied=false;
	   Iterator<Node> iter;
	   Iterator<Node> iterClosedList;
	   Node iterNode,iterClosedNode;
	   
	   //Set initialNode G H F value
	   initialNode.setG(0);
	   initialNode.setH(Math.abs(ghostX-(initialNode.getPacmanX())+Math.abs(ghostY-(initialNode.getPacmanY()))));
	   initialNode.setF(Math.abs(ghostX-(initialNode.getPacmanX())+Math.abs(ghostY-(initialNode.getPacmanY()))));
	   
	   openList.offer(initialNode);
	   
	   while(judgeFinished!=true && openList.isEmpty()==false){
		   haveOccupied=false;
		   //Judge if the goal is in openList
		   iter=openList.iterator();
		   iterClosedList=closedList.iterator();
		   while(iter.hasNext()){
			   iterNode=iter.next();
			   if (iterNode.getPacmanX()==ghostX && iterNode.getPacmanY()==ghostY){
				   judgeFinished=true;
			   }
		   }
		   if (judgeFinished==true)
			   break;
		   
		   Node waitForPop=openList.poll();
		   closedList.add(waitForPop);
		   
		   //Search directions: up, down, left, right
		   int[] dx=new int[]{-1,1,0,0};
		   int[] dy=new int[]{0,0,-1,1};
		   
		   int currentX=closedList.get(closedList.size()-1).getPacmanX();
		   int currentY=closedList.get(closedList.size()-1).getPacmanY();
		   
		   //Check if the node around minimum F will be added in openList. 
		   //1) If it's in closedList, ignore it.
		   //2) If it's not in openList, add it in openList, as well as record its F G H value.
		   //3) If it has already been in openList, judge if it's the better path
		   for (int i=0;i<4;i++){
			   int newX=currentX+dx[i];
			   int newY=currentY+dy[i];
			   
			   Node aroundNode=new Node(newX,newY);
			   
			   //If it's approachable and it's not in closedList. Check if it's in openList.
			   aroundNode.setParentNode(waitForPop);
			   aroundNode.setG(aroundNode.getParentNode().getG()+calculateCost(newX,newY));
			   aroundNode.setH(Math.abs(ghostX-(currentX+dx[i]))+Math.abs(ghostY-(currentY+dy[i])));
			   aroundNode.setF(aroundNode.getG()+aroundNode.getH());
			   
			   //If it's unapproachable or it's in closedList, ignore it
			   if (openSpace(newX,newY)==false && (newX > 0 && newX < mazePlan.length - 1) && (newY > 0 && newY < mazePlan.length - 1)){
				   iterClosedList=closedList.iterator();
				   while(iterClosedList.hasNext()){
					   iterClosedNode=iterClosedList.next();
					   if (iterClosedNode.getPacmanX()==aroundNode.getPacmanX() && iterClosedNode.getPacmanY()==aroundNode.getPacmanY() && iterClosedNode.getF()==aroundNode.getF())
						   haveOccupied=true;
					   
					   if (haveOccupied==true)
						   continue;
				   }
				   
				   //If it's approachable and it's not in closedList. Check if it's in openList.
				   if (!openList.contains(aroundNode)){
					   openList.offer(aroundNode);
				   }
				   else
					   if ((waitForPop.getG()+calculateCost(newX,newY))<aroundNode.getG()){
						   aroundNode.setParentNode(waitForPop);
						   aroundNode.setG(waitForPop.getG()+calculateCost(newX,newY));
						   aroundNode.setF(aroundNode.getG()+aroundNode.getH());
					   }
				   
			   }
		   }
	   }
   }
   
   //******************************************************
   //***   MODIFY HERE --  MODIFY HERE  --  MODIFY HERE
   //******************************************************
   public static void main(String [] args) throws InterruptedException{
				
	   Node moveStep=null;
	   Stack<Node> moveStack=new Stack<Node>();
	   Iterator<Node> iterMove;
	   Node initialNode=new Node(pacmanX,pacmanY);
	   
       //*** create a new frame and make it visible
       Maze mz = new Maze();
       mz.setVisible(true);

       //*** Pacman's current board position
       int gbx = pacmanX, gby = pacmanY;
       
       initialNode.setParentNode(null);

       mz.AStarAlgo(initialNode);
       
       mz.movePacman(gbx, gby);
       mz.wait(200);
       mz.removePacman(gbx, gby);
       
       iterMove=mz.openList.iterator();
       while (iterMove.hasNext()){
    	   moveStep=iterMove.next();
    	   if (moveStep.getPacmanX()==ghostX && moveStep.getPacmanY()==ghostY)
    		   break;
       }
       
       while (!(moveStep.getPacmanX()==pacmanX && moveStep.getPacmanY()==pacmanY)){
    	   moveStack.push(moveStep);
    	   moveStep=moveStep.getParentNode();
       }
       
       //Print the Pacman's path
       Node currentPos=null;
       while (!(moveStack.isEmpty())){
    	   currentPos=moveStack.pop();
    	   gbx=currentPos.getPacmanX();
    	   gby=currentPos.getPacmanY();
    	   mz.movePacman(gbx,gby);
    	   mz.wait(200);
           mz.removePacman(gbx, gby);
       }
    	   
       //*** exhaustively search all open spaces one row at a time
//       for (gbx = 1; gbx < mazePlan.length - 1; gbx++)
//          for (gby = 1; gby < mazePlan.length - 1; gby++)
//
//             if (mz.openSpace(gbx, gby)) {
//                 //*** move Pacman to new location (gbx, gby)
//                 mz.movePacman(gbx, gby);
//
//                 //*** delay updating the screen
//                 //*** change this parameter as you wish
//                 mz.wait(200);
//
//
//                 //*** remove Pacman from location (gbx, gby)
//                 mz.removePacman(gbx, gby);
//             }
   } // main

} // Maze
