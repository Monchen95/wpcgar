using OpenTK;

namespace computergraphics
{
	/**
	 * Shared interfaces for triangle meshes.
	 * */
	public interface ITriangleMesh
	{

		/**
		 * Add vertex. Return index of vertex in vertex list.
		 * */
		int AddVertex(Vector3 vertex);

		/**
		 * Getter: Number of vertices
		 */
		int GetNumberOfVertices();

		/**
		 * Getter: Vertex
		 * */
		Vector3 GetVertex(int vertexIndex);

		/**
		 * Add triangle by indices.
		 * */
		void AddTriangle(int a, int b, int c);

		/**
		 * Add triangle.
		 * */
		void AddTriangle(Triangle t);

		/**
		 * Getter: number of triangles
		 * */
		int GetNumberOfTriangles();

		/**
 		 * Getter for a triangle
 		 * */
		Triangle GetTriangle(int triangleIndex);

		/**
		 * Clear datastructure.
		 * */
		void Clear();

		/**
		 * Compute normals for all triangles.
		 * */
		void ComputeTriangleNormals();

		/**
		 * Returns a color for the mesh.
		 * */
		Vector4 GetColor();

		/**
		 * Returns a color for the mesh.
		 * */
		void SetColor(Vector4 color);

		/**
		 * Add texture coordinate.
		 * */
		void AddTextureCoordinate(Vector2 texCoord);

		/**
		 * Get a texture coordinate
		 * */
		Vector2 GetTextureCoordinate(int index);

		/**
		 * Getter for the texture object*/
		Texture GetTexture();

		/**
		 * Set a texture object for the mesh.
		 * */
		void SetTexture(Texture texture);

		/**
		 * Create a mesh of the shadow polygons.
		 * 
		 * lightPosition: Position of the light source.
		 * extend: Length of the polygons
		 * shadowPolygonMesh: Result is put in there
		 * */
		void CreateShadowPolygons(Vector3 lightPosition, float extend, ITriangleMesh shadowPolygonMesh);
	}
}

