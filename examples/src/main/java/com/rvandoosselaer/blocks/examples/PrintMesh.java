package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.mikktspace.MikkTSpaceImpl;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;
import com.simsilica.util.LogAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An application to load and print out mesh information.
 *
 * @author rvandoosselaer
 */
public class PrintMesh extends SimpleApplication {

    // decimals to round to. 1000f = 0.000
    private float precision = 1000f;
    // add rotation to vector
    private boolean canRotate = true;
    // noticed an issue with blender, when exporting uv's the v (y) coordinate was flipped. set flag to counter this.
    private boolean flipV = true;

    public static void main(String[] args) {
        LogAdapter.initialize();

        PrintMesh printMesh = new PrintMesh();
        printMesh.start();
    }

    public PrintMesh() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new BasicProfilerState(false));
    }

    @Override
    public void simpleInitApp() {
        Spatial spatial = assetManager.loadModel("stair-outercorner-inverted.j3o");
        spatial.depthFirstTraversal(new SceneGraphVisitorAdapter() {
            @Override
            public void visit(Geometry geom) {
                if (geom.getName().startsWith("bottom")) {
                    Mesh mesh = geom.getMesh();
                    MikktspaceTangentGenerator.genTangSpaceDefault(new MikkTSpaceImpl(mesh));

                    printPositions(mesh);
                    printIndices(mesh);
                    System.out.println("\t\tif (!chunkMesh.isCollisionMesh()) {");
                    printNormals(mesh);
                    printTangents(mesh);
                    System.out.println("\t\tif (!multipleImages) {");
                    printTexCoords(mesh);
                    System.out.println("\t\t} else {");
                    printTexCoordsMultiple(mesh);
                    System.out.println("\t\t}");
                    System.out.println("\t\t}");
                }
            }
        });

        rootNode.attachChild(spatial);

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));

        flyCam.setMoveSpeed(10f);
    }

    // when filter is set to true, no duplicate vertices will be added
    private List<Vector3f> getPositions(Mesh mesh, boolean filter) {
        List<Vector3f> positions = new ArrayList<>();
        VertexBuffer positionBuffer = mesh.getBuffer(VertexBuffer.Type.Position);

        for (int i = 0; i < positionBuffer.getNumElements(); i++) {
            Vector3f vertex = new Vector3f();
            for (int j = 0; j < positionBuffer.getNumComponents(); j++) {
                float value = (Float) positionBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        vertex.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        vertex.setY(Math.round(value * precision) / precision);
                        break;
                    case 2:
                        vertex.setZ(Math.round(value * precision) / precision);
                        break;
                }
            }

            if (filter) {
                if (!positions.contains(vertex)) {
                    positions.add(vertex);
                }
            } else {
                positions.add(vertex);
            }

        }

        return positions;
    }

    private void printPositions(Mesh mesh) {
        List<Vector3f> positions = getPositions(mesh, true);
        System.out.println("int offset = chunkMesh.getPositions().size();");
        System.out.println("\t\t// # Positions:" + positions.size());
        String line = "chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(%.3ff, %.3ff, %.3ff), location, blockScale));";
        if (canRotate) {
            line = "chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(%.3ff, %.3ff, %.3ff)), location, blockScale));";
        }
        for (Vector3f v : positions) {
            System.out.println(String.format(Locale.ROOT, line, v.x, v.y, v.z));
        }
    }

    private void printIndices(Mesh mesh) {
        List<Vector3f> allPositions = getPositions(mesh, false);
        List<Vector3f> filteredPositions = getPositions(mesh, true);

        IndexBuffer indexBuffer = mesh.getIndexBuffer();
        System.out.println("\t\t// Index:");
        for (int i = 0; i < indexBuffer.size(); i++) {
            // fetch the index from the positions
            int index = indexBuffer.get(i);
            Vector3f vertex = allPositions.get(index);
            // find the corresponding vertex in the filtered list
            int newIndex = filteredPositions.indexOf(vertex);
            String line = "chunkMesh.getIndices().add(offset + %d);";
            System.out.println(String.format(line, newIndex));
        }
    }

    private void printNormals(Mesh mesh) {
        List<Vector3f> allPositions = getPositions(mesh, false);

        // gather all normals and put them in a map. The key is the vertex position so duplicates will be overwritten.
        // vertex -> normal
        Map<Vector3f, Vector3f> normalsMap = new LinkedHashMap<>();
        VertexBuffer normalBuffer = mesh.getBuffer(VertexBuffer.Type.Normal);
        for (int i = 0; i < normalBuffer.getNumElements(); i++) {
            Vector3f normal = new Vector3f();
            for (int j = 0; j < normalBuffer.getNumComponents(); j++) {
                float value = (Float) normalBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        normal.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        normal.setY(Math.round(value * precision) / precision);
                        break;
                    case 2:
                        normal.setZ(Math.round(value * precision) / precision);
                        break;
                }
                normalsMap.put(allPositions.get(i), normal);
            }
        }

        System.out.println("\t\t// Normals:");
        String line = "chunkMesh.getNormals().add(new Vector3f(%.3ff, %.3ff, %.3ff));";
        if (canRotate) {
            line = "chunkMesh.getNormals().add(rotation.mult(new Vector3f(%.3ff, %.3ff, %.3ff)));";
        }
        for (Vector3f n : normalsMap.values()) {
            System.out.println(String.format(Locale.ROOT, line, n.x, n.y, n.z));
        }
    }

    private void printTangents(Mesh mesh) {
        List<Vector3f> allPositions = getPositions(mesh, false);

        // gather all tangents and put them in a map. The key is the vertex position so duplicates will be overwritten.
        // vertex -> tangent
        Map<Vector3f, Vector4f> tangentsMap = new LinkedHashMap<>();
        VertexBuffer tangentBuffer = mesh.getBuffer(VertexBuffer.Type.Tangent);
        for (int i = 0; i < tangentBuffer.getNumElements(); i++) {
            Vector4f tangent = new Vector4f();
            for (int j = 0; j < tangentBuffer.getNumComponents(); j++) {
                float value = (Float) tangentBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        tangent.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        tangent.setY(Math.round(value * precision) / precision);
                        break;
                    case 2:
                        tangent.setZ(Math.round(value * precision) / precision);
                        break;
                    case 3:
                        tangent.setW(Math.round(value * precision) / precision);
                }
                tangentsMap.put(allPositions.get(i), tangent);
            }

        }
        System.out.println("\t\t// Tangents:");
        String line = "chunkMesh.getTangents().add(new Vector4f(%.3ff, %.3ff, %.3ff, %.3ff));";
        if (canRotate) {
            System.out.println("Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());");
            line = "chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(%.3ff, %.3ff, %.3ff, %.3ff)));";
        }
        for (Vector4f t : tangentsMap.values()) {
            System.out.println(String.format(Locale.ROOT, line, t.x, t.y, t.z, t.w));
        }
    }

    private void printTexCoords(Mesh mesh) {
        List<Vector3f> allPositions = getPositions(mesh, false);

        // gather all uv's and put them in a map. The key is the vertex position so duplicates will be overwritten.
        // vertex -> uv
        Map<Vector3f, Vector2f> uvsMap = new LinkedHashMap<>();
        VertexBuffer textureBuffer = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        for (int i = 0; i < textureBuffer.getNumElements(); i++) {
            Vector2f uv = new Vector2f();
            for (int j = 0; j < textureBuffer.getNumComponents(); j++) {
                float value = (Float) textureBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        uv.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        uv.setY(Math.round(value * precision) / precision);
                        break;
                }
            }
            // round values
            if (uv.x > 0.999) {
                uv.x = 1.0f;
            }
            if (uv.y > 0.999) {
                uv.y = 1.0f;
            }
            if (flipV) {
                uv.y = Math.abs(1 - uv.y);
            }
            if (!uvsMap.containsKey(allPositions.get(i))) {
                uvsMap.put(allPositions.get(i), uv);
            }
        }
        String line = "chunkMesh.getUvs().add(new Vector2f(%.3ff, %.3ff));";
        for (Vector2f uv : uvsMap.values()) {
            System.out.println(String.format(Locale.ROOT, line, uv.x, uv.y));
        }
    }

    private void printTexCoordsMultiple(Mesh mesh) {
        List<Vector3f> allPositions = getPositions(mesh, false);

        // gather all uv's and put them in a map. The key is the vertex position so duplicates will be overwritten.
        // vertex -> uv
        Map<Vector3f, Vector2f> uvsMap = new LinkedHashMap<>();
        VertexBuffer textureBuffer = mesh.getBuffer(VertexBuffer.Type.TexCoord);
        for (int i = 0; i < textureBuffer.getNumElements(); i++) {
            Vector2f uv = new Vector2f();
            for (int j = 0; j < textureBuffer.getNumComponents(); j++) {
                float value = (Float) textureBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        uv.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        uv.setY(Math.round(value * precision) / precision);
                        break;
                }
            }
            // round values
            if (uv.x > 0.999) {
                uv.x = 1.0f;
            }
            if (uv.y > 0.999) {
                uv.y = 1.0f;
            }
            if (flipV) {
                uv.y = Math.abs(1 - uv.y);
            }
            if (!uvsMap.containsKey(allPositions.get(i))) {
                uvsMap.put(allPositions.get(i), uv);
            }
        }

        // top image
        String line = "chunkMesh.getUvs().add(new Vector2f(%.3ff, %.3ff));";
        for (Vector2f uv : uvsMap.values()) {
            System.out.println(String.format(Locale.ROOT, line, uv.x, uv.y));
        }
    }

}
