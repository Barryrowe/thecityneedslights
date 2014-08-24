package com.pineapplepiranha.games.scene2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kasetagen.engine.gdx.scenes.scene2d.KasetagenStateUtil;
import com.pineapplepiranha.games.scene2d.GenericActor;

/**
 * Created with IntelliJ IDEA.
 * User: barry
 * Date: 8/23/14
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class Player extends DepthActor{

    //THIS IS GROSS!!!
    private static float BUFFER = 20f;

    public float speed = 180f;

    private float keyFrameTotal = 0f;
    public Animation animation = null;

    public TextureRegion hidingTexture;
    public TextureRegion normalTexture;
    public TextureRegion disguisedTexture;

    public boolean isHiding = false;
    public boolean isDisguised = false;

    public Player(float x, float y, float width, float height, TextureRegion tr){
        super(x+BUFFER, y+BUFFER, width-BUFFER*2, height-(BUFFER*2), tr, 0);
        setColor(Color.YELLOW);
        normalTexture = tr;
        animation = null;
        collider.set(x+(width/4), y+(height/4), width/2, height/2);
    }

    public Player(float x, float y, float width, float height, Animation anim){
        super(x, y, width, height, 0);
        setColor(Color.YELLOW);
        animation = anim;
        normalTexture = animation.getKeyFrame(0f);
    }

    @Override
    protected void adjustCollidingBox(float delta) {
        collider.set(getX()+(getWidth()/4), getY()+(getHeight()/4), getWidth()/2, getHeight()/2);
    }

    @Override
    public void adjustPosition(float delta) {
        if(!isDisguised){
            super.adjustPosition(delta);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);


        if(isDisguised){
            //Draw Disguised and return

            if(disguisedTexture != null){
                textureRegion = disguisedTexture;
            }
            return;
        }else{
            if(velocity.x == 0.0f && velocity.y == 0.0f){
                //
                if(isHiding){
                    textureRegion = hidingTexture;
                }else {
                    textureRegion = normalTexture;
                }
                keyFrameTotal = 0f;
            }else if(animation != null){
                textureRegion = animation.getKeyFrame(keyFrameTotal, true);
                if(velocity.x < 0f){
                    textureRegion.flip(true, false);

                }
                keyFrameTotal += delta;
            }

            if(velocity.x > 0.0f){
                if(!textureRegion.isFlipX()){
                    textureRegion.flip(true, false);
                }

                if(!hidingTexture.isFlipX()){
                    hidingTexture.flip(true, false);
                }

                if(!normalTexture.isFlipX()){
                    normalTexture.flip(true, false);
                }
            }else if(velocity.x < 0.0f){
                if(textureRegion.isFlipX()){
                    textureRegion.flip(true, false);
                }

                if(hidingTexture.isFlipX()){
                    hidingTexture.flip(true, false);
                }

                if(normalTexture.isFlipX()){
                    normalTexture.flip(true, false);
                }
            }
        }
    }

    public void setXVelocity(float vel){
        velocity.x = vel;
    }

    public void setYVelocity(float vel){
        velocity.y = vel;
    }

    public void setHidingTexture(TextureRegion tr){
        hidingTexture = tr;
    }

    public void setNormalTexture(TextureRegion tr){
        normalTexture = tr;
    }

    @Override
    protected void drawFull(Batch batch, float parentAlpha) {
        //super.drawFull(batch, parentAlpha);
        batch.draw(textureRegion, getX()-BUFFER, getY()-BUFFER, getWidth()+(BUFFER*2), getHeight()+(BUFFER*2));
        if(KasetagenStateUtil.isDebugMode()){
            batch.end();
            batch.begin();
            Gdx.gl20.glLineWidth(1f);
            //Set the projection matrix, and line shape
            debugRenderer.setProjectionMatrix(getStage().getCamera().combined);
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);

            Color c = getColor() != null ? getColor() : Color.WHITE;
            debugRenderer.setColor(Color.PINK);
            debugRenderer.rect(collider.x, collider.y, collider.width, collider.height);

            //End our shapeRenderer, flush the batch, and re-open it for future use as it was open
            // coming in.
            debugRenderer.end();
            batch.end();
            batch.begin();
        }
    }
}