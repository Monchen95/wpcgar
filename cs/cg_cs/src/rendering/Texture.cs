﻿using System;
using System.Drawing;
using System.Drawing.Imaging;
using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;

namespace computergraphics
{
	/**
	 * Handles an OpenGL-Texture.
	 * */
	public class Texture
	{

		/**
		 * OpenGL texture id.
		 * */
		private int textureId = -1;

		/**
		 * Texture filename
		 * */
		private string filename = null;

		public Texture (string filename)
		{
			this.filename = filename;
		}

		public Texture(int textureId)
		{
			this.textureId = textureId;
		}

		/**
		 * Returns true if the texture is loaded.
		 * */
		public bool IsLoaded()
		{
			return textureId >= 0;
		}

		/**
		 * Load texture image from file and create GL texture object.
		 * */
		public void Load()
		{
			Load (filename);
		}

		/**
		 * Load texture image from file and create GL texture object.
		 * */
		public void Load(string filename)
		{
			this.filename = filename;
			if (String.IsNullOrEmpty (filename)) {
				throw new ArgumentException (filename);
			}
			textureId = GL.GenTexture();
			GL.BindTexture(TextureTarget.Texture2D, textureId);
			GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureWrapS, (int)TextureWrapMode.Repeat);
			GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureWrapT, (int)TextureWrapMode.Repeat);
			GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMinFilter, (int)TextureMinFilter.Linear);
			GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMagFilter, (int)TextureMagFilter.Linear);
			Bitmap bmp = new Bitmap(AssetPath.GetPathToAsset(filename));
			BitmapData bmp_data = bmp.LockBits(new Rectangle(0, 0, bmp.Width, bmp.Height), ImageLockMode.ReadOnly, System.Drawing.Imaging.PixelFormat.Format32bppArgb);
			GL.TexImage2D(TextureTarget.Texture2D, 0, PixelInternalFormat.Rgba, bmp_data.Width, bmp_data.Height, 0,
				OpenTK.Graphics.OpenGL.PixelFormat.Bgra, PixelType.UnsignedByte, bmp_data.Scan0);
			bmp.UnlockBits(bmp_data);
			Console.WriteLine ("Texture " + filename + " loaded.");
		}

		/**
		 * Bind the texture as current texture.
		 * */
		public void Bind()
		{
			GL.BindTexture(TextureTarget.Texture2D, textureId);
		}
	}
}

