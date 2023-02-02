package persistenciaDb;

import db.EntityManagerHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;


public class PruebaDBTest implements WithGlobalEntityManager {

    @Test
    public void contextUp(){
        Assertions.assertNotNull(entityManager());
    }

    @Test
    public void contextUpWithTransaction() throws Exception {
        EntityManagerHelper entity = new EntityManagerHelper();
        entity.withTransaction(()-> {});
    }

}
