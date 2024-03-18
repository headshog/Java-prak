package work.webprak.DAO;

import work.webprak.models.InnerSubdivisions;
import work.webprak.models.Subdivisions;

import java.util.List;

public interface InnerSubdivisionsDAO extends CommonDAO<InnerSubdivisions, Long> {
    // Get inner Subdivisions on Subdivisions page subtables.
    List<Subdivisions> getInnerSubdivisions(Long subd_id);
}
