package com.tejadillas.armov3;

import es.ucm.look.ar.ar2D.drawables.Text2D;
import es.ucm.look.data.EntityData;
import es.ucm.look.data.WorldEntity;
import es.ucm.look.data.WorldEntityFactory;

public class LabelEntityFactory extends WorldEntityFactory{

public static final String NAME = "name";
	
	public WorldEntity createWorldEntity(EntityData data){
		WorldEntity we = new WorldEntity(data);
		we.setDrawable2D(new Text2D(data.getPropertyValue(NAME)));
		return we;
	}
	
}
