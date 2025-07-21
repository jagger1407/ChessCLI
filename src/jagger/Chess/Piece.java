package jagger.Chess;
import java.util.ArrayList;

public class Piece {
	  private PieceType type;
	  private Color color;
	  
	  public ArrayList<Integer> possibleMoves;
	  
	  private boolean moved;
	  
	  public Piece(PieceType type, Color color) {
		  this.type = type;
		  this.color = color;
		  this.possibleMoves = new ArrayList<Integer>();
		  setMoved(false);
	  }
	  
	  public Piece(char type) {
		  String pstr = "";
		  if(Character.isUpperCase(type))  {
			  color = Color.White;
			  pstr += "PBNRQK";
		  }
		  else  {
			  color = Color.Black;
			  pstr += "pbnrqk";
		  }
		  
		  this.type = PieceType.values()[pstr.indexOf(type)];
		  this.possibleMoves = new ArrayList<Integer>();
		  setMoved(false);
	  }
	  
	  public boolean hasMoved() {
			return moved;
		  }

	  public void setMoved(boolean moved) {
		this.moved = moved;
	  }
	  
	  public Color getColor() {
		  return color;
	  }
	  
	  public PieceType getType() {
		  return type;
	  }
	  
	  public String getName() {
		  return type.toString();
	  }
	  
	  public boolean promote(PieceType newType) {
		  if(type != PieceType.Pawn || newType == PieceType.Pawn || newType == PieceType.King) return false;
		  this.type = newType;
		  return true;
	  }
	  
	  public String toString() {
		  String letters = "pbnrqk";
		  String out = "" + letters.charAt(type.ordinal());
		  if(color == Color.White) out = out.toUpperCase();
		  return out;
	  }

	  
}

