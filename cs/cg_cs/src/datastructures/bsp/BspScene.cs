using System;
using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using OpenTK.Input;
using System.Collections.Generic;

namespace computergraphics
{
	/**
	 * Testing the BSP tree implementation.
	 * */
	public class BspScene : Scene
	{
		/**
         * Scene graph node with the BSP tree.
         * */
		private BspNode node;

		public BspScene() : base(100, Shader.ShaderMode.NO_LIGHTING, RenderMode.REGULAR)
		{
		}

		/**
		 * Initialization.
		 */
		public override void SetupScenegraph()
		{
			Random random = new Random();
			int numberOfPoints = 10;
			List<Vector3> points = new List<Vector3>();
			List<int> pointIndices = new List<int>();
			for (int i = 0; i < numberOfPoints; i++)
			{
				points.Add(new Vector3((float)(2 * random.NextDouble() - 1), (float)(2 * random.NextDouble() - 1), 0));
				pointIndices.Add(i);
			}

			BspTreeToolsDummy tools = new BspTreeToolsDummy();
			BspTreeNode rootNode = tools.CreateBspTree(null, points, pointIndices);
			if (rootNode != null)
			{
				Vector3 observer = new Vector3(1, 1, 0);
				List<int> sortedPoints = tools.GetBackToFront(rootNode, points, observer);
				node = new BspNode(rootNode, points, sortedPoints, observer);
				GetRoot().AddChild(node);
			}

			GetRoot().LightPosition = new Vector3(0, 0, 1);
			GetRoot().BackgroundColor = Color4.DarkGray;
			GL.PointSize(5);
			GL.LineWidth(5);
		}

		public override void KeyPressed(Key key)
		{
			switch (key)
			{
				case Key.P:
					node.ShowPoints = !node.ShowPoints;
					break;
				case Key.E:
					node.ShowElements = !node.ShowElements;
					break;
				case Key.L:
					node.ShowPlanes = !node.ShowPlanes;
					break;
				case Key.B:
					node.ShowBack2Front = !node.ShowBack2Front;
					break;
			}
		}
	}
}

