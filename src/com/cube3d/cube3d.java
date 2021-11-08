package com.cube3d;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;



import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
//import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class cube3d extends JFrame {

	private JPanel contentPane;
	Cube cube;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cube3d frame = new cube3d();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void render(JSlider tX, JSlider tY, JSlider tZ, JPanel panel)
    {
        //Set the rotation values
        cube.setxRotation(tX.getValue());
        cube.setyRotation(tY.getValue());
        cube.setzRotation(tZ.getValue());

        //Cube is positioned based on center
        Point origin = new Point(panel.getWidth() / 2, panel.getHeight() / 2);
        
        panel.getGraphics().clearRect(0, 0, panel.getWidth(), panel.getHeight());

        cube.drawCube(origin, panel.getGraphics());
    }

	/**
	 * Create the frame.
	 */
	public cube3d() {
		
		setBackground(Color.GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 617, 378);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JSlider sliderX = new JSlider();
		sliderX.setMaximum(360);
		sliderX.setValue(0);
		
		sliderX.setBackground(Color.GREEN);
		sliderX.setPaintTicks(true);
		sliderX.setBounds(359, 85, 232, 29);
		contentPane.add(sliderX);

		JSlider sliderY = new JSlider();
		sliderY.setMaximum(360);
		sliderY.setValue(0);
		
		sliderY.setPaintTicks(true);
		sliderY.setBackground(Color.GREEN);
		sliderY.setBounds(359, 138, 232, 29);
		contentPane.add(sliderY);

		JSlider sliderZ = new JSlider();
		sliderZ.setMaximum(360);
		sliderZ.setValue(0);
		
		sliderZ.setPaintTicks(true);
		sliderZ.setBackground(Color.GREEN);
		sliderZ.setBounds(359, 201, 232, 29);
		contentPane.add(sliderZ);

		JLabel lblX = new JLabel("X");
		lblX.setBounds(349, 86, 16, 16);
		contentPane.add(lblX);

		JLabel lblY = new JLabel("Y");
		lblY.setBounds(349, 139, 16, 16);
		contentPane.add(lblY);

		JLabel lblZ = new JLabel("Z");
		lblZ.setBounds(349, 201, 16, 16);
		contentPane.add(lblZ);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sliderX.setValue(0);
				sliderY.setValue(0);
				sliderZ.setValue(0);
			}
		});

		btnReset.setBounds(464, 241, 117, 29);
		contentPane.add(btnReset);
		
		JPanel panel = new JPanel();
		
		panel.setBounds(6, 6, 320, 320);
		contentPane.add(panel);
		
		sliderX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
//				if (!slider.getValueIsAdjusting()) {
					int value = slider.getValue();
					System.out.println("slider 1:" + value);
					render(sliderX, sliderY, sliderZ, panel);
//				}
			}
		});

		sliderY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
//				if (!slider.getValueIsAdjusting()) {
					render(sliderX, sliderY, sliderZ, panel);
				}
//			}
		});

		sliderZ.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
//				if (!slider.getValueIsAdjusting()) {
					int value = slider.getValue();
					System.out.println("slider 3:" + value);
					render(sliderX, sliderY, sliderZ, panel);
//				}
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				cube = new Cube(200);
				render(sliderX, sliderY, sliderZ, panel);
			}
		});
	}
	public static class Math3D{
		public static class Point3D
        {
            //The Point3D class is rather simple, just keeps track of X Y and Z values,
            //and being a class it can be adjusted to be comparable
            public double X;
            public double Y;
            public double Z;

            public Point3D(int x, int y, int z)
            {
                X = x;
                Y = y;
                Z = z;
            }

            public Point3D(float x, float y, float z)
            {
                X = (double)x;
                Y = (double)y;
                Z = (double)z;
            }

            public Point3D(double x, double y, double z)
            {
                X = x;
                Y = y;
                Z = z;
            }

            public Point3D()
            {
            }

            @Override
            public String toString() {
            	// TODO Auto-generated method stub
            	return "(" + X + ", " + Y + ", " + Z + ")";
            }
        }
		
		public static class Camera
        {
            //For 3D drawing we need a point of perspective, thus the Camera
            public Point3D Position = new Point3D();
        }
		
		public static Point3D RotateX(Point3D point3D, double degrees)
        {
            //Here we use Euler's matrix formula for rotating a 3D point x degrees around the x-axis

            //[ a  b  c ] [ x ]   [ x*a + y*b + z*c ]
            //[ d  e  f ] [ y ] = [ x*d + y*e + z*f ]
            //[ g  h  i ] [ z ]   [ x*g + y*h + z*i ]

            //[ 1    0        0   ]
            //[ 0   cos(x)  sin(x)]
            //[ 0   -sin(x) cos(x)]

            double cDegrees = (Math.PI * degrees) / 180.0f; //Convert degrees to radian for .Net Cos/Sin functions
            double cosDegrees = Math.cos(cDegrees);
            double sinDegrees = Math.sin(cDegrees);

            double y = (point3D.Y * cosDegrees) + (point3D.Z * sinDegrees);
            double z = (point3D.Y * -sinDegrees) + (point3D.Z * cosDegrees);

            return new Point3D(point3D.X, y, z);
        }
		public static Point3D RotateY(Point3D point3D, double degrees)
        {
            //Y-axis

            //[ cos(x)   0    sin(x)]
            //[   0      1      0   ]
            //[-sin(x)   0    cos(x)]

            double cDegrees = (Math.PI * degrees) / 180.0; //Radians
            double cosDegrees = Math.cos(cDegrees);
            double sinDegrees = Math.sin(cDegrees);

            double x = (point3D.X * cosDegrees) + (point3D.Z * sinDegrees);
            double z = (point3D.X * -sinDegrees) + (point3D.Z * cosDegrees);

            return new Point3D(x, point3D.Y, z);
        }

        public static Point3D RotateZ(Point3D point3D, double degrees)
        {
            //Z-axis

            //[ cos(x)  sin(x) 0]
            //[ -sin(x) cos(x) 0]
            //[    0     0     1]

            double cDegrees = (Math.PI * degrees) / 180.0; //Radians
            double cosDegrees = Math.cos(cDegrees);
            double sinDegrees = Math.sin(cDegrees);

            double x = (point3D.X * cosDegrees) + (point3D.Y * sinDegrees);
            double y = (point3D.X * -sinDegrees) + (point3D.Y * cosDegrees);

            return new Point3D(x, y, point3D.Z);
        }
        
        public static Point3D Translate(Point3D points3D, Point3D oldOrigin, Point3D newOrigin)
        {
            //Moves a 3D point based on a moved reference point
            Point3D difference = new Point3D(newOrigin.X - oldOrigin.X, newOrigin.Y - oldOrigin.Y, newOrigin.Z - oldOrigin.Z);
            points3D.X += difference.X;
            points3D.Y += difference.Y;
            points3D.Z += difference.Z;
            return points3D;
        }

        //These are to make the above functions workable with arrays of 3D points
        public static Point3D[] RotateX(Point3D[] points3D, double degrees)
        {
            for (int i = 0; i < points3D.length; i++)
            {
                points3D[i] = RotateX(points3D[i], degrees);
            }
            return points3D;
        }

        public static Point3D[] RotateY(Point3D[] points3D, double degrees)
        {
            for (int i = 0; i < points3D.length; i++)
            {
                points3D[i] = RotateY(points3D[i], degrees);
            }
            return points3D;
        }

        public static Point3D[] RotateZ(Point3D[] points3D, double degrees)
        {
            for (int i = 0; i < points3D.length; i++)
            {
                points3D[i] = RotateZ(points3D[i], degrees);
            }
            return points3D;
        }

        public static Point3D[] Translate(Point3D[] points3D, Point3D oldOrigin, Point3D newOrigin)
        {
            for (int i = 0; i < points3D.length; i++)
            {
                points3D[i] = Translate(points3D[i], oldOrigin, newOrigin);
            }
            return points3D;
        }
	}
	
	public static class Cube{
		public int width = 0;
        public int height = 0;
        public int depth = 0;

        double xRotation = 0.0;
        double yRotation = 0.0;
        double zRotation = 0.0;

        Math3D.Camera camera1 = new Math3D.Camera();
        Math3D.Point3D cubeOrigin;
        

        public double getxRotation() {
			return xRotation;
		}

		public void setxRotation(double xRotation) {
			this.xRotation = xRotation;
		}

		public double getyRotation() {
			return yRotation;
		}

		public void setyRotation(double yRotation) {
			this.yRotation = yRotation;
		}

		public double getzRotation() {
			return zRotation;
		}

		public void setzRotation(double zRotation) {
			this.zRotation = zRotation;
		}

		public Cube(int side)
        {
            width = side;
            height = side;
            depth = side;
            cubeOrigin = new Math3D.Point3D(width / 2, height / 2, depth / 2);
        }

        public Cube(int side, Math3D.Point3D origin)
        {
            width = side;
            height = side;
            depth = side;
            cubeOrigin = origin;
        }

        public Cube(int Width, int Height, int Depth)
        {
            width = Width;
            height = Height;
            depth = Depth;
            cubeOrigin = new Math3D.Point3D(width / 2, height / 2, depth / 2);
        }

        public Cube(int Width, int Height, int Depth, Math3D.Point3D origin)
        {
            width = Width;
            height = Height;
            depth = Depth;
            cubeOrigin = origin;
        }

        //Finds the othermost points. Used so when the cube is drawn on a bitmap,
        //the bitmap will be the correct size
        public static Rectangle getBounds(Point2D[] points)
        {
            double left = points[0].getX();
            double right = points[0].getX();
            double top = points[0].getY();
            double bottom = points[0].getY();
            for (int i = 1; i < points.length; i++)
            {
                if (points[i].getX() < left)
                    left = points[i].getX();
                if (points[i].getX() > right)
                    right = points[i].getX();
                if (points[i].getY() < top)
                    top = points[i].getY();
                if (points[i].getY() > bottom)
                    bottom = points[i].getY();
            }

            return new Rectangle(0, 0, (int)Math.round(right - left), (int)Math.round(bottom - top));
        }
        
        public void drawCube(Point drawOrigin, Graphics g)
        {
            //FRONT FACE
            //Top Left - 7
            //Top Right - 4
            //Bottom Left - 6
            //Bottom Right - 5
        	

            //Vars
            Point[] point3D = new Point[24]; //Will be actual 2D drawing points
            Point tmpOrigin = new Point(0, 0);

            Math3D.Point3D point0 = new Math3D.Point3D(0, 0, 0); //Used for reference

            //Zoom factor is set with the monitor width to keep the cube from being distorted
//            double zoom = (double)Screen.PrimaryScreen.Bounds.Width / 1.5;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			int screenHeight = screenSize.height;
			double zoom = screenSize.width/1.5;

            //Set up the cube
            Math3D.Point3D[] cubePoints = fillCubeVertices(width, height, depth);

            //Calculate the camera Z position to stay constant despite rotation            
            Math3D.Point3D anchorPoint = (Math3D.Point3D)cubePoints[4]; //anchor point
            double cameraZ = -(((anchorPoint.X - cubeOrigin.X) * zoom) / cubeOrigin.X) + anchorPoint.Z;
            camera1.Position = new Math3D.Point3D(cubeOrigin.X, cubeOrigin.Y, cameraZ);            

            //Apply Rotations, moving the cube to a corner then back to middle
            cubePoints = Math3D.Translate(cubePoints, cubeOrigin, point0);
            cubePoints = Math3D.RotateX(cubePoints, xRotation); //The order of these
            cubePoints = Math3D.RotateY(cubePoints, yRotation); //rotations is the source
            cubePoints = Math3D.RotateZ(cubePoints, zRotation); //of Gimbal Lock
            cubePoints = Math3D.Translate(cubePoints, point0, cubeOrigin);

            //Convert 3D Points to 2D
            Math3D.Point3D vec;
            for (int i = 0; i < point3D.length; i++)
            {
                vec = cubePoints[i];
                if (vec.Z - camera1.Position.Z >= 0)
                {
                	point3D[i] = new Point(0,0);
                    point3D[i].x = (int)((double)-(vec.X - camera1.Position.X) / (-0.1f) * zoom) + drawOrigin.x;
                    point3D[i].y = (int)((double)(vec.Y - camera1.Position.Y) / (-0.1f) * zoom) + drawOrigin.y;
                }
                else
                {
                    tmpOrigin.x = (int)((double)(cubeOrigin.X - camera1.Position.X) / (double)(cubeOrigin.Z - camera1.Position.Z) * zoom) + drawOrigin.x;
                    tmpOrigin.y = (int)((double)-(cubeOrigin.Y - camera1.Position.Y) / (double)(cubeOrigin.Z - camera1.Position.Z) * zoom) + drawOrigin.y;

                    point3D[i] = new Point(0,0);
                    int a = (int)((vec.X - camera1.Position.X) / (vec.Z - camera1.Position.Z) * zoom + drawOrigin.x);
                    point3D[i].x = a;
                    point3D[i].y = (int)(-(vec.Y - camera1.Position.Y) / (vec.Z - camera1.Position.Z) * zoom + drawOrigin.y);

                    point3D[i].x = (int)point3D[i].x;
                    point3D[i].y = (int)point3D[i].y;
                }
            }

            //Now to plot out the points
            Rectangle bounds = getBounds(point3D);
            bounds.width += drawOrigin.x;
            bounds.height += drawOrigin.y;

//            BufferedImage tmpBmp = new BufferedImage(bounds.width, bounds.height);
//            Graphics g = new Graphics();
//            g.(tmpBmp);
             
            

            //Back Face
            g.drawLine(point3D[0].x, point3D[0].y, point3D[1].x, point3D[1].y);
            g.drawLine(point3D[1].x, point3D[1].y, point3D[2].x, point3D[2].y);
            g.drawLine(point3D[2].x, point3D[2].y, point3D[3].x, point3D[3].y);
            g.drawLine(point3D[3].x, point3D[3].y, point3D[0].x, point3D[0].y);

            //Front Face
            g.drawLine(point3D[4].x, point3D[4].y, point3D[5].x, point3D[5].y);
            g.drawLine(point3D[5].x, point3D[5].y, point3D[6].x, point3D[6].y);
            g.drawLine(point3D[6].x, point3D[6].y, point3D[7].x, point3D[7].y);
            g.drawLine(point3D[7].x, point3D[7].y, point3D[4].x, point3D[4].y);

            //Right Face
            g.drawLine(point3D[8].x, point3D[8].y, point3D[9].x, point3D[9].y);
            g.drawLine(point3D[9].x, point3D[9].y, point3D[10].x, point3D[10].y);
            g.drawLine(point3D[10].x, point3D[10].y, point3D[11].x, point3D[11].y);
            g.drawLine(point3D[11].x, point3D[11].y, point3D[8].x, point3D[8].y);

            //Left Face
            g.drawLine(point3D[12].x, point3D[12].y, point3D[13].x, point3D[13].y);
            g.drawLine(point3D[13].x, point3D[13].y, point3D[14].x, point3D[14].y);
            g.drawLine(point3D[14].x, point3D[14].y, point3D[15].x, point3D[15].y);
            g.drawLine(point3D[15].x, point3D[15].y, point3D[12].x, point3D[12].y);

            //Bottom Face
            g.drawLine(point3D[16].x, point3D[16].y, point3D[17].x, point3D[17].y);
            g.drawLine(point3D[17].x, point3D[17].y, point3D[18].x, point3D[18].y);
            g.drawLine(point3D[18].x, point3D[18].y, point3D[19].x, point3D[19].y);
            g.drawLine(point3D[19].x, point3D[19].y, point3D[16].x, point3D[16].y);

            //Top Face
            g.drawLine(point3D[20].x, point3D[20].y, point3D[21].x, point3D[21].y);
            g.drawLine(point3D[21].x, point3D[21].y, point3D[22].x, point3D[22].y);
            g.drawLine(point3D[22].x, point3D[22].y, point3D[23].x, point3D[23].y);
            g.drawLine(point3D[23].x, point3D[23].y, point3D[20].x, point3D[20].y);

            g.dispose(); //Clean-up
//
//            return tmpBmp;
        }
        
        public static Math3D.Point3D[] fillCubeVertices(int width, int height, int depth)
        {
            Math3D.Point3D[] verts = new Math3D.Point3D[24];

            //front face
            verts[0] = new Math3D.Point3D(0, 0, 0);
            verts[1] = new Math3D.Point3D(0, height, 0);
            verts[2] = new Math3D.Point3D(width, height, 0);
            verts[3] = new Math3D.Point3D(width, 0, 0);

            //back face
            verts[4] = new Math3D.Point3D(0, 0, depth);
            verts[5] = new Math3D.Point3D(0, height, depth);
            verts[6] = new Math3D.Point3D(width, height, depth);
            verts[7] = new Math3D.Point3D(width, 0, depth);

            //left face
            verts[8] = new Math3D.Point3D(0, 0, 0);
            verts[9] = new Math3D.Point3D(0, 0, depth);
            verts[10] = new Math3D.Point3D(0, height, depth);
            verts[11] = new Math3D.Point3D(0, height, 0);

            //right face
            verts[12] = new Math3D.Point3D(width, 0, 0);
            verts[13] = new Math3D.Point3D(width, 0, depth);
            verts[14] = new Math3D.Point3D(width, height, depth);
            verts[15] = new Math3D.Point3D(width, height, 0);

            //top face
            verts[16] = new Math3D.Point3D(0, height, 0);
            verts[17] = new Math3D.Point3D(0, height, depth);
            verts[18] = new Math3D.Point3D(width, height, depth);
            verts[19] = new Math3D.Point3D(width, height, 0);

            //bottom face
            verts[20] = new Math3D.Point3D(0, 0, 0);
            verts[21] = new Math3D.Point3D(0, 0, depth);
            verts[22] = new Math3D.Point3D(width, 0, depth);
            verts[23] = new Math3D.Point3D(width, 0, 0);

            return verts;
        }
	}
}
