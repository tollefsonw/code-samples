package ajeffrey.teaching.util.list;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An implementation of a non-blocking mutable list.
 * @author Wade Tollefson
 * @version 1.0.1
 * @see ImmutableList
 */
 
public class OptimisticMutableList {

    private final AtomicReference<ImmutableList> contents
    					= new AtomicReference<ImmutableList>();
    public OptimisticMutableList()
    { contents.set(new ImmutableListEmpty()); } 
    
    /**
     * Add a new element to the list.
     * @param element the object to add
     */
    public void add (final Object element) 
    { 
    	ImmutableList oldContents;
    	ImmutableList newContents;
        do {
            oldContents = contents.get();
	    newContents = oldContents.cons(element);
	} while (!contents.compareAndSet(oldContents, newContents));
    }

    /**
     * Remove an element from the list.
     * @param element the object to remove
     * @exception NoSuchElementException thrown if the element 
     *   is not in the list.
     */
    public void remove (final Object element) 
    {	
    	ImmutableList oldContents;
    	ImmutableList newContents;
        do {
            oldContents = contents.get();
	    newContents = oldContents.remove(element);
	} while (!contents.compareAndSet(oldContents, newContents));
    }

    /**
     * Get an iterator over the elements in the list.
     * @return an iterator over the elements in the list
     */
    public Iterator iterator () { 
	return contents.get().iterator ();
    }

    /**
     * The size of the list.
     * @return the size of the list
     */
    public int size () {
	return contents.get().size ();
    }

    public String toString () {
	return "{ contents = " + contents + " }";
    }

}
