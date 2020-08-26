package grid.history;

import java.util.Stack;

/**
 * Actions that user can do on the grid.
 * This actions are keeping as history to make able users to do undo.
 * @author Majid Feyzi
 * @see Action
 * */
public class History {

    // Use history stack to keep history of actions
    private final Stack<Action> stack = new Stack<>();

    /**
     * Add new action to history
     * @param action Action to keep
     * */
    public void push(Action action) {
        stack.push(action);
    }

    /**
     * Get last action that has been done until now
     * @return Last action that has been done until now
     * */
    public Action pop() {
        return stack.pop();
    }

    /**
     * Clear all items of history
     * */
    public void clear() {
        stack.clear();
    }

    /**
     * Get size of history
     * */
    public int size() {
        return stack.size();
    }
}
