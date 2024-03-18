package work.webprak.DAO;

import work.webprak.models.Workers;
import work.webprak.models.Subdivisions;

import java.util.List;

public interface SubdivisionsDAO extends CommonDAO<Subdivisions, Long> {
    // Search for Subdivisions by Name(or substring of Name)
    // on Subdivisions page (for search).
    List<Subdivisions> getSubdivisionsByName(String name);
    // Search for Subdivisions by Director on Subdivisions page (for search).
    List<Subdivisions> getSubdivisionsByDirector(Long director_id);
    // Get Workers of current Subdivision for Subdivisions page subtables.
    List<Workers> getWorkersFromSubdivision(Long subd_id);
    // Get Inner Subdivisions for current Subdivision for Subdivisions page subtables.
    List<Subdivisions> getInnerSubdivisions(Long subd_id);
}
