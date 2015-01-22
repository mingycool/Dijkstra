import java.util.*; // For ArrayList

public final class FibonacciHeap{

    public static class Node{
        private int     mDegree = 0;       // Number of children
        private boolean mChildCut = false; // if children has been cut

        private Node mNext;   // Next and previous elements in the list
        private Node mPrev;

        private Node mParent; // Parent in the tree, if any.
        private Node mChild;  // Child node, if any.

        private int mKey;     // key for the entry node, in our project, it is the index of node in the graph
        private int mPriority; // Its priority, in our project it is the current shortest path to the node

        public int getValue() {
            return mKey;
        }

        public void setValue(int value) {// set the value
            mKey = value;
        }

        public int getPriority() {// get the priority of node
            return mPriority;
        }

        public Node(int elem, int priority) {
            mNext = mPrev = this; //when create new node, its next and prev is itself
            mKey = elem;
            mPriority = priority;
        }
    }


    private Node mMin = null;// min element
    private int mSize = 0;// number of nodes in Fheap
    
    public boolean isEmpty() {
    	return mMin == null;
    }

    public Node min() {
    	if (isEmpty())
    		throw new NoSuchElementException("Heap is empty.");
    	return mMin;
    }
       
    public int size() {// get size
    	return mSize;
    }

    public Node insert(int key, int priority) {   // insert into fibheap	
        Node result = new Node(key, priority);
        mMin = mergeLists(mMin, result); //merge the new entry node into the top level list
        ++mSize;
        return result;  //return reference to the new node
    }

 
    public Node deleteMin() {
        if (isEmpty()){// delet min element
            throw new NoSuchElementException("Heap is empty.");
        }
        
        --mSize;

        Node minElem = mMin; //temp reference to min entry we will return later

        //remove mMin from the FHeap
        if (mMin.mNext == mMin) { 
        	// Case one, only one entry in top level list
            mMin = null;
        }
        else { 
        	// Case two, there are other entries in the list of roots, then remove the Min entry 
        	// from the list and arbitrary set the Min.
            mMin.mPrev.mNext = mMin.mNext;
            mMin.mNext.mPrev = mMin.mPrev;
            mMin = mMin.mNext; // Arbitrary element of the root list.
        }

        // Next, clear the parent fields of all of the min element's children,
        // since they're about to become roots.
        if (minElem.mChild != null) {
            Node curr = minElem.mChild;
            do {
                curr.mParent = null;
                curr = curr.mNext;
            } while (curr != minElem.mChild);
        }

        // Next, splice the children of the root node into the top level list, 
        // then set mMin to point somewhere in that list.
        mMin = mergeLists(mMin, minElem.mChild);

        
        // If there are no entries left, then the min entry is the only entry in the FHeap, we do not need the pairwise combination
        if (mMin == null) return minElem;

        List<Node> treeTable = new ArrayList<Node>();  //used to record roots with different degree
        List<Node> toVisit = new ArrayList<Node>(); //used to record all the entry in the root list

        //Add all node in the top level list to the toVisit
        for (Node curr = mMin; toVisit.isEmpty() || toVisit.get(0) != curr; curr = curr.mNext){
            toVisit.add(curr);
        }

        // Traverse this list
        for (Node curr: toVisit) {
            while (true) {
                // Ensure that the list is long enough to hold degree
                while (curr.mDegree >= treeTable.size()){
                    treeTable.add(null);
                }

                // if there is no tree with the same degree we visited before, just record this node
                if (treeTable.get(curr.mDegree) == null) {
                    treeTable.set(curr.mDegree, curr);
                    break; //note this is the only way we can break the while loop
                }

                // Otherwise, merge with what's there. 
                Node other = treeTable.get(curr.mDegree);
                treeTable.set(curr.mDegree, null); // Clear the slot

                // Determine which of the two trees has the smaller root
                Node min = (other.mPriority < curr.mPriority)? other : curr;
                Node max = (other.mPriority < curr.mPriority)? curr  : other;

                // Break max out of the root list, then merge it into min's child list.
                max.mNext.mPrev = max.mPrev;
                max.mPrev.mNext = max.mNext;
                // Make max a singleton so that we can merge it.
                max.mNext = max.mPrev = max;
                min.mChild = mergeLists(min.mChild, max);
                
                // Re-parent max.
                max.mParent = min;
                // max can now lose another child. */
                max.mChildCut = false;
                // Increase min's degree
                ++min.mDegree;

                // notice the new tree may still have some other tree with the same degree
                // it is still in the while loop and check whether there will be another pairwise combination
                curr = min;
            }

            // Update the global min 
            if (curr.mPriority <= mMin.mPriority) mMin = curr;
        }
        return minElem;
    }

    /**
     * Decreases the key of the specified element to a new priority.
     * Please make sure the entry is in the FHeap, I do not check this. 
     */
    public void decreaseKey(Node node, int newPriority) {
    	
    	
        if (newPriority > node.mPriority){
            throw new IllegalArgumentException("New priority exceeds old.");
        }

        node.mPriority = newPriority;
        // if the node have parent and after decreaseKey its priority is < than its parent
        // we have to cut this node out and merge it to top level list 
        if (node.mParent != null && node.mPriority <= node.mParent.mPriority){
            cutNode(node);
        }

        if (node.mPriority <= mMin.mPriority)
            mMin = node;
    }
    
    /**
     * Deletes this Entry from the Fibonacci heap that contains it.
     * Please make sure the entry is in the FHeap, I do not check this. 
     */
    public void delete(Node node) {
    	// just decrease the specific entry's priority to -infinity and then deleteMin
        decreaseKey(node, Integer.MIN_VALUE);
        deleteMin();
    }

    /**
     * Merge two FHeap to get a new FHeap
     */
    public static FibonacciHeap merge(FibonacciHeap one, FibonacciHeap two) {
        FibonacciHeap result = new FibonacciHeap();
        result.mMin = mergeLists(one.mMin, two.mMin);
        result.mSize = one.mSize + two.mSize;

        //clear old FHeap
        one.mSize = two.mSize = 0;
        one.mMin  = null;
        two.mMin  = null;

        return result;
    }
    
    /**
     * mergeLists merge two circularly double linked lists into one
     * need two min-pointer to those two lists, and return the min-pointer to the merged one
     */
    private static Node mergeLists(Node one, Node two) {
        if (one == null && two == null) { // Both null, resulting list is null.
            return null;
        }
        else if (one != null && two == null) { // Two is null, result is one.
            return one;
        }
        else if (one == null && two != null) { // One is null, result is two.
            return two;
        }
        else { // Both non-null; actually do the splice.
            Node oneNext = one.mNext;
            one.mNext = two.mNext;
            one.mNext.mPrev = one;
            two.mNext = oneNext;
            two.mNext.mPrev = two;

            return one.mPriority < two.mPriority? one : two;
        }
    }

    private void cutNode(Node node) {
        node.mChildCut = false;  //update ChildCut

        if (node.mParent == null) return; //if the node have no parent, done

        //the node have parent, next if it has siblings, update them
        if (node.mNext != node) {
            node.mNext.mPrev = node.mPrev;
            node.mPrev.mNext = node.mNext;
        }

        // If the node is the one identified by its parent as its child,
        // we need to rewrite that pointer to point to some arbitrary other child.
        if (node.mParent.mChild == node) {
            // If there are any other children, pick one of them arbitrarily. 
            if (node.mNext != node) {
                node.mParent.mChild = node.mNext;
            }
            // Otherwise, there aren't any other children 
            else {
                node.mParent.mChild = null;
            }
        }
        
        --node.mParent.mDegree; //entry's parent lost a child 

        //merge entry into the top level list
        node.mPrev = node.mNext = node;
        mMin = mergeLists(mMin, node);

        //recursively check entry's parent, whether cut it or update mChildCut
        if (node.mParent.mChildCut){
            cutNode(node.mParent);
        }
        else{
            node.mParent.mChildCut = true;
        }

        node.mParent = null;  //update mParent
    }
}
