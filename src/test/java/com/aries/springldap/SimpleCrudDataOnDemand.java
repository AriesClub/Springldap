package com.aries.springldap;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class SimpleCrudDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<SimpleCrud> data;

	public SimpleCrud getNewTransientSimpleCrud(int index) {
        com.aries.springldap.SimpleCrud obj = new com.aries.springldap.SimpleCrud();
        setMessage(obj, index);
        return obj;
    }

	private void setMessage(SimpleCrud obj, int index) {
        java.lang.String message = "message_" + index;
        obj.setMessage(message);
    }

	public SimpleCrud getSpecificSimpleCrud(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        SimpleCrud obj = data.get(index);
        return SimpleCrud.findSimpleCrud(obj.getId());
    }

	public SimpleCrud getRandomSimpleCrud() {
        init();
        SimpleCrud obj = data.get(rnd.nextInt(data.size()));
        return SimpleCrud.findSimpleCrud(obj.getId());
    }

	public boolean modifySimpleCrud(SimpleCrud obj) {
        return false;
    }

	public void init() {
        data = com.aries.springldap.SimpleCrud.findSimpleCrudEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'SimpleCrud' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.aries.springldap.SimpleCrud>();
        for (int i = 0; i < 10; i++) {
            com.aries.springldap.SimpleCrud obj = getNewTransientSimpleCrud(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}
