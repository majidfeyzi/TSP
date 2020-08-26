package grid;

/**
 * Context abstract class to notify grid changes.
 * @author Majid Feyzi
 * */
public abstract class Context {

    /**
     * This method notify result of algorithm after finish.
     * @param result result of algorithm
     * */
    public void onSolveComplete(String result) {}
}
