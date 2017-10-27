using OpenTK;
using System.Collections.Generic;

namespace computergraphics
{
	/**
	 * Tools to work with BSP trees
	 * */
	public class BspTreeToolsDummy
	{
		/**
   		 * Recursively create a BSP tree for a given set of points.
   		 * 
   		 * @param parentNode
   		 *    	      Parent scene graph node
   		 * @param allPoints
   		 *          List with all point positions in the dataset
   		 * @param Set
   		 *          if indices used in the current recursive call
   		 */
		public virtual BspTreeNode CreateBspTree(BspTreeNode parentNode, List<Vector3> allPoints, List<int> pointIndices)
		{

			// YOUR CODE GOES HERE!

			return null;
		}

		/**
   		 * Compute the back-to-front ordering for all points in 'points' based on the
   		 * tree in 'node' and the given eye position
   		 * 
   		 * @param node
   		 *          Root node of the BSP tree
   		 * @param points
   		 *          List of points to be considered
   		 * @param eye
   		 *          Observer position
   		 * @return Sorted (back-to-front) list of points
   		 */
		public virtual List<int> GetBackToFront(BspTreeNode node, List<Vector3> points, Vector3 eye)
		{

			// YOUR CODE GOES HERE!

			return null;
		}
	}
}

