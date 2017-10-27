namespace computergraphics
{
	/**
	 * Application main class.
	 * */
	class Application
	{
		public static void Main()
		{
			Scene scene = new Exercise1();
			OpenTKWindow window = new OpenTKWindow(scene);
			window.Run ();
		}
	}
}
