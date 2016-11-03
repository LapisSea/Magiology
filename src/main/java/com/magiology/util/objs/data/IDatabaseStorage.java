package com.magiology.util.objs.data;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.statics.UtilM;

public interface IDatabaseStorage<T>{

	<C> List<C> getByExtension(Class<C> c);
		List<T> getByName(String name);
}
