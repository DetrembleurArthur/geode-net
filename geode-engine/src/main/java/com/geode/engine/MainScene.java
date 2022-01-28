package com.geode.engine;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.components.RenderComponent;
import com.geode.engine.graphics.Mesh;
import com.geode.engine.graphics.MeshContext;
import com.geode.engine.graphics.Renderer;
import com.geode.engine.graphics.Shader;
import com.geode.engine.system.*;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MainScene extends Scene<Main>
{
    float[] positions = new float[]{
        1f, 0f, 0f,
        0f, 1f, 0f,
        1f, 1f, 0f,
        0f, 0f, 0f,
    };

    float[] colors = new float[]{
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,
    };

    float[] uvs = new float[]{
            1, 0,
            0, 1,
            1, 1,
            0, 0,
    };

    int[] indices = new int[]{
            2, 1, 0,
            0, 1, 3
    };

    GameObject object;

    @Override
    public void load()
    {
        System.out.println("load scene");
        getParent().getWindow().setClearColor(new Vector4f(0f, 1f, 1f, 1f));

        object = new GameObject();
        object.setTexture(getParent().texture);

        MeshContext context = new MeshContext();
        MeshContext.Attribute positionsAttr = MeshContext.Attribute.builder().data(positions).size(3).build();
        MeshContext.Attribute colorsAttr = MeshContext.Attribute.builder().data(colors).size(4).build();
        MeshContext.Attribute uvsAttr = MeshContext.Attribute.builder().data(uvs).size(2).build();
        object.setMesh(new Mesh(context.addAttribute(positionsAttr).addAttribute(colorsAttr).addAttribute(uvsAttr), indices));
        Renderer renderer = new Renderer(Shader.DEFAULT, getCamera());
        object.addComponent(new RenderComponent(object, renderer));

        object.getTransform().setSize(new Vector3f(100, 100, 0));
        object.getTransform().setPosition(new Vector3f(0,0,0));

    }

    @Override
    public void update(float dt)
    {
        Vector2i mp = MouseManager.getMousePosition(getCamera());
        object.update();
        object.getTransform().setPosition(new Vector3f(mp.x, mp.y, 0));


        if(KeyManager.keyManager.isKeyReleased(GLFW.GLFW_KEY_SPACE))
            getCamera().focus(new Vector2f(mp.x, mp.y));
    }

    @Override
    public void draw(Window window)
    {

    }

    @Override
    public void destroy()
    {
        System.out.println("destroy");
    }
}
