using OpenTK;

namespace computergraphics
{
	/**
	 * A facet has a reference to one of its half edges. This datastructure
	 * represents a general mesh (triangle, quad, ...). However, we only use
	 * triangle meshes here.
	 * 
	 * @author Philipp Jenke
	 *
	 */
	public class HalfEdgeTriangle
	{

		/**
		 * One of the half edges around the facet.
		 */
		private HalfEdge halfEdge;

		/**
		 * Facet normal
		 */
		private Vector3 normal;

		public HalfEdge getHalfEdge()
		{
			return halfEdge;
		}

		public void setHalfEdge(HalfEdge halfEdge)
		{
			this.halfEdge = halfEdge;
		}

	  public override string ToString()
		{
			return "Triangular Facet";
		}

		public Vector3 getNormal()
		{
			return normal;
		}

		public void setNormal(Vector3 normal)
		{
			this.normal = normal;
		}

		/**
		 * Compute the area of the facet. Area of the facet.
		 * 
		 * @return Area of the triangle.
		 */
		public double getArea()
		{
			Vector3 v0 = halfEdge.getStartVertex().getPosition();
			Vector3 v1 = halfEdge.getNext().getStartVertex().getPosition();
			Vector3 v2 = halfEdge.getNext().getNext().getStartVertex().getPosition();
			return Vector3.Cross(Vector3.Subtract(v1, v0), (Vector3.Subtract(v2, v0))).Length / 2.0;
		}

		/**
		 * Compute the centroid (center of mass) of the triangle.
		 * 
		 * @return Centroid of the triangle.
		 */
		public Vector3 getCentroid()
		{
			Vector3 v0 = halfEdge.getStartVertex().getPosition();
			Vector3 v1 = halfEdge.getNext().getStartVertex().getPosition();
			Vector3 v2 = halfEdge.getNext().getNext().getStartVertex().getPosition();
			return Vector3.Multiply(Vector3.Add(Vector3.Add(v0, v1), v2), 1.0f / 3.0f);
		}

		public int getVertexIndex(int i)
		{
			return -1;
		}

		public int getTexCoordIndex(int i)
		{
			return -1;
		}
	}
}