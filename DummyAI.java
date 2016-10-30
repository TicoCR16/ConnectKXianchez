/*
 * Comments: 
 * 	1.Lacking Heuristic Function
 *  2.Alpha Beta Implementation Semi-Complete
 */

import java.awt.*;
import connectK.CKPlayer;
import java.util.ArrayList;
import connectK.BoardModel;

import java.util.Random;

public class DummyAI extends CKPlayer
{
	private final byte PLAYER_1 = 1;
	private final byte PLAYER_2 = 2;
	
	private final int P_INF = Integer.MAX_VALUE;
	private final int N_INF = Integer.MIN_VALUE;
	
	private final int DEPTH_MAX = 2;
	
	//Needs to be removed
	private Random random = new Random();
	
	
	//2-Param Constructor
	public DummyAI( byte player, BoardModel state )
	{
		super( player, state );
        teamName = "Xiachez";
	}
	
	@Override
    public Point getMove( BoardModel state ) { return minimax( state ); }
	
	@Override
	public Point getMove( BoardModel state, int deadline ) { return getMove( state ); }
	
	//Simulates Minimax algorithm
	public Point minimax( BoardModel state )
	{
		int value = Integer.MIN_VALUE;
		Point action = null;
		int score = heuristicFunction(); //Evaluate Score
		for( Point cell : possibleMoves( state ) )
		{
			int temp = depthScore( state.placePiece( cell, PLAYER_2 ), 0, score, N_INF, P_INF );
			if( value < temp )
			{
				value = temp;
				action = cell;
			}
		}
		return action;
	}
	
	//Assigns a score to each possible state
	public int depthScore( BoardModel state, int depth, int score, int alpha, int beta )
	{	
		//Base Cases
		byte terminateStatusCode = state.winner();
        if( terminateStatusCode != -1 )
            return ( !state.hasMovesLeft() ? 0 : ( (terminateStatusCode == 1 ) ? N_INF : P_INF ) );
        if( depth > DEPTH_MAX ) return heuristicFunction();
        //End of Base Cases
        
        
        
        
        //Comment this section to remove Alpha-Beta
        
        int returnValue = 0;
        
        for( Point cell : possibleMoves( state ) )
        {
        	if( depth % 2 == 0 )
        	{
        		returnValue = Math.min( beta, depthScore( state.placePiece( cell, PLAYER_1 ),
        			          depth + 1, heuristicFunction(), alpha, beta ) );
        		if( returnValue <= alpha ) break;
        	}
        	else
        	{
        		returnValue = Math.max( alpha, depthScore( state.placePiece( cell,  PLAYER_2 ),
        				      depth + 1, heuristicFunction(), alpha, beta ) ); 
        		if( beta <= returnValue ) break;	
        	}
        }
        
        return returnValue;
        
        
        /******************************************/

        //Uncomment this section for regular minimax
        /*
        int value = depth % 2 != 0  ? N_INF : P_INF;
        for( Point cell : possibleMoves( state ) )
           	value = depth % 2 == 0 ? Math.min( value, depthScore( state.placePiece( cell, PLAYER_1 ), depth + 1, heuristicFunction(), alpha, beta ) ) :
        		Math.max( value, depthScore( state.placePiece( cell,  PLAYER_2 ), depth + 1, heuristicFunction(), alpha, beta ) ); 
        return value;
        */    
	}
	
	//Compares scores
	public int heuristicFunction()
	{
		return random.nextInt( 10000 );
	}
	
	//Returns an ArrayList object containing all available( 0 pieces ) moves
	public ArrayList< Point > possibleMoves( BoardModel state )
	{
		ArrayList< Point > possibleMoves = new ArrayList< Point >();
		for( int x = 0; x < state.width; x++ )
            for( int y = 0; y < state.height; y++ )
            {
                if( state.getSpace( x, y ) == 0 ) 
                {
                    possibleMoves.add( new Point( x, y ) );
                    if( state.gravity ) break; 
                }
            }		
		return possibleMoves;
	}
}