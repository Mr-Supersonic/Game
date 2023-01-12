package com.game.game.engine.graph;

import com.game.game.engine.Utils;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class ShaderProgram {

    private final int PROGRAM_ID;

    public ShaderProgram(List<ShaderModuleData> shaderModuleDataList) {
        PROGRAM_ID = glCreateProgram();
        if (PROGRAM_ID == 0) {
            throw new RuntimeException("Could not create Shader");
        }

        List<Integer> shaderModules = new ArrayList<>();
        shaderModuleDataList.forEach(s -> shaderModules.add(createShader(Utils.readFile(s.shaderFile), s.shaderType)));

        link(shaderModules);
    }

    public void bind() {
        glUseProgram(PROGRAM_ID);
    }

    public void cleanup() {
        unbind();
        if (PROGRAM_ID != 0) {
            glDeleteProgram(PROGRAM_ID);
        }
    }

    protected int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(PROGRAM_ID, shaderId);

        return shaderId;
    }

    public int getProgramId() {
        return PROGRAM_ID;
    }

    private void link(List<Integer> shaderModules) {
        glLinkProgram(PROGRAM_ID);
        if (glGetProgrami(PROGRAM_ID, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(PROGRAM_ID, 1024));
        }

        shaderModules.forEach(s -> glDetachShader(PROGRAM_ID, s));
        shaderModules.forEach(GL30::glDeleteShader);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void validate() {
        glValidateProgram(PROGRAM_ID);
        if (glGetProgrami(PROGRAM_ID, GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException("Error validating Shader code: " + glGetProgramInfoLog(PROGRAM_ID, 1024));
        }
    }

    public record ShaderModuleData(String shaderFile, int shaderType) {
    }
}
