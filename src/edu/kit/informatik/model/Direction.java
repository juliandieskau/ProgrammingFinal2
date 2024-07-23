package edu.kit.informatik.model;

/**
 * Gives specific types for the four cardinal directions to know where to move.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public enum Direction {
    /** To the north */
    N {
        /**
         * Overrides the Direction intArray() so that its values represent the north-direction.
         * 
         * @return an int-array with two values, the first being the value for the rows and the second
         *      for the columns.
         */
        public int[] intArray() {
            int[] r = {-1, 0};
            return r;
        }
    },
    /** To the east */
    E {
        /**
         * Overrides the Direction intArray() so that its values represent the east-direction.
         * 
         * @return an int-array with two values, the first being the value for the rows and the second
         *      for the columns.
         */
        public int[] intArray() {
            int[] r = {0, 1};
            return r;
        }
    },
    /** To the south */
    S {
        /**
         * Overrides the Direction intArray() so that its values represent the south-direction.
         * 
         * @return an int-array with two values, the first being the value for the rows and the second
         *      for the columns.
         */
        public int[] intArray() {
            int[] r = {1, 0};
            return r;
        }
    },
    /** To the west */
    W {
        /**
         * Overrides the Direction intArray() so that its values represent the west-direction.
         * 
         * @return an int-array with two values, the first being the value for the rows and the second
         *      for the columns.
         */
        public int[] intArray() {
            int[] r = {0, -1};
            return r;
        }
    };
    
    /**
     * Converts the cardinal direction in a 2-dimensional vector represented by an array of length 2.
     * intArray[0] is the direction for the rows, so 1 (S) is down and -1 is up (N)
     * intArray[1] is the direction for the columns, so 1 is to the right (E) and -1 to the left (W)
     * 
     * @return int[] that can be used to simply add the values to find Squares in a modular direction
     */
    public abstract int[] intArray();
}
