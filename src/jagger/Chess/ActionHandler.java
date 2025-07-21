package jagger.Chess;

public class ActionHandler {

	public String action;
	public Runnable handler;
	
	public ActionHandler(String action, Runnable handler) {
		this.action = action;
		this.handler = handler;
	}
	
	public void run() {
		handler.run();
	}

}
