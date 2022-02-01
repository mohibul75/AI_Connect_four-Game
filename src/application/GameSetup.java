package application;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.xml.transform.Source;

public class GameSetup {
	
	Scanner input=new Scanner(System.in);
	//Main  obj =new Main();
	
	int Row_count=6;
	int Column_count=7;
	
	int Player_piece=1;
	int AI_piece=2;
	
	int Player_turn=0;
	int AI_turn=1;
	
	int Empty=0;
	
	int Window_length=4;
	
	Random rand=new Random();
	
	int [][] board= new int [Row_count][Column_count];
	
	void drop_piece(int [][] board,int col,int row, int piece)
	{
		board[row][col]=piece;
	}
	
	boolean is_valid_location(int [][] board,int col)
	{
		return board[Row_count-1][col]==0;
	}
	
	int get_next_open_row(int [][] board,int col)
	{
		for(int r=0;r<Row_count;r++)
		{
			if(board[r][col]==0)
				return r;
		}
		return -1;
	}
	
	void print_board(int [][] board)
	{
		for(int i=Row_count-1;i>=0;i--)
		{
			for(int j=0;j<Column_count;j++)
				System.out.print(board[i][j]+" ");
			
			System.out.println();
		}
	}
	
	boolean winning_move(int [][] board, int piece)
	{
		//check horizontal
		
		for(int c=0;c<Column_count-3;c++)
		{
			for(int r=0;r<Row_count;r++)
			{
				if(board[r][c]== piece && board[r][c+1]== piece && board[r][c+2]== piece && board[r][c+3]== piece)
				{
					return true;
				}
			}
		}
		
		//check vertically
		
		for(int c=0;c<Column_count;c++)
		{
			for(int r=0;r<Row_count-3;r++)
			{
				if(board[r][c]== piece && board[r+1][c]== piece && board[r+2][c]== piece && board[r+3][c]== piece)
				{
					return true;
				}
			}
		}
		
		//positively sloped diagonals
		for(int c=0;c<Column_count-3;c++)
		{
			for(int r=0;r<Row_count-3;r++)
			{
				if(board[r][c]== piece && board[r+1][c+1]== piece && board[r+2][c+2]== piece && board[r+3][c+3]== piece)
				{
					return true;
				}
			}
		}
		
		
		//negatively sloped diagonals
		for(int c=0;c<Column_count-3;c++)
		{
			for(int r=3;r<Row_count;r++)
			{
				if(board[r][c]== piece && board[r-1][c+1]== piece && board[r-2][c+2]== piece && board[r-3][c+3]== piece)
				{
					return true;
				}
			}
		}
		return false;
		
		
	}
	
	
	int [] get_row(int row_index)
	{
		int [] row_array=new int [Column_count];
		
		for(int i=0;i<Column_count;i++)
			row_array[i]=board[row_index][i];
		
		return row_array;
	}
	
	int [] get_column(int col_index)
	{
		int [] col_array=new int [Row_count];
		
		for(int i=0;i<Row_count;i++)
			col_array[i]=board[i][col_index];
		
		return col_array;
	}
	
	int count(int [] array, int value)
	{
		int c=0;
		
		for(int i=0;i<array.length;i++)
		{
			if(array[i]==value)
				c++;
		}
		return c;
	}
	
	int [] get_window(int [] array,int start_index, int end_index)
	{
		int [] window=new int [Window_length];
		
		for(int i=start_index;i<end_index;i++)
			window[i-start_index]=array[i];
		
		return window;
	}
	
	int [] get_window_diagonally(int board[][],int r,int c,boolean positive)
	{
		int [] window=new int [Window_length];
		
		for(int i=0;i<Window_length;i++)
		{
			if(positive)
				window[i]=board[r+i][c+i];
			else
				window[i]=board[r+3-i][c+i];
		}

		return window;
	}
	
	int evaluate_window(int window[],int piece)
	{
		int score=0;
		
		int opp_piece=Player_piece;
		
		if(piece==Player_piece)
			opp_piece=AI_piece;
		
		if(count(window, piece)==4)
			score+=100;
		else if(count(window, piece)==3 && count(window, Empty)==1)
			score+=5;
		else if(count(window, piece)==2 && count(window, Empty)==2)
			score+=2;
		
		if(count(window,opp_piece)==3 && count(window,Empty)==1)
			score-=4;
		
		return score;
	}
	
	int [] get_centre_column(int [][] board)
	{
		int [] centre_column=new int[Row_count];
		
		for(int i=0;i<Row_count;i++)
			centre_column[i]=board[i][Column_count/2];
		
		return centre_column;
	}
	
	int scoring(int board[][], int piece)
	{
		int score=0;
		
		int [] centre_column=get_centre_column(board);
		int centre_count=count(centre_column, piece);
		score+=centre_count*3;
		
		
		//horizontal score
		for(int r=0;r<Row_count;r++)
		{
			int row_array[]=get_row(r);
			
			for(int c=0;c<Column_count-3;c++)
			{
				int window[]=get_window(row_array, c, c+Window_length);
				
				score+=evaluate_window(window, piece);
			}
		}
		
		//vertical score
		for(int c=0;c<Column_count;c++)
		{
			int col_array[]=get_column(c);
			for(int r=0;r<Row_count-3;r++)
			{
				int window[]=get_window(col_array, r, r+Window_length);
				score+=evaluate_window(window, piece);
			}
		}
		
		//score positively sloped diagonal
		for(int r=0;r<Row_count-3;r++)
		{
			for(int c=0;c<Column_count-3;c++)
			{
				int window[]=get_window_diagonally(board, r, c,true);
				
				score+=evaluate_window(window, piece);
			}
		}
		
		//score negatively sloped diagonal
		for(int r=0;r<Row_count-3;r++)
		{
			for(int c=0;c<Column_count-3;c++)
			{
				int window[]=get_window_diagonally(board, r, c,false);
				
				score+=evaluate_window(window, piece);
			}
		}		

		return score;
	}
	
	int [][] create_copy(int [][] board)
	{
		int b_copy[][]=new int[Row_count][Column_count];
		
		for(int i=0;i<Row_count;i++)
		{
			for(int j=0;j<Column_count;j++)
				b_copy[i][j]=board[i][j];
		}
		
		return b_copy;
	}
	
	boolean is_terminal_node(int [][] board)
	{
		return winning_move(board, Player_piece) || winning_move(board, AI_piece) || get_valid_location(board).size()==0;
	}
	
	
	public int[][] getBoard() {
		return board;
	}

	public void setBoardP(int x ,int y) {
		System.out.println("GameP	"+x+"	"+y);
		this.board[5-x][y] = 1;
	}
	
	public void setBoardAI(int x ,int y) {
		System.out.println("GameA	"+x+"	"+y);
		this.board[5-x][y] = 2;
	}

	double[] minimax(int [][] board, int depth, double alpha, double beta, boolean maximizing_player)
	{
		ArrayList<Integer> valid_locations=get_valid_location(board);
		boolean is_terminal=is_terminal_node(board);
		
		if(depth==0 || is_terminal)
		{
			double column_value[]=new double[2];
			if(is_terminal)
			{
				if(winning_move(board, AI_piece))
				{
					column_value[0]=-1;
					column_value[1]=10000000.0;
				}
				else if(winning_move(board, Player_piece))
				{
					column_value[0]=-1;
					column_value[1]=-10000000.0;
				}
				else
				{
					column_value[0]=-1;
					column_value[1]=0;
				}
				
				return column_value;
			}
			else
			{
				column_value[0]=-1;
				column_value[1]=scoring(board, AI_piece);
				
				return column_value;
			}
		}
		
		if(maximizing_player)
		{
			double value=Double.NEGATIVE_INFINITY;
			double column_value[]=new double[2];
			
			int column=valid_locations.get(rand.nextInt(valid_locations.size()));
			
			for(int col : valid_locations)
			{
				int row=get_next_open_row(board, col);
				int b_copy[][]=create_copy(board);
				drop_piece(b_copy, col, row, AI_piece);
				double new_score=minimax(b_copy, depth-1,alpha,beta, false)[1];
				
				if(new_score>value)
				{
					value=new_score;
					column=col;
					
					column_value[0]=column;
					column_value[1]=value;
				}
				
				alpha=Math.max(alpha, value);
				
				if(alpha>=beta)
					break;
			}
			return column_value;
		}
		
		else
		{
			double value=Double.POSITIVE_INFINITY;
			double column_value[]=new double[2];
			
			int column=valid_locations.get(rand.nextInt(valid_locations.size()));
			
			for(int col : valid_locations)
			{
				int row=get_next_open_row(board, col);
				int b_copy[][]=create_copy(board);
				drop_piece(b_copy, col, row, Player_piece);
				double new_score=minimax(b_copy, depth-1,alpha,beta, true)[1];
				
				if(new_score<value)
				{
					value=new_score;
					column=col;
					
					column_value[0]=column;
					column_value[1]=value;
				}
				
				beta=Math.min(beta, value);
				
				if(alpha>=beta)
					break;
				
			}
			return column_value;
		}
		
	}
	
	int pick_best_move(int board[][], int piece)
	{
		ArrayList<Integer> valid_locations=get_valid_location(board);
		
		int best_score=-10000;
		int best_column=0;
		
		for(int col:valid_locations)
		{
			int row=get_next_open_row(board, col);
			int [][] temp_board=board.clone();
			drop_piece(temp_board, col, row, piece);
			int score=scoring(temp_board, piece);
			if(score>best_score)
			{
				best_score=score;
				best_column=col;
			}
		}
		
		return best_column;
	}
	
	ArrayList<Integer> get_valid_location(int board[][])
	{
		ArrayList <Integer> valid_locations=new ArrayList<>();
		
		for(int c=0;c<Column_count;c++)
		{
			if(is_valid_location(board, c))
				valid_locations.add(c);
		}
		
		return valid_locations;
	}
	
	public void game_session(int cl)
	{
		boolean game_over=false;
		int turn=0;
		int col;
		while(!game_over)
		{
			if(turn==Player_turn)
			{
				System.out.println("Player 1");
				col=cl;
				
				if(is_valid_location(board, col))
				{
					int row=get_next_open_row(board, col);
					drop_piece(board, col, row, Player_piece);
					
					if(winning_move(board, Player_piece))
					{
						System.out.println("Player 1 wins");
						game_over=true;
					}
				}
				
			}
			else
			{
				System.out.println("Player 2");
				//col=pick_best_move(board, AI_piece);
				col=(int) minimax(board, 8, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true)[0];
				if(is_valid_location(board, col))
				{
					int row=get_next_open_row(board, col);
					drop_piece(board, col, row, AI_piece);
					
					if(winning_move(board, AI_piece))
					{ 
						System.out.println("Player 2 wins");
						game_over=true;
					}
				}
			}
			
			turn+=1;
			turn%=2;
			
			print_board(board);
			
		}
	}

}
