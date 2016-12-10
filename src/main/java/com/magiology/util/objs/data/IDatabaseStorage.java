package com.magiology.util.objs.data;

import java.util.List;

public interface IDatabaseStorage<T>{

	<C> List<C> getByExtension(Class<C> c);
		List<T> getByName(String name);
}
