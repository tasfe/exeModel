import org.exemodel.session.AbstractSession;
import org.exemodel.session.Session;
import org.exemodel.util.Function;
import model.Role;
import org.exemodel.util.MapTo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zp on 17/2/16.
 */
public class RoleDaoTest {
    static {
        new InitResource();
    }

    private Role addRole() {
        Role role = new Role();
        role.setTitle("Software Test");
        role.setUserId(10);
        role.setPermissions("admin");
        role.setDetails("starking");
        role.save();
        return role;
    }


    @Test
    public void testFindCache() {
        Role role = addRole();
        Session session = AbstractSession.currentSession();
        Role role1 = CustomStatement.build(Role.class).findCache(role.getId());
        Assert.assertTrue(role.getUserId() == role1.getUserId());

        Role role2 = CustomStatement.build(Role.class).id(role.getId()).selectOne("userId");
        Assert.assertTrue(role.getUserId() == role2.getUserId());

        session.getCache().delete(role.generateKey());
        Role role3 = CustomStatement.build(Role.class).findCache(role.getId(), role.getUserId());
        Assert.assertTrue(role3 == null);

        Role role4 = CustomStatement.build(Role.class).findCache(role.getId());
        Assert.assertTrue(role4.getDetails() == null);//because details not cache

    }

    @Test
    public void testSet() {
        String newTitle = "Software Test";
        Role role = addRole();
        CustomStatement.build(Role.class).id(role.getId()).set("title", newTitle);

        Role role1 = CustomStatement.build(Role.class).findById(role.getId());
        Assert.assertTrue(role1.getTitle().equals(newTitle));
    }

    @Test
    public void testUpdate() {
        String newTitle = "CTO";
        Role role = addRole();
        role.setTitle(newTitle);
        role.update();

        Role role1 = CustomStatement.build(Role.class).findById(role.getId());
        Assert.assertTrue(role1.getTitle().equals(newTitle));
    }

    private List<Role> getList(Session session) {
        List<Role> roleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Role role = new Role();
            role.setPermissions("fxxx dog");
            role.setTitle("jyz_FxxxDog");
            role.setUserId(10 + i);
            roleList.add(role);
        }
        session.saveBatch(roleList);
        return roleList;
    }

    @Test
    public void testBatch() {
        try (Session session = AbstractSession.currentSession()) {//try with material
            List<Role> roleList = getList(session);

//            String tmp = "dog_fxxx";
//            for (Role role : roleList) {
//                role.setPermissions(tmp);
//            }
//            session.updateBatch(roleList);
//
//            Map<Integer, Role> test = session.getCache().batchGet(roleList, new MapTo<Integer, Role>() {
//                @Override
//                public Integer apply(Role role) {
//                    return role.getId();
//                }
//            }, Role.class);
//
//            Integer[] ids = new Integer[10];
//            int i = 0;
//            for (Role role : roleList) {
//                ids[i++] = role.getId();
//            }
//
//            Map<Integer, Role> map = session.getCache().batchGet(ids, Role.class);
//
//            List<Role> res = new ArrayList<>();
//
//            session.startCacheBatch();
//            for (Object id : ids) {
//                Role role = CustomStatement.build(Role.class).id(id).selectOne("permissions");
//                res.add(role);
//                Role role1 = CustomStatement.build(Role.class).findCache(id);
//                res.add(role1);
//            }
//            session.executeCacheBatch();
//            for (Role role : res) {
//                Assert.assertTrue(role != null);
//            }
//
//            Role role0 = roleList.get(0);
//            session.startCacheBatch();
//            CustomStatement.build(Role.class).id(role0.getId()).set("permissions", "fuck_the_wildest_dog");
//            CustomStatement.build(Role.class).id(role0.getId()).set("userId", 20);
//            role0 = CustomStatement.build(Role.class).findCache(role0.getId());
//            session.executeCacheBatch();
//            Assert.assertTrue(role0.getUserId() == 20);
//
//            roleList.get(0).setUserId(20);
//            session.deleteBatch(roleList);
//            Role role = CustomStatement.build(Role.class).findById(ids[0]);
//            Assert.assertTrue(role == null);
//            Role role1 = CustomStatement.build(Role.class).findCache(ids[1], 10);
//            Assert.assertTrue(role1 == null);
        }
    }

    @Test
    public void onValid() {
        try (Session session = AbstractSession.currentSession()) {
            List<Role> roleList = getList(session);
            session.startCacheBatch();
            for (Role role : roleList) {
                final Role role1 = CustomStatement.build(Role.class).findCache(role.getId());
                role1.onValid(new Function<Role>() {
                    @Override
                    public void apply(Role o) {
                        System.out.println(role1.getPermissions());
                    }
                });
            }
            session.executeCacheBatch();

        }

    }


    @Test
    public void testBatchGet() {
        Integer[] ids = {1, 2, 1001};
        Map<Integer, Role> map = AbstractSession.currentSession().getCache().batchGet(ids, Role.class);
        System.out.println(map);
    }


    @Test
    public void testSetByObject() {
        Role role = addRole();
        RoleUpdateForm roleUpdateForm = new RoleUpdateForm();
        roleUpdateForm.setPermissions("set_by_object");

        CustomStatement.build(Role.class).id(role.getId()).setByObject(roleUpdateForm);
        Role role1 = CustomStatement.build(Role.class).findCache(role.getId());
        Assert.assertTrue(role1.getPermissions().equals("set_by_object"));
        Assert.assertTrue(role1.getTitle().equals(role.getTitle()));

    }

}
