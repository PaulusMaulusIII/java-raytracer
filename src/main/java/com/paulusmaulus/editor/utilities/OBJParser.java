package com.paulusmaulus.editor.utilities;

import com.paulusmaulus.editor.geometries.Polygon;
import com.paulusmaulus.editor.geometries.additional.Face;
import com.paulusmaulus.editor.materials.BasicMaterial;
import com.paulusmaulus.editor.shaders.BasicShader;
import com.paulusmaulus.editor.utilities.math.Vector3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class OBJParser {
	private Material currentMaterial = new BasicMaterial(new BasicShader(), Color.WHITE);

	public Polygon parse(File objFile) {
		List<Vector3> points = new LinkedList<>();
		List<Vector3> textureCoords = new LinkedList<>();
		List<Vector3> normals = new LinkedList<>();
		List<Face> faces = new LinkedList<>();
		String name = "";

		System.out.println("Parsing " + objFile.getName());
		try (Scanner fs = new Scanner(objFile)) {
			while (fs.hasNextLine()) {
				String line = fs.nextLine().trim();
				if (line.isEmpty() || line.charAt(0) == '#') {
					continue; // Skip comments and empty lines
				}

				if (line.startsWith("o")) {
					name = line.split(" ")[1];
				}
				else if (line.startsWith("v ")) {
					String[] xyz = line.split("\\s+");
					double x = Double.parseDouble(xyz[1]);
					double y = Double.parseDouble(xyz[2]);
					double z = Double.parseDouble(xyz[3]);
					points.add(new Vector3(x, y, z));
				}
				else if (line.startsWith("vt ")) {
					String[] uvw = line.split("\\s+");
					double u = Double.parseDouble(uvw[1]);
					double v = uvw.length > 2 ? Double.parseDouble(uvw[2]) : 0.0;
					double w = uvw.length > 3 ? Double.parseDouble(uvw[3]) : 0.0;
					textureCoords.add(new Vector3(u, v, w));
				}
				else if (line.startsWith("vn ")) {
					String[] xyz = line.split("\\s+");
					double x = Double.parseDouble(xyz[1]);
					double y = Double.parseDouble(xyz[2]);
					double z = Double.parseDouble(xyz[3]);
					normals.add(new Vector3(x, y, z));
				}
				else if (line.startsWith("f ")) {
					String[] parts = line.split("\\s+");
					List<Face.Vertex> faceVertices = new LinkedList<>();
					for (int i = 1; i < parts.length; i++) {
						String[] indices = parts[i].split("/");
						int vertexIndex = Integer.parseInt(indices[0]) - 1;
						int texCoordIndex = indices.length > 1 && !indices[1].isEmpty()
								? Integer.parseInt(indices[1]) - 1
								: -1;
						int normalIndex = indices.length > 2 && !indices[2].isEmpty() ? Integer.parseInt(indices[2]) - 1
								: -1;

						Vector3 vertex = points.get(vertexIndex);
						Vector3 texCoord = texCoordIndex >= 0 ? textureCoords.get(texCoordIndex) : null;
						Vector3 normal = normalIndex >= 0 ? normals.get(normalIndex) : null;

						faceVertices.add(new Face.Vertex(vertex, texCoord, normal));
					}
					faces.add(new Face(currentMaterial, faceVertices));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Polygon polygon = new Polygon(new Vector3(0, 0, 0), currentMaterial, points, faces);
		polygon.setAnchor(new Vector3(0, 0, 0));
		if (!name.equals(""))
			polygon.setName(name);

		System.out.println("Successfully parsed " + objFile.getName());

		return polygon;
	}
}
