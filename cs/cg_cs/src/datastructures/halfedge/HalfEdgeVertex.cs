using OpenTK;

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

namespace computergraphics
{

	/**
	 * Representation of a vertex.
	 * 
	 * @author Philipp Jenke
	 */
	public class HalfEdgeVertex
	{

		/**
		 * 3D position of the vertex.
		 */
		private Vector3 position = new Vector3(0, 0, 0);

		/**
		 * (Normalized) normal direction of the vertex.
		 */
		private Vector3 normal = new Vector3(1, 0, 0);

		/**
		 * Color value at the vertex
		 */
		private Vector3 color = new Vector3(0, 0, 0);

		/**
		 * Reference to one of the outgoing half edges.
		 */
		private HalfEdge halfEgde = null;

		/**
		 * Constructor.
		 * 
		 * @param position
		 *          Initial value for position.
		 */
		public HalfEdgeVertex(Vector3 position)
		{
			this.position = position;
		}

		/**
		 * Constructor.
		 * 
		 * @param position
		 *          Initial value for position.
		 * @param normal
		 *          Initial value for normal.
		 */
		public HalfEdgeVertex(Vector3 position, Vector3 normal)
		{
			this.position = position;
			this.normal = normal;
		}

		/**
		 * Constructor.
		 * 
		 * @param position
		 *          Initial value for position.
		 * @param normal
		 *          Initial value for normal.
		 */
		public HalfEdgeVertex(Vector3 position, Vector3 normal, Vector3 color)
		{
			this.position = position;
			this.normal = normal;
			this.color = color;
		}

		public Vector3 getPosition()
		{
			return position;
		}

		public Vector3 getNormal()
		{
			return normal;
		}

		public Vector3 getColor()
		{
			return color;
		}

		public void setNormal(Vector3 normal)
		{
			this.normal = normal;
		}

		public void setColor(Vector3 color)
		{
			this.color = color;
		}

		public HalfEdge getHalfEdge()
		{
			return halfEgde;
		}

		public void setHalfEgde(HalfEdge halfEgde)
		{
			this.halfEgde = halfEgde;
		}

		public override string ToString()
		{
			return "Vertex";
		}
	}
}
