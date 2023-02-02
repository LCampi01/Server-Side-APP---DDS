package persistenciaDb;

import db.EntityManagerHelper;
import models.entities.ubicaciones.territorio.Departamento;
import models.entities.ubicaciones.ubicacion.Direccion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class recuperarDatosConQueryTest {

    @Test
    public void recuperarCosasParaPersistirNuevasCosas(){
        //obtenemos el quilmes persistido (hidratacion)
        Departamento quilmes = (Departamento) EntityManagerHelper.createQuery("from Departamento where nombre='Quilmes'").getSingleResult();
        Assertions.assertEquals("Quilmes", quilmes.getNombre());

        Direccion direccionQuilmes = new Direccion(quilmes, "Av Goyena", 456);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(direccionQuilmes);
        EntityManagerHelper.commit();

        //hidratacion de la direccion
        Direccion direccion1 = (Direccion) EntityManagerHelper.createQuery("from Direccion where calle = 'Av Goyena'").getSingleResult();
        Assertions.assertEquals("Av Goyena", direccion1.getCalle());
    }
}
