using OpenTK;
using System.Collections.Generic;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;

namespace computergraphics
{
	/** 
	 * Draw a coordinate frame in the scene to allow orientation
	*/
	public class CoordinateFrameNode : LeafNode
	{

		private VertexBufferObject vbo = new VertexBufferObject();

		private float scale = 1;

		public CoordinateFrameNode() : this(1)
		{
		}

		public CoordinateFrameNode(float scale)
		{
			CreateVBO();
		}

		private void CreateVBO()
		{
			List<RenderVertex> renderVertices = new List<RenderVertex>();
			renderVertices.Add(new RenderVertex(new Vector3(0, 0, 0), new Vector3(0, 1, 0), Color4.Red));
			renderVertices.Add(new RenderVertex(new Vector3(scale, 0, 0), new Vector3(0, 1, 0), Color4.Red));
			renderVertices.Add(new RenderVertex(new Vector3(0, 0, 0), new Vector3(0, 1, 0), Color4.Red));
			renderVertices.Add(new RenderVertex(new Vector3(0, scale, 0), new Vector3(0, 1, 0), Color4.Green));
			renderVertices.Add(new RenderVertex(new Vector3(0, 0, 0), new Vector3(0, 1, 0), Color4.Blue));
			renderVertices.Add(new RenderVertex(new Vector3(0, 0, scale), new Vector3(0, 1, 0), Color4.Green));
			vbo.Setup(renderVertices, PrimitiveType.Lines);
		}

		public override void DrawGL(RenderMode mode, Matrix4 modelMatrix)
		{
			vbo.Draw();
		}

		public override void TimerTick(int counter)
		{
		}
	}
}
