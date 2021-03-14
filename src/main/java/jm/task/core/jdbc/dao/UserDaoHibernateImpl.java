package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.jpa.HibernateQuery;
import org.hibernate.query.NativeQuery;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory = Util.getInstance().getSessionFactory();
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Transaction tx = null;
        String sqlCommand = "CREATE TABLE IF NOT EXISTS Users (id INT PRIMARY KEY AUTO_INCREMENT," +
                " name VARCHAR(32),  lastName VARCHAR(32), age smallint)";

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(sqlCommand).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String sqlCommand = "DROP TABLE IF EXISTS Users";
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(sqlCommand).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = new User(name, lastName, age);
            System.out.println(session.save(user));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

          /* Почему-то  когда, я выполняю удаление нижепредставленным способом вылетает исключение:

          * javax.persistence.OptimisticLockException: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1; statement executed: delete from users where id=?
	       at org.hibernate.internal.ExceptionConverterImpl.wrapStaleStateException(ExceptionConverterImpl.java:238)
	       at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:93)
	       at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:181)
	       at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:188)
	       at org.hibernate.internal.SessionImpl.doFlush(SessionImpl.java:1366)
	       at org.hibernate.internal.SessionImpl.managedFlush(SessionImpl.java:453)
	       at org.hibernate.internal.SessionImpl.flushBeforeTransactionCompletion(SessionImpl.java:3212)
	       at org.hibernate.internal.SessionImpl.beforeTransactionCompletion(SessionImpl.java:2380)
	       at org.hibernate.engine.jdbc.internal.JdbcCoordinatorImpl.beforeTransactionCompletion(JdbcCoordinatorImpl.java:447)
	       at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl.beforeCompletionCallback(JdbcResourceLocalTransactionCoordinatorImpl.java:183)
	       at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl.access$300(JdbcResourceLocalTransactionCoordinatorImpl.java:40)
	       at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.commit(JdbcResourceLocalTransactionCoordinatorImpl.java:281)
	       at org.hibernate.engine.transaction.internal.TransactionImpl.commit(TransactionImpl.java:101)
	       at jm.task.core.jdbc.dao.UserDaoHibernateImpl.removeUserById(UserDaoHibernateImpl.java:83)
	       at jm.task.core.jdbc.service.UserServiceImpl.removeUserById(UserServiceImpl.java:24)
	       at UserServiceTest.removeUserById(UserServiceTest.java:64)
	       at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	       at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:64)
	       at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	       at java.base/java.lang.reflect.Method.invoke(Method.java:564)
	       at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	       at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	       at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	       at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	       at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	       at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	       at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	       at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	       at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	       at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	       at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	       at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	       at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	       at org.apache.maven.surefire.junit4.JUnit4Provider.execute(JUnit4Provider.java:252)
	       at org.apache.maven.surefire.junit4.JUnit4Provider.executeTestSet(JUnit4Provider.java:141)
	       at org.apache.maven.surefire.junit4.JUnit4Provider.invoke(JUnit4Provider.java:112)
	       at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	       at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:64)
	       at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	       at java.base/java.lang.reflect.Method.invoke(Method.java:564)
	       at org.apache.maven.surefire.util.ReflectionUtils.invokeMethodWithArray(ReflectionUtils.java:189)
	       at org.apache.maven.surefire.booter.ProviderFactory$ProviderProxy.invoke(ProviderFactory.java:165)
	       at org.apache.maven.surefire.booter.ProviderFactory.invokeProvider(ProviderFactory.java:85)
	       at org.apache.maven.surefire.booter.ForkedBooter.runSuitesInProcess(ForkedBooter.java:115)
	       at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:75)
           Caused by: org.hibernate.StaleStateException: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1; statement executed: delete from users where id=?
	       at org.hibernate.jdbc.Expectations$BasicExpectation.checkBatched(Expectations.java:67)
	       at org.hibernate.jdbc.Expectations$BasicExpectation.verifyOutcome(Expectations.java:54)
	       at org.hibernate.engine.jdbc.batch.internal.NonBatchingBatch.addToBatch(NonBatchingBatch.java:47)
	       at org.hibernate.persister.entity.AbstractEntityPersister.delete(AbstractEntityPersister.java:3614)
	       at org.hibernate.persister.entity.AbstractEntityPersister.delete(AbstractEntityPersister.java:3874)
	       at org.hibernate.action.internal.EntityDeleteAction.execute(EntityDeleteAction.java:123)
	       at org.hibernate.engine.spi.ActionQueue.executeActions(ActionQueue.java:604)
	       at org.hibernate.engine.spi.ActionQueue.lambda$executeActions$1(ActionQueue.java:478)
	       at java.base/java.util.LinkedHashMap.forEach(LinkedHashMap.java:723)
	       at org.hibernate.engine.spi.ActionQueue.executeActions(ActionQueue.java:475)
	       at org.hibernate.event.internal.AbstractFlushingEventListener.performExecutions(AbstractFlushingEventListener.java:345)
	       at org.hibernate.event.internal.DefaultFlushEventListener.onFlush(DefaultFlushEventListener.java:40)
	       at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:93)
	       at org.hibernate.internal.SessionImpl.doFlush(SessionImpl.java:1362)
          * */
        // User user = new User();
        // user.setId(id);
        // session.delete(user);

        Query query = session.createQuery("delete User where id = :ID");
        query.setParameter("ID", id);
        int result = query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction tx = null;
        List<User> users = null;
        String hql = "FROM User";

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            users = session.createQuery(hql).list();
            tx.commit();
            System.out.println(users);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sqlCommand = "TRUNCATE TABLE Users";
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(sqlCommand).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}