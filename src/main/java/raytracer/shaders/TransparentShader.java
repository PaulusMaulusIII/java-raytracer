package raytracer.shaders;

import java.util.List;

import raytracer.lights.Light;
import raytracer.utilities.Color;
import raytracer.utilities.GlobalSettings;
import raytracer.utilities.Material;
import raytracer.utilities.Object3D;
import raytracer.utilities.Shape;
import raytracer.utilities.data.PixelData;
import raytracer.utilities.math.Ray;
import raytracer.utilities.math.RayHit;
import raytracer.utilities.math.Vector3;

public class TransparentShader implements Shader {

	private PixelData calculateTransparency(RayHit rayHit, List<Light> lights, List<Object3D> objects,
			Material material, int depth) {
		if (material.getTransparencyAt(rayHit.getHitPoint()) <= 0 || depth > GlobalSettings.MAX_REFLECTION_DEPTH)
			return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);

		Vector3 transparencyStart = rayHit.getHitPoint();
		Vector3 transparencyDirection = rayHit.getRay().getDirection();
		Ray transparencyRay = new Ray(transparencyStart.scale(1e-6), transparencyDirection);

		RayHit transparencyHit = transparencyRay.cast(objects);

		if (transparencyHit != null && transparencyHit.getObject() instanceof Shape
				&& transparencyHit.getShape() != material.getShape()) {
			return new PixelData(
					transparencyHit.getShape().getMaterial().getShader().shade(transparencyHit, lights, objects,
							transparencyHit.getShape().getMaterial(), depth + 1),
					transparencyRay.getOrigin().distance(transparencyHit.getHitPoint())
							+ (rayHit.getHitPoint().distance(rayHit.getRay().getOrigin())),
					transparencyHit.getShape().getMaterial().getEmissionAt(transparencyHit.getHitPoint()));

		}
		return new PixelData(GlobalSettings.SKY_BOX_COLOR, Double.POSITIVE_INFINITY, GlobalSettings.SKY_EMISSION);
	}

	@Override
	public Color shade(RayHit rayHit, List<Light> lights, List<Object3D> objects, Material material, int depth) {
		material.setTransparency(.5);
		return material.getColor(rayHit.getHitPoint())
				.interpolate(calculateTransparency(rayHit, lights, objects, material, depth).getColor(), .9);
	}
}
