package com.aries.springldap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class SimpleCrudIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private SimpleCrudDataOnDemand dod;

	@Test
    public void testCountSimpleCruds() {
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to initialize correctly", dod.getRandomSimpleCrud());
        long count = com.aries.springldap.SimpleCrud.countSimpleCruds();
        org.junit.Assert.assertTrue("Counter for 'SimpleCrud' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindSimpleCrud() {
        com.aries.springldap.SimpleCrud obj = dod.getRandomSimpleCrud();
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to provide an identifier", id);
        obj = com.aries.springldap.SimpleCrud.findSimpleCrud(id);
        org.junit.Assert.assertNotNull("Find method for 'SimpleCrud' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'SimpleCrud' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllSimpleCruds() {
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to initialize correctly", dod.getRandomSimpleCrud());
        long count = com.aries.springldap.SimpleCrud.countSimpleCruds();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'SimpleCrud', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.aries.springldap.SimpleCrud> result = com.aries.springldap.SimpleCrud.findAllSimpleCruds();
        org.junit.Assert.assertNotNull("Find all method for 'SimpleCrud' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'SimpleCrud' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindSimpleCrudEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to initialize correctly", dod.getRandomSimpleCrud());
        long count = com.aries.springldap.SimpleCrud.countSimpleCruds();
        if (count > 20) count = 20;
        java.util.List<com.aries.springldap.SimpleCrud> result = com.aries.springldap.SimpleCrud.findSimpleCrudEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'SimpleCrud' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'SimpleCrud' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        com.aries.springldap.SimpleCrud obj = dod.getRandomSimpleCrud();
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to provide an identifier", id);
        obj = com.aries.springldap.SimpleCrud.findSimpleCrud(id);
        org.junit.Assert.assertNotNull("Find method for 'SimpleCrud' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySimpleCrud(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'SimpleCrud' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        com.aries.springldap.SimpleCrud obj = dod.getRandomSimpleCrud();
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to provide an identifier", id);
        obj = com.aries.springldap.SimpleCrud.findSimpleCrud(id);
        boolean modified =  dod.modifySimpleCrud(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.aries.springldap.SimpleCrud merged = (com.aries.springldap.SimpleCrud) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'SimpleCrud' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to initialize correctly", dod.getRandomSimpleCrud());
        com.aries.springldap.SimpleCrud obj = dod.getNewTransientSimpleCrud(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'SimpleCrud' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'SimpleCrud' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        com.aries.springldap.SimpleCrud obj = dod.getRandomSimpleCrud();
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'SimpleCrud' failed to provide an identifier", id);
        obj = com.aries.springldap.SimpleCrud.findSimpleCrud(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'SimpleCrud' with identifier '" + id + "'", com.aries.springldap.SimpleCrud.findSimpleCrud(id));
    }
}
