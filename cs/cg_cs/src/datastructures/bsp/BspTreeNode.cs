using OpenTK;
using System.Collections.Generic;

namespace computergraphics
{
	/**
	 * Represents a node (plane) in a BSP tree.
	 * */
	public class BspTreeNode
	{
		/**
		 * Enum for the orientaiton
		 */
		public enum Orientation
		{
			POSITIVE, NEGATIVE
		}

		/**
		 * Child node on positive half space (in normal direction).
		 * */
		private BspTreeNode[] children = new BspTreeNode[2];

		/**
		 * Plane point.
		 * */
		private Vector3 p;

		/**
		 * Element indices in positive and negative subspace.
		 * */
		private List<int>[] elements = { new List<int>(), new List<int>() };

		/**
		 * Plane normal
		 * */
		private Vector3 n;

		public Vector3 P
		{
			get { return p; }
			set { p = value; }
		}

		public Vector3 N
		{
			get { return n; }
			set { n = value; }
		}

		public BspTreeNode()
		{
		}

		/**
		 * Returns true if the point is on the positive side of the plane, false otherwise.
		 * */
		public bool IsPositive(Vector3 p)
		{
			return Vector3.Dot(p, N) - Vector3.Dot(P, N) > 0;
		}

		public void SetChild(Orientation orientation, BspTreeNode childNode)
		{
			children[(int)orientation] = childNode;
		}

		/**
         * Get the child node in the specified orientation.
         * */
		public BspTreeNode GetChild(Orientation orientation)
		{
			return children[(int)orientation];
		}

		/**
		 * Add an elements (data point) to a specified side of a BSP plane.
		 * */
		public void AddElement(Orientation orientation, int index)
		{
			elements[(int)orientation].Add(index);
		}

		/**
		 * Get number of elements (data points) on a specified side of a BSP plane.
		 * */
		public int getNumberOfElements(Orientation orientation)
		{
			return elements[(int)orientation].Count;
		}

		/**
		 * Get an element (data point) on a specified side of a BSP plane.
		 * */
		public int getElement(Orientation orientation, int index)
		{
			return elements[(int)orientation][index];
		}
	}
}

