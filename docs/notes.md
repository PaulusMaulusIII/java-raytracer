# Material.java

## shading

```java
    public Color shade(RayHit rayHit, int depth) {
        Scene scene = shape.getScene();
        List<Shape> objects = scene.getChildren();

        Vector3 hitPos = rayHit.getHitPoint();
        Vector3 rayDir = rayHit.getRay().getDirection();
        Color baseColor = shape.getMaterial().getColor(hitPos);

        double brightness = calculateDiffuseBrightness(rayHit);
        double specularBrightness = calculateSpecularBrightness(rayHit);

        Color reflection;
        Vector3 reflectionVector = rayDir.subtract(shape.getNormal(rayHit).scale(2 * rayDir.dot(shape.getNormal(rayHit))));
        Vector3 reflectionRayOrigin = hitPos.add(reflectionVector.scale(0.001));
        RayHit reflectionHit = (depth < GlobalSettings.MAX_REFLECTION_DEPTH) ? new Ray(reflectionRayOrigin, reflectionVector).cast(objects) : null;
        if (reflectionHit != null) {
            reflection = shade(reflectionHit, depth + 1);
        }
        else {
            reflection = GlobalSettings.SKY_BOX_COLOR.brighten(GlobalSettings.SKY_EMISSION);
        }

        Color finalColor = Color.lerp(baseColor, reflection, reflectiveness).brighten(brightness).add(specularBrightness).add(baseColor.brighten(emission)).add(reflection);

        return finalColor;
    }

    private double calculateSpecularBrightness(RayHit rayHit) {
        Scene scene = shape.getScene();
        List<Light> lights = scene.getLights();
        Vector3 hitPos = rayHit.getHitPoint();
        Vector3 cameraDirection = scene.getCurrentCamera().getPosition().subtract(hitPos).normalize();

        double specularFactor = 0;
        for (Light light : lights) {
            Vector3 lightDirection = hitPos.subtract(light.getAnchor()).normalize();
            Vector3 lightReflectionVector = lightDirection.subtract(shape.getNormal(rayHit).scale(2 * lightDirection.dot(shape.getNormal(rayHit))));

            specularFactor = Math.max(specularFactor, Math.min(1, lightReflectionVector.dot(cameraDirection)));
        }

        return (float) Math.pow(specularFactor, 2) * shape.getMaterial().getReflectiveness();
    }

    private double calculateDiffuseBrightness(RayHit rayHit) {
        Scene scene = shape.getScene();
        List<Shape> objects = scene.getChildren();
        List<Light> lights = scene.getLights();

        boolean inShadow = false;
        double brightness = 0;
        for (Light light : lights) {
            // Raytrace to light to check if something blocks the light
            RayHit lightBlocker = new Ray(light.getAnchor(), rayHit.getHitPoint().subtract(light.getAnchor()).normalize()).cast(objects);
            if (lightBlocker != null && lightBlocker.getShape() != shape) {
                inShadow = true;
            }
            else {
                brightness = Math.max(GlobalSettings.AMBIENT_BRIGHTNESS, Math.min(1, shape.getNormal(rayHit).dot(light.getAnchor().subtract(rayHit.getHitPoint()))));
            }
        }
        return inShadow ? GlobalSettings.AMBIENT_BRIGHTNESS : brightness;
    }
```

Stolen from CarlVonBonin
