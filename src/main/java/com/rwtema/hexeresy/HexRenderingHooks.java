package com.rwtema.hexeresy;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class HexRenderingHooks {

    static {
        HexRenderingCoreMod.logger.info("HexRenderingHooks online.");
    }

    private final static Face[] REG_FACES = genRegFaces();
    private static final float dr = 2 / 16F + 1 / 1024F;
    private final static Face[] HEX_FACES_X = genHexFacesX();
    private final static Face[] HEX_FACES_Y = genHexFacesY();
    private final static Face[] HEX_FACES_Z = genHexFacesZ();

    @SuppressWarnings("unused")
    public static TexturedQuad[] assignQuadList(ModelRenderer renderer, int texU, int texV, float x1, float y1, float z1, int dx, int dy, int dz, float delta, boolean mirror, TexturedQuad[] quadList) {
        float x2 = x1 + (float) dx;
        float y2 = y1 + (float) dy;
        float z2 = z1 + (float) dz;
        x1 = x1 - delta;
        y1 = y1 - delta;
        z1 = z1 - delta;
        x2 = x2 + delta;
        y2 = y2 + delta;
        z2 = z2 + delta;

        if (mirror) {
            float t = x2;
            x2 = x1;
            x1 = t;
        }

        float w = renderer.textureWidth;
        float h = renderer.textureHeight;

        Face[] faces = HEX_FACES_Z;

        int[] dims = {dx, dy, dz};
        if(dims[1] ==0 )
            return quadList;
        Arrays.sort(dims);
        if (dims[2] >= (8 * Math.min(dims[0], dims[1]))) {
            if (dy == dims[1]) {
                faces = HEX_FACES_Y;
            } else if (dx == dims[1]) {
                faces = HEX_FACES_X;
            }
        }


        TexturedQuad[] quads = new TexturedQuad[faces.length];
        for (int i = 0; i < faces.length; i++) {
            quads[i] = faces[i].build(x1, y1, z1, x2, y2, z2, texU, texV, dx, dy, dz, w, h);
            if (mirror) {
                quads[i].flipFace();
            }
        }

        return quads;
    }

    private static Face[] genHexFacesX() {
        float[] f000 = {0, 0, dr};
        float[] f001 = {0, 0, 1 - dr};
        float[] f010 = {0, 1, dr};
        float[] f011 = {0, 1, 1 - dr};
        float[] f100 = {1, 0, dr};
        float[] f101 = {1, 0, 1 - dr};
        float[] f110 = {1, 1, dr};
        float[] f111 = {1, 1, 1 - dr};

        float[] fa00 = {0.5F, 0, -dr};
        float[] fa01 = {0.5F, 0, 1 + dr};
        float[] fa10 = {0.5F, 1, -dr};
        float[] fa11 = {0.5F, 1, 1 + dr};

        float[] dxdz = {1, 0, 1};
        float[] dxdz2 = {1, 0, 2};
        float[] dz = {0, 0, 1};
        float[] dydz = {0, 1, 1};
        float[] d0 = {0, 0, 0};
        float[] dx2dz = {2, 0, 1};
        float[] dx2dz2 = {2, 0, 2};

        float[] daxdz = {0.5F, 0, 1F};
        float[] dxaxdz = {1.5F, 0, 1F};
        float[] dxaxdz2 = {1.5F, 0, 2F};

        List<Face> faces = new ArrayList<>();
        faces.add(new Face(f101, f100, f110, f111, dxdz, dz, dxdz2, dydz)); //x+
        faces.add(new Face(f000, f001, f011, f010, d0, dz, dz, dydz)); //x-

//        faces.add(new Face(f101, f001, f000, f100, dz, d0, dxdz, dz)); //y-
        faces.add(new Face(f101, fa01, fa00, f100, daxdz, d0, dxdz, dz)); //y-
        faces.add(new Face(fa01, f001, f000, fa00, dz, d0, daxdz, dz)); //y-

//        faces.add(new Face(f110, f010, f011, f111, dxdz, dz, dx2dz, d0)); //y+
        faces.add(new Face(f110, fa10, fa11, f111, dxaxdz, dz, dx2dz, d0)); //y+
        faces.add(new Face(fa10, f010, f011, fa11, dxdz, dz, dxaxdz, d0)); //y+

//        faces.add(new Face(f100, f000, f010, f110, dz, dz, dxdz, dydz)); //z-
        faces.add(new Face(f100, fa00, fa10, f110, daxdz, dz, dxdz, dydz)); //z-
        faces.add(new Face(fa00, f000, f010, fa10, dz, dz, daxdz, dydz)); //z-

//        faces.add(new Face(f001, f101, f111, f011, dxdz2, dz, dx2dz2, dydz)); //z+
        faces.add(new Face(f001, fa01, fa11, f011, dxaxdz2, dz, dx2dz2, dydz)); //z+
        faces.add(new Face(fa01, f101, f111, fa11, dxdz2, dz, dxaxdz2, dydz)); //z+
        return faces.toArray(new Face[faces.size()]);
    }


    private static Face[] genHexFacesY() {
        float[] f000 = {0, dr, 0};
        float[] f001 = {0, dr, 1};
        float[] f010 = {0, 1 - dr, 0};
        float[] f011 = {0, 1 - dr, 1};
        float[] f100 = {1, dr, 0};
        float[] f101 = {1, dr, 1};
        float[] f110 = {1, 1 - dr, 0};
        float[] f111 = {1, 1 - dr, 1};

        float[] f0a0 = {0, 0.5F, -dr};
        float[] f0a1 = {0, 0.5F, 1 + dr};
        float[] f1a0 = {1, 0.5F, -dr};
        float[] f1a1 = {1, 0.5F, 1 + dr};

        float[] dxdz = {1, 0, 1};
        float[] dxdz2 = {1, 0, 2};
        float[] dz = {0, 0, 1};
        float[] dydz = {0, 1, 1};
        float[] d0 = {0, 0, 0};
        float[] dx2dz = {2, 0, 1};
        float[] dx2dz2 = {2, 0, 2};

        float[] daydz = {0, 0.5F, 1F};


        List<Face> faces = new ArrayList<>();
//        faces.add(new Face(f101, f100, f110, f111, dxdz, dz, dxdz2, dydz)); //x+
        faces.add(new Face(f101, f100, f1a0, f1a1, dxdz, dz, dxdz2, daydz)); //x+
        faces.add(new Face(f1a1, f1a0, f110, f111, dxdz, daydz, dxdz2, dydz)); //x+

//        faces.add(new Face(f000, f001, f011, f010, d0, dz, dz, dydz)); //x-
        faces.add(new Face(f000, f001, f0a1, f0a0, d0, dz, dz, daydz)); //x-
        faces.add(new Face(f0a0, f0a1, f011, f010, d0, daydz, dz, dydz)); //x-

        faces.add(new Face(f101, f001, f000, f100, dz, d0, dxdz, dz)); //y-
        faces.add(new Face(f110, f010, f011, f111, dxdz, dz, dx2dz, d0)); //y+

//        faces.add(new Face(f100, f000, f010, f110, dz, dz, dxdz, dydz)); //z-
        faces.add(new Face(f100, f000, f0a0, f1a0, dz, dz, dxdz, daydz)); //z-
        faces.add(new Face(f1a0, f0a0, f010, f110, dz, daydz, dxdz, dydz)); //z-

//        faces.add(new Face(f001, f101, f111, f011, dxdz2, dz, dx2dz2, dydz)); //z+
        faces.add(new Face(f001, f101, f1a1, f0a1, dxdz2, dz, dx2dz2, daydz)); //z+
        faces.add(new Face(f0a1, f1a1, f111, f011, dxdz2, daydz, dx2dz2, dydz)); //z+
        return faces.toArray(new Face[faces.size()]);
    }

    private static Face[] genHexFacesZ() {
        float[] f000 = {dr, 0, 0};
        float[] f001 = {dr, 0, 1};
        float[] f010 = {dr, 1, 0};
        float[] f011 = {dr, 1, 1};
        float[] f100 = {1 - dr, 0, 0};
        float[] f101 = {1 - dr, 0, 1};
        float[] f110 = {1 - dr, 1, 0};
        float[] f111 = {1 - dr, 1, 1};

        float[] f00a = {-dr, 0, 0.5F};
        float[] f01a = {-dr, 1, 0.5F};
        float[] f10a = {1 + dr, 0, 0.5F};
        float[] f11a = {1 + dr, 1, 0.5F};


        float[] dxdz = {1, 0, 1};
        float[] dxdz2 = {1, 0, 2};
        float[] dz = {0, 0, 1};
        float[] dydz = {0, 1, 1};
        float[] d0 = {0, 0, 0};
        float[] dx2dz = {2, 0, 1};
        float[] dx2dz2 = {2, 0, 2};

        float[] dxdzaz = {1, 0, 1.5F};
        float[] daz = {0, 0, 0.5F};

        List<Face> faces = new ArrayList<>();
        faces.add(new Face(f101, f10a, f11a, f111, dxdzaz, dz, dxdz2, dydz)); //x+ (a)
        faces.add(new Face(f10a, f100, f110, f11a, dxdz, dz, dxdzaz, dydz)); //x+ (b)

        faces.add(new Face(f000, f00a, f01a, f010, daz, dz, dz, dydz)); //x- (a)
        faces.add(new Face(f00a, f001, f011, f01a, d0, dz, daz, dydz)); //x- (b)

        faces.add(new Face(f10a, f00a, f000, f100, dz, daz, dxdz, dz)); //y- (a)
        faces.add(new Face(f101, f001, f00a, f10a, dz, d0, dxdz, daz)); //y- (b)

        faces.add(new Face(f11a, f01a, f011, f111, dxdz, daz, dx2dz, d0)); //y+ (a)
        faces.add(new Face(f110, f010, f01a, f11a, dxdz, dz, dx2dz, daz)); //y+ (b)

        faces.add(new Face(f100, f000, f010, f110, dz, dz, dxdz, dydz)); //z- //front
        faces.add(new Face(f001, f101, f111, f011, dxdz2, dz, dx2dz2, dydz)); //z+
        return faces.toArray(new Face[faces.size()]);
    }


    private static Face[] genRegFaces() {
        float[] f000 = {0, 0, 0};
        float[] f001 = {0, 0, 1};
        float[] f010 = {0, 1, 0};
        float[] f011 = {0, 1, 1};
        float[] f100 = {1, 0, 0};
        float[] f101 = {1, 0, 1};
        float[] f110 = {1, 1, 0};
        float[] f111 = {1, 1, 1};

        float[] dxdz = {1, 0, 1};
        float[] dxdz2 = {1, 0, 2};
        float[] dz = {0, 0, 1};
        float[] dydz = {0, 1, 1};
        float[] d0 = {0, 0, 0};
        float[] dx2dz = {2, 0, 1};
        float[] dx2dz2 = {2, 0, 2};

        List<Face> faces = new ArrayList<>();
        faces.add(new Face(f101, f100, f110, f111, dxdz, dz, dxdz2, dydz)); //x+
        faces.add(new Face(f000, f001, f011, f010, d0, dz, dz, dydz)); //x-
        faces.add(new Face(f101, f001, f000, f100, dz, d0, dxdz, dz)); //y-
        faces.add(new Face(f110, f010, f011, f111, dxdz, dz, dx2dz, d0)); //y+
        faces.add(new Face(f100, f000, f010, f110, dz, dz, dxdz, dydz)); //z-
        faces.add(new Face(f001, f101, f111, f011, dxdz2, dz, dx2dz2, dydz)); //z+
        return faces.toArray(new Face[faces.size()]);
    }

    public static class Face {

        float[][] vecs;
        float[][] uvs;

        public Face(float[][] vecs, float[][] uvs) {
            this.vecs = vecs;
            this.uvs = uvs;
        }

        public Face(float[] p0, float[] p1, float[] p2, float[] p3, float[] u0, float[] v0, float[] u1, float[] v1) {
            this.vecs = new float[][]{p0, p1, p2, p3};

            this.uvs = new float[][]{u0, v0, u1, v1};
        }

        public TexturedQuad build(float x1, float y1, float z1, float x2, float y2, float z2, float texU, float texV, float dx, float dy, float dz, float w, float h) {
            PositionTextureVertex[] vertices = new PositionTextureVertex[4];
            for (int i = 0; i < 4; i++) {
                vertices[i] = new PositionTextureVertex(
                        x1 + (x2 - x1) * vecs[i][0],
                        y1 + (y2 - y1) * vecs[i][1],
                        z1 + (z2 - z1) * vecs[i][2],
                        0, 2432
                );
            }


            return new TexturedQuad(vertices,
                    Math.round(2*(texU + uvs[0][0] * dx + uvs[0][1] * dy + uvs[0][2] * dz)),
                    Math.round(2*(texV + uvs[1][0] * dx + uvs[1][1] * dy + uvs[1][2] * dz)),
                    Math.round(2*(texU + uvs[2][0] * dx + uvs[2][1] * dy + uvs[2][2] * dz)),
                    Math.round(2*(texV + uvs[3][0] * dx + uvs[3][1] * dy + uvs[3][2] * dz)),
                    2 * w,  2 * h
            );
        }


    }
}
