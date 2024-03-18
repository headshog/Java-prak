package work.webprak.DAO.impl;

import org.springframework.stereotype.Repository;
import work.webprak.DAO.InnerSubdivisionsDAO;
import work.webprak.models.InnerSubdivisions;
import work.webprak.models.Subdivisions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class InnerSubdivisionsDAOImpl extends CommonDAOImpl<InnerSubdivisions, Long> implements InnerSubdivisionsDAO {
    public InnerSubdivisionsDAOImpl() {
        super(InnerSubdivisions.class);
    }

    @Override
    public List<Subdivisions> getInnerSubdivisions(Long subd_id) {
        List<Subdivisions> ret = new ArrayList<>();
        for (InnerSubdivisions inner_subd : getAll()) {
            if (Objects.equals(inner_subd.getMain_subdivision().getId(), subd_id)) {
                ret.add(inner_subd.getInner_subdivision());
            }
        }
        return ret;
    }
}
