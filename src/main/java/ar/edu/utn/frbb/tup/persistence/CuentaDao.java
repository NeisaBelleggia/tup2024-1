package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CuentaDao extends AbstractBaseDao {

    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    // Nuevo método save que acepta CuentaEntity
    public void save(CuentaEntity cuentaEntity) {
        getInMemoryDatabase().put(cuentaEntity.getId(), cuentaEntity);
    }

    public Cuenta find(long id) {
        CuentaEntity entity = (CuentaEntity) getInMemoryDatabase().get(id);
        return entity != null ? entity.toCuenta() : null;
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object : getInMemoryDatabase().values()) {
            CuentaEntity cuenta = (CuentaEntity) object;
            if (cuenta.getTitular().equals(dni)) {
                cuentasDelCliente.add(cuenta.toCuenta());
            }
        }
        return cuentasDelCliente;
    }

    // Nuevo método para encontrar una cuenta por su ID
    public CuentaEntity findById(long id) {
        return (CuentaEntity) getInMemoryDatabase().get(id);
    }
}
