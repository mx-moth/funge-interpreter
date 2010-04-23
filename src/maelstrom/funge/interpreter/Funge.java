package maelstrom.funge.interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

import maelstrom.funge.event.*;
import maelstrom.funge.interpreter.operator.*;
import maelstrom.funge.interpreter.stack.*;


public class Funge implements Runnable {

	/*
	 * According to the Funge-98 spec, a space operation must take
	 * only one 'tick' to complete. The problem with this is if
	 * there is an infinite loop of spaces. This will limit the
	 * space skipping to the specified number of skips, before
	 * another 'tick' occurs, thus preventing an infinite loop
	 * Default is 1000, which is wider than most programs, so it
	 * will loop all the way around at least once
	 */
	private final static int                  MAX_SPACE_SKIPS = 1000;

	public static final char                  SPACE_CHAR      = ' ';
	public static final char                  JUMP_CHAR       = ';';
	public static final char                  STRING_TOGGLE   = '"';

	private int                               sleepTime       = 100;
	private boolean                           fullSpeed       = true;

	private Grid                              grid;
	private Pointer                           pointer;
	private StackStack                        stackStack;

	private Thread                            run;
	private boolean                           firstIteration;
	private boolean                           skipStringSpace;

	// A list of objects to notify when this grid changes.
	private ArrayList<RunStateChangeListener> listeners       = new ArrayList<RunStateChangeListener>();

	public static void main (String[] args) {
		long grid[][];

		/*
		grid = {
			{ '0', '>', ':', '4', '8', '*', '-', ' ', ' ', ' ', '#', 'v', '_', '', '>', '0', '1', '4', '5', '*', 'p', '0', '>', ':', '0', '4', '5', '*', 'p', ':', '4', '8', '*', '1', '-', '-', '#', 'v', '_', '', '7', '>', '4', '4', '*', '>', '?', '<', ' ', ' ', ' ', ' ', '_', '', '+', '+', '+', '+', '\\','1', '-', ':', '#', '^', '_', 'v', ' ', '>', '>', '^', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '>', },
			{ ' ', '^', '+', '1', 'p', '*', '7', '3', '\\','0', ':', '<', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '^', '+', '1', ' ', ' ', '_', 'v', '#', '`', '2', 'g', '*', '7', '3', ':', '<', '>', ':', '2', '/', ' ', ':', 'v', ' ', '>', '0', '\\','2', '/', ':', '^', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'v', '<', ' ', '^', '"', 'T', 'W', 'A', 'N', 'G', '.', '.', '"', '<', ' ', },
			{ '1', '4', '5', '*', 'g', '1', '+', ':', '1', '4', '5', '*', 'p', '8', ' ', '8', '*', '`', ' ', ' ', ' ', ' ', ' ', '#', 'v', '_', ' ', '', '>', ' ', ' ', '>', ' ', ' ', '4', '4', '*', '>', '?', '<', ' ', ' ', ' ', ' ', '_', '', '+', '+', '+', '+', ':', '3', '7', '*', 'g', '2', '`', '!', '#', 'v', '_', ' ', ' ', ' ', ' ', '>', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', },
			{ '>', '7', '*', 'p', ':', '3', '7', '*', 'g', '3', '7', '*', '+', 'p', '^', 'v', 'g', '*', '5', '4', '0', '+', '1', 'g', '', '#', '*', '7', '3', 'g', '<', '^', '', '<', ' ', ' ', ' ', '<', '>', '0', '\\','2', '/', ':', '^', 'v', ' ', '_', 'v', '#', '!', '`', 'g', '*', '5', '4', '0', ':', ' ', '<', ' ', ' ', ' ', ' ', '#', ' ', '>', '"', 'r', 'r', 'a', ' ', 'e', 'h', 'T', '.', '"', '^', ' ', },
			{ '^', '3', '\\','+', '1', 'g', '*', '7', '3', ':', ':', 'p', '*', '7', '3', '<', ' ', ' ', '>', '+', '2', '*', '+', 'p', '#', ' ', '0', '4', '5', '*', '^', ' ', ' ', ' ', ' ', ' ', ' ', '^', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '<', ' ', '<', '>', ':', '0', '4', '5', '*', 'g', '6', '4', '*', '1', '-', 'g', '-', '#', 'v', '_', '^', ' ', ' ', ' ', '>', ':', '#', ',', '_', '', '', ' ', ' ', '^', },
			{ 'C', '.', '"', '*', '5', '2', '0', '<', 'p', '*', '5', '4', '0', '0', '<', ' ', ' ', ' ', '^', '5', '6', 'g', '*', '7', '3', ':', 'g', '*', '5', '4', '0', ':', '_', '^', '#', '-', 'g', '*', '4', '6', 'g', '*', '5', '4', '0', ':', '_', '^', '#', ' ', '-', 'g', '*', '2', '+', '5', '6', 'g', '*', '5', '4', '0', ':', '<', '#', ' ', ' ', ' ', ' ', ' ', 'v', '"', 'r', 'e', ' ', 'A', ',', 'B', ',', },
			{ 'A', '(', '"', '2', '5', '*', '"', '"', 'v', '>', '5', '*', 'p', ' ', '^', 'v', '1', '', '<', ' ', ' ', ' ', ' ', ' ', '>', '0', '2', '5', '*', '"', '.', 'n', 'i', 'a', 'g', 'a', ' ', 'g', 'n', 'i', 'y', 'r', 'T', ' ', '.', 's', 'e', 'v', 'a', 'c', ' ', 'g', 'n', 'i', 'k', 'a', 'm', ' ', 'p', 'u', ' ', 'd', 'e', 'w', 'e', 'r', 'c', 'S', '"', '^', '>', '"', 'i', 'F', ' ', ':', ')', 'C', 'B', },
			{ ' ', 'A', ',', 'B', ',', 'C', '.', '"', '<', '^', ' ', ' ', '4', '1', ' ', '<', ' ', '_', '^', '#', ':', 'p', '*', '7', '3', '0', '3', 'p', '*', '3', '7', '\\','5', 'p', '*', '3', '7', '\\','5', 'p', '*', '3', '7', '\\','5', 'p', '*', '3', '7', '\\','4', 'p', '*', '3', '7', '\\','4', 'p', '*', '3', '7', '\\','4', '', '<', 'v', '2', '"', '(', 'a', 'b', 'c', ')', ':', ' ', 'M', 'o', 'v', 'e', },
			{ '*', '7', '3', 'g', '+', '2', '*', ' ', '7', '3', ':', '\\','g', '*', '7', '3', 'g', '+', '1', '*', '7', '3', ':', ',', '*', '5', '2', '.', '+', '1', ':', 'g', '*', '5', '4', '0', '', '_', ',', '#', '!', ' ', '#', ':', '<', '"', 'C', 'a', 'v', 'e', ' ', '"', '0', '<', '', '_', ',', '#', '!', ' ', '#', ':', '<', '*', '5', '<', 'v', '3', '0', 'g', '*', '7', '3', 'g', '*', '4', '6', '\\','g', },
			{ '5', '4', '0', '_', 'v', '#', '-', ' ', 'g', '-', '1', '*', '4', '6', '<', 'v', '"', 'b', 'a', 't', 's', '.', '"', '*', '2', '5', '_', 'v', '#', 'g', '*', '5', '4', '4', '0', 'p', '*', '5', '4', ':', 'p', '*', '5', '4', ':', 'p', '*', '5', '4', ':', 'p', '*', '5', '4', '5', '0', 'p', '*', '5', '4', '4', '0', 'p', '*', '5', '4', '<', ' ', ' ', ' ', ' ', ' ', 'v', ' ', ' ', ' ', '4', 'g', '*', },
			{ ' ', ' ', 'v', 'v', '<', ' ', '>', ' ', ':', '0', '4', '5', '*', 'g', '^', '>', '"', ' ', 'r', 'a', 'e', 'h', ' ', 'u', 'o', 'Y', '"', '>', '5', '4', '5', '*', 'g', '!', '#', 'v', '_', '2', '5', '*', '"', '.', 't', 'f', 'a', 'r', 'd', ' ', '"', 'v', '>', 'v', ' ', '#', '"', 'T', 'u', 'n', 'n', 'e', 'l', 's', ' ', 'l', 'e', 'a', 'd', ' ', 't', 'o', ':', '"', '>', '#', '<', '6', '*', 'g', '-', },
			{ '2', '0', '_', 'v', '', '_', '^', '#', '!', '-', 'g', '+', '1', '*', '7', '3', 'g', '*', '5', '4', '0', ':', 'g', '*', '5', '4', '1', '', '_', ',', '#', '!', ' ', '#', ':', '<', '"', 'Y', 'o', 'u', ' ', 'f', 'e', 'e', 'l', ' ', 'a', '"', ' ', '<', ',', ':', '>', '', '"', ':', 'A', '"', ',', ',', '0', '4', '5', '*', 'g', ':', '6', '5', '+', '2', '*', 'g', '1', 'v', '^', ',', ':', '*', '5', },
			{ ' ', ' ', '1', '>', '>', '0', '2', '5', '*', '"', '!', 's', 'u', 'p', 'm', 'u', 'w', ' ', 'a', ' ', 'l', '"', ' ', 'v', '>', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '`', '#', 'v', '_', 'v', ' ', 'v', '_', '\\','', '3', '7', '*', '1', '+', '+', 'v', '^', '_', '^', '#', 'v', '-', '1', '*', '4', '6', ':', ',', ',', '"', 'B', ':', '"', ',', '*', '5', '2', '.', '+', '<', ' ', ' ', ' ', ' ', ' ', },
			{ '2', 'v', '^', ' ', '', '_', ',', '#', '!', ' ', '#', ':', '<', '"', 'Y', 'o', 'u', ' ', 's', 'm', 'e', 'l', '"', '<', '2', 'v', '`', ' ', '2', ':', '-', '"', 'A', '"', '', '<', ' ', ' ', ' ', '<', '!', 'v', 'g', '\\','g', '*', '5', '4', '0', '<', ' ', ' ', ' ', ' ', '>', 'g', '1', '+', '.', '"', ':', 'C', '"', '2', '5', '*', ',', ',', ',', '6', '4', '*', 'g', '1', '+', '.', '"', '>', '"', },
			{ 'v', '>', '5', '*', ',', ',', '>', ' ', '~', ':', '2', '5', '*', '-', '!', '#', 'v', '_', ':', '"', 'a', '"', '-', ':', '^', '>', '#', 'v', '_', ':', '0', '1', ' ', 'v', 'v', '0', ':', '<', '>', '`', '^', '>', ':', '0', '4', '5', '*', 'p', '3', '7', '*', 'g', ':', ' ', '4', '-', ' ', '#', 'v', '_', '', '0', '2', '5', '*', '"', '!', 't', 'a', 'b', ' ', 't', 'n', 'a', 'i', 'g', ' ', 'a', '"', },
			{ '<', '>', '_', '#', '"', '#', '^', '^', '#', ' ', ' ', ' ', ' ', ' ', ' ', '', '<', '"', '0', '', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '_', 'v', '#', '!', '`', '-', '<', '>', ' ', '1', '-', '^', 'v', '', '_', ',', '#', '!', ' ', '#', ':', '<', '"', 'Y', 'o', '"', ' ', ' ', '"', 'u', '\'','v', 'e', ' ', 'b', 'e', 'e', 'n', ' ', 's', 'n', 'a', 't', 'c', 'h', 'e', 'd', ' ', 'b', 'y', ' ', '"', },
			{ '"', 'n', 'i', ' ', 'l', 'l', 'e', 'f', ' ', 'u', 'o', 'Y', '.', '.', '.', 'e', 'e', 'e', 'e', 'e', 'e', 'E', 'E', 'E', 'E', 'E', 'E', '"', ' ', '"', 'Y', '"', '>', ':', '#', ',', '_', '', '@', '0', ' ', ' ', ' ', ' ', '>', ':', '2', '/', ':', ' ', 'v', '>', ' ', '^', ' ', ' ', ' ', '<', '>', '5', '-', '#', 'v', '_', '0', '2', '5', '*', '"', '!', 't', 'i', 'p', ' ', 'a', ' ', 'o', 't', '"', },
			{ '\\','3', '7', '*', 'g', '5', '-', '#', 'v', '_', ' ', ' ', '', '>', ' ', ' ', '', '', '>', ' ', ' ', '$', '$', '>', '$', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '>', ' ', ' ', '>', '4', '4', '*', '>', '?', '<', ' ', ' ', ' ', ' ', '_', '$', '+', '+', '+', '+', ':', ' ', ':', ' ', ' ', '0', '4', '5', '*', 'g', '\\',':', '1', '4', '5', '*', 'g', '\\',':', '3', '7', '*', 'g', },
			{ '"', ' ', 'n', 'e', 't', '"', 'v', '"', '>', '4', '-', '!', '#', '^', '_', '-', '!', '#', '^', '_', '-', '!', '#', '^', '_', '\\','0', '+', '#', ' ', '4', '5', ' ', '*', 'p', '#', '^', ' ', '#', '1', ' ', '#', '<', 'v', '>', '0', '\\','2', '/', ':', '^', '#', 'v', '5', '4', '1', 'g', ' ', '*', '5', '\\','0', '<', ' ', '>', '"', '!', 's', 'u', 'p', 'm', 'u', 'w', ' ', 'a', ' ', 'y', 'b', '"', },
			{ ':', '#', ',', '_', '', '@', '>', '"', 'a', 'e', ' ', 'n', 'e', 'e', 'b', ' ', 'e', 'v', '\'','u', 'o', 'Y', '.', '.', '.', 'P', 'L', 'U', 'G', '"', ' ', ' ', '^', ' ', ' ', ' ', '>', ':', '#', ',', '_', '', '^', '>', ' ', ' ', ' ', ' ', '>', ' ', ' ', '^', '>', '*', 'g', '-', '#', '^', '_', '0', '2', '5', '*', '>', '?', '>', 'v', '>', '"', 'l', 'l', 'i', 'k', ' ', 'u', 'o', 'Y', '"', '>', },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '>', '3', '7', '*', '1', '+', '+', 'v', '^', '"', 'Y', 'o', 'u', ' ', 'b', 'u', 'm', 'p', 'e', 'd', ' ', 't', 'h', 'e', ' ', 'w', 'u', 'm', 'p', 'u', 's', '.', '"', ' ', ' ', ' ', '^', '#', '<', '^', '"', 'e', 'd', ' ', 't', 'h', 'e', ' ', 'w', '"', '<', },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'v', '4', '0', '<', '^', '"', 'T', 'h', 'e', ' ', 'c', 'r', 'a', 'f', 't', 'y', ' ', 'w', 'u', 'm', 'p', 'u', 's', ' ', 'd', 'o', 'd', 'g', 'e', 'd', ' ', 'y', 'o', 'u', 'r', ' ', 'a', 'r', 'r', 'o', 'w', '"', ' ', ' ', '<', ' ', '"', },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '5', '>', '0', '\\','2', '5', '*', '\\','#', 'v', '_', ' ', ' ', ' ', ' ', '#', '^', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', '<', '"', '!', '"', '>', '?', '<', 'u', },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*', '^', '-', 'g', '*', '5', '<', ' ', ' ', '>', '"', '.', 'g', 'n', 'i', 'h', 't', 'y', 'n', 'a', ' ', 't', 'i', 'h', ' ', 't', '\'','n', 'd', 'i', 'd', ' ', 'w', 'o', '"', 'v', 'v', '_', '', '^', 'v', '"', 's', '"', '<', ' ', '"', },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '>', 'g', '\\','g', '1', '4', '^', '>', '_', 'v', '#', '"', ' ', ' ', '>', ':', '2', '/', ' ', ':', 'v', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '"', '0', '<', ' ', ' ', 'v', '<', ',', ':', ' ', ' ', '>', '"', 'u', 'p', 'm', '"', '^', },
		};
		*/
		/*
		String text[] = {
			"0&>:1-:v v *_.@",
			"  ^    _>\\:^",
		};
		*/

		String fileName = "bin/maelstrom/funge/test/fungus/sem";

		java.util.Vector<String> text = new java.util.Vector<String>();

	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(fileName));
	      try {
	        String line = null; //not declared within while loop
	        /*
	        * readLine is a bit quirky :
	        * it returns the content of a line MINUS the newline.
	        * it returns null only for the END of the stream.
	        * it returns an empty String if two newlines appear in a row.
	        */
	        while (( line = input.readLine()) != null){
	          text.add(line);
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }


		int maxLength = 0;
		for (Iterator<String> iter = text.iterator(); iter.hasNext();) {

			String line = iter.next();

			if (line.length() > maxLength) {
				maxLength = line.length();
			}
		}

		grid = new long [75][300];
		for (int y = 0; y < 75; y++) {
			for (int x = 0; x < 300; x++) {
				grid[y][x] = Funge.SPACE_CHAR;
			}
		}

		int y = 0;
		for (Iterator<String> iter = text.iterator(); iter.hasNext();) {

			String line = iter.next();

			for (int x = 0; x < maxLength; x++) {

				char cell;
				if (x >= line.length()) {
					cell = Funge.SPACE_CHAR;
				} else {
					cell = line.charAt(x);
				}

				grid[y][x] = cell;
			}

			y++;
		}

		Funge funge = new Funge(grid);

		//funge.setFullSpeed(false);
		//funge.setSleepTime(1000);
		funge.start();
	}

	public Funge() {
		grid = new Grid();
		initialise();
	}

	public Funge(long[][] grid) {
		this.grid = new Grid(grid);
		initialise();
	}

	public Funge(Grid grid) {
		this.grid = grid;
		initialise();
	}

	private void initialise() {
		pointer = new Pointer(grid);
		pointer.setPosition(new Vector(0, 0));
		stackStack = new StackStack();
		firstIteration = true;
	}


	public void run() {
		while (Thread.currentThread() == run) {
			computeNext();

			// Delay the process if needed.
			// Used in debugging, when coupled with a GUI, usually
			if (!fullSpeed && run != null) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException err) {
					err.printStackTrace();
				}
			}
		}
	}

	public void computeNext() {
		// The first time through we do _not_ want to move the pointer,
		// as then the top left corner will not execute. We can't just
		// rearrange the order of everything, otherwise it looks bad on
		// a GUI edtior. This, unfortunately, is the best way I can
		// think to do it
		if (firstIteration) {
			firstIteration = false;
		} else {
			pointer.move();
		}


		// Move along, skipping spaces if we are on one
		long nextCode = grid.get(pointer.getPosition());
		int i = 0;

		if (!pointer.isString() || skipStringSpace) {
    		while ((nextCode == Funge.SPACE_CHAR || (nextCode == Funge.JUMP_CHAR && !pointer.isString())) && i < Funge.MAX_SPACE_SKIPS) {
    			i++;

    			// If we hit a jump character...
    			if (nextCode == Funge.JUMP_CHAR) {
        			nextCode = 0;
        			// Loop till we find the closing jump character
        			while (nextCode != Funge.JUMP_CHAR) {
        				pointer.move();
        				nextCode = grid.get(pointer.getPosition());
        			}
    			}

    			pointer.move();
    			nextCode = grid.get(pointer.getPosition());
    		}
		}
		skipStringSpace = false;

		// Get the operator
		long operatorCode = grid.get(pointer.getPosition());


		// Process the operator, or push it to the stack if in string mode
		if (pointer.isString() && operatorCode != STRING_TOGGLE) {
			Funge.log(this, "computeNext()", "Processing string: '" + (char) operatorCode + "' @ " + pointer.getPosition().toString());
			stackStack.getStack().push(operatorCode);

			if (operatorCode == Funge.SPACE_CHAR) {
				skipStringSpace = true;
			}

		} else {
			Funge.log(this, "computeNext()", "Processing operation: '" + (char) operatorCode + "' @ " + pointer.getPosition().toString());
			Operator operator = Operators.get(operatorCode);
			operator.perform(this);
		}
	}


	public void start() {
		Funge.log(this, "start()", " ** Starting ** ");

		pointer.setPosition(new Vector(0, 0));
		pointer.setDirection(new Vector(1, 0));
		resume();

		this.skipStringSpace = false;

		fireRunStateChangeEvent(RunStateChangeEvent.START);
	}

	public void resume() {
		run = new Thread(this, "Befunge");
		run.start();
		fireRunStateChangeEvent(RunStateChangeEvent.RESUME);
	}

	public void pause() {
		run = null;
		fireRunStateChangeEvent(RunStateChangeEvent.PAUSE);
	}

	public void stop() {
		pause();
		fireRunStateChangeEvent(RunStateChangeEvent.STOP);
		Funge.log(this, "start()", " ** Stopping ** ");
	}

	public void quit(long returnVal) {
		pause();

		fireRunStateChangeEvent(RunStateChangeEvent.QUIT, returnVal);
	}


	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}


	public Pointer getPointer() {
		return pointer;
	}

	public void setPointer(Pointer pointer) {
		this.pointer = pointer;
	}


	public Stack getStack() {
		return stackStack.getStack();
	}

	public StackStack getStackStack() {
		return stackStack;
	}

	public int getSleepTime() {
		return this.sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public boolean issFullSpeed() {
		return fullSpeed;
	}

	public void setFullSpeed(boolean fullSpeed) {
		this.fullSpeed = fullSpeed;
	}

	public void setStackStack(StackStack stacks) {
		stackStack = stacks;
	}


	/**
	 * Adds a listener for this grid.
	 *
	 * @param listener
	 *        The object to notify when this grid is modified
	 */
	public synchronized void addRunStateChangeListener(RunStateChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener for the grid.
	 *
	 * @param listener
	 *        The object to remove from the notify list
	 */
	public synchronized void removeRunStateChangeListener(RunStateChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all listening objects that the pointer has moved
	 */
	private synchronized void fireRunStateChangeEvent(int runState) {
		fireRunStateChangeEvent(runState, 0);
	}
	private synchronized void fireRunStateChangeEvent(int runState, long returnVal) {
		RunStateChangeEvent event = new RunStateChangeEvent(this, runState, returnVal);
		Iterator<RunStateChangeListener> iter = listeners.iterator();

		while (iter.hasNext()) {
			iter.next().runStateChanged(event);
		}
	}


	public static void log(Object caller, String method, String text) {
		//System.out.println("	" + caller.getClass().getCanonicalName() + ": " + method + ": " + text);
	}


}
