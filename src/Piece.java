import java.util.ArrayList;

public class Piece {
	  private PieceType type;
	  private Color color;
	  
	  public ArrayList<Integer> possibleMoves;
	  
	  private boolean moved;
	  
	  public Piece(PieceType type, Color color) {
		  this.type = type;
		  this.color = color;
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
	  
	  public String toString() {
		  String letters = "pbnrqk";
		  String out = "" + letters.charAt(type.ordinal());
		  if(color == Color.White) out = out.toUpperCase();
		  return out;
	  }

	  
}

