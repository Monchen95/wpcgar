﻿using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using System.Collections.Generic;

namespace computergraphics
{
	/** 
	 * Draws a cube.
	 * */
	public class CubeNode : LeafNode
	{

		/**
		 * Cube is centered at the origin and its sidelength is twice this value.
		 * */
		private float lX;
		private float lY;
		private float lZ;

		private VertexBufferObject vbo = new VertexBufferObject();

		public CubeNode(float sideLength) : this(sideLength, sideLength, sideLength)
		{
		}

		public CubeNode(float lX, float lY, float lZ) : this(lX, lY, lZ, new Vector3(0, 0, 0))
		{
		}

		public CubeNode(float lX, float lY, float lZ, Vector3 center)
		{
			this.lX = lX / 2.0f;
			this.lY = lY / 2.0f;
			this.lZ = lZ / 2.0f;
			CreateVBO(center);
		}

		public override void DrawGL(RenderMode mode, Matrix4 modelMatrix)
		{
			if (mode == RenderMode.REGULAR)
			{
				vbo.Draw();
			}
		}

		public override void TimerTick(int counter)
		{
		}

		void CreateVBO(Vector3 offset)
		{
			List<RenderVertex> renderVertices = new List<RenderVertex>();

			Vector3 p0 = Vector3.Add(new Vector3(-lX, -lY, -lZ), offset);
			Vector3 p1 = Vector3.Add(new Vector3(lX, -lY, -lZ), offset);
			Vector3 p2 = Vector3.Add(new Vector3(lX, lY, -lZ), offset);
			Vector3 p3 = Vector3.Add(new Vector3(-lX, lY, -lZ), offset);
			Vector3 p4 = Vector3.Add(new Vector3(-lX, -lY, lZ), offset);
			Vector3 p5 = Vector3.Add(new Vector3(lX, -lY, lZ), offset);
			Vector3 p6 = Vector3.Add(new Vector3(lX, lY, lZ), offset);
			Vector3 p7 = Vector3.Add(new Vector3(-lX, lY, lZ), offset);
			Vector3 n0 = new Vector3(0, 0, -1);
			Vector3 n1 = new Vector3(1, 0, 0);
			Vector3 n2 = new Vector3(0, 0, 1);
			Vector3 n3 = new Vector3(-1, 0, 0);
			Vector3 n4 = new Vector3(0, 1, 0);
			Vector3 n5 = new Vector3(0, -1, 0);

			Color4 color = Color4.Aquamarine;
			AddSideVertices(renderVertices, p0, p1, p2, p3, n0, color);
			AddSideVertices(renderVertices, p1, p5, p6, p2, n1, color);
			AddSideVertices(renderVertices, p4, p7, p6, p5, n2, color);
			AddSideVertices(renderVertices, p0, p3, p7, p4, n3, color);
			AddSideVertices(renderVertices, p2, p6, p7, p3, n4, color);
			AddSideVertices(renderVertices, p5, p1, p0, p4, n5, color);

			vbo.Setup(renderVertices, PrimitiveType.Quads);
		}

		/**
		 * Add 4 vertices to the render list.
		 * */
		private void AddSideVertices(List<RenderVertex> renderVertices, Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3, Vector3 normal, Color4 color)
		{
			renderVertices.Add(new RenderVertex(p3, normal, color));
			renderVertices.Add(new RenderVertex(p2, normal, color));
			renderVertices.Add(new RenderVertex(p1, normal, color));
			renderVertices.Add(new RenderVertex(p0, normal, color));
		}
	}
}

