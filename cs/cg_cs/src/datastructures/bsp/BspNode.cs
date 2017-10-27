using OpenTK;
using System.Collections.Generic;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;

namespace computergraphics
{
	/***
	 * Scene graph node to hold a BSP tree.
	 * */
	public class BspNode : LeafNode
	{
		/**
		 * BSP tree root node
		 * */
		private BspTreeNode rootNode;

		/**
		 * Rendering normal
		 * */
		private static Vector3 normal = Vector3.UnitZ;

		/**
		 * List of points used to create the tree.
		 * */
		private List<Vector3> points;

		/**
 		 * Observer position
 		 */
		private Vector3 eye;

		/**
		 * Flags to control the display information.
		 * */
		private bool showPoints = true;
		private bool showPlanes = true;
		private bool showBackToFront = true;
		private bool showElements = true;

		/**
		 * VBOs
		 */
		private VertexBufferObject vboPoints = new VertexBufferObject();
		private VertexBufferObject vboPlanes = new VertexBufferObject();
		private VertexBufferObject vboBack2FrontPath = new VertexBufferObject();
		private VertexBufferObject vboElements = new VertexBufferObject();

		public bool ShowPoints
		{
			get { return showPoints; }
			set { showPoints = value; }
		}

		public bool ShowPlanes
		{
			get { return showPlanes; }
			set { showPlanes = value; }
		}

		public bool ShowBack2Front
		{
			get { return showBackToFront; }
			set { showBackToFront = value; }
		}

		public bool ShowElements
		{
			get { return showElements; }
			set { showElements = value; }
		}

		public BspNode(BspTreeNode rootNode, List<Vector3> points, List<int> sortedPoints, Vector3 eye)
		{
			this.rootNode = rootNode;
			this.points = points;
			this.eye = eye;

			vboPoints.Setup(CreateVBOPoints(), PrimitiveType.Points);
			vboBack2FrontPath.Setup(CreateVBOBack2Front(sortedPoints), PrimitiveType.LineStrip);
			vboPlanes.Setup(CreateVBOPlanes(rootNode, 0.7f), PrimitiveType.Lines);
			vboElements.Setup(CreateVBOElements(rootNode), PrimitiveType.Lines);
		}

		/**
		 * Draw content in OpenGL
		 * */
		public override void DrawGL(RenderMode mode, Matrix4 modelMatrix)
		{
			if (mode == RenderMode.REGULAR)
			{
				if (showPlanes)
				{
					vboPlanes.Draw();
				}

				if (showPoints)
				{
					vboPoints.Draw();
				}

				if (showBackToFront)
				{
					vboBack2FrontPath.Draw();
				}

				if (showElements)
				{
					vboElements.Draw();
				}
			}
		}

		public override void TimerTick(int counter)
		{
		}

		/**
         * create VBO render vertices for the data points
         **/
		private List<RenderVertex> CreateVBOPoints()
		{
			List<RenderVertex> renderVertices = new List<RenderVertex>();
			foreach (Vector3 p in points)
			{
				renderVertices.Add(new RenderVertex(p, normal, Color4.LightGreen));
			}
			renderVertices.Add(new RenderVertex(eye, normal, Color4.Yellow));
			return renderVertices;
		}

		/**
		 * Create VBO render vertices for the back-to-front-sorting 
		 * */
		private List<RenderVertex> CreateVBOBack2Front(List<int> sortedPoints)
		{
			List<RenderVertex> renderVertices = new List<RenderVertex>();
			foreach (int index in sortedPoints)
			{
				renderVertices.Add(new RenderVertex(points[index], normal, Color4.Yellow));
			}
			renderVertices.Add(new RenderVertex(eye, normal, Color4.Yellow));
			return renderVertices;
		}

		/**
 		 * Create VBO render vertices for the BSP tree planes.
 		 * */
		private List<RenderVertex> CreateVBOPlanes(BspTreeNode node, float scale)
		{
			List<RenderVertex> renderVertices = new List<RenderVertex>();
			if (node == null)
			{
				return renderVertices;
			}
			Vector3 tangent = Vector3.Multiply(new Vector3(node.N.Y, -node.N.X, 0), scale);
			renderVertices.Add(new RenderVertex(Vector3.Add(node.P, tangent), normal, Color4.White));
			renderVertices.Add(new RenderVertex(Vector3.Subtract(node.P, tangent), normal, Color4.White));
			renderVertices.Add(new RenderVertex(node.P, normal, Color4.White));
			renderVertices.Add(new RenderVertex(Vector3.Add(node.P, Vector3.Multiply(node.N, scale * 0.3f)), normal, Color4.White));

			renderVertices.AddRange(CreateVBOPlanes(node.GetChild(BspTreeNode.Orientation.POSITIVE), scale * 0.5f));
			renderVertices.AddRange(CreateVBOPlanes(node.GetChild(BspTreeNode.Orientation.NEGATIVE), scale * 0.5f));

			return renderVertices;
		}

		/**
		 * Create VBO render vertices for the elements in a node (front and back).
		 */
		private List<RenderVertex> CreateVBOElements(BspTreeNode node)
		{
			List<RenderVertex> renderVertices = new List<RenderVertex>();
			if (node == null)
			{
				return renderVertices;
			}
			for (int orientation = 0; orientation < 2; orientation++)
			{
				Color4 color = (orientation == 0) ? Color4.Magenta : Color4.Orange;
				for (int i = 0; i < node.getNumberOfElements((BspTreeNode.Orientation)orientation); i++)
				{
					int index = node.getElement((BspTreeNode.Orientation)orientation, i);
					renderVertices.Add(new RenderVertex(node.P, normal, color));
					renderVertices.Add(new RenderVertex(points[index], normal, color));
				}
			}
			renderVertices.AddRange(CreateVBOElements(node.GetChild(BspTreeNode.Orientation.POSITIVE)));
			renderVertices.AddRange(CreateVBOElements(node.GetChild(BspTreeNode.Orientation.NEGATIVE)));
			return renderVertices;
		}
	}
}

